package crypto.view.components;

import crypto.view.MainWindow;
import crypto.controller.NavigationController;
import crypto.controller.AuthController;
import crypto.session.UserSession;
import crypto.utils.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * En-tête d'application unifiée — style corporate.
 * Affiche le logo, l'info utilisateur et les boutons de navigation contextuels.
 */
public class AppHeader extends JPanel
{
    private final MainWindow           window;
    private final NavigationController nav;
    private final AuthController       auth;
    private       JLabel               userInfoLabel;
    private       Timer                sessionTimer;

    // Boutons (visibilité dynamique)
    private JButton btnHome;
    private JButton btnLogout;
    private JButton btnLogin;
    private JButton btnRegister;
    private JButton btnCryptAnalyst;

    public AppHeader(MainWindow window, NavigationController nav)
    {
        this.window = window;
        this.nav    = nav;
        this.auth   = new AuthController(nav);
        buildUI();
        startSessionTimer();
    }

    // ── Construction UI ───────────────────────────────────────────────────────

    private void buildUI()
    {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(ThemeManager.BG_PANEL);
        setPreferredSize(new Dimension(0, 64));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.BORDER));

        // ── Logo (gauche) ────────────────────────────────────────────────────
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        logoPanel.setOpaque(false);

        JLabel logo = new JLabel("◈ CryptoApp");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(ThemeManager.ACCENT);
        logo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logo.addMouseListener(new MouseAdapter()
        {
            @Override public void mouseClicked(MouseEvent e) { nav.showWelcome(); }
        });
        logoPanel.add(logo);

        // ── Info session (centre) ─────────────────────────────────────────────
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        centerPanel.setOpaque(false);

        userInfoLabel = new JLabel();
        userInfoLabel.setFont(ThemeManager.FONT_SMALL);
        userInfoLabel.setForeground(ThemeManager.TEXT_MUTED);
        updateUserInfo();
        centerPanel.add(userInfoLabel);

        // ── Boutons nav (droite) ──────────────────────────────────────────────
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        btnHome         = ThemeManager.pillButtonGhost("Accueil");
        btnCryptAnalyst = ThemeManager.pillButtonOutline("Cryptanalyse");
        btnLogin        = ThemeManager.pillButton("Connexion");
        btnRegister     = ThemeManager.pillButtonOutline("Inscription");
        btnLogout       = ThemeManager.pillButtonDanger("Déconnexion");

        // Taille compacte pour l'en-tête
        for (JButton b : new JButton[]{btnHome, btnCryptAnalyst, btnLogin, btnRegister, btnLogout})
            b.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));

        btnHome.addActionListener(e -> nav.showHome());
        btnLogin.addActionListener(e -> nav.showLogin());
        btnRegister.addActionListener(e -> nav.showRegister());
        btnLogout.addActionListener(e -> auth.logout(window));
        btnCryptAnalyst.addActionListener(e -> nav.showCryptAnalyst());

        rightPanel.add(btnHome);
        rightPanel.add(btnCryptAnalyst);
        rightPanel.add(btnLogin);
        rightPanel.add(btnRegister);
        rightPanel.add(btnLogout);

        add(logoPanel,  BorderLayout.WEST);
        add(centerPanel,BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Centrage vertical
        setAlignmentY(Component.CENTER_ALIGNMENT);
        refreshButtons();
    }

    // ── Visibilité contextuelle ────────────────────────────────────────────────

    public void refreshButtons()
    {
        UserSession session = UserSession.getInstance();
        boolean loggedIn = session.isLoggedIn();
        boolean isGuest  = session.isGuest();

        btnHome.setVisible(loggedIn);
        btnCryptAnalyst.setVisible(loggedIn && !isGuest);
        btnLogout.setVisible(loggedIn);
        btnLogin.setVisible(!loggedIn);
        btnRegister.setVisible(!loggedIn);

        updateUserInfo();
        revalidate();
        repaint();
    }

    public void hideHomeButton()      { btnHome.setVisible(false); }
    public void hideLoginButton()     { btnLogin.setVisible(false); }
    public void hideRegisterButton()  { btnRegister.setVisible(false); }
    public void hideLogoutButton()    { btnLogout.setVisible(false); }
    public void hideAnalystButton()   { btnCryptAnalyst.setVisible(false); }

    // ── Session timer ─────────────────────────────────────────────────────────

    private void startSessionTimer()
    {
        sessionTimer = new Timer(5000, e ->
        {
            updateUserInfo();
            UserSession session = UserSession.getInstance();
            if (session.isLoggedIn() && !session.isSessionValid())
            {
                sessionTimer.stop();
                handleSessionExpired();
            }
        });
        sessionTimer.start();
    }

    private void updateUserInfo()
    {
        SwingUtilities.invokeLater(() ->
        {
            UserSession s = UserSession.getInstance();
            if (s.isLoggedIn())
            {
                String name = s.getCurrentUser().getLogin();
                String type = s.isGuest() ? "Invité" : "Connecté";
                userInfoLabel.setText(name + " · " + type + "  |  " + s.getFormattedSessionDuration());
            }
            else
                userInfoLabel.setText("");
        });
    }

    private void handleSessionExpired()
    {
        UserSession.getInstance().destroySession();
        JOptionPane.showMessageDialog(window,
            "<html><body style='font-family:Segoe UI;'>Votre session a expiré. Veuillez vous reconnecter.</body></html>",
            "Session expirée", JOptionPane.WARNING_MESSAGE);
        nav.showWelcome();
    }

    public void stopTimer()
    {
        if (sessionTimer != null) sessionTimer.stop();
    }

    @Override
    public void removeNotify()
    {
        stopTimer();
        super.removeNotify();
    }
}
