package crypto.view.auth;

import crypto.view.MainWindow;
import crypto.view.components.AppHeader;
import crypto.controller.NavigationController;
import crypto.controller.AuthController;
import crypto.utils.ThemeManager;
import crypto.utils.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Vue d'inscription — formulaire corporate avec validation inline.
 */
public class RegisterView extends JPanel
{
    private final MainWindow           window;
    private final NavigationController nav;
    private final AuthController       auth;

    private JTextField     loginField;
    private JTextField     emailField;
    private JPasswordField passField;
    private JPasswordField confirmField;
    private JLabel         errorLabel;
    private JLabel         strengthLabel;
    private JButton        btnSubmit;

    public RegisterView(MainWindow window, NavigationController nav)
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
        header.hideRegisterButton();
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
        card.setPreferredSize(new Dimension(440, 560));

        JLabel title = ThemeManager.subtitleLabel("Créer un compte");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Rejoignez CryptoApp en quelques secondes");
        sub.setFont(ThemeManager.FONT_SMALL);
        sub.setForeground(ThemeManager.TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Login
        JLabel loginLbl = ThemeManager.fieldLabel("Nom d'utilisateur");
        loginLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginField = ThemeManager.styledField("ex: jean_dupont");
        loginField.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        // Email
        JLabel emailLbl = ThemeManager.fieldLabel("Adresse email");
        emailLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField = ThemeManager.styledField("email@exemple.com");
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        // Mot de passe
        JLabel passLbl = ThemeManager.fieldLabel("Mot de passe");
        passLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        passField = ThemeManager.styledPasswordField();
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        strengthLabel = new JLabel(" ");
        strengthLabel.setFont(ThemeManager.FONT_SMALL);
        strengthLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passField.addKeyListener(new KeyAdapter()
        {
            @Override public void keyReleased(KeyEvent e) { updateStrengthIndicator(); }
        });

        // Confirmation
        JLabel confirmLbl = ThemeManager.fieldLabel("Confirmer le mot de passe");
        confirmLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmField = ThemeManager.styledPasswordField();
        confirmField.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        // Afficher mot de passe
        JCheckBox showPass = new JCheckBox("Afficher les mots de passe");
        showPass.setFont(ThemeManager.FONT_SMALL);
        showPass.setForeground(ThemeManager.TEXT_MUTED);
        showPass.setOpaque(false);
        showPass.setFocusPainted(false);
        showPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        showPass.addActionListener(e ->
        {
            char echo = showPass.isSelected() ? (char) 0 : '•';
            passField.setEchoChar(echo);
            confirmField.setEchoChar(echo);
        });

        // Erreur
        errorLabel = new JLabel(" ");
        errorLabel.setFont(ThemeManager.FONT_SMALL);
        errorLabel.setForeground(ThemeManager.ERROR);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Bouton
        btnSubmit = ThemeManager.pillButton("Créer mon compte");
        btnSubmit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSubmit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.addActionListener(e -> attemptRegister());

        // Lien connexion
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        linkPanel.setOpaque(false);
        linkPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel linkText = new JLabel("Déjà un compte ?");
        linkText.setFont(ThemeManager.FONT_SMALL);
        linkText.setForeground(ThemeManager.TEXT_MUTED);
        JButton linkLogin = makeLinkButton("Se connecter");
        linkLogin.addActionListener(e -> nav.showLogin());
        linkPanel.add(linkText);
        linkPanel.add(linkLogin);

        JSeparator sep = ThemeManager.separator();
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(24));
        card.add(loginLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(loginField);
        card.add(Box.createVerticalStrut(14));
        card.add(emailLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(emailField);
        card.add(Box.createVerticalStrut(14));
        card.add(passLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(passField);
        card.add(Box.createVerticalStrut(4));
        card.add(strengthLabel);
        card.add(Box.createVerticalStrut(14));
        card.add(confirmLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(confirmField);
        card.add(Box.createVerticalStrut(8));
        card.add(showPass);
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(btnSubmit);
        card.add(Box.createVerticalStrut(20));
        card.add(sep);
        card.add(Box.createVerticalStrut(14));
        card.add(linkPanel);

        return card;
    }

    private void attemptRegister()
    {
        errorLabel.setText(" ");
        btnSubmit.setEnabled(false);
        btnSubmit.setText("Création...");

        String login   = loginField.getText().trim();
        String email   = emailField.getText().trim();
        String pass    = new String(passField.getPassword());
        String confirm = new String(confirmField.getPassword());

        auth.register(login, email, pass, confirm, new AuthController.AuthCallback()
        {
            @Override
            public void onSuccess()
            {
                JOptionPane.showMessageDialog(window,
                    "<html><body style='font-family:Segoe UI;font-size:13px;'>Compte créé avec succès !<br>Vous pouvez maintenant vous connecter.</body></html>",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                nav.showLogin();
            }

            @Override
            public void onError(String message)
            {
                errorLabel.setText(message);
                btnSubmit.setEnabled(true);
                btnSubmit.setText("Créer mon compte");
            }
        });
    }

    private void updateStrengthIndicator()
    {
        String pass = new String(passField.getPassword());
        if (pass.isEmpty()) { strengthLabel.setText(" "); return; }

        if (Util.isStrongPassword(pass))
        {
            strengthLabel.setText("✓ Mot de passe fort");
            strengthLabel.setForeground(ThemeManager.SUCCESS);
        }
        else if (pass.length() >= 6)
        {
            strengthLabel.setText("⚠ Mot de passe moyen (ajoutez chiffres et caractères spéciaux)");
            strengthLabel.setForeground(ThemeManager.WARNING);
        }
        else
        {
            strengthLabel.setText("✗ Mot de passe trop court (minimum 8 caractères)");
            strengthLabel.setForeground(ThemeManager.ERROR);
        }
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
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setForeground(ThemeManager.ACCENT_HOVER); }
            @Override public void mouseExited(java.awt.event.MouseEvent e)  { btn.setForeground(ThemeManager.ACCENT); }
        });
        return btn;
    }
}
