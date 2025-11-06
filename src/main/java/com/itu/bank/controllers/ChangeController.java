package com.itu.bank.controllers;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itu.bank.models.Change;
import com.itu.bank.models.Virement;
import com.itu.bank.utils.Connexion;

@RestController
@RequestMapping("/api/changes")
public class ChangeController {
    // ✅ Nouveau endpoint pour récupérer tous les virements
    @GetMapping()
    public ResponseEntity<?> getAllChange() {
        Connection conn = null;
        try {
            conn = Connexion.getConnexion();
            List<Change> changes = Change.getAllChangeDistinctDevise(conn);
            return ResponseEntity.ok(changes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", e.getMessage()));
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                // Rien à faire
            }
        }
    }
}
