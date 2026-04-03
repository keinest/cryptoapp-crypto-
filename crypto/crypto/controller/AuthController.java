package crypto.controller;

import crypto.users.User;
import crypto.dal.DbConnection;
import crypto.dal.dao.UserDao;
import crypto.session.UserSession;
import crypto.users.db.HASHCode;
import crypto.utils.Util;

import javax.swing.*;
import java.awt.*;

/**
 * Controleur d'authentification.
 * Toute la logique metier login/register/logout est ici — pas dans les vues.
 */
public class AuthController
{
    private final NavigationController nav;

    public AuthController(NavigationController nav)
    {
        this.nav = nav;
    }

    // -- Connexion ---------------------------

    /**
     * Tente une connexion avec email + mot de passe en clair.
     * @param callback appele sur le EDT avec le resultat.
     */
    public void login(String email, String password, AuthCallback callback)
    {
        if(email.isBlank() || password.isBlank())
        {
            callback.onError("Veuillez renseigner l'email et le mot de passe.");
            return;
        }

        SwingWorker<AuthResult, Void> worker = new SwingWorker<>()
        {
            @Override
            protected AuthResult doInBackground()
            {
                try
                {
                    if(!DbConnection.isConfigured())
                        return AuthResult.DB_ERROR;

                    String hashedEmail    = HASHCode.hash(email);
                    String hashedPassword = HASHCode.hash(password);

                    if(!UserDao.verifyUser(hashedEmail, hashedPassword))
                        return AuthResult.WRONG_CREDENTIALS;

                    User user = UserDao.getUserByEmail(hashedEmail);
                    if(user == null)
                        return AuthResult.NOT_FOUND;

                    UserSession.getInstance().createUserSession(user, false);
                    return AuthResult.SUCCESS;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    return AuthResult.DB_ERROR;
                }
            }

            @Override
            protected void done()
            {
                try
                {
                    switch(get())
                    {
                        case SUCCESS           -> callback.onSuccess();
                        case WRONG_CREDENTIALS -> callback.onError("Email ou mot de passe incorrect.");
                        case NOT_FOUND         -> callback.onError("Utilisateur introuvable.");
                        case DB_ERROR          -> callback.onError("Erreur de base de donnees. Verifiez votre configuration.");
                    }
                }
                catch(Exception e) { callback.onError("Erreur inattendue : " + e.getMessage()); }
            }
        };

        worker.execute();
    }

    // -- Inscription -------------------------

    
    public void register(String login, String email, String password, String confirmPassword, AuthCallback callback)
    {
        // Validations cote client
        if(login.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank())
        {
            callback.onError("Tous les champs sont obligatoires.");
            return;
        }

        if(!Util.mailVeri(email))
        {
            callback.onError("Adresse email invalide.");
            return;
        }

        if(!password.equals(confirmPassword))
        {
            callback.onError("Les mots de passe ne correspondent pas.");
            return;
        }

        SwingWorker<AuthResult, Void> worker = new SwingWorker<>()
        {
            @Override
            protected AuthResult doInBackground()
            {
                try
                {
                    if(!DbConnection.isConfigured())
                        return AuthResult.DB_ERROR;

                    UserDao.ensureTableExists();

                    String hashedEmail = HASHCode.hash(email);
                    String hashedPass  = HASHCode.hash(password);

                    if(UserDao.userExists(login, hashedEmail))
                        return AuthResult.ALREADY_EXISTS;

                    User newUser = new User(login, hashedEmail, hashedPass);

                    if(!UserDao.registerUser(newUser))
                        return AuthResult.DB_ERROR;

                    return AuthResult.SUCCESS;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    return AuthResult.DB_ERROR;
                }
            }

            @Override
            protected void done()
            {
                try
                {
                    switch(get())
                    {
                        case SUCCESS       -> callback.onSuccess();
                        case ALREADY_EXISTS-> callback.onError("Ce login ou cet email est dejà utilise.");
                        case DB_ERROR      -> callback.onError("Erreur de base de donnees. Verifiez votre configuration.");
                        default            -> callback.onError("Erreur inattendue.");
                    }
                }
                catch(Exception e) { callback.onError("Erreur inattendue : " + e.getMessage()); }
            }
        };

        worker.execute();
    }

    // -- Deconnexion -------------------------

    public void logout(Component parent)
    {
        int confirm = JOptionPane.showConfirmDialog(
            parent,
            "<html><body style='font-family:Segoe UI;font-size:13px;'>Êtes-vous sûr de vouloir vous deconnecter ?</body></html>",
            "Deconnexion",
            JOptionPane.YES_NO_OPTION
        );

        if(confirm == JOptionPane.YES_OPTION)
        {
            UserSession.getInstance().destroySession();
            nav.showWelcome();
        }
    }

    // -- Mode invite -------------------------

    public void continueAsGuest()
    {
        UserSession session = UserSession.getInstance();
        if(session.isLoggedIn())
        {
            int choice = JOptionPane.showConfirmDialog(
                nav.getWindow(),
                "<html><body style='font-family:Segoe UI;font-size:13px;'>Vous avez dejà une session active.<br>Continuer en mode invite fermera cette session.</body></html>",
                "Session existante",
                JOptionPane.YES_NO_OPTION
            );
            if(choice != JOptionPane.YES_OPTION) return;
            session.destroySession();
        }
        session.createGuestSession();
        nav.showDashboard();
    }

    // -- Types internes ----------------------

    public enum AuthResult { SUCCESS, WRONG_CREDENTIALS, NOT_FOUND, ALREADY_EXISTS, DB_ERROR }

    public interface AuthCallback
    {
        void onSuccess();
        void onError(String message);
    }
}
