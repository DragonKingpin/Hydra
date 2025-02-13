package com.walnut.sparta.ucdn.console.umc;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class Transmit {
    public File bytesToFile( String path, byte[] bytes ) throws IOException {
        File tempFile = new File(path);
        tempFile.createNewFile();

        try (FileOutputStream fos = new FileOutputStream(tempFile,true)) {
            fos.write( bytes );
        }  catch (IOException e) {
            throw e;
        }
        return tempFile;
    }
}
