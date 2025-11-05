package com.itu.bank.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compte")
public class CompteController {

    @GetMapping
    public String lele(){
        return "io lelena a";
    }
}