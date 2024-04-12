package com.innowise.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/tickets/{id}")
    public String view(){return "ticket-overview";}

    @GetMapping("/tickets/{id}/edit")
    public String edit(){return "edit-ticket";}

    @GetMapping("/tickets/add")
    public String create(){return "create-ticket";}
}
