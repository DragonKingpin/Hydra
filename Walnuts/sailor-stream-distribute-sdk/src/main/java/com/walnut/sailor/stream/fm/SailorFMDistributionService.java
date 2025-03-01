package com.walnut.sailor.stream.fm;

import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.walnut.sailor.stream.fm.protocol.RequestHead;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class SailorFMDistributionService implements SingleStreamFileMultiDistributionService {

    protected UlfBroadcastControlNode  controlClient;

    protected BroadcastControlProducer controlProducer;

    protected BroadcastControlConsumer controlConsumer;

    protected SFMConfig                config;

    public SailorFMDistributionService( UlfBroadcastControlNode client, SFMConfig config ) {
        this.controlClient = client;
        this.config        = config;
    }


    @Override
    public UlfBroadcastControlNode getControlClient() {
        return this.controlClient;
    }

    @Override
    public BroadcastControlConsumer getControlConsumer() {
        return this.controlConsumer;
    }

    @Override
    public BroadcastControlProducer getControlProducer() {
        return this.controlProducer;
    }


    @Override
    public boolean hasStarted() {
        return this.controlProducer != null;
    }

    @Override
    public void start() throws UMBServiceException {
        if ( this.controlProducer == null ) {
            this.controlProducer = this.controlClient.createBroadcastControlProducer();
            this.controlConsumer = this.controlClient.createBroadcastControlConsumer( this.config.getFileCloudDistributeTransmitTopic(), this.config.getFileServiceTransmitGroup() );
            this.controlConsumer.registerController( new SFMDistributionController( this ) );
            this.controlConsumer.start();
            this.controlProducer.start();
        }
    }

    @Override
    public void shutdown() {
        if ( this.controlProducer != null ) {
            this.controlConsumer.close();
            this.controlProducer.close();
            this.controlConsumer = null;
            this.controlProducer = null;
        }
    }

    @Override
    public SFMConfig getConfig() {
        return this.config;
    }

    @Override
    public void distributeFile( File file, String topic, String destinedDirectory ) throws IOException {
        FileMultiDistributionIface distributionIface = this.controlProducer.getIface( FileMultiDistributionIface.class, topic );
        RequestHead head = RequestHead.newRequest().setSessionId(System.currentTimeMillis());
        distributionIface.startDistribution( head, file.getName() );

        try ( FileInputStream fileInputStream = new FileInputStream(file) ) {
            int bufferSize = 900 * 1024;
            byte[] buffer = new byte[bufferSize];
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
    public void distributeFile( String szFileName, String originalDirectory, String topic, String destinedDirectory ) throws IOException {

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