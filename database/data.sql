-- \i '/home/antonio/ITU/S5/mr-tahina/bank-tsotra/compte-courant-api/database/data.sql'
-- -----------------------------
-- Insertions Utilisateurs
-- -----------------------------
INSERT INTO utilisateur(username, password) VALUES
('admin', 'pass'),
('user1', 'pass'),
('user2', 'pass');

-- -----------------------------
-- Insertions Clients
-- -----------------------------
INSERT INTO client(nom) VALUES
('Rakoto'),
('Rabe'),
('Andry');

-- -----------------------------
-- Insertions Type Compte (id_type_compte SERIAL)
-- -----------------------------
INSERT INTO type_compte(libelle) VALUES
('Compte courant'),
('Compte prêt'),
('Compte dépôt');

-- -----------------------------
-- Insertions Type Mouvement
-- -----------------------------
INSERT INTO type_mouvement(libelle) VALUES
('Débit'),
('Crédit'),
('Crédit banque');

-- -----------------------------
-- Insertions Change (devise)
-- -----------------------------
INSERT INTO change(devise, date_debut, date_fin, montant) VALUES
('MGA', '2025-11-01', '2025-12-07', 1),
('USD', '2025-11-06', '2025-11-06', 4500),
('USD', '2025-11-07', '2025-11-07', 4550);

-- -----------------------------
-- Insertions Compte bancaire
-- -----------------------------
-- Pour le test, on crée un compte courant pour le premier client
-- Ici, il faudra récupérer l'id_type_compte généré pour "Compte courant"
-- Supposons que c'est 1 (auto-incrément)
INSERT INTO compte(solde, plafond, id_type_compte, id_client) VALUES
(3000000, 10000000, 1, 1),
(3000000, 10000000, 1, 2);

-- -----------------------------
-- Insertions Frais pour Compte courant (id_frais SERIAL)
-- -----------------------------
INSERT INTO frais(montant_inf, montant_sup, frais_en_montant, frais_pourcentage, id_type_compte) VALUES
(0, 500000, 20000, 5, 1),
(500001, 2000000, 50000, 3, 1);

