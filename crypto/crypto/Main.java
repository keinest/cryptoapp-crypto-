package crypto;

import crypto.view.MainWindow;
import crypto.utils.ThemeManager;
import crypto.session.UserSession;

import javax.swing.*;
import java.awt.*;

public class Main extends MainWindow
{
    // -- Compatibilite : methodes attendues par les anciennes vues -------------

    public void showHome()
    {
        getNav().showWelcome();
    }

    /** Navigue vers le tableau de bord utilisateur ou invite. */
    public void navigateToHome(boolean isGuest)
    {
        if (isGuest)
        {
            UserSession.getInstance().createGuestSession();
            getNav().showDashboard();
        }
        else
            getNav().showHome();
    }

    
    public static JButton createCyberButton(String text, Color background)
    {
        if (background != null)
        {
            int r = background.getRed(), g = background.getGreen(), b = background.getBlue();

            // Rouge = danger
            if (r > 180 && g < 100 && b < 100)
                return ThemeManager.pillButtonDanger(text);

            // Vert = succès / action principale
            if (g > 150 && r < 100)
                return ThemeManager.pillButtonSuccess(text);
        }
        return ThemeManager.pillButtonOutline(text);
    }

    // -- Point d'entree -------------------

    public static void main(String[] args)
    {
        MainWindow.main(args);
    }
}
