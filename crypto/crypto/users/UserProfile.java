package crypto.users;

import crypto.view.MainWindow;
import crypto.view.components.AppHeader;
import crypto.controller.NavigationController;
import crypto.session.UserSession;
import crypto.utils.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Tableau de bord de l'utilisateur connecté — thème corporate indigo.
 */
public class UserProfile extends JPanel
{
    private final MainWindow           window;
    private final NavigationController nav;
    private final UserSession          session;

    public UserProfile(MainWindow window)
    {
        this.window  = window;
        this.nav     = window.getNav();
        this.session = UserSession.getInstance();
        buildUI();
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

        if (!session.isLoggedIn() || session.getCurrentUser() == null)
        {
            nav.showWelcome();
            return body;
        }

        String userName = session.getCurrentUser().getLogin();

        JLabel titleLbl = new JLabel("Bon retour, " + userName + " !");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLbl.setForeground(ThemeManager.TEXT_PRIMARY);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subLbl = new JLabel("Session active depuis " + session.getFormattedSessionDuration());
        subLbl.setFont(ThemeManager.FONT_SMALL);
        subLbl.setForeground(ThemeManager.TEXT_MUTED);
        subLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(titleLbl);
        body.add(Box.createVerticalStrut(6));
        body.add(subLbl);
        body.add(Box.createVerticalStrut(36));
        body.add(buildSectionHeader("Algorithmes cryptographiques"));
        body.add(Box.createVerticalStrut(16));
        body.add(buildAlgoGrid());
        body.add(Box.createVerticalStrut(36));
        body.add(buildSectionHeader("Outils avancés"));
        body.add(Box.createVerticalStrut(16));
        body.add(buildAdvancedRow());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(ThemeManager.BG_APP);
        wrapper.add(body, BorderLayout.NORTH);
        return wrapper;
    }

    // ── Grille algorithmes ────────────────────────────────────────────────────

    private JPanel buildAlgoGrid()
    {
        JPanel grid = new JPanel(new GridLayout(0, 4, 16, 16));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        Object[][] algos = {
            {"🔐", "RSA",      (Runnable) nav::showRSA      },
            {"🔤", "César",    (Runnable) nav::showCesar    },
            {"🔑", "Vigenère", (Runnable) nav::showVigenere },
            {"🎲", "Vernam",   (Runnable) nav::showVernam   },
            {"🧮", "Hill",     (Runnable) nav::showHill     },
            {"➗", "Affine",   (Runnable) nav::showAffine   },
            {"⚙", "Feistel",  (Runnable) nav::showFeistel  },
        };

        for (Object[] a : algos)
            grid.add(buildAlgoCard((String)a[0], (String)a[1], (Runnable)a[2]));

        return grid;
    }

    private JPanel buildAlgoCard(String icon, String name, Runnable action)
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
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        card.setPreferredSize(new Dimension(0, 100));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        iconLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(ThemeManager.FONT_SUBTITLE);
        nameLbl.setForeground(ThemeManager.TEXT_PRIMARY);
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel arrow = new JLabel("→");
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 13));
        arrow.setForeground(ThemeManager.ACCENT);
        arrow.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(iconLbl);
        card.add(Box.createVerticalStrut(8));
        card.add(nameLbl);
        card.add(Box.createVerticalGlue());
        card.add(arrow);
        return card;
    }

    // ── Outils avancés ────────────────────────────────────────────────────────

    private JPanel buildAdvancedRow()
    {
        JPanel row = new JPanel(new GridLayout(1, 2, 16, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        row.add(buildAdvancedCard("🔍", "Cryptanalyse",
            "Attaques par force brute sur les algorithmes classiques.",
            () -> nav.showCryptAnalyst()));

        row.add(buildAdvancedCard("👤", "Mon profil",
            "Modifier vos informations personnelles et votre mot de passe.",
            () -> JOptionPane.showMessageDialog(window,
                "Disponible dans une prochaine version.", "Profil",
                JOptionPane.INFORMATION_MESSAGE)));
        return row;
    }

    private JPanel buildAdvancedCard(String icon, String title, String desc, Runnable action)
    {
        JPanel card = ThemeManager.cardPanel();
        card.setLayout(new BorderLayout(16, 0));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        iconLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(ThemeManager.FONT_SUBTITLE);
        titleLbl.setForeground(ThemeManager.ACCENT_HOVER);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLbl = new JLabel("<html><div style='width:260px'>" + desc + "</div></html>");
        descLbl.setFont(ThemeManager.FONT_SMALL);
        descLbl.setForeground(ThemeManager.TEXT_SECONDARY);
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        left.add(iconLbl);
        left.add(Box.createVerticalStrut(8));
        left.add(titleLbl);
        left.add(Box.createVerticalStrut(6));
        left.add(descLbl);

        JButton btn = ThemeManager.pillButton("Ouvrir →");
        btn.addActionListener(e -> action.run());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(btn);

        card.add(left,  BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JPanel buildSectionHeader(String label)
    {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(ThemeManager.TEXT_MUTED);

        JSeparator sep = new JSeparator();
        sep.setForeground(ThemeManager.DIVIDER);

        row.add(lbl, BorderLayout.WEST);
        row.add(sep, BorderLayout.CENTER);
        return row;
    }
}
