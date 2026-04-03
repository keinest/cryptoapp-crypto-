package crypto.npk_datas;

import crypto.Main;
import crypto.utils.DrawBackground;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Service extends JPanel
{
    private static final Color PRIMARY_COLOR = new Color(30, 40, 60);
    private static final Color ACCENT_COLOR  = new Color(0, 150, 255);
    private static final Color TEXT_LIGHT    = Color.WHITE;
    private static final Font TITLE_FONT     = new Font("SansSerif", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT  = new Font("SansSerif", Font.BOLD, 20);
    private static final Font BODY_FONT      = new Font("SansSerif", Font.PLAIN, 15);
    
    protected Main main_window;
    protected DrawBackground background;
    protected JButton back;
    private final JPanel previousPanel;
    private final String previousTitle;
    
    public Service(Main main_window)
    {
        this(main_window, null, "CryptoApp — Accueil");
    }

    public Service(Main main_window, JPanel previousPanel, String previousTitle)
    {
        this.main_window    = main_window; 
        this.previousPanel  = previousPanel;
        this.previousTitle  = previousTitle;
        this.back           = createStyledButton("Back",new Color(255,30,30,200), TEXT_LIGHT, BODY_FONT);
        this.background     = new DrawBackground("crypto/ressources/IMG-20251027-WA0032.jpg");

        JPanel backPanel    = new JPanel();
        backPanel.setOpaque(false);
        backPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        backPanel.add(this.back);

        this.back.addActionListener(event -> restorePreviousView());

        this.setLayout(new BorderLayout());
        this.background.setLayout(new BorderLayout());
        this.background.add(backPanel, BorderLayout.NORTH);
        this.background.add(buildScrollPane(), BorderLayout.CENTER);
        this.add(background, BorderLayout.CENTER);
    }
    
    private JButton createStyledButton(String text, Color background, Color foreground, Font font)
    {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBackground(background);
        button.setForeground(foreground);
        
        button.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                button.setBackground(background == PRIMARY_COLOR ? ACCENT_COLOR : background.darker());
                button.setForeground(TEXT_LIGHT);
            }

            @Override 
            public void mouseExited(MouseEvent event)
            {
                button.setForeground(foreground);
                button.setBackground(background);
            }
        });

        return button;
    } 
    
    public void restoreWindow(JPanel panelToRestore)
    {
        this.main_window.showView(panelToRestore, previousTitle);
    }

    private void restorePreviousView()
    {
        if (previousPanel != null)
        {
            main_window.showView(previousPanel, previousTitle);
            return;
        }
        main_window.showHome();
    }

    private JScrollPane buildScrollPane()
    {
        JScrollPane scrollPane = new JScrollPane(buildServicesContent());
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel buildServicesContent()
    {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(30, 40, 40, 40));

        JLabel title = new JLabel("Nos services NPK");
        title.setFont(TITLE_FONT);
        title.setForeground(ACCENT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Des solutions concrètes pour protéger, construire et faire évoluer vos produits numériques.");
        subtitle.setFont(BODY_FONT);
        subtitle.setForeground(TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(10));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(30));
        content.add(createServiceCard(
            "Cybersécurité",
            "Audit, durcissement, tests d'intrusion et accompagnement pour réduire les risques avant qu'ils ne deviennent des incidents.",
            new Color(46, 139, 87, 210)
        ));
        content.add(Box.createVerticalStrut(18));
        content.add(createServiceCard(
            "Applications web et mobile",
            "Conception de plateformes métier, sites modernes et applications desktop/mobile pensées pour les usages réels.",
            new Color(0, 150, 255, 210)
        ));
        content.add(Box.createVerticalStrut(18));
        content.add(createServiceCard(
            "Intégration et automatisation",
            "Connexion de vos outils, automatisation de traitements répétitifs et amélioration de la productivité technique.",
            new Color(123, 104, 238, 210)
        ));
        content.add(Box.createVerticalStrut(18));
        content.add(createServiceCard(
            "Support et accompagnement",
            "Assistance, maintenance corrective, montée en compétence des équipes et suivi continu après livraison.",
            new Color(255, 140, 0, 210)
        ));

        return content;
    }

    private JPanel createServiceCard(String title, String description, Color borderColor)
    {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(true);
        card.setBackground(new Color(15, 20, 30, 205));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 2, true),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(850, Integer.MAX_VALUE));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(borderColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea descLabel = new JTextArea(description);
        descLabel.setEditable(false);
        descLabel.setOpaque(false);
        descLabel.setLineWrap(true);
        descLabel.setWrapStyleWord(true);
        descLabel.setFont(BODY_FONT);
        descLabel.setForeground(TEXT_LIGHT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(descLabel);
        return card;
    }
}
