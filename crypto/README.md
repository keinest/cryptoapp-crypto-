# 🛡️ Crypto Application - Suite Complète de Cryptographie

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Swing](https://img.shields.io/badge/GUI-Swing-blue?style=for-the-badge)
![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1?style=for-the-badge&logo=mysql)
![MVC](https://img.shields.io/badge/Architecture-MVC-green?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

Une application Java complète pour le chiffrement, déchiffrement et analyse cryptographique avec interface graphique intuitive, entièrement réorganisée selon le pattern **Modèle-Vue-Contrôleur (MVC)**.

## Table des Matières
- [Fonctionnalités](#-fonctionnalités)
- [Nouvelle Architecture](#-nouvelle-architecture-mvc)
- [Structure du Projet](#-structure-du-projet)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Compilation & Exécution](#-compilation--exécution)
- [Algorithmes Implémentés](#-algorithmes-implémentés)
- [Gestion des Utilisateurs](#-gestion-des-utilisateurs)
- [Base de Données](#-base-de-données)
- [Sécurité](#-sécurité)
- [Développement](#-développement)
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
- Hachage sécurisé des mots de passe (SHA‑256 + salt)

### 🗄️ Base de Données
- Stockage sécurisé des données utilisateurs
- Historique des opérations cryptographiques
- Connexion MySQL avec gestion de pool (DAO)

## 🏛️ Nouvelle Architecture (MVC)

Le projet a été entièrement refactorisé selon le pattern **Modèle‑Vue‑Contrôleur** :

- **Modèle (Model)** : `crypto/model/` – Représente les données (ex: `User.java`).
- **Vue (View)** : `crypto/view/` – Interfaces graphiques (`LoginView`, `RegisterView`, `HomeView`, `WelcomeView`, `MainWindow`...).
- **Contrôleur (Controller)** : `crypto/controller/` – Logique métier et navigation (`AuthController`, `NavigationController`).
- **Accès aux données (DAL)** : `crypto/dal/` – `DbConnection` et `UserDao` pour l’abstraction base de données.
- **Utilitaires** : `crypto/utils/` – Gestion du thème, animations, dessin de fond, etc.
- **Sessions** : `crypto/session/` – `UserSession` et `SessionInterceptor`.

Cette séparation améliore la maintenabilité, la testabilité et l’évolutivité.

## 📁 Structure du Projet

cryptoapp-crypto/
├── Makefile # Makefile racine (appelle celui de crypto/)
├── README.md
├── config.properties # Configuration globale (si utilisée)
└── crypto/ # Dossier source principal
├── Main.java # Point d’entrée
├── Makefile # Compilation spécifique au code source
├── config.properties # Configuration DB & app
│
├── controller/ # Contrôleurs MVC
│ ├── AuthController.java
│ └── NavigationController.java
│
├── view/ # Vues Swing
│ ├── MainWindow.java
│ ├── WelcomeView.java
│ ├── HomeView.java
│ ├── AlgorithmBaseView.java
│ ├── auth/
│ │ ├── LoginView.java
│ │ └── RegisterView.java
│ └── components/
│ └── AppHeader.java
│
├── model/ # Modèles de données
│ └── User.java
│
├── dal/ # Data Access Layer
│ ├── DbConnection.java
│ └── dao/
│ └── UserDao.java
│
├── encryption_decryption/ # Algorithmes de chiffrement
│ ├── affine/ (Affine.java, AffineEncrypt.java, AffineDecrypt.java)
│ ├── cesar/ (Cesar.java, CesarEncrypt.java, CesarDecrypt.java)
│ ├── feistel/ (Feistel.java, FeistelEncrypt.java, FeistelDecrypt.java)
│ ├── hill/ (Hill.java, HillEncrypt.java, HillDecrypt.java)
│ ├── rsa/ (RSA.java, RsaEncrypt.java, RsaDecrypt.java)
│ ├── vernam/ (Vernam.java, VernamEncrypt.java, VernamDecrypt.java)
│ └── vigenere/ (Vigenere.java, VigenereEncrypt.java, VigenereDecrypt.java)
│
├── crypt_analyst_brute_force/ # Analyse cryptographique
│ ├── CryptAnalyst.java
│ ├── cesar_brute_force/CesarBruterForce.java
│ ├── vigenere_brute_force/VigenereBruterForce.java
│ ├── affine_brute_force/AffineBruterForce.java
│ ├── rsa_brute_force/RSABruterForce.java
│ └── vernam_brute_force/VernamBruterForce.java
│
├── users/ # Gestion des utilisateurs (legacy, en cours d’intégration MVC)
│ ├── Connect.java
│ ├── Registration.java
│ ├── User.java
│ ├── UserProfile.java
│ ├── UserStorage.java
│ └── db/
│ ├── crypto_app.sql
│ ├── dbmanagement.java
│ ├── HASHCode.java
│ └── Restore.java
│
├── session/ # Gestion de session
│ ├── UserSession.java
│ └── SessionInterceptor.java
│
├── npk_datas/ # Informations annexes (À propos, Contact, Services)
│ ├── About.java
│ ├── Contact.java
│ └── Service.java
│
├── utils/ # Classes utilitaires
│ ├── AnimatedBubble.java
│ ├── DrawBackground.java
│ ├── ThemeManager.java
│ └── Util.java
│
├── ressources/ # Images, vidéos, assets graphiques
│ ├── *.png, *.jpeg, *.jpg, *.webm
│ └── ...
│
└── driver_sql/ # Connecteur JDBC MySQL
└── mysql-connector-j-8.0.33.jar


## ⚙️ Prérequis

- **Java JDK 17+** ([Télécharger](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html))
- **MySQL 8.0+** ([Télécharger](https://dev.mysql.com/downloads/))
- **Make** (optionnel, recommandé pour la compilation automatique)
- **Git** (pour cloner le dépôt)

## 🔧 Installation

### 1. Cloner le dépôt
```bash
git clone https://github.com/keinest/cryptoapp-crypto-.git
cd cryptoapp-crypto
```

### 2. Configurer la base de données

Créez une base de données MySQL (ex: crypto_db).

Exécutez le script SQL : crypto/users/db/crypto_app.sql

Modifiez les paramètres de connexion dans crypto/config.properties :
    ```
    db.url=jdbc:mysql://localhost:3306/crypto_db
    db.username = votre_utilisateur
    db.password = votre_mot_de_passe
    ```

🏗️ Compilation & Exécution

### Méthode 1 : Avec Makefile (recommandé)

À la racine du projet :
    
    ```
    make                # compile et exécute
    make compile        # compile uniquement
    make clean          # nettoie les fichiers compilés
    make run-gui        # exécute l’interface graphique (nécessite X11)
    ```

### Méthode 2 : Compilation manuelle

# Se placer dans le dossier crypto/
cd crypto

# Créer le dossier de sortie

mkdir -p ../bin

# Compiler toutes les classes

javac -d ../bin -cp ".:driver_sql/mysql-connector-j-8.0.33.jar" $(find . -name "*.java")

# Exécuter l’application
java -cp "../bin:driver_sql/mysql-connector-j-8.0.33.jar" crypto.Main

## 🧮 Algorithmes Implémentés

| Algorithme | Type                 | Classe principale   |
|------------|----------------------|---------------------|
| César      | Substitution         | `cesar.Cesar`       |
| Vigenère   | Polyalphabétique     | `vigenere.Vigenere` |
| Affine     | Affine               | `affine.Affine`     |
| Hill       | Matriciel            | `hill.Hill`         |
| Vernam     | Flux (XOR)           | `vernam.Vernam`     |
| Feistel    | Réseau de Feistel    | `feistel.Feistel`   |
| RSA        | Asymétrique          | `rsa.RSA`           |
|------------|----------------------|---------------------|

### Chaque algorithme dispose de trois classes :

    NomAlgo.java – logique principale

    NomAlgoEncrypt.java – chiffrement

    NomAlgoDecrypt.java – déchiffrement

### 👤 Gestion des Utilisateurs

    Inscription : view/auth/RegisterView -> AuthController -> UserDao

    Connexion : view/auth/LoginView -> AuthController -> validation + session

    Profil : UserProfile.java (ancien) / à migrer vers MVC

    Hachage : users/db/HASHCode.java (SHA‑256 + salt)

### 🗄️ Base de Données

    Connexion : dal/DbConnection (pattern Singleton)

    DAO : dal/dao/UserDao (CRUD utilisateurs)

    Script SQL : users/db/crypto_app.sql

    Utilitaire de restauration : users/db/Restore.java

### 🔒 Sécurité

    Hachage des mots de passe (SHA‑256 + salt)

    Prévention des injections SQL (utilisation de PreparedStatement)

    Validation des entrées dans les contrôleurs

    Sessions utilisateur avec UserSession (thread‑local)

### 👨‍💻 Développement

Ajouter un nouvel algorithme

    Créez un package encryption_decryption/nouvel_algo/

    Implémentez NouvelAlgo.java, NouvelAlgoEncrypt.java, NouvelAlgoDecrypt.java

    Ajoutez la vue correspondante dans view/AlgorithmBaseView.java ou une vue dédiée

    Liez via le contrôleur (NavigationController)

### Bonnes pratiques

    Respectez le pattern MVC : pas de logique métier dans les vues.

    Documentez les méthodes publiques avec Javadoc.

    Testez unitairement les algorithmes (futur dossier test/).

### 🤝 Contribution

Les contributions sont les bienvenues !

    Fork le projet

    Créez une branche (git checkout -b feature/AmazingFeature)

    Commitez (git commit -m 'Add some AmazingFeature')

    Pushez (git push origin feature/AmazingFeature)

    Ouvrez une Pull Request

### 📞 Support

Pour toute question :

    Issues GitHub : Ouvrir une issue

    Email : linedevils271@gmail.com

Développé avec ❤️ pour la communauté cryptographique
Dernière mise à jour : Avril 2026 (refonte MVC)
