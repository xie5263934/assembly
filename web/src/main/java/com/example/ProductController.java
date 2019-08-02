package com.example;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jinrun.xie
 * @date 2019/7/18
 **/
@RestController
@RequestMapping("/product")
public class ProductController {

    @RequestMapping("/info")
    //@PreAuthorize("hasAnyRole('USER')")
    public String productInfo() {
        String currentUser;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            currentUser = ((UserDetails) principal).getUsername();
        } else {
            currentUser = principal.toString();
        }
        return "some product inof=" + currentUser;
    }
}
