package crypto.utils;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * ThemeManager — Design system corporate/indigo.
 * Palette unique : fond sombre + accent indigo #5C6BC0.
 */
public class ThemeManager
{
    // -- Backgrounds --------------------
    public static final Color BG_APP      = new Color(14, 14, 26);   // fenêtre principale
    public static final Color BG_PANEL    = new Color(20, 20, 38);   // panneaux
    public static final Color BG_CARD     = new Color(26, 26, 46);   // cartes
    public static final Color BG_SURFACE  = new Color(32, 32, 58);   // surfaces élevées
    public static final Color BG_INPUT    = new Color(22, 22, 40);   // champs de saisie

    // -- Accent Indigo -------------------
    public static final Color ACCENT         = new Color(92, 107, 192);   // #5C6BC0
    public static final Color ACCENT_HOVER   = new Color(121, 134, 203);  // #7986CB
    public static final Color ACCENT_PRESSED = new Color(63,  81,  181);  // #3F51B5
    public static final Color ACCENT_MUTED   = new Color(92, 107, 192, 45);

    // -- Statuts -------------------------
    public static final Color SUCCESS = new Color(76,  175, 80);   // vert
    public static final Color ERROR   = new Color(239, 83,  80);   // rouge
    public static final Color WARNING = new Color(255, 183, 77);   // orange
    public static final Color INFO    = new Color(41,  182, 246);  // bleu ciel

    // -- Texte ----------------------------
    public static final Color TEXT_PRIMARY   = new Color(232, 232, 240);
    public static final Color TEXT_SECONDARY = new Color(158, 158, 184);
    public static final Color TEXT_MUTED     = new Color(100, 100, 130);
    public static final Color TEXT_ON_ACCENT = Color.WHITE;

    // -- Bordures -------------------------
    public static final Color BORDER        = new Color(42,  42, 72);
    public static final Color BORDER_FOCUS  = ACCENT;
    public static final Color BORDER_ERROR  = ERROR;

    // -- Séparateur -----------------------
    public static final Color DIVIDER = new Color(38, 38, 62);

