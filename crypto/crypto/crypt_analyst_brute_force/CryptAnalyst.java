package crypto.crypt_analyst_brute_force;

import crypto.view.MainWindow;
import crypto.view.components.AppHeader;
import crypto.controller.NavigationController;
import crypto.session.UserSession;
import crypto.utils.ThemeManager;
import crypto.crypt_analyst_brute_force.rsa_brute_force.RSABruterForce;
import crypto.crypt_analyst_brute_force.cesar_brute_force.CesarBruterForce;
import crypto.crypt_analyst_brute_force.affine_brute_force.AffineBruterForce;
import crypto.crypt_analyst_brute_force.vernam_brute_force.VernamBruterForce;
import crypto.crypt_analyst_brute_force.vigenere_brute_force.VigenereBruterForce;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Tableau de bord de cryptanalyse — theme corporate indigo.
 */
public class CryptAnalyst extends JPanel
{
    private final MainWindow           window;
    private final NavigationController nav;

    public CryptAnalyst(MainWindow window)
    {
        this.window = window;
        this.nav    = window.getNav();

        UserSession.getInstance().updateActivity();
        buildUI();
    }

    public MainWindow getWindow() { return window; }

    public void restoreHome()
    {
        window.showView(this, "CryptoApp — Cryptanalyse");
    }

    private void buildUI()
    {
        setLayout(new BorderLayout());
        setBackground(ThemeManager.BG_APP);

        AppHeader header = new AppHeader(window, nav);
        header.hideLoginButton();
        header.hideRegisterButton();
        add(header, BorderLayout.NORTH);

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

        JLabel title = ThemeManager.titleLabel("Cryptanalyse par force brute");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Selectionnez un algorithme pour lancer l'analyse");
        sub.setFont(ThemeManager.FONT_BODY);
        sub.setForeground(ThemeManager.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(title);
        body.add(Box.createVerticalStrut(6));
        body.add(sub);
        body.add(Box.createVerticalStrut(32));
        body.add(buildGrid());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(ThemeManager.BG_APP);
        wrapper.add(body, BorderLayout.NORTH);
        return wrapper;
    }

    private JPanel buildGrid()
    {
        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        grid.add(buildCard("RSA",      "Factorisation de cles RSA faibles.",            () -> open(new RSABruterForce(this),      "RSA - Force brute")));
        grid.add(buildCard("Cesar",    "Essai des 26 decalages possibles.",              () -> open(new CesarBruterForce(this),    "Cesar - Force brute")));
        grid.add(buildCard("Affine",   "Enumeration de toutes les paires (a, b).",      () -> open(new AffineBruterForce(this),   "Affine - Force brute")));
        grid.add(buildCard("Vigenere", "Attaque par dictionnaire sur la longueur cle.",  () -> open(new VigenereBruterForce(this), "Vigenere - Force brute")));
        grid.add(buildCard("Vernam",   "Recherche exhaustive de cle courte.",            () -> open(new VernamBruterForce(this),   "Vernam - Force brute")));
        grid.add(buildCardDisabled("Hill",    "Analyse matricielle - a venir."));
        grid.add(buildCardDisabled("Feistel", "Analyse de tour - a venir."));

        return grid;
    }

    private JPanel buildCard(String name, String desc, Runnable action)
    {
        JPanel card = new JPanel()
        {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter()
                {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); }
                    @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                    @Override public void mouseClicked(MouseEvent e) { action.run(); }
                });
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
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, ThemeManager.RADIUS_CARD, ThemeManager.RADIUS_CARD);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        card.setPreferredSize(new Dimension(0, 140));

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(ThemeManager.FONT_SUBTITLE);
        nameLbl.setForeground(ThemeManager.TEXT_PRIMARY);
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLbl = new JLabel("<html><div style='width:180px'>" + desc + "</div></html>");
        descLbl.setFont(ThemeManager.FONT_SMALL);
        descLbl.setForeground(ThemeManager.TEXT_MUTED);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel arrow = new JLabel("Lancer l'analyse ->");
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 11));
        arrow.setForeground(ThemeManager.ACCENT);
        arrow.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(nameLbl);
        card.add(Box.createVerticalStrut(8));
        card.add(descLbl);
        card.add(Box.createVerticalGlue());
        card.add(arrow);
        return card;
    }

    private JPanel buildCardDisabled(String name, String desc)
    {
        JPanel card = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ThemeManager.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), ThemeManager.RADIUS_CARD, ThemeManager.RADIUS_CARD);
                g2.setColor(ThemeManager.DIVIDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, ThemeManager.RADIUS_CARD, ThemeManager.RADIUS_CARD);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        card.setPreferredSize(new Dimension(0, 140));

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(ThemeManager.FONT_SUBTITLE);
        nameLbl.setForeground(ThemeManager.TEXT_MUTED);
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLbl = new JLabel("<html><div style='width:180px'>" + desc + "</div></html>");
        descLbl.setFont(ThemeManager.FONT_SMALL);
        descLbl.setForeground(ThemeManager.TEXT_MUTED);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel badge = new JLabel("Bientot disponible");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(ThemeManager.TEXT_MUTED);
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(nameLbl);
        card.add(Box.createVerticalStrut(8));
        card.add(descLbl);
        card.add(Box.createVerticalGlue());
        card.add(badge);
        return card;
    }

    private void open(JPanel panel, String title)
    {
        window.showView(panel, title);
    }
}
