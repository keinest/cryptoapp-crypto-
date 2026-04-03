package crypto.view;

import crypto.controller.NavigationController;
import crypto.session.SessionInterceptor;
import crypto.utils.ThemeManager;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;

/**
 * Fenetre principale de l'application.
 * Responsabilite unique : contenir les vues, configurer le L&F.
 * Toute la navigation passe par NavigationController.
 */
public class MainWindow extends JFrame
{
    private NavigationController nav;

    public MainWindow()
    {
        configureWindow();
        this.nav = new NavigationController(this);
        SessionInterceptor.startInactivityMonitor(this);
        nav.showWelcome();
    }

    private void configureWindow()
    {
        setTitle("CryptoApp — Bienvenue");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 820);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        getContentPane().setBackground(ThemeManager.BG_APP);
    }

    public NavigationController getNav() { return nav; }

    /**
     * Affiche une vue dans la fenetre principale avec un rafraîchissement homogène.
     */
    public void showView(JPanel view, String title)
    {
        SwingUtilities.invokeLater(() ->
        {
            setTitle(title);
            setContentPane(view);
            revalidate();
            repaint();
        });
    }

    // == Point d'entree ========================================================

    public static void main(String[] args)
    {
        applyLookAndFeel();
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }

    private static void applyLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            UIManager.put("control",              ThemeManager.BG_PANEL);
            UIManager.put("info",                 ThemeManager.BG_SURFACE);
            UIManager.put("nimbusBase",           ThemeManager.BG_APP);
            UIManager.put("nimbusFocus",          ThemeManager.ACCENT);
            UIManager.put("nimbusLightBackground",ThemeManager.BG_PANEL);
            UIManager.put("text",                 ThemeManager.TEXT_PRIMARY);
            UIManager.put("nimbusSelectedText",   ThemeManager.TEXT_ON_ACCENT);
            UIManager.put("nimbusSelectionBackground", ThemeManager.ACCENT);
        }
        catch (UnsupportedLookAndFeelException e)
        {
            System.out.println("[MainWindow] Nimbus non disponible, theme par defaut utilise.");
        }
    }
}
