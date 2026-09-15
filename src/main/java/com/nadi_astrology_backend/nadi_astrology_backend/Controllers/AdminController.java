package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping("/test")
    public ResponseEntity<String> adminTest() {

        return ResponseEntity.ok(
                "Welcome Admin! You have admin access."
        );
    }
}