package crypto.view.auth;

import crypto.view.MainWindow;
import crypto.view.components.AppHeader;
import crypto.controller.NavigationController;
import crypto.controller.AuthController;
import crypto.utils.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Vue de connexion — formulaire épuré style corporate.
 */
public class LoginView extends JPanel
{
    private final MainWindow           window;
    private final NavigationController nav;
    private final AuthController       auth;

    private JTextField    emailField;
    private JPasswordField passwordField;
    private JLabel        errorLabel;
    private JButton       btnLogin;

    public LoginView(MainWindow window, NavigationController nav)
    {
        this.window = window;
        this.nav    = nav;
        this.auth   = new AuthController(nav);
        buildUI();
    }

    private void buildUI()
    {
        setLayout(new BorderLayout());
        setBackground(ThemeManager.BG_APP);

        AppHeader header = new AppHeader(window, nav);
        header.hideLoginButton();
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(ThemeManager.BG_APP);
        center.add(buildCard());
        add(center, BorderLayout.CENTER);
    }

    private JPanel buildCard()
    {
        JPanel card = ThemeManager.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(420, 430));

        // En-tête de carte
        JLabel title = ThemeManager.subtitleLabel("Connexion");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Bienvenue — entrez vos identifiants");
        sub.setFont(ThemeManager.FONT_SMALL);
        sub.setForeground(ThemeManager.TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Champ email
        JLabel emailLbl = ThemeManager.fieldLabel("Adresse email");
        emailLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField = ThemeManager.styledField("email@exemple.com");
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        // Champ mot de passe
        JLabel passLbl = ThemeManager.fieldLabel("Mot de passe");
        passLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField = ThemeManager.styledPasswordField();
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        // Option "Afficher mot de passe"
        JCheckBox showPass = new JCheckBox("Afficher le mot de passe");
        showPass.setFont(ThemeManager.FONT_SMALL);
        showPass.setForeground(ThemeManager.TEXT_MUTED);
        showPass.setBackground(new Color(0,0,0,0));
        showPass.setOpaque(false);
        showPass.setFocusPainted(false);
        showPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        showPass.addActionListener(e ->
            passwordField.setEchoChar(showPass.isSelected() ? (char)0 : '•'));

        // Message d'erreur
        errorLabel = new JLabel(" ");
        errorLabel.setFont(ThemeManager.FONT_SMALL);
        errorLabel.setForeground(ThemeManager.ERROR);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Bouton connexion
        btnLogin = ThemeManager.pillButton("Se connecter");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.addActionListener(e -> attemptLogin());

        // Enter key
        passwordField.addKeyListener(new KeyAdapter()
        {
            @Override public void keyPressed(KeyEvent e)
            { if (e.getKeyCode() == KeyEvent.VK_ENTER) attemptLogin(); }
        });

        // Lien inscription
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        linkPanel.setOpaque(false);
        linkPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel linkText = new JLabel("Pas encore de compte ?");
        linkText.setFont(ThemeManager.FONT_SMALL);
        linkText.setForeground(ThemeManager.TEXT_MUTED);
        JButton linkRegister = makeLinkButton("S'inscrire");
        linkRegister.addActionListener(e -> nav.showRegister());
        linkPanel.add(linkText);
        linkPanel.add(linkRegister);

        // Séparateur
        JSeparator sep = ThemeManager.separator();
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // Mode invité
        JButton btnGuest = ThemeManager.pillButtonGhost("Continuer sans compte →");
        btnGuest.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGuest.addActionListener(e -> new AuthController(nav).continueAsGuest());

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(28));
        card.add(emailLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(emailField);
        card.add(Box.createVerticalStrut(16));
        card.add(passLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(8));
        card.add(showPass);
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(20));
        card.add(sep);
        card.add(Box.createVerticalStrut(16));
        card.add(linkPanel);
        card.add(Box.createVerticalStrut(8));
        card.add(btnGuest);

        return card;
    }

    private void attemptLogin()
    {
        errorLabel.setText(" ");
        btnLogin.setEnabled(false);
        btnLogin.setText("Connexion...");

        String email = emailField.getText().trim();
        String pass  = new String(passwordField.getPassword());

        auth.login(email, pass, new AuthController.AuthCallback()
        {
            @Override
            public void onSuccess()
            {
                nav.showHome();
            }

            @Override
            public void onError(String message)
            {
                errorLabel.setText(message);
                btnLogin.setEnabled(true);
                btnLogin.setText("Se connecter");
                passwordField.setText("");
            }
        });
    }

    private JButton makeLinkButton(String text)
    {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(ThemeManager.ACCENT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter()
        {
            @Override public void mouseEntered(MouseEvent e) { btn.setForeground(ThemeManager.ACCENT_HOVER); }
            @Override public void mouseExited(MouseEvent e)  { btn.setForeground(ThemeManager.ACCENT); }
        });
        return btn;
    }
}
