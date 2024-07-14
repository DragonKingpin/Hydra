package com.genius.common.UlfUMC;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.directory.SearchResult;
import java.io.Serializable;

/**
 * @author Genius
 * @date 2023/05/16 16:32
 **/

@AllArgsConstructor
@NoArgsConstructor
public class UlfUMCProtocol implements Serializable {

    public static final String header = "UMC/1.1";

    private int length;

    public void setLength(int length){
        this.length = length;
    }

}
