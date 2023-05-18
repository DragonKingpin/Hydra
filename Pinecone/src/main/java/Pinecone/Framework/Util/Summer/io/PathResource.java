package Pinecone.Framework.Util.Summer.io;

import Pinecone.Framework.System.util.Assert;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathResource extends AbstractResource implements WritableResource {
    private final Path path;

    public PathResource(Path path) {
        Assert.notNull(path, "Path must not be null");
        this.path = path.normalize();
    }

    public PathResource(String path) {
        Assert.notNull(path, "Path must not be null");
        this.path = Paths.get(path).normalize();
    }

    public PathResource(URI uri) {
        Assert.notNull(uri, "URI must not be null");
        this.path = Paths.get(uri).normalize();
    }

    public final String getPath() {
        return this.path.toString();
    }

    public boolean exists() {
        return Files.exists(this.path, new LinkOption[0]);
    }

    public boolean isReadable() {
        return Files.isReadable(this.path) && !Files.isDirectory(this.path, new LinkOption[0]);
    }

    public InputStream getInputStream() throws IOException {
        if (!this.exists()) {
            throw new FileNotFoundException(this.getPath() + " (no such file or directory)");
        } else if (Files.isDirectory(this.path, new LinkOption[0])) {
            throw new FileNotFoundException(this.getPath() + " (is a directory)");
        } else {
            return Files.newInputStream(this.path);
        }
    }

    public URL getURL() throws IOException {
        return this.path.toUri().toURL();
    }

    public URI getURI() throws IOException {
        return this.path.toUri();
    }

    public File getFile() throws IOException {
        try {
            return this.path.toFile();
        } catch (UnsupportedOperationException var2) {
            throw new FileNotFoundException(this.path + " cannot be resolved to " + "absolute file path");
        }
    }

    public long contentLength() throws IOException {
        return Files.size(this.path);
    }

    public long lastModified() throws IOException {
        return Files.getLastModifiedTime(this.path).toMillis();
    }

    public Resource createRelative(String relativePath) throws IOException {
        return new PathResource(this.path.resolve(relativePath));
    }

    public String getFilename() {
        return this.path.getFileName().toString();
    }

    public String getDescription() {
        return "path [" + this.path.toAbsolutePath() + "]";
    }

    public boolean isWritable() {
        return Files.isWritable(this.path) && !Files.isDirectory(this.path, new LinkOption[0]);
    }

    public OutputStream getOutputStream() throws IOException {
        if (Files.isDirectory(this.path, new LinkOption[0])) {
            throw new FileNotFoundException(this.getPath() + " (is a directory)");
        } else {
            return Files.newOutputStream(this.path);
        }
    }

    public boolean equals(Object obj) {
        return this == obj || obj instanceof PathResource && this.path.equals(((PathResource)obj).path);
    }

    public int hashCode() {
        return this.path.hashCode();
    }
}

