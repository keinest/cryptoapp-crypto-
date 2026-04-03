# CryptoApp — Suite Cryptographique

Application Java Swing de cryptographie, refactorisée en architecture **MVC** avec thème **Corporate Indigo**.

## Prérequis
- Java 11+ (JDK)
- MySQL 8.0+ (optionnel — mode invité disponible sans BD)

## Configuration BD
Éditer `crypto/config.properties` :
```
db.url      = jdbc:mysql://localhost:3306/crypto_app
db.username = crypto_user
db.password = votre_mot_de_passe
```

Puis initialiser la base :
```sql
CREATE DATABASE crypto_app;
CREATE USER 'crypto_user'@'localhost' IDENTIFIED BY 'votre_mot_de_passe';
GRANT ALL ON crypto_app.* TO 'crypto_user'@'localhost';
```

## Compilation & Lancement
```bash
make          # compile + lance
make compile  # compile seulement
make run      # lance (recompile si nécessaire)
make clean    # supprime les .class
```

## Architecture MVC
```
crypto/
├── Main.java                         Point d'entrée
├── model/User.java                   Modèle de données
├── dao/DbConnection.java             Connexion BD
├── dao/UserDao.java                  Accès données 
├── controller/
│   ├── NavigationController.java     Navigation centralisée
│   └── AuthController.java          Authentification
├── view/
│   ├── MainWindow.java              Fenêtre principale (JFrame)
│   ├── WelcomeView.java             Page d'accueil
│   ├── HomeView.java                Tableau de bord
│   ├── AlgorithmBaseView.java       Base partagée algorithmes
│   ├── auth/LoginView.java          Formulaire connexion
│   ├── auth/RegisterView.java       Formulaire inscription
│   └── components/AppHeader.java    En-tête unifié
├── session/
│   ├── UserSession.java             Gestion session (Singleton)
│   └── SessionInterceptor.java      Surveillance inactivité
├── encryption_decryption/
│   ├── cesar/   Cesar.java + CesarEncrypt + CesarDecrypt
│   ├── rsa/     RSA.java + RsaEncrypt + RsaDecrypt
│   ├── affine/  Affine.java + AffineEncrypt + AffineDecrypt
│   ├── vigenere/Vigenere.java + VigenereEncrypt + VigenereDecrypt
│   ├── vernam/  Vernam.java + VernamEncrypt + VernamDecrypt
│   ├── feistel/ Feistel.java + FeistelEncrypt + FeistelDecrypt
│   └── hill/    Hill.java + HillEncrypt + HillDecrypt (NOUVEAU)
└── crypt_analyst_brute_force/
    ├── CryptAnalyst.java
    ├── rsa_brute_force/RSABruterForce.java
    ├── cesar_brute_force/CesarBruterForce.java
    ├── affine_brute_force/AffineBruterForce.java
    ├── vigenere_brute_force/VigenereBruterForce.java
    └── vernam_brute_force/VernamBruterForce.java
```

## Bugs corrigés
| Bug | Correction |
|-----|------------|
| Injection SQL dans `verifyUser()` | PreparedStatement paramétré |
| Fenêtre 1800×1800 px | → 1280×820, taille min 900×650 |
| `HillDecrypt.java` manquant | Créé avec inversion matricielle mod 26 |
| Import typo `crypt_analys_brute_force` | → `crypt_analyst_brute_force` |
| 5 couleurs accent simultanées | → 1 seule (Indigo #5C6BC0) |
| Navigation dupliquée/incohérente | Centralisée dans NavigationController |
| `finalize()` déprécié | → `removeNotify()` |
