package com.itu.bank.controllers;

import com.itu.bank.models.Virement;
import com.itu.bank.utils.Connexion;
import com.itu.bank.models.Validation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/virements")
public class VirementController {

    // Endpoint de test
    @GetMapping
    public String test() {
        return "Virement controller actif";
    }

    // Endpoint pour valider un virement
    @PostMapping("/{id}/valider")
    public ResponseEntity<?> validerVirement(
            @PathVariable("id") String idVirement,
            @RequestBody Map<String, String> body) {

        try {
            // Récupération de l'utilisateur depuis le JSON
            String idUtilisateurStr = body.get("idUtilisateur");

            // Création de l'objet Virement et affectation de l'ID
            Virement virement = new Virement();
            virement.setIdVirement(idVirement);

            // Appel de la méthode valider
            Validation validation = virement.valider(idUtilisateurStr);

            // Retour du résultat JSON avec code 200
            return ResponseEntity.ok(validation);

        } catch (Exception e) {
            // Gestion propre des erreurs : retourne un JSON avec l'erreur
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", e.getMessage()));
        }
    }

    // ✅ Nouveau endpoint pour récupérer tous les virements
    @GetMapping("/all-non-valider")
    public ResponseEntity<?> getAllVirementsNonValider() {
        Connection conn = null;
        try {
            conn = Connexion.getConnexion();
            List<Virement> virements = Virement.getAllVirementNonValider(conn);
            return ResponseEntity.ok(virements);

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
