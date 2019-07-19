package com.example;

import com.example.config.UserDetail;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jinrun.xie
 * @date 2019/7/18
 **/
@RestController
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/home")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String home(@AuthenticationPrincipal UserDetail userDetails) {
        return "admin home page,welcome:" +userDetails.getId()+"-"+ userDetails.getUsername() + "-" + userDetails.getPassword() + "-" + userDetails.getAuthorities().toString();
    }
}
