package com.walnut.sailor.stream.fm;

import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.walnut.sailor.stream.fm.protocol.RequestHead;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SailorFMDistributionService implements SingleStreamFileMultiDistributionService {

    protected UlfBroadcastControlNode       transmitClient;

    protected BroadcastControlProducer      transmitProducer;

    protected BroadcastControlConsumer      transmitConsumer;

    protected SFMConfig                     config;

    protected Map<String, String >          directionRoute;

    public SailorFMDistributionService( UlfBroadcastControlNode client, SFMConfig config ) {
        this.transmitClient  = client;
        this.config          = config;
        this.directionRoute  = new ConcurrentHashMap<>();
    }


    @Override
    public UlfBroadcastControlNode getTransmitClient() {
        return this.transmitClient;
    }

    @Override
    public BroadcastControlConsumer getTransmitConsumer() {
        return this.transmitConsumer;
    }

    @Override
    public BroadcastControlProducer getTransmitProducer() {
        return this.transmitProducer;
    }

    @Override
    public String queryDestinedDirectoryByToken( String token ) {
        return this.directionRoute.get( token );
    }

    @Override
    public void registerDirectionRoute( String token, String directoryPath ) {
        this.directionRoute.put( token, directoryPath );
    }

    @Override
    public void deregisterDirectionRoute( String token ) {
        this.directionRoute.remove( token );
    }

    @Override
    public boolean hasStarted() {
        return this.transmitProducer != null;
    }

    @Override
    public void start() throws UMBServiceException {
        if ( !this.hasStarted() ) {
            this.transmitProducer = this.transmitClient.createBroadcastControlProducer();
            this.transmitConsumer = this.transmitClient.createBroadcastControlConsumer( this.config.getFileCloudDistributeTransmitTopic(), this.config.getFileServiceTransmitGroup() );
            this.transmitConsumer.registerController( new SFMDistributionController( this ) );
            this.transmitConsumer.start();
            this.transmitProducer.start();
        }
    }

    @Override
    public void shutdown() {
        if ( this.hasStarted() ) {
            this.transmitConsumer.close();
            this.transmitProducer.close();
            this.transmitConsumer = null;
            this.transmitProducer = null;
        }
    }

    @Override
    public SFMConfig getConfig() {
        return this.config;
    }

    @Override
    public void distributeFile( File file, String directionRouteToken ) throws IOException {
        FileMultiDistributionIface distributionIface = this.transmitProducer.getIface( FileMultiDistributionIface.class, this.config.getFileCloudDistributeTransmitTopic() );
        RequestHead head = RequestHead.newRequest().setSessionId(System.currentTimeMillis());
        distributionIface.startDistribution( head, file.getName(), directionRouteToken );

        try ( FileInputStream fileInputStream = new FileInputStream(file) ) {
            int bufferSize = this.config.getFileFrameSize() * 1024;
            byte[] buffer  = new byte[ bufferSize ];
            int bytesRead;
            long currentPosition = 0;

            while ( (bytesRead = fileInputStream.read(buffer)) != -1 ) {
                byte[] dataChunk = bytesRead == bufferSize ? buffer : Arrays.copyOf(buffer, bytesRead);
                distributionIface.transmitFileContent(head, new SFMFileFrame( dataChunk, file.length(), file.getName(), currentPosition, bytesRead) );
                currentPosition += bytesRead;
            }
        }
    }

    @Override
    public void distributeFile( String szFileName, String originalDirectory, String directionRouteToken ) throws IOException {
        Path targetPath = Path.of( originalDirectory, szFileName );
        File file = new File( targetPath.toString() );

        this.distributeFile( file, directionRouteToken );
    }

    /*@Override
    public void fileDistributionJar(File file, String topic) throws IOException {
        if (isJarFile(file)) {
            stopCurrentJarProcess();
            cleanExistingBackup(); // 清理旧备份
            File backupFile = createBackup(file); // 创建新备份
            this.fileDistribution(backupFile, topic);
            this.currentJarFile = backupFile;
            startJarProcess(this.currentJarFile);
        } else {
            this.fileDistribution(file, topic);
        }
    }

    private File createBackup(File jarFile) throws IOException {
        File backupDir = new File(BACKUP_DIR);
        if (!backupDir.exists() && !backupDir.mkdirs()) {
            throw new IOException("无法创建备份目录: " + BACKUP_DIR);
        }

        String baseName = jarFile.getName().replaceFirst("\\.jar$", "");
        File backupFile = new File(backupDir, baseName + BACKUP_SUFFIX);

        // 覆盖
        try ( InputStream in = new FileInputStream(jarFile);
             OutputStream out = new FileOutputStream(backupFile, false)) {
            byte[] buffer = new byte[1024 * 1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            System.out.println("已更新备份文件: " + backupFile.getAbsolutePath());
            return backupFile;
        }
    }

    private void cleanExistingBackup() {
        if (currentJarFile != null && currentJarFile.exists()) {
            try {
                Files.delete(currentJarFile.toPath());
                System.out.println("已清理旧文件: " + currentJarFile.getName());
            } catch (IOException e) {
                System.err.println("清理旧文件失败: " + e.getMessage());
            }
        }
    }

    private boolean isJarFile(File file) {
        return file != null && file.isFile() && file.getName().toLowerCase().endsWith(".jar");
    }

    private void stopCurrentJarProcess() {
        if (currentJarProcess != null) {
            currentJarProcess.destroyForcibly();
            try {
                if (!currentJarProcess.waitFor(10, TimeUnit.SECONDS)) {
                    System.err.println("警告: 进程终止超时");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("进程终止被中断");
            }
            currentJarProcess = null;
        }
    }

    private void startJarProcess(File jarFile) throws IOException {
        if (!jarFile.exists()) {
            throw new FileNotFoundException("JAR文件不存在: " + jarFile.getAbsolutePath());
        }
        String javaPath = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        ProcessBuilder processBuilder = new ProcessBuilder(javaPath, "-jar", jarFile.getAbsolutePath());
        processBuilder.redirectErrorStream(true);
        try {
            this.currentJarProcess = processBuilder.start();
            new Thread(() -> readStream(currentJarProcess.getInputStream())).start();
            System.out.println("已启动最新版本: " + jarFile.getName());
        } catch (IOException e) {
            throw new IOException("进程启动失败: " + e.getMessage(), e);
        }
    }

    private void readStream(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[JAR输出] " + line);
            }
        } catch (IOException e) {
            System.err.println("输出读取错误: " + e.getMessage());
        }
    }*/
}