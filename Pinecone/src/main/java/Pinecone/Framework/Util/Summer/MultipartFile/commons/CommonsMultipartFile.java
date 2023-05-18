package Pinecone.Framework.Util.Summer.MultipartFile.commons;

import Pinecone.Framework.Debug.Debug;
import Pinecone.Framework.Util.Summer.MultipartFile.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;

public class CommonsMultipartFile implements MultipartFile, Serializable {
    private final FileItem fileItem;
    private final long size;

    public CommonsMultipartFile(FileItem fileItem) {
        this.fileItem = fileItem;
        this.size = this.fileItem.getSize();
    }

    public final FileItem getFileItem() {
        return this.fileItem;
    }

    public String getName() {
        return this.fileItem.getFieldName();
    }

    public String getOriginalFilename() {
        String filename = this.fileItem.getName();
        if (filename == null) {
            return "";
        } else {
            int pos = filename.lastIndexOf("/");
            if (pos == -1) {
                pos = filename.lastIndexOf("\\");
            }

            return pos != -1 ? filename.substring(pos + 1) : filename;
        }
    }

    public String getContentType() {
        return this.fileItem.getContentType();
    }

    public boolean isEmpty() {
        return this.size == 0L;
    }

    public long getSize() {
        return this.size;
    }

    public byte[] getBytes() {
        if (!this.isAvailable()) {
            throw new IllegalStateException("File has been moved - cannot be read again");
        } else {
            byte[] bytes = this.fileItem.get();
            return bytes != null ? bytes : new byte[0];
        }
    }

    public InputStream getInputStream() throws IOException {
        if (!this.isAvailable()) {
            throw new IllegalStateException("File has been moved - cannot be read again");
        } else {
            InputStream inputStream = this.fileItem.getInputStream();
            return (InputStream)(inputStream != null ? inputStream : new ByteArrayInputStream(new byte[0]));
        }
    }

    public void transferTo(File dest) throws IOException, IllegalStateException {
        if (!this.isAvailable()) {
            throw new IllegalStateException("File has already been moved - cannot be transferred again");
        } else if (dest.exists() && !dest.delete()) {
            throw new IOException("Destination file [" + dest.getAbsolutePath() + "] already exists and could not be deleted");
        } else {
            try {
                this.fileItem.write(dest);
                String action = "transferred";
                if (!this.fileItem.isInMemory()) {
                    action = this.isAvailable() ? "copied" : "moved";
                }

                Debug.trace("Multipart file '" + this.getName() + "' with original filename [" + this.getOriginalFilename() + "], stored " + this.getStorageDescription() + ": " + action + " to [" + dest.getAbsolutePath() + "]");


            } catch (FileUploadException e) {
                throw new IllegalStateException(e.getMessage());
            } catch (IOException ioException) {
                throw ioException;
            } catch (Exception e2) {
                Debug.trace("Could not transfer to file");
                throw new IOException("Could not transfer to file: " + e2.getMessage());
            }
        }
    }

    protected boolean isAvailable() {
        if (this.fileItem.isInMemory()) {
            return true;
        } else if (this.fileItem instanceof DiskFileItem) {
            return ((DiskFileItem)this.fileItem).getStoreLocation().exists();
        } else {
            return this.fileItem.getSize() == this.size;
        }
    }

    public String getStorageDescription() {
        if (this.fileItem.isInMemory()) {
            return "in memory";
        } else {
            return this.fileItem instanceof DiskFileItem ? "at [" + ((DiskFileItem)this.fileItem).getStoreLocation().getAbsolutePath() + "]" : "on disk";
        }
    }

    public String getStoragePath(){
        return this.fileItem instanceof DiskFileItem ? ((DiskFileItem)this.fileItem).getStoreLocation().getAbsolutePath() : "";
    }



    public void finalize() throws Throwable{
        super.finalize();
/*        if ( !this.fileItem.isInMemory() ){
            String szGarbage = this.getStoragePath();
            File fGarbage = new File( szGarbage );
            if ( fGarbage.exists() ) { *//* Jesus fucking christ with tomcat.. **//*
                //System.err.println( "Upload garbage annihilating." );
                if( !fGarbage.delete() ){
                    System.err.println( "Error after upload garbage annihilated." );
                }
            }
        }*/
    }
}
