package com.starter.SpringStarter.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;


@Controller
public class AdminController {
    
    @GetMapping("/admin")
    public String admin(Model Model) {
        return "admin";
    }
    
}
