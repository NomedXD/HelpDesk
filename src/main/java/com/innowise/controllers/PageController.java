package com.innowise.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/auth/login")
    public String login () {
        return "login";
    }

    @GetMapping("/tickets")
    public String tickets () {
        return "tickets";
    }

    @GetMapping("/tickets/{id}/feedback")
    public String feedback() {
        return "feedback";
    }
}