    // -- Polices --------------------------
    public static final Font FONT_TITLE    = new Font("Segoe UI", Font.BOLD,  26);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD,  18);
    public static final Font FONT_BODY     = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL    = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON   = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FONT_LABEL    = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FONT_MONO     = new Font("Consolas", Font.PLAIN, 13);
    public static final Font FONT_MONO_SM  = new Font("Consolas", Font.PLAIN, 12);

    // -- Arrondi commun -------------------
    public static final int RADIUS_PILL  = 50;   // boutons pill
    public static final int RADIUS_CARD  = 14;   // cartes
    public static final int RADIUS_INPUT = 10;   // champs

    // ------------------------------------
    //  Fabriques de composants
    // ------------------------------------

    /** Bouton Pill — style primaire (rempli, indigo). */
    public static JButton pillButton(String text)
    {
        return pillButton(text, ACCENT, TEXT_ON_ACCENT);
    }

    /** Bouton Pill — style outlined (contour, fond transparent). */
    public static JButton pillButtonOutline(String text)
    {
        JButton btn = pillButton(text, new Color(0,0,0,0), ACCENT);
        btn.putClientProperty("outline", Boolean.TRUE);
        btn.putClientProperty("outlineColor", ACCENT);
        return btn;
    }

    /** Bouton Pill — style danger (rempli, rouge). */
    public static JButton pillButtonDanger(String text)
    {
        return pillButton(text, ERROR, TEXT_ON_ACCENT);
    }

    /** Bouton Pill — style succès (rempli, vert). */
    public static JButton pillButtonSuccess(String text)
    {
        return pillButton(text, SUCCESS, TEXT_ON_ACCENT);
    }

    /** Bouton Pill — style ghost (texte uniquement, fond transparent). */
    public static JButton pillButtonGhost(String text)
    {
        JButton btn = pillButton(text, new Color(0,0,0,0), TEXT_SECONDARY);
        btn.putClientProperty("ghost", Boolean.TRUE);
        return btn;
    }

    private static JButton pillButton(String text, Color bg, Color fg)
    {
        JButton btn = new JButton(text)
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight(), arc = h; // pill = arc = height

                boolean isOutline = Boolean.TRUE.equals(getClientProperty("outline"));
                boolean isGhost   = Boolean.TRUE.equals(getClientProperty("ghost"));
                Color outlineCol  = (Color) getClientProperty("outlineColor");

                Color currentBg = bg;
                if (!isOutline && !isGhost)
                {
                    if (getModel().isPressed())
                        currentBg = ACCENT_PRESSED;
                    else if (getModel().isRollover())
                        currentBg = ACCENT_HOVER;
                }

                // Fond
                if (!isGhost)
                {
                    g2.setColor(currentBg);
                    g2.fillRoundRect(0, 0, w, h, arc, arc);
                }

                // Bordure outline
                if (isOutline && outlineCol != null)
                {
                    Color borderColor = getModel().isRollover() ? ACCENT_HOVER : outlineCol;
                    g2.setColor(borderColor);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(1, 1, w - 2, h - 2, arc, arc);
                    if (getModel().isRollover())
                    {
                        g2.setColor(new Color(92, 107, 192, 18));
                        g2.fillRoundRect(0, 0, w, h, arc, arc);
                    }
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFont(FONT_BUTTON);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 24, 9, 24));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover text color pour ghost/outline
        btn.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.repaint(); }
            @Override public void mouseExited(java.awt.event.MouseEvent e)  { btn.repaint(); }
        });

        return btn;
    }

    /** Champ de saisie stylisé (texte). */
    public static JTextField styledField(String placeholder)
    {
        JTextField field = new JTextField()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_INPUT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS_INPUT, RADIUS_INPUT);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean focused = isFocusOwner();
                g2.setColor(focused ? BORDER_FOCUS : BORDER);
                g2.setStroke(new BasicStroke(focused ? 1.8f : 1.2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, RADIUS_INPUT, RADIUS_INPUT);
                g2.dispose();
            }
        };

        field.setFont(FONT_BODY);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(new Color(0, 0, 0, 0));
        field.setCaretColor(ACCENT);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        field.putClientProperty("placeholder", placeholder);
        return field;
    }

    /** Champ mot de passe stylisé. */
    public static JPasswordField styledPasswordField()
    {
        JPasswordField field = new JPasswordField()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_INPUT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS_INPUT, RADIUS_INPUT);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean focused = isFocusOwner();
                g2.setColor(focused ? BORDER_FOCUS : BORDER);
                g2.setStroke(new BasicStroke(focused ? 1.8f : 1.2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, RADIUS_INPUT, RADIUS_INPUT);
                g2.dispose();
            }
        };

        field.setFont(FONT_BODY);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(new Color(0, 0, 0, 0));
        field.setCaretColor(ACCENT);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        return field;
    }

    /** Label de champ de formulaire. */
    public static JLabel fieldLabel(String text)
    {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_SECONDARY);
        return lbl;
    }

    /** Titre de section. */
    public static JLabel titleLabel(String text)
    {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    /** Sous-titre. */
    public static JLabel subtitleLabel(String text)
    {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_SUBTITLE);
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    /** Panneau-carte avec coins arrondis. */
    public static JPanel cardPanel()
    {
        JPanel card = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS_CARD, RADIUS_CARD);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_CARD, RADIUS_CARD);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        return card;
    }

    /** Zone de texte stylisée (résultats). */
    public static JTextArea styledTextArea(int rows, int cols)
    {
        JTextArea ta = new JTextArea(rows, cols);
        ta.setFont(FONT_MONO);
        ta.setForeground(TEXT_PRIMARY);
        ta.setBackground(BG_INPUT);
        ta.setCaretColor(ACCENT);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER, RADIUS_INPUT, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return ta;
    }

    /** Séparateur horizontal discret. */
    public static JSeparator separator()
    {
        JSeparator sep = new JSeparator();
        sep.setForeground(DIVIDER);
        sep.setBackground(BG_PANEL);
        return sep;
    }

    /** Assombrit une couleur d'un facteur (0.0–1.0). */
    public static Color darken(Color c, float f)
    {
        return new Color(
            Math.max(0, (int)(c.getRed()   * f)),
            Math.max(0, (int)(c.getGreen() * f)),
            Math.max(0, (int)(c.getBlue()  * f)),
            c.getAlpha()
        );
    }

    /** Éclaircit une couleur d'un facteur. */
    public static Color lighten(Color c, float f)
    {
        return new Color(
            Math.min(255, (int)(c.getRed()   * (1 + f))),
            Math.min(255, (int)(c.getGreen() * (1 + f))),
            Math.min(255, (int)(c.getBlue()  * (1 + f))),
            c.getAlpha()
        );
    }

    // ------------------------------------
    //  Bordure arrondie réutilisable
    // ------------------------------------
    public static class RoundedBorder implements Border
    {
        private final Color  color;
        private final int    radius;
        private final int    thickness;

        public RoundedBorder(Color color, int radius, int thickness)
        {
            this.color     = color;
            this.radius    = radius;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
        {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }

        @Override public Insets getBorderInsets(Component c) { return new Insets(thickness, thickness, thickness, thickness); }
        @Override public boolean isBorderOpaque() { return false; }
    }

    // Compatibilite AnimatedBubble
    private static final java.util.Random _bubbleRand = new java.util.Random();
    private static final Color[] BUBBLE_COLORS = {
        new Color(92, 107, 192, 60), new Color(63, 81, 181, 40),
        new Color(121, 134, 203, 50), new Color(32, 32, 58, 80)
    };
    public static Color getRandomBubbleColor() {
        return BUBBLE_COLORS[_bubbleRand.nextInt(BUBBLE_COLORS.length)];
    }

}
