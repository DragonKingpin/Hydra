package com.genius.common.UlfUMC;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Genius
 * @date 2023/05/16 21:34
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UlfUMCBody implements Serializable {
    private UlfUMCMessageType method;

    private String function;

    private Map<String,Object> data;
}
