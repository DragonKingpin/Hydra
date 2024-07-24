package com.walnut.sparta.system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/system" )
public class SystemController {
    @GetMapping( "/undefined" )
    public String undefined() {
        return "Hello, hi, good afternoon! This is undefined specking!";
    }
}
