package crypto.controller;

import crypto.view.MainWindow;
import crypto.view.WelcomeView;
import crypto.view.HomeView;
import crypto.view.auth.LoginView;
import crypto.view.auth.RegisterView;
import crypto.session.UserSession;

import crypto.encryption_decryption.cesar.Cesar;
import crypto.encryption_decryption.rsa.RSA;
import crypto.encryption_decryption.affine.Affine;
import crypto.encryption_decryption.vigenere.Vigenere;
import crypto.encryption_decryption.vernam.Vernam;
import crypto.encryption_decryption.feistel.Feistel;
import crypto.encryption_decryption.hill.Hill;
import crypto.crypt_analyst_brute_force.CryptAnalyst;
import crypto.users.UserProfile;

import javax.swing.*;

/**
 * Controleur de navigation — point unique pour tous les changements de vue.
 * Aucun panneau de vue ne doit manipuler directement getContentPane().
 */

public class NavigationController
{
    private final MainWindow window;

    public NavigationController(MainWindow window)
    {
        this.window = window;
    }

    // -- Vues principales -----------------

    public void showWelcome()
    {
        navigate(new WelcomeView(window, this), "CryptoApp — Accueil");
    }

    public void showHome()
    {
        UserSession session = UserSession.getInstance();
        if(!session.isLoggedIn())
        {
            session.createGuestSession();
        }

        if(session.isGuest())
            navigate(new HomeView(window, this), "CryptoApp — Tableau de bord(Invité)");
        else
            navigate(new UserProfile(window), "CryptoApp — Profil utilisateur");
    }

    public void showLogin()
    {
        navigate(new LoginView(window, this), "CryptoApp — Connexion");
    }

    public void showRegister()
    {
        navigate(new RegisterView(window, this), "CryptoApp — Inscription");
    }

    public void showDashboard()
    {
        navigate(new HomeView(window, this), "CryptoApp — Tableau de bord");
    }

    public void showProfile()
    {
        navigate(new UserProfile(window), "CryptoApp — Mon profil");
    }

    public void showCryptAnalyst()
    {
        UserSession session = UserSession.getInstance();
        if(!session.isLoggedIn() || session.isGuest())
        {
            showLoginRequired("La cryptanalyse est réservée aux utilisateurs connectés.");
            return;
        }

        if(!session.isSessionValid())
        {
            session.destroySession();
            JOptionPane.showMessageDialog(
                window,
                "<html><body style='font-family:Segoe UI;font-size:13px;'>Votre session a expiré. Veuillez vous reconnecter.</body></html>",
                "Session expirée",
                JOptionPane.WARNING_MESSAGE
            );
            showWelcome();
            return;
        }
        navigate(new CryptAnalyst(window), "CryptoApp — Cryptanalyse");
    }

    // -- Algorithmes ----------------------

    public void showCesar()   { navigate(new Cesar(window),    "Chiffrement César");   }
    public void showRSA()     { navigate(new RSA(window),      "Chiffrement RSA");     }
    public void showAffine()  { navigate(new Affine(window),   "Chiffrement Affine");  }
    public void showVigenere(){ navigate(new Vigenere(window), "Chiffrement Vigenère");}
    public void showVernam()  { navigate(new Vernam(window),   "Chiffrement Vernam");  }
    public void showFeistel() { navigate(new Feistel(window),  "Chiffrement Feistel"); }
    public void showHill()    { navigate(new Hill(window),     "Chiffrement Hill");    }

    // -- Helpers --------------------------

    private void navigate(JPanel view, String title)
    {
        window.showView(view, title);
        UserSession.getInstance().updateActivity();
    }

    private void showLoginRequired(String message)
    {
        int choice = JOptionPane.showConfirmDialog(
            window,
            "<html><body style='font-family:Segoe UI;font-size:13px;'>" +
            message + "<br><br>Voulez-vous vous connecter ?</body></html>",
            "Connexion requise",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        if(choice == JOptionPane.YES_OPTION) showLogin();
    }

    public MainWindow getWindow() { return window; }
}
