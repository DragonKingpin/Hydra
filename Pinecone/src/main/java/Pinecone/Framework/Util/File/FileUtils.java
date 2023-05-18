package Pinecone.Framework.Util.File;

import Pinecone.Framework.System.util.OSIdentifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    public static byte[] readByteAll( File pFile ) throws IOException {
        Long fileLength = pFile.length();
        byte[] fileContent = new byte[ fileLength.intValue() ];

        FileInputStream fileInputStream = new FileInputStream(pFile);
        int nRealReadied = fileInputStream.read(fileContent);
        fileInputStream.close();

        if ( nRealReadied != fileLength.intValue() ) {
            throw new IOException( "Read all content failed !" );
        }
        return fileContent;
    }

    public static String readAll ( String szFileDir ) throws IOException {
        File file = new File(szFileDir);
        return readAll(file);
    }

    public static String readAll ( String szFileDir, Charset charset ) throws IOException {
        File file = new File(szFileDir);
        return readAll( file, charset );
    }

    public static String readAll ( File pFile, Charset charset ) throws IOException {
        return new String( readByteAll( pFile ), charset );
    }

    public static String readAll ( File pFile ) throws IOException {
        return new String( readByteAll( pFile ), StandardCharsets.UTF_8 );
    }



    private static void checkDirectory( File directory ) {
        if (!directory.exists()) {
            throw new IllegalArgumentException(directory + " does not exist");
        } else if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory + " is not a directory");
        }
    }

    public static boolean isSymlink( File file ) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        } else if ( OSIdentifier.isWindows() ) {
            return false;
        } else {
            File fileInCanonicalDir = null;
            if (file.getParent() == null) {
                fileInCanonicalDir = file;
            } else {
                File canonicalDir = file.getParentFile().getCanonicalFile();
                fileInCanonicalDir = new File(canonicalDir, file.getName());
            }

            return !fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile());
        }
    }

    public static long sizeOf( File file ) {
        if ( !file.exists() ) {
            String message = file + " does not exist";
            throw new IllegalArgumentException(message);
        } else {
            return file.isDirectory() ? sizeOfDirectory(file) : file.length();
        }
    }

    public static BigInteger sizeOfAsBigInteger( File file ) {
        if (!file.exists()) {
            String message = file + " does not exist";
            throw new IllegalArgumentException(message);
        } else {
            return file.isDirectory() ? sizeOfDirectoryAsBigInteger(file) : BigInteger.valueOf(file.length());
        }
    }

    public static long sizeOfDirectory( File directory ) {
        FileUtils.checkDirectory(directory);
        File[] files = directory.listFiles();
        if (files == null) {
            return 0L;
        } else {
            long size = 0L;
            File[] arr$ = files;
            int len$ = files.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                File file = arr$[i$];

                try {
                    if ( !isSymlink(file) ) {
                        size += sizeOf(file);
                        if (size < 0L) {
                            break;
                        }
                    }
                } catch (IOException var9) {
                }
            }

            return size;
        }
    }

    public static BigInteger sizeOfDirectoryAsBigInteger( File directory ) {
        FileUtils.checkDirectory(directory);
        File[] files = directory.listFiles();
        if (files == null) {
            return BigInteger.ZERO;
        } else {
            BigInteger size = BigInteger.ZERO;
            File[] arr$ = files;
            int len$ = files.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                File file = arr$[i$];

                try {
                    if (!isSymlink(file)) {
                        size = size.add(BigInteger.valueOf(sizeOf(file)));
                    }
                } catch (IOException var8) {
                }
            }

            return size;
        }
    }


    public static void forceDelete( File file ) throws IOException {
        if ( file.isDirectory() ) {
            FileUtils.deleteDirectory(file);
        }
        else {
            boolean filePresent = file.exists();
            if ( !file.delete() ) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: " + file);
                }

                String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }

    }

    public static void deleteDirectory( File directory ) throws IOException {
        if (directory.exists()) {
            if (!isSymlink(directory)) {
                FileUtils.cleanDirectory(directory);
            }

            if (!directory.delete()) {
                String message = "Unable to delete directory " + directory + ".";
                throw new IOException(message);
            }
        }
    }

    public static boolean deleteQuietly( File file ) {
        if (file == null) {
            return false;
        } else {
            try {
                if (file.isDirectory()) {
                    FileUtils.cleanDirectory(file);
                }
            } catch (Exception var3) {
            }

            try {
                return file.delete();
            } catch (Exception var2) {
                return false;
            }
        }
    }

    public static void cleanDirectory( File directory ) throws IOException {
        String message;
        if (!directory.exists()) {
            message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        } else if (!directory.isDirectory()) {
            message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        } else {
            File[] files = directory.listFiles();
            if (files == null) {
                throw new IOException("Failed to list contents of " + directory);
            } else {
                IOException exception = null;
                File[] arr$ = files;
                int len$ = files.length;

                for( int i$ = 0; i$ < len$; ++i$ ) {
                    File file = arr$[i$];

                    try {
                        FileUtils.forceDelete(file);
                    } catch (IOException var8) {
                        exception = var8;
                    }
                }

                if (null != exception) {
                    throw exception;
                }
            }
        }
    }

}
