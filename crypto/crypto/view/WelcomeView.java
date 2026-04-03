package crypto.view;

import crypto.controller.NavigationController;
import crypto.controller.AuthController;
import crypto.utils.ThemeManager;

import javax.swing.*;
import java.awt.*;

/**
 * Page d'accueil - presentation de l'application + boutons d'entree.
 */
public class WelcomeView extends JPanel
{
    private final MainWindow           window;
    private final NavigationController nav;
    private final AuthController       auth;

    public WelcomeView(MainWindow window, NavigationController nav)
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
        setOpaque(true);

        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildHero(),      BorderLayout.CENTER);
        add(buildAlgoStrip(), BorderLayout.SOUTH);
    }

    // -- Top bar ---------------------

    private JPanel buildTopBar()
    {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(ThemeManager.BG_PANEL);
        bar.setPreferredSize(new Dimension(0, 60));
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.BORDER),
            BorderFactory.createEmptyBorder(0, 28, 0, 28)
        ));

        // Logo
        JLabel logo = new JLabel("◈  CryptoApp");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(ThemeManager.ACCENT);

        // Boutons à droite
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        right.setOpaque(false);

        JButton btnGuest    = ThemeManager.pillButtonGhost("Mode invite");
        JButton btnLogin    = ThemeManager.pillButton("Connexion");
        JButton btnRegister = ThemeManager.pillButtonOutline("Inscription");
        JButton btnExit     = ThemeManager.pillButtonDanger("Quitter");

        btnGuest.addActionListener(e -> auth.continueAsGuest());
        btnLogin.addActionListener(e -> nav.showLogin());
        btnRegister.addActionListener(e -> nav.showRegister());
        btnExit.addActionListener(e ->
        {
            SessionInterceptor.stopInactivityMonitor();
            window.dispose();
        });

        right.add(btnGuest);
        right.add(btnLogin);
        right.add(btnRegister);
        right.add(btnExit);

        bar.add(logo, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private static final class SessionInterceptor
    {
        static void stopInactivityMonitor()
        { crypto.session.SessionInterceptor.stopInactivityMonitor(); }
    }

    // -- Section heros ---------------

    private JPanel buildHero()
    {
        JPanel hero = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Degrade sombre
                GradientPaint gp = new GradientPaint(
                    0, 0,            ThemeManager.BG_APP,
                    0, getHeight(),  new Color(20, 20, 40)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Cercle decoratif accent
                g2.setColor(new Color(92, 107, 192, 18));
                g2.fillOval(getWidth() / 2 - 300, -150, 700, 700);
                g2.setColor(new Color(92, 107, 192, 8));
                g2.fillOval(getWidth() - 200, getHeight() / 2 - 200, 500, 500);

                g2.dispose();
            }
        };
        hero.setLayout(new GridBagLayout());
        hero.setOpaque(false);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setMaximumSize(new Dimension(720, 600));

        // Badge
        JLabel badge = new JLabel("  Suite cryptographique  ");
        badge.setFont(ThemeManager.FONT_SMALL);
        badge.setForeground(ThemeManager.ACCENT);
        badge.setOpaque(true);
        badge.setBackground(new Color(92, 107, 192, 28));
        badge.setBorder(new ThemeManager.RoundedBorder(ThemeManager.ACCENT_MUTED, 20, 1));
        badge.setBorder(BorderFactory.createCompoundBorder(
            new ThemeManager.RoundedBorder(new Color(92, 107, 192, 50), 20, 1),
            BorderFactory.createEmptyBorder(4, 14, 4, 14)
        ));
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Titre principal
        JLabel title = new JLabel("<html><div align='center'>Cryptographie<br><span style='color:#5C6BC0'>Moderne & Securisee</span></div></html>");
        title.setFont(new Font("Segoe UI", Font.BOLD, 46));
        title.setForeground(ThemeManager.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Sous-titre
        JLabel subtitle = new JLabel("<html><div align='center' style='width:480px;'>Explorez et appliquez les algorithmes classiques et modernes de cryptographie.<br>Chiffrez, dechiffrez et analysez vos messages en toute securite.</div></html>");
        subtitle.setFont(ThemeManager.FONT_BODY);
        subtitle.setForeground(ThemeManager.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        // Boutons CTA
        JPanel cta = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        cta.setOpaque(false);

        JButton btnStart  = ThemeManager.pillButton("Commencer - Inscription");
        JButton btnGuest2 = ThemeManager.pillButtonOutline("Accès rapide (Invite)");
        btnStart.setBorder(BorderFactory.createEmptyBorder(12, 32, 12, 32));
        btnGuest2.setBorder(BorderFactory.createEmptyBorder(12, 28, 12, 28));
        btnStart.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnStart.addActionListener(e -> nav.showRegister());
        btnGuest2.addActionListener(e -> new AuthController(nav).continueAsGuest());

        cta.add(btnStart);
        cta.add(btnGuest2);

        content.add(badge);
        content.add(Box.createVerticalStrut(28));
        content.add(title);
        content.add(Box.createVerticalStrut(18));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(36));
        content.add(cta);

        hero.add(content);
        return hero;
    }

    // -- Bande algorithmes -----------

    private JPanel buildAlgoStrip()
    {
        JPanel strip = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        strip.setBackground(ThemeManager.BG_PANEL);
        strip.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ThemeManager.BORDER));
        strip.setPreferredSize(new Dimension(0, 50));

        String[] algos = {"RSA", "Cesar", "Vigenère", "Vernam", "Hill", "Affine", "Feistel"};
        for (int i = 0; i < algos.length; i++)
        {
            if (i > 0)
            {
                JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
                sep.setForeground(ThemeManager.DIVIDER);
                sep.setPreferredSize(new Dimension(1, 22));
                strip.add(sep);
                strip.add(Box.createHorizontalStrut(8));
            }
            JLabel lbl = new JLabel(algos[i]);
            lbl.setFont(ThemeManager.FONT_SMALL);
            lbl.setForeground(ThemeManager.TEXT_MUTED);
            lbl.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
            strip.add(lbl);
        }

        return strip;
    }
}
