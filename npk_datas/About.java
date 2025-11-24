package crypto.npk_datas;

import crypto.Main;
import crypto.utils.DrawBackground;
import crypto.users.Connect; 
import crypto.users.Registration; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;


public class About extends JPanel
{
    private static final Color PRIMARY_COLOR = new Color(30, 40, 60);
    private static final Color ACCENT_COLOR  = new Color(0, 150, 255);
    private static final Color TEXT_LIGHT    = Color.WHITE;
    private static final Color TEXT_DARK     = new Color(40, 40, 40);
    private static final Font TITLE_FONT     = new Font("SansSerif", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT  = new Font("SansSerif", Font.BOLD, 20);
    private static final Font BODY_FONT      = new Font("SansSerif", Font.PLAIN, 15);
    
    protected DrawBackground background;
    protected JButton back;
    protected Main main_window;
    
    public About(Main main_window) 
    {
        this.main_window = main_window;

        this.setLayout(new BorderLayout());

        this.background = new DrawBackground("crypto/ressources/IMG-20251023-WA0023.jpg");
        
        this.back = createStyledButton("Back", Color.RED.darker());

        this.back.addActionListener(event -> About.this.main_window.showHome());

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        navPanel.setOpaque(false);
        navPanel.add(this.back);
        
        JScrollPane scrollPane = new JScrollPane(describe_datas());
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.addMouseWheelListener(new MouseWheelListener()
        {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event)
            {
                JScrollBar bar = scrollPane.getVerticalScrollBar();
                int value      = event.getWheelRotation() * bar.getUnitIncrement();
                bar.setValue(bar.getValue() + value);
            }
        });

        this.background.setLayout(new BorderLayout());
        this.background.add(navPanel, BorderLayout.NORTH); 
        this.background.add(scrollPane, BorderLayout.CENTER);
        
        this.add(this.background, BorderLayout.CENTER);
    }

    public void restoreWindow(JPanel panelToRestore)
    {
        this.main_window.getContentPane().removeAll();
        this.main_window.setContentPane(panelToRestore);
        panelToRestore.revalidate();
        panelToRestore.repaint();
        this.main_window.revalidate();
        this.main_window.repaint();
        this.main_window.setVisible(true);
    }

