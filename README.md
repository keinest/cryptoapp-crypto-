# 🛡️ Crypto Application - Suite Complète de Cryptographie

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Swing](https://img.shields.io/badge/GUI-Swing-blue?style=for-the-badge)
![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1?style=for-the-badge&logo=mysql)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

Une application Java complète pour le chiffrement, déchiffrement et analyse cryptographique avec interface graphique intuitive.

## 📋 Table des Matières
- [Fonctionnalités](#-fonctionnalités)
- [Structure du Projet](#-structure-du-projet)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Compilation & Exécution](#-compilation--exécution)
- [Architecture](#-architecture)
- [Algorithmes Implémentés](#-algorithmes-implémentés)
- [Gestion des Utilisateurs](#-gestion-des-utilisateurs)
- [Développement](#-développement)
- [Captures d'Écran](#-captures-décran)
- [Contribution](#-contribution)
- [Licence](#-licence)

## ✨ Fonctionnalités

### 🔐 Chiffrement & Déchiffrement
- **César** : Chiffrement par décalage
- **Vigenère** : Chiffrement polyalphabétique
- **RSA** : Cryptographie asymétrique
- **Affine** : Chiffrement affine
- **Vernam** : Chiffrement à flux (One-Time Pad)
- **Feistel** : Réseau de Feistel
- **Hill** : Chiffrement par matrices

### 🔍 Analyse Cryptographique
- Force brute sur César, Vigenère, Affine, RSA et Vernam
- Analyse statistique des textes chiffrés
- Détection de vulnérabilités

### 👥 Gestion Utilisateurs
- Inscription et connexion sécurisée
- Gestion de profils utilisateurs
- Sessions persistantes
- Hachage sécurisé des mots de passe

### 💾 Base de Données
- Stockage sécurisé des données utilisateurs
- Historique des opérations cryptographiques
- Connexion MySQL avec gestion de pool

## 📁 Structure du Projet

crypto/
├── Main.java # Point d'entrée principal
├── EnhancedHeader.java # En-tête améliorée de l'interface
├── Header.java # En-tête standard
├── Home.java # Page d'accueil
│
├── crypt_analyst_brute_force/ # Analyse cryptographique
│ ├── CryptAnalyst.java
│ ├── affine_brute_force/
│ ├── cesar_brute_force/
│ ├── rsa_brute_force/
│ ├── vernam_brute_force/
│ └── vigenere_brute_force/
│
├── encryption_decryption/ # Algorithmes de chiffrement
│ ├── affine/
│ ├── cesar/
│ ├── feistel/
│ ├── hill/
│ ├── rsa/
│ ├── vernam/
│ └── vigenere/
│
├── driver_sql/ # Connexion base de données
│ ├── mysql-connector-j-8.0.33.jar
│ └── jar_run
│
├── npk_datas/ # Données et informations
│ ├── About.java
│ ├── Contact.java
│ └── Service.java
│
├── ressources/ # Ressources graphiques
│ ├── images (logos, icônes)
│ └── cryptoapp.webm
│
├── session/ # Gestion des sessions
│ ├── SessionInterceptor.java
│ └── UserSession.java
│
├── users/ # Gestion utilisateurs
│ ├── Connect.java
│ ├── Registration.java
│ ├── User.java
│ ├── UserProfile.java
│ ├── UserStorage.java
│ └── db/ (gestion base de données)
│   ├── dbmanagement.java
|   ├── HASHCode.java
|
└── utils/ # Utilitaires
├── AnimatedBubble.java
├── DrawBackground.java
├── ThemeManager.java
└── Util.java


## ⚙️ Prérequis

- **Java JDK 17+** ([Télécharger](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html))
- **MySQL 8.0+** ([Télécharger](https://dev.mysql.com/downloads/))
- **Make** (optionnel, pour la compilation automatique)
- **Git** (pour cloner le dépôt)

## 🚀 Installation

### 1. Cloner le dépôt
```bash
git clone https://github.com/votre-username/votre-repo.git
cd cryptoapp-crypto

2. Configurer la base de données

-- Créer la base de données
CREATE DATABASE crypto_db;
USE crypto_db;

-- Créer la table utilisateurs
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Créer la table d'historique
CREATE TABLE crypto_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    algorithm VARCHAR(50),
    operation VARCHAR(20),
    input_text TEXT,
    output_text TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

3. Configurer les paramètres de connexion

Mofifiez le fichier crypto/config.properties :

db.url=jdbc:mysql://localhost:3306/crypto_db
db.username = votre_utilisateur
db.password = votre_mot_de_passe

🏗️ Compilation & Exécution
Méthode 1 : Avec Makefile (recommandé)

# Compiler et exécuter
make

# Compiler seulement
make compile

# Nettoyer les fichiers compilés
make clean

# Exécuter avec interface graphique (nécessite X11)
make run-gui

Méthode 2 : Compilation manuelle
bash

# Créer le dossier de sortie
mkdir -p bin

# Compiler toutes les classes
javac -d bin -sourcepath crypto -cp "crypto/driver_sql/mysql-connector-j-8.0.33.jar" $(find crypto -name "*.java")

# Exécuter l'application
java -cp "bin:crypto/driver_sql/mysql-connector-j-8.0.33.jar" crypto.Main

Méthode 3 : Dans GitHub Codespaces
bash

# Installer les dépendances
sudo apt-get update && sudo apt-get install -y xvfb

# Lancer avec environnement graphique virtuel
make run-gui

🏛️ Architecture
Modèle Vue Contrôleur (MVC)

    Modèle : Classes métier dans encryption_decryption/, users/

    Vue : Interface Swing dans Main.java, Home.java, etc.

    Contrôleur : Logique de gestion dans utils/, session/

Gestion des Sessions

    Sessions utilisateurs persistantes

    Interception des requêtes non authentifiées

    Gestion des timeouts de session

Sécurité

    Hachage des mots de passe avec SHA-256 + salt

    Validation des entrées utilisateur

    Protection contre les injections SQL

🧮 Algorithmes Implémentés
Chiffrement par Substitution

    César : C = (P + k) mod 26

    Affine : C = (aP + b) mod 26

Chiffrement Polygraphique

    Hill : C = KP mod 26 (matrice de chiffrement)

Chiffrement Asymétrique

    RSA : Génération de clés publique/privée

    RSA : Chiffrement C = M^e mod n

    RSA : Déchiffrement M = C^d mod n

Chiffrement à Flux

    Vernam : XOR avec clé à usage unique

    Feistel : Réseau de tours avec fonction F

👤 Gestion des Utilisateurs
Inscription
java

// Hachage sécurisé du mot de passe
String salt = generateSalt();
String hashedPassword = sha256(password + salt);

// Stockage dans la base de données
userStorage.saveUser(username, hashedPassword, salt, email);

Connexion
java

// Vérification des identifiants
User user = userStorage.authenticate(username, password);
if (user != null) {
    UserSession.createSession(user);
    // Redirection vers l'interface principale
}

Profil Utilisateur

    Visualisation des informations personnelles

    Historique des opérations cryptographiques

    Modification des préférences

🛠️ Développement
Code Style

    Conventions de nommage Java standard

    Documentation Javadoc complète

    Séparation claire des responsabilités

Tests
bash

# Pour ajouter des tests unitaires
mkdir -p crypto/test
javac -d bin -cp "bin:lib/junit.jar" crypto/test/*.java
java -cp "bin:lib/junit.jar" org.junit.runner.JUnitCore crypto.test.TestSuite

Extensions Possibles

    Nouveaux algorithmes : Ajouter AES, Blowfish, etc.

    Analyse avancée : Détection de patterns statistiques

    Interface web : Version Spring Boot + Angular

    API REST : Service cryptographique en ligne

🤝 Contribution

Les contributions sont les bienvenues ! Pour contribuer :

    Fork le projet

    Créer une branche (git checkout -b feature/AmazingFeature)

    Commit vos changements (git commit -m 'Add some AmazingFeature')

    Push vers la branche (git push origin feature/AmazingFeature)

    Ouvrir une Pull Request

Guide de Contribution

    Respectez le style de code existant

    Ajoutez des tests pour les nouvelles fonctionnalités

    Documentez votre code avec Javadoc

    Mettez à jour le README si nécessaire

📞 Support

Pour toute question ou problème :

    Issues GitHub : Ouvrir une issue

    Email : linedevils271@gmail.com

Développé avec ❤️ pour la communauté cryptographique

Dernière mise à jour : Decembre 2025
