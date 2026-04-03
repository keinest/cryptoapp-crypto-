package crypto.view;

import crypto.view.components.AppHeader;
import crypto.controller.NavigationController;
import crypto.session.UserSession;
import crypto.utils.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Tableau de bord principal — grille de cartes d'algorithmes.
 */
public class HomeView extends JPanel
{
    private final MainWindow           window;
    private final NavigationController nav;

    private static final String[][] ALGORITHMS = {
        {"🔐", "RSA",      "Chiffrement asymetrique a cles publique/privee.",          "rsa"},
        {"🔤", "Cesar",    "Decalage alphabetique classique de Jules Cesar.",           "cesar"},
        {"🔑", "Vigenère", "Chiffrement polyalphabetique par mot-cle.",                "vigenere"},
        {"🎲", "Vernam",   "Masque jetable — confidentialite parfaite.",               "vernam"},
        {"🧮", "Hill",     "Chiffrement matriciel par blocs de lettres.",              "hill"},
        {"➗", "Affine",   "Combinaison multiplication + decalage modulaire.",          "affine"},
        {"⚙️", "Feistel",  "Reseau de Feistel — structure des chiffrements modernes.", "feistel"},
    };

    public HomeView(MainWindow window, NavigationController nav)
    {
        this.window = window;
        this.nav    = nav;
        buildUI();
    }

    private void buildUI()
    {
        setLayout(new BorderLayout());
        setBackground(ThemeManager.BG_APP);
        setOpaque(true);

        // En-tête
        AppHeader header = new AppHeader(window, nav);
        header.hideLoginButton();
        header.hideRegisterButton();
        add(header, BorderLayout.NORTH);

        // Corps scrollable
        JPanel body = buildBody();
        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildBody()
    {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(ThemeManager.BG_APP);
        body.setBorder(BorderFactory.createEmptyBorder(36, 48, 48, 48));

        // Titre de section
        UserSession session = UserSession.getInstance();
        String greeting = session.isGuest()
            ? "Bienvenue, Invite"
            : "Bienvenue, " + session.getCurrentUser().getLogin();

        JLabel title = ThemeManager.titleLabel(greeting);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Choisissez un algorithme cryptographique pour commencer");
        subtitle.setFont(ThemeManager.FONT_BODY);
        subtitle.setForeground(ThemeManager.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(title);
        body.add(Box.createVerticalStrut(6));
        body.add(subtitle);
        body.add(Box.createVerticalStrut(32));

        // Grille de cartes
        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String[] algo : ALGORITHMS)
            grid.add(buildAlgoCard(algo[0], algo[1], algo[2], algo[3]));

        // Si mode connecte : ajouter carte cryptanalyse
        if (!session.isGuest())
        {
            grid.add(buildAnalystCard());
        }

        body.add(grid);

        // Wrapper pour aligner a gauche
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(ThemeManager.BG_APP);
        wrapper.add(body, BorderLayout.NORTH);
        return wrapper;
    }

    private JPanel buildAlgoCard(String icon, String name, String description, String key)
    {
        JPanel card = new JPanel()
        {
            private boolean hovered = false;

            {
                addMouseListener(new MouseAdapter()
                {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) { handleClick(key); }
                });
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? ThemeManager.BG_SURFACE : ThemeManager.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), ThemeManager.RADIUS_CARD, ThemeManager.RADIUS_CARD);
                g2.setColor(hovered ? ThemeManager.ACCENT : ThemeManager.BORDER);
                g2.setStroke(new BasicStroke(hovered ? 1.5f : 1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ThemeManager.RADIUS_CARD, ThemeManager.RADIUS_CARD);
                g2.dispose();
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        card.setPreferredSize(new Dimension(0, 160));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(ThemeManager.FONT_SUBTITLE);
        nameLabel.setForeground(ThemeManager.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><div style='width:200px'>" + description + "</div></html>");
        descLabel.setFont(ThemeManager.FONT_SMALL);
        descLabel.setForeground(ThemeManager.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel arrow = new JLabel("Ouvrir →");
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 12));
        arrow.setForeground(ThemeManager.ACCENT);
        arrow.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(descLabel);
        card.add(Box.createVerticalGlue());
        card.add(arrow);

        return card;
    }

    private JPanel buildAnalystCard()
    {
        JPanel card = new JPanel()
        {
            private boolean hovered = false;

            {
                addMouseListener(new MouseAdapter()
                {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) { nav.showCryptAnalyst(); }
                });
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Card speciale avec fond legèrement teinte indigo
                g2.setColor(hovered ? new Color(40, 44, 80) : new Color(30, 32, 62));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), ThemeManager.RADIUS_CARD, ThemeManager.RADIUS_CARD);
                g2.setColor(hovered ? ThemeManager.ACCENT_HOVER : ThemeManager.ACCENT);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ThemeManager.RADIUS_CARD, ThemeManager.RADIUS_CARD);
                g2.dispose();
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        card.setPreferredSize(new Dimension(0, 160));

        JLabel iconLabel = new JLabel("🔍");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel("Cryptanalyse");
        nameLabel.setFont(ThemeManager.FONT_SUBTITLE);
        nameLabel.setForeground(ThemeManager.ACCENT_HOVER);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><div style='width:200px'>Attaques par force brute sur les chiffrements classiques.</div></html>");
        descLabel.setFont(ThemeManager.FONT_SMALL);
        descLabel.setForeground(ThemeManager.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel arrow = new JLabel("Ouvrir →");
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 12));
        arrow.setForeground(ThemeManager.ACCENT);
        arrow.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(descLabel);
        card.add(Box.createVerticalGlue());
        card.add(arrow);

        return card;
    }

    private void handleClick(String key)
    {
        switch (key)
        {
            case "rsa"      -> nav.showRSA();
            case "cesar"    -> nav.showCesar();
            case "vigenere" -> nav.showVigenere();
            case "vernam"   -> nav.showVernam();
            case "hill"     -> nav.showHill();
            case "affine"   -> nav.showAffine();
            case "feistel"  -> nav.showFeistel();
        }
    }
}