    private JButton createStyledButton(String text, Color background)
    {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(TEXT_LIGHT);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() 
        {
            public void mouseEntered(MouseEvent event) 
            {
                button.setBackground(background.darker());
            }
            public void mouseExited(MouseEvent event) 
            {
                button.setBackground(background);
            }
        });
        return button;
    }

    private JPanel describe_datas()
    {
        Border describe_border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            "Network Protection Kit (NPK)", TitledBorder.LEFT, TitledBorder.TOP, SUBTITLE_FONT, PRIMARY_COLOR
        );

        JLabel title_label = new JLabel("NPK votre partenaire numérique d'excellence !");
        title_label.setFont(TITLE_FONT);
        title_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        title_label.setForeground(ACCENT_COLOR);
        
        JLabel intro_title = new JLabel("🚨️ Marre des cyber-catastrophes ?");
        intro_title.setFont(SUBTITLE_FONT);
        intro_title.setForeground(PRIMARY_COLOR);
        intro_title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea intro_textT = new JTextArea("Que votre site web ressemble à un dinosaure en pixels ?🦕️\n " +
                                                "Et que vos idées géniales restent bloquées dans votre cerveau faute d'une app ?🤯️\n "+ 
                                                "Respirez un grand coup ! **Network Protection Kit (NPK)** est là pour transformer vos\n" +
                                                "cauchemars numériques en rêves connectés !🚀️"
                                            );
        intro_textT.setEditable(false);
        intro_textT.setFont(BODY_FONT);
        intro_textT.setOpaque(false);
        intro_textT.setLineWrap(true);
        intro_textT.setWrapStyleWord(true);
        intro_textT.setForeground(TEXT_DARK);

        //--------------------------------------------------------------------------------------------

        JLabel secu_label = new JLabel("🏦️ La Sécurité, Notre Priorité");
        secu_label.setFont(TITLE_FONT);
        secu_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        secu_label.setForeground(new Color(46, 139, 87));

        JLabel und_secu_label = new JLabel("🏗️ Les Architectes de Votre Sérénité Digitale");
        und_secu_label.setFont(SUBTITLE_FONT);
        und_secu_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        und_secu_label.setForeground(PRIMARY_COLOR);

        JTextArea archit = new JTextArea(
                                            "Chez NPK, on ne se contente pas de coller des pansements sur les bobos numériques.👌️\n"+
                                            "Nous sommes les architectes de votre sécurité digitale!\n" +
                                            "Ce que nous vous offrons avec un grand sourire (et une sécurité de fer):"
                                        );

        archit.setEditable(false);
        archit.setFont(BODY_FONT);
        archit.setOpaque(false);
        archit.setForeground(TEXT_DARK);
       
        JLabel secu_prin = new JLabel("🛡️ Cyber-sécurité à l'épreuve des balles (et des hackers)!");
        secu_prin.setFont(TITLE_FONT);
        secu_prin.setAlignmentX(Component.CENTER_ALIGNMENT);
        secu_prin.setForeground(new Color(46, 139, 87));

        JTextArea secu_des = new JTextArea("Fini les insomnies à cause des menaces en ligne !👻️ Nous érigeons des forteresses numériques imprenables.\n" + 
                                            "Audits de sécurité qui feraient pâlir un agent secret, tests d'intrusion pour débusquer les failles avant\n" + 
                                            "les méchants 👩‍💻️ (oui, on \"hacke\" votre propre système pour le rendre plus fort 💪️), et des solutions qui vous feront\n" + 
                                            "dormir sur vos deux oreilles. La protection de vos données, c'est sacré pour NPK!"
                                            );
        secu_des.setEditable(false);
        secu_des.setOpaque(false);
        secu_des.setFont(BODY_FONT);
        secu_des.setForeground(TEXT_DARK);

        JLabel web_title = new JLabel("🌐️ Votre Présence En Ligne, Notre Spécialité !");
        web_title.setFont(TITLE_FONT);
        web_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        web_title.setForeground(ACCENT_COLOR);

        JTextArea web = new JTextArea(
                                        "Votre site web n'est pas qu'une carte de visite, c'est votre vitrine numérique !\n " +
                                        "🌟️ NPK conçoit des expériences web si fluides, si belles et si performantes que vos \n" +
                                        "visiteurs ne voudront plus partir. Du design élégant aux fonctionnalités les plus complexes,\n" +
                                        "nous faisons briller votre présence en ligne. Prêt pour un site qui fait \"WOUAH !\"?😎️"
                                    );
        web.setEditable(false);
        web.setOpaque(false);
        web.setFont(BODY_FONT);
        web.setForeground(TEXT_DARK);

        JLabel app_title = new JLabel("📱️💻️ Applications Mobiles & Bureau : Vos Idées Prennent Vie !");
        app_title.setFont(TITLE_FONT);
        app_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        app_title.setForeground(ACCENT_COLOR);

        JTextArea app = new JTextArea("Vous avez une idée pour une application ? \n"+
                                        "On est vos magiciens du code ! Que ce soit pour iOS, \n"+
                                        "Android ou le Bureau, NPK transforme vos concepts les plus fous \n"+
                                        "en application intuitives, robustes et innovantes. Nos super-pouvoirs de codeurs ? "
                                    );
        app.setEditable(false);
        app.setOpaque(false);
        app.setFont(BODY_FONT);
        app.setForeground(TEXT_DARK);

        JLabel tech_title = new JLabel("🌃️ Nos Technologies : ");
        tech_title.setFont(TITLE_FONT);
        tech_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        tech_title.setForeground(PRIMARY_COLOR);

        JTextArea tech = new JTextArea("\t==> Java pour la robustesse 🍵️\n"+
                                        "\t==> Dart(Flutter) pour la vitesse et la beauté multiplateforme 🦋️\n"+
                                        "\t==> C pour la puissance et l'efficacité ⚡️\n"+
                                        "\t==> JavaScript pour le dynamisme et l'interactivité 🌪️\n"+
                                        "\t==> Python pour l'intelligence artificielle et l'automatisation 🐍️\n"
                                    );

        tech.setEditable(false);
        tech.setOpaque(false);
        tech.setFont(new Font("Monospaced", Font.PLAIN, 15));
        tech.setForeground(TEXT_DARK);

        JLabel train_title = new JLabel("👨‍🎓️ Devenez un As de la Tech !");
        train_title.setFont(TITLE_FONT);
        train_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        train_title.setForeground(new Color(46, 139, 87));

        JTextArea training = new JTextArea("Envie de monter en compétence ? NPK, c'est aussi votre centre d'excellence !📚️ \n" +
                                            "Nos formations sont conçues par des experts pour faire de vous un as de la \n" +
                                            "cybersécurité, un maître du développement web, ou un virtuose des applications. \n" +
                                            "Des connaissances pointues, mais toujours dans une ambiance conviviale !😉️"
                                        );
        training.setEditable(false);
        training.setOpaque(false);
        training.setFont(BODY_FONT);
        training.setForeground(TEXT_DARK);

        JLabel contact_title = new JLabel("🤝️ Contactez-nous dès aujourd'hui !");
        contact_title.setFont(TITLE_FONT);
        contact_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        contact_title.setForeground(ACCENT_COLOR);

        JTextArea contact = new JTextArea("Prêt à blinder votre réseau, à faire briller votre présence en ligne et à concrétiser vos idées les plus folles ? \n "+
                                            "Contactez NPK aujourd'hui ! On est là pour vous protéger, vous construire, vous innover et vous former. \n" +
                                            "Votre succès numérique, c'est notre mission !"
                                        );

        contact.setEditable(false);
        contact.setOpaque(false);
        contact.setFont(BODY_FONT);
        contact.setForeground(TEXT_DARK);


        JPanel describe_panel = new JPanel();
        describe_panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(40, 40, 40, 40),
            describe_border
        ));
        describe_panel.setLayout(new BoxLayout(describe_panel,BoxLayout.Y_AXIS));
        describe_panel.setOpaque(true);
        describe_panel.setBackground(new Color(255,255,255,240));

        describe_panel.add(title_label);
        describe_panel.add(Box.createVerticalStrut(30));
        describe_panel.add(intro_title);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(intro_textT);
        describe_panel.add(Box.createVerticalStrut(40));
        describe_panel.add(secu_label);
        describe_panel.add(Box.createVerticalStrut(20));
        describe_panel.add(und_secu_label);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(archit);
        describe_panel.add(Box.createVerticalStrut(40));
        describe_panel.add(secu_prin);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(secu_des);
        describe_panel.add(Box.createVerticalStrut(40));
        describe_panel.add(web_title);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(web);
        describe_panel.add(Box.createVerticalStrut(40));
        describe_panel.add(app_title);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(app);
        describe_panel.add(Box.createVerticalStrut(40));
        describe_panel.add(tech_title);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(tech);
        describe_panel.add(Box.createVerticalStrut(40));
        describe_panel.add(train_title);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(training);
        describe_panel.add(Box.createVerticalStrut(40));
        describe_panel.add(contact_title);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(contact);

        return describe_panel;
    }
}
