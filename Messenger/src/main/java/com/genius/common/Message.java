package com.genius.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Genius
 * @date 2023/05/09 02:40
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    private MessageType method;

    private String function;

    private Map<String,Object> data;
}
