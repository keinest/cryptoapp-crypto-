package crypto.session;

import crypto.Main;
import javax.swing.*;
import java.awt.Component;
import crypto.users.Connect;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SessionInterceptor 
{
    private static Timer inactivityTimer;
    private static final int INACTIVITY_CHECK_INTERVAL = 60000; // Verifier si une activite ou non
    private static final int INACTIVITY_WARNING_MINUTES = 25; // Avertir apres 25 min d'inactivite
    
    public static boolean checkSessionAndRedirect(Main mainWindow) 
    {
        return checkSessionAndRedirect(mainWindow, true);
    }
    
    public static boolean checkSessionAndRedirect(Main mainWindow, boolean showPrompt) 
    {
        UserSession session = UserSession.getInstance();
        session.updateActivity();
        
        if(!session.isLoggedIn()) 
        {
            if(showPrompt) 
            {
                int choice = JOptionPane.showConfirmDialog(
                    mainWindow,
                    "<html><div style='color:#00ffff;'>" +
                    "<b>Session requise</b><br><br>" +
                    "Vous devez etre connecte pour acceder a cette fonctionnalite.<br>" +
                    "Voulez-vous vous connecter maintenant ?</div></html>",
                    "Session requise",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if(choice == JOptionPane.YES_OPTION)
                    redirectToLogin(mainWindow);
            }   
            else
                redirectToLogin(mainWindow);

            return false;
        }

        if(!session.isSessionValid()) 
        {
            JOptionPane.showMessageDialog(
                mainWindow,
                "<html><div style='color:#ff5555;'>" +
                "<b>Session expiree</b><br><br>" +
                "Votre session a expire pour des raisons de securite.<br>" +
                "Veuillez vous reconnecter pour continuer.</div></html>",
                "Session expiree",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            session.destroySession();
            redirectToLogin(mainWindow);
            return false;
        }
        
        return true;
    }
    
    public static boolean checkGuestAccess(Main mainWindow, String featureName) 
    {
        return checkGuestAccess(mainWindow, featureName, true);
    }
    
    public static boolean checkGuestAccess(Main mainWindow, String featureName, boolean showPrompt) 
    {
        UserSession session = UserSession.getInstance();
        
        if(session.isGuest()) 
        {
            if(showPrompt) 
            {
                int choice = JOptionPane.showConfirmDialog(
                    mainWindow,
                    "<html><div style='color:#00ffff;'>" +
                    "<b>Fonctionnalite limitee</b><br><br>" +
                    "La fonctionnalite <b>'" + featureName + "'</b> n'est disponible " +
                    "que pour les utilisateurs enregistres.<br><br>" +
                    "Avantages d'un compte :<br>" +
                    "• Acces a toutes les fonctionnalites<br>" +
                    "• Sauvegarde de vos resultats<br>" +
                    "• Historique des operations<br>" +
                    "• Personnalisation<br><br>" +
                    "Voulez-vous creer un compte maintenant ?</div></html>",
                    "Fonctionnalite limitee",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                if(choice == JOptionPane.YES_OPTION)
                    redirectToRegistration(mainWindow);
            }
            return false;
        }
        
        return true;
    }
    
    public static boolean checkPermission(Main mainWindow, String permission) 
    {
        UserSession session = UserSession.getInstance();
        
        if(!session.hasPermission(permission)) 
        {
            JOptionPane.showMessageDialog(
                mainWindow,
                "<html><div style='color:#ff5555;'>" +
                "<b>Permission refusee</b><br><br>" +
                "Vous n'avez pas les permissions necessaires pour acceder a cette fonctionnalite.<br>" +
                "Veuillez contacter un administrateur si vous pensez que c'est une erreur.</div></html>",
                "Permission refusee",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        
        return true;
    }
    
    public static void startInactivityMonitor(Main mainWindow) 
    {
        if (inactivityTimer != null)
            inactivityTimer.stop();
        
        inactivityTimer = new Timer(INACTIVITY_CHECK_INTERVAL, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                UserSession session = UserSession.getInstance();
                
                if(session.isLoggedIn()) 
                {
                    long inactivityMinutes = session.getInactivityDuration() / (60 * 1000);
                    
                    if(inactivityMinutes >= INACTIVITY_WARNING_MINUTES && 
                        inactivityMinutes < 30) 
                        showInactivityWarning(mainWindow);
                    
                    if(!session.isSessionValid()) 
                        forceLogout(mainWindow);
                }
            }
        });
        inactivityTimer.start();
    }
    
    private static void showInactivityWarning(Main mainWindow) 
    {
        UserSession session = UserSession.getInstance();
        
        int choice = JOptionPane.showConfirmDialog(
            mainWindow,
            "<html><div style='color:#ffff55;'>" +
            "<b>Inactivite detectee</b><br><br>" +
            "Votre session expirera dans " + (30 - session.getInactivityDuration() / (60*1000)) + 
            " minutes en raison de l'inactivite.<br>" +
            "Souhaitez-vous rester connecte ?</div></html>",
            "Session inactive",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if(choice == JOptionPane.YES_OPTION)
            session.updateActivity();
    }
    
    private static void forceLogout(Main mainWindow) 
    {
        UserSession session = UserSession.getInstance();
        
        JOptionPane.showMessageDialog(
            mainWindow,
            "<html><div style='color:#ff5555;'>" +
            "<b>Session expiree</b><br><br>" +
            "Votre session a ete fermee automatiquement apres 30 minutes d'inactivite.<br>" +
            "Veuillez vous reconnecter pour continuer.</div></html>",
            "Session expiree",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        session.destroySession();
        mainWindow.showHome();
    }
    
    public static void stopInactivityMonitor() 
    {
        if(inactivityTimer != null) 
        {
            inactivityTimer.stop();
            inactivityTimer = null;
        }
    }
    
    private static void redirectToLogin(Main mainWindow) 
    {
        mainWindow.getContentPane().removeAll();
        Connect loginPanel = new Connect(mainWindow);
        mainWindow.setContentPane(loginPanel);
        mainWindow.revalidate();
        mainWindow.repaint();
    }
    
    private static void redirectToRegistration(Main mainWindow) 
    {
        mainWindow.getContentPane().removeAll();
        crypto.users.Registration registrationPanel = new crypto.users.Registration(mainWindow);
        mainWindow.setContentPane(registrationPanel);
        mainWindow.revalidate();
        mainWindow.repaint();
    }
    
    public static void showSessionInfo(Component parent) 
    {
        UserSession session = UserSession.getInstance();
        
        if(session.isLoggedIn()) 
        {
            String userType   = session.isGuest() ? "Invite" : "Utilisateur";
            String userName   = session.getCurrentUser().getLogin();
            String duration   = session.getFormattedSessionDuration();
            String inactivity = session.getFormattedInactivityDuration();
            String status     = session.isSessionValid() ? "Active" : "Expiree";
            
            JOptionPane.showMessageDialog(
                parent,
                "<html><div style='color:#00ffff;'>" +
                "<b>Informations de session</b><br><br>" +
                "<table border='0' cellspacing='5'>" +
                "<tr><td><b>Type :</b></td><td>" + userType + "</td></tr>" +
                "<tr><td><b>Utilisateur :</b></td><td>" + userName + "</td></tr>" +
                "<tr><td><b>Duree :</b></td><td>" + duration + "</td></tr>" +
                "<tr><td><b>Inactivite :</b></td><td>" + inactivity + "</td></tr>" +
                "<tr><td><b>Statut :</b></td><td>" + status + "</td></tr>" +
                "</table></div></html>",
                "Informations de session",
                JOptionPane.INFORMATION_MESSAGE
            );
        } 
        else 
        {
            JOptionPane.showMessageDialog(
                parent,
                "<html><div style='color:#ffff55;'>Aucune session active.</div></html>",
                "Informations de session",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
