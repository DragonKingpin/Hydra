package com.pinecone.hydra.storage.file.direct;


import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public final class NativeDirectFileSystemAccessors {

    public static void copy( String sourcePath, String destinationPath ) throws IOException {
        // 注意参数语义交换：destinationPath是待复制的内容，sourcePath是目标容器目录
        Path source = Paths.get(destinationPath); // 实际要复制的源内容
        Path destinationDir = Paths.get(sourcePath); // 目标容器目录

        // 校验源是否存在
        if ( !Files.exists(source) ) {
            throw new IOException("Source to copy does not exist: " + source);
        }

        // 确保目标目录存在
        if ( !Files.exists(destinationDir) ) {
            Files.createDirectories(destinationDir);
        }

        // 如果源是单个文件，直接复制到目标目录
        if ( Files.isRegularFile(source) ) {
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

}
