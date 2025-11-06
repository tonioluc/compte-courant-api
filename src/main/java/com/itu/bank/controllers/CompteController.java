package com.itu.bank.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.itu.bank.models.Compte;
import com.itu.bank.models.Virement;

import java.util.Map;

@RestController
@RequestMapping("/api/compte")
public class CompteController {

    @GetMapping
    public String test() {
        return "Mandeha";
    }

    @PostMapping("/virement")
    public ResponseEntity<?> creerVirement(@RequestBody Map<String, String> body) {
        try {
            // Récupération des paramètres depuis le JSON
            String idCompteEmetteur = body.get("idCompteEmetteur");
            String idCompteDestinataire = body.get("idCompteDestinataire");
            String dateEffective = body.get("dateEffective");
            String devise = body.get("devise");
            String montant = body.get("montant");
            String idUtilisateur = body.get("idUtilisateur");

            // Création du compte et exécution du virement
            Compte compte = new Compte();
            Virement virementCree = compte.virer(
                    idCompteEmetteur,
                    idCompteDestinataire,
                    dateEffective,
                    devise,
                    montant,
                    idUtilisateur
            );

            // Retour du virement en JSON avec code 201
            return ResponseEntity.status(HttpStatus.CREATED).body(virementCree);

        } catch (Exception e) {
            // Retour d'erreur propre
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", e.getMessage()
            ));
        }
    }
}
