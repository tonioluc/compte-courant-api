package com.itu.bank.controllers;

import com.itu.bank.models.Utilisateur;
import com.itu.bank.utils.Connexion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Endpoint pour vérifier le login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "username et password sont requis"
            ));
        }

        Connection conn = null;
        try {
            conn = Connexion.getConnexion();
            Utilisateur utilisateur = Utilisateur.checkLogin(conn, username, password);

            if (utilisateur != null) {
                return ResponseEntity.ok(utilisateur);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "error", "Identifiants invalides"
                ));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", e.getMessage()
            ));
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                // Rien à faire
            }
        }
    }
}
