package crypto.session;

import crypto.view.MainWindow;

import javax.swing.*;
import java.awt.Component;

/**
 * Intercepteur de session.
 * Surveille l'inactivite et les acces non autorises.
 */
public class SessionInterceptor
{
    private static Timer inactivityTimer;
    private static final int INACTIVITY_CHECK_INTERVAL  = 60_000;
    private static final int INACTIVITY_WARNING_MINUTES = 8;

    public static boolean checkSessionAndRedirect(MainWindow window)
    {
        return checkSessionAndRedirect(window, true);
    }

    public static boolean checkSessionAndRedirect(MainWindow window, boolean showPrompt)
    {
        UserSession session = UserSession.getInstance();
        session.updateActivity();

        if (!session.isLoggedIn())
        {
            if (showPrompt)
            {
                int choice = JOptionPane.showConfirmDialog(window,
                    "<html><body style='font-family:Segoe UI'>Connexion requise pour cette fonctionnalite.<br>Se connecter maintenant ?</body></html>",
                    "Connexion requise", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) window.getNav().showLogin();
            }
            else window.getNav().showLogin();
            return false;
        }

        if (!session.isSessionValid())
        {
            session.destroySession();
            JOptionPane.showMessageDialog(window,
                "<html><body style='font-family:Segoe UI'>Votre session a expire. Veuillez vous reconnecter.</body></html>",
                "Session expiree", JOptionPane.WARNING_MESSAGE);
            window.getNav().showWelcome();
            return false;
        }

        return true;
    }

    public static boolean checkGuestAccess(MainWindow window, String featureName)
    {
        return checkGuestAccess(window, featureName, true);
    }

    public static boolean checkGuestAccess(MainWindow window, String featureName, boolean showPrompt)
    {
        if (UserSession.getInstance().isGuest())
        {
            if (showPrompt)
            {
                int c = JOptionPane.showConfirmDialog(window,
                    "<html><body style='font-family:Segoe UI'><b>" + featureName + "</b> est reserve aux comptes enregistres.<br>Creer un compte ?</body></html>",
                    "Fonctionnalite limitee", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (c == JOptionPane.YES_OPTION) window.getNav().showRegister();
            }
            return false;
        }
        return true;
    }

    public static boolean checkPermission(MainWindow window, String permission)
    {
        if (!UserSession.getInstance().hasPermission(permission))
        {
            JOptionPane.showMessageDialog(window,
                "<html><body style='font-family:Segoe UI'>Permission insuffisante.</body></html>",
                "Acces refuse", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static void startInactivityMonitor(MainWindow window)
    {
        if (inactivityTimer != null) inactivityTimer.stop();

        inactivityTimer = new Timer(INACTIVITY_CHECK_INTERVAL, e ->
        {
            UserSession session = UserSession.getInstance();
            if (!session.isLoggedIn()) return;

            if (session.getInactivityDuration() / 60_000 >= INACTIVITY_WARNING_MINUTES)
            {
                int c = JOptionPane.showConfirmDialog(window,
                    "<html><body style='font-family:Segoe UI'>Session bientot expiree. Rester connecte ?</body></html>",
                    "Inactivite detectee", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (c == JOptionPane.YES_OPTION) session.updateActivity();
            }

            if (!session.isSessionValid())
            {
                session.destroySession();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(window, "Session fermee apres inactivite.", "Session expiree", JOptionPane.INFORMATION_MESSAGE);
                    window.getNav().showWelcome();
                });
            }
        });
        inactivityTimer.start();
    }

    public static void stopInactivityMonitor()
    {
        if (inactivityTimer != null) { inactivityTimer.stop(); inactivityTimer = null; }
    }

    public static void showSessionInfo(Component parent)
    {
        UserSession s = UserSession.getInstance();
        if (!s.isLoggedIn()) { JOptionPane.showMessageDialog(parent, "Aucune session active.", "Session", JOptionPane.INFORMATION_MESSAGE); return; }

        JOptionPane.showMessageDialog(parent,
            "<html><body style='font-family:Segoe UI'>" +
            "<b>Utilisateur :</b> " + s.getCurrentUser().getLogin() + "<br>" +
            "<b>Type :</b> " + (s.isGuest() ? "Invite" : "Connecte") + "<br>" +
            "<b>Duree :</b> " + s.getFormattedSessionDuration() + "<br>" +
            "<b>Inactivite :</b> " + s.getFormattedInactivityDuration() +
            "</body></html>", "Informations de session", JOptionPane.INFORMATION_MESSAGE);
    }
}
