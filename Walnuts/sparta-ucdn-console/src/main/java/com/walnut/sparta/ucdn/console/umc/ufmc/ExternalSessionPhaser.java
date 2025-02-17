package com.walnut.sparta.ucdn.console.umc.ufmc;

import com.walnut.sparta.ucdn.console.umc.ufmc.session.UFMCTransaction;

import java.io.FileOutputStream;

public interface ExternalSessionPhaser {
    void registerSessionTransaction(Long sessionId, UFMCTransaction ufmcTransaction);

    UFMCTransaction getUFMCTransaction( Long sessionId );

    void removeUFMCTransaction( Long sessionId );

    void registerFileOutputStream(Long sessionId, FileOutputStream fileOutputStream);

    FileOutputStream getFileOutputStream( Long sessionId );

    void removeFileOutputStream( Long sessionId );

}
