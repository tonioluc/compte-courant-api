-- \i '/home/antonio/ITU/S5/mr-tahina/compte-courant-api/database/base.sql'
\c postgres;
drop database if exists bank;
create database bank;
\c bank;

CREATE TABLE utilisateur(
   id_utilisateur SERIAL,
   username VARCHAR(50),
   password VARCHAR(50),
   PRIMARY KEY(id_utilisateur)
);

CREATE TABLE client(
   id_client SERIAL,
   nom VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_client)
);

CREATE TABLE type_compte(
   id_type_compte SERIAL,
   libelle VARCHAR(50),
   PRIMARY KEY(id_type_compte)
);

CREATE TABLE type_mouvement(
   id_type_mouvement SERIAL,
   libelle VARCHAR(50),
   PRIMARY KEY(id_type_mouvement)
);

CREATE TABLE change(
   id_change SERIAL,
   devise VARCHAR(50),
   date_debut DATE,
   date_fin DATE,
   montant NUMERIC(15, 2),
   PRIMARY KEY(id_change)
);

CREATE TABLE compte(
   id_compte SERIAL,
   solde NUMERIC(15, 2),
   plafond NUMERIC(15, 2),
   id_type_compte INTEGER NOT NULL,
   id_client INTEGER NOT NULL,
   PRIMARY KEY(id_compte),
   UNIQUE(id_client),
   FOREIGN KEY(id_type_compte) REFERENCES type_compte(id_type_compte),
   FOREIGN KEY(id_client) REFERENCES client(id_client)
);

CREATE TABLE frais(
   id_frais SERIAL,
   montant_inf NUMERIC(15, 2),
   montant_sup NUMERIC(15, 2),
   frais_en_montant NUMERIC(15, 2),
   frais_pourcentage NUMERIC(15, 2),
   id_type_compte INTEGER NOT NULL,
   PRIMARY KEY(id_frais),
   FOREIGN KEY(id_type_compte) REFERENCES type_compte(id_type_compte)
);

CREATE TABLE virement(
   id_virement VARCHAR(50),
   id_compte_emetteur INTEGER,
   id_compte_destinataire INTEGER,
   montant NUMERIC(15, 2),
   etat INTEGER,
   date_effective TIMESTAMP,
   id_change INTEGER NOT NULL,
   PRIMARY KEY(id_virement),
   FOREIGN KEY(id_change) REFERENCES change(id_change),
   FOREIGN KEY(id_compte_emetteur) REFERENCES compte(id_compte),
   FOREIGN KEY(id_compte_destinataire) REFERENCES compte(id_compte)
);

CREATE TABLE historique_virement(
   id_historique_virement SERIAL,
   id_objet VARCHAR(50),
   date_heure TIMESTAMP,
   id_utilisateur INTEGER,
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur),
   PRIMARY KEY(id_historique_virement)
);

CREATE TABLE mouvement(
   id_mouvement SERIAL,
   date_heure TIMESTAMP,
   source VARCHAR(50),
   id_type_mouvement INTEGER NOT NULL,
   PRIMARY KEY(id_mouvement),
   FOREIGN KEY(id_type_mouvement) REFERENCES type_mouvement(id_type_mouvement)
);

CREATE TABLE validation_virement(
   id_validation_virement SERIAL,
   date_de_validation TIMESTAMP,
   id_objet VARCHAR(50) NOT NULL,
   id_utilisateur INTEGER NOT NULL,
   PRIMARY KEY(id_validation_virement),
   UNIQUE(id_objet),
   FOREIGN KEY(id_objet) REFERENCES virement(id_virement),
   FOREIGN KEY(id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);


-- Création des views
CREATE VIEW v_virement_valider AS
SELECT *
FROM virement
WHERE etat > 10;
