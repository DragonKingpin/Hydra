package com.walnuts.sparta.uofs.console.api.controller.v2;


import com.walnuts.sparta.uofs.console.api.response.BasicResultResponse;
import com.walnuts.sparta.uofs.console.domain.dto.UserLoginDTO;
import com.walnuts.sparta.uofs.console.util.JWTUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api/v2/user" )
@CrossOrigin
public class UserController {

    @PostMapping("/login")
    public BasicResultResponse<String> login(@RequestBody UserLoginDTO dto){
       if( dto.getPassword().equals("11122233") ){
           return BasicResultResponse.success(JWTUtil.createJWT());
       }
       return BasicResultResponse.error("Permission code error");
    }
}
