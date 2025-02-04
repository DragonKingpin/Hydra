package com.pinecone.hydra.storage.file.direct;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.name.path.PathResolver;
import com.pinecone.hydra.storage.file.FileSystemConfig;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.cache.DefaultCacheConstants;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;
import com.pinecone.hydra.storage.file.source.ExternalSymbolicManipulator;
import com.pinecone.hydra.storage.file.source.FileManipulator;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.storage.file.source.FolderManipulator;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.io.TitanInputStreamChanface;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.kom.PathSelector;
import com.pinecone.hydra.unit.imperium.ImperialTree;
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;
import com.pinecone.ulf.util.guid.GUIDs;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class KenDirectFileSystemAccess implements DirectFileSystemAccess {
    protected KOMFileSystem                 fileSystem;

    protected PathResolver                  pathResolver;

    protected PathSelector                  pathSelector;

    protected FileMasterManipulator         fileMasterManipulator;

    protected FolderManipulator             folderManipulator;

    protected FileManipulator               fileManipulator;

    protected ExternalSymbolicManipulator   externalSymbolicManipulator;

    protected ImperialTree                  imperialTree;


    public KenDirectFileSystemAccess(KOMFileSystem fileSystem){
        this.fileSystem                     = fileSystem;
        this.pathResolver                   = new KOPathResolver( fileSystem.getConfig() );
        this.fileMasterManipulator          = this.fileSystem.getFileMasterManipulator();
        this.fileManipulator                = this.fileMasterManipulator.getFileManipulator();
        this.folderManipulator              = this.fileMasterManipulator.getFolderManipulator();
        this.externalSymbolicManipulator    = this.fileMasterManipulator.getExternalSymbolicManipulator();;
        this.imperialTree                   = fileSystem.getMasterTrieTree();

        this.pathSelector = new KenExternalSymbolicSelector(
                this.pathResolver, this.fileSystem.getMasterTrieTree(),this.folderManipulator, new GUIDNameManipulator[] { this.fileManipulator },
                this.externalSymbolicManipulator
        );
    }

    @Override
    public ElementNode queryElement(String path) {
        GUID guid = this.queryGUIDByPath(path);
        if(guid == null){
            return null;
        }

        ExternalSymbolic externalSymbolic = this.externalSymbolicManipulator.getSymbolicByGuid(guid);
        String externalPath = this.fileSystem.getPath(externalSymbolic.getGuid());
        String remainingPath = path.substring(externalPath.length()).replaceFirst("^/", "");

        String realFilePath = externalSymbolic.getReparsedPoint()+ "/" + remainingPath;
        File file = new File(realFilePath);
        if( file.isDirectory() ){
            return new GenericExternalFolder(file);
        }else {
            return new GenericExternalFile(file);
        }
    }

    @Override
    public void insertExternalSymbolic(ExternalSymbolic externalSymbolic) {
        this.externalSymbolicManipulator.insert( externalSymbolic );
    }

    @Override
    public void copy(String sourcePath, String destinationPath) throws IOException {
        // 注意参数语义交换：destinationPath是待复制的内容，sourcePath是目标容器目录
        Path source = Paths.get(destinationPath); // 实际要复制的源内容
        Path destinationDir = Paths.get(sourcePath); // 目标容器目录

        // 校验源是否存在
        if (!Files.exists(source)) {
            throw new IOException("Source to copy does not exist: " + source);
        }

        // 确保目标目录存在
        if (!Files.exists(destinationDir)) {
            Files.createDirectories(destinationDir);
        }

        // 如果源是单个文件，直接复制到目标目录
        if (Files.isRegularFile(source)) {
            Path target = destinationDir.resolve(source.getFileName());
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            return;
        }

        // 处理目录复制（保留目录结构）
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // 计算相对路径：从源根目录到当前目录
                Path relative = source.relativize(dir);

                // 构建目标目录路径
                Path targetDir = destinationDir.resolve(relative);

                // 创建目标目录（如果不存在）
                if (!Files.exists(targetDir)) {
                    Files.createDirectories(targetDir);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // 计算相对路径：从源根目录到当前文件
                Path relative = source.relativize(file);

                // 构建目标文件路径
                Path targetFile = destinationDir.resolve(relative);

                // 复制文件并覆盖已存在文件
                Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc != null) {
                    throw exc; // 传播异常
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    public void createExternalSymbolic(String folderPath, String externalSymbolicName, String reparsedPoint) {
        ElementNode elementNode = this.fileSystem.queryElement(folderPath);
        elementNode.evinceFolder().createExternalSymbolic( externalSymbolicName,reparsedPoint );
    }

    private GUID queryGUIDByPath(String path ) {
        return this.queryGUIDByNS( path, null, null );
    }

    private GUID queryGUIDByNS( String path, String szBadSep, String szTargetSep ) {
        if( szTargetSep != null ) {
            path = path.replace( szBadSep, szTargetSep );
        }

        String[] parts = this.pathResolver.segmentPathParts( path );
        List<String > resolvedParts = this.pathResolver.resolvePath( parts );
        path = this.pathResolver.assemblePath( resolvedParts );

        GUID guid = this.imperialTree.queryGUIDByPath( path );
        if ( guid != null ){
            return guid;
        }


        guid = this.pathSelector.searchGUID( resolvedParts );
        return guid;
    }
}
