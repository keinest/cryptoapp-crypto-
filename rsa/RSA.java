package crypto.rsa;
import crypto.utils.DrawBackground;
import crypto.rsa.RsaEncrypt;
import crypto.Header;
import crypto.Home;
import crypto.Main;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.*;
import javax.imageio.*;

public class RSA extends JPanel
{
    private final String MAIN_BG_PATH = "crypto/ressources/IMG-20251026-WA0104.jpg";
    private final String PANE_BG_1    = "crypto/ressources/Pivot.png";
    private final String PANE_BG_2    = "crypto/ressources/IMG-20251026-WA0098.jpg";
    private final String PANE_BG_3    = "crypto/ressources/IA.png";
    private final String PANE_BG_4    = "crypto/ressources/downgrade.png";

    private static final Color PRIMARY_BUTTON_COLOR = new Color(0, 150, 255);
    private static final Color HOVER_COLOR          = new Color(0, 100, 200);
    private static final Color BACK_BUTTON_COLOR    = new Color(255, 50, 50);
    private static final Color SECTION_TEXT_COLOR   = Color.WHITE;
    private static final Color SECTION_TITLE_COLOR  = new Color(255, 255, 0);
    
    protected JButton       back;
    protected JButton       encrypt;
    protected JButton       decrypt;
    protected Main          mainWindow;
    
    public RSA(Main mainWindow)
    {
        this.mainWindow = mainWindow;
        this.setSize(new Dimension(750,750));
        
        DrawBackground background = new DrawBackground(MAIN_BG_PATH);
        this.setVisible(true);
        background.setOpaque(true);
        background.setLayout(new BorderLayout());
        
        this.encrypt = Main.createStyledButton("Encrypt", PRIMARY_BUTTON_COLOR, Color.WHITE, new Font("SansSerif", Font.BOLD, 22));
        this.decrypt = Main.createStyledButton("Decrypt", PRIMARY_BUTTON_COLOR, Color.WHITE, new Font("SansSerif", Font.BOLD, 22));
        this.back    = Main.createStyledButton("Back", BACK_BUTTON_COLOR, Color.WHITE, new Font("SansSerif", Font.BOLD, 22));

        MouseAdapter actionButtonHover = new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                ((JButton)event.getSource()).setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                ((JButton)event.getSource()).setBackground(PRIMARY_BUTTON_COLOR);
            }
        };

        this.encrypt.addActionListener(event -> 
        {
            RSA.this.mainWindow.setTitle("Système de Chiffrement RSA");
            RSA.this.mainWindow.setContentPane(new RsaEncrypt(RSA.this));
            RSA.this.mainWindow.revalidate();
            RSA.this.mainWindow.repaint();    
        });
        this.encrypt.addMouseListener(actionButtonHover);

        this.decrypt.addActionListener(event -> 
        {
            RSA.this.mainWindow.setTitle("Système de Déchiffrement RSA");
            RSA.this.mainWindow.setContentPane(new RsaDecrypt(RSA.this));
            RSA.this.mainWindow.revalidate();
            RSA.this.mainWindow.repaint();
            
        });
        this.decrypt.addMouseListener(actionButtonHover);

        this.back.addActionListener(event ->
        {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(new Header(RSA.this.mainWindow),BorderLayout.NORTH);
            panel.add(new Home(RSA.this.mainWindow),BorderLayout.CENTER);
            RSA.this.mainWindow.setTitle("Application Crypto");
            RSA.this.mainWindow.getContentPane().removeAll();
            RSA.this.mainWindow.setContentPane(panel);
            RSA.this.mainWindow.getContentPane().revalidate();
            RSA.this.mainWindow.getContentPane().repaint();
        });
        
        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                RSA.this.back.setBackground(Color.DARK_GRAY); 
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                RSA.this.back.setBackground(BACK_BUTTON_COLOR);
            }
        });
        
        Dimension btnSize = new Dimension(280, 55); 
        this.back.setPreferredSize(btnSize);
        this.encrypt.setPreferredSize(btnSize);
        this.decrypt.setPreferredSize(btnSize);

        JPanel btn_panel = new JPanel();
        btn_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        btn_panel.add(this.encrypt);
        btn_panel.add(this.decrypt);
        btn_panel.add(this.back);
        btn_panel.setOpaque(false);

        JPanel doc_panel = doc_panel();
        doc_panel.add(btn_panel, BorderLayout.NORTH);

        background.add(doc_panel);
        this.setLayout(new BorderLayout());
        this.add(background);
    }

    private JPanel doc_panel()
    {
        JPanel main_panel_sections = new JPanel(new GridLayout(2, 2, 15, 15));
        main_panel_sections.setOpaque(false);
        
        main_panel_sections.add(createSectionPanel(
            PANE_BG_1,
            "🔐 Le Cryptosystème RSA : Asymétrique et Sûr",
            "Le cryptosystème RSA (Rivest-Shamir-Adleman) est l'un des premiers algorithmes de chiffrement à clé publique. Il est largement utilisé pour sécuriser les communications sur Internet (TLS/SSL) et la signature numérique."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_2,
            "💡 Principe de Base : Factorisation Difficile",
            "Le RSA repose sur la **difficulté de factoriser un grand nombre entier n en ses deux facteurs premiers p et q**.\n\n" +
            "Il utilise une paire de clés :\n" + 
            "• **Clé Publique** : Utilisée pour chiffrer les données (e, n).\n" + 
            "• **Clé Privée** : Utilisée pour déchiffrer les données (d, n)."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_3,
            "🧮 Fonctionnement et Formules",
            "**1. Chiffrement (avec la clé publique)** : Pour chiffrer un message m, on calcule :\n" +
            "c = m ^ e mod(n)\n\n" +
            "**2. Déchiffrement (avec la clé privée)** : Pour déchiffrer le message chiffré c, on calcule :\n" +
            "m = c ^ d mod(n)\n" + 
            "Où e et d sont choisis tels que e * d quiv 1 mod(phi(n)), avec phi(n) = (p - 1)(q - 1)."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_4,
            "📈 Avantages et Inconvénients",
            "**Avantages** :\n" +
            "• **Sécurité Élevée** : Considéré comme très sûr pour les grandes tailles de clés.\n" +
            "• **Double Usage** : Permet le chiffrement et la signature numérique.\n\n" +
            "**Inconvénients** :\n" + 
            "• **Lenteur** : Plus lent que les algorithmes de chiffrement symétrique.\n" +
            "• **Taille des Clés** : Les clés doivent être très grandes (2048 bits ou plus) pour maintenir la sécurité, ce qui impacte les performances."
        ));

        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        container.add(main_panel_sections, BorderLayout.CENTER);
        
        return container;
    }

    private JPanel createSectionPanel(String imagePath, String title, String text) 
    {
        JPanel panel = new DrawBackground(imagePath); 
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setForeground(SECTION_TITLE_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea textArea = new JTextArea(text);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        textArea.setForeground(SECTION_TEXT_COLOR);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        
        textArea.setBackground(new Color(0, 0, 0, 150));
        textArea.setOpaque(true); 
    
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null); 
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        panel.add(Box.createVerticalStrut(20));
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        
        panel.add(scrollPane); 
        panel.add(Box.createVerticalGlue());

        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        panel.setOpaque(false);

        return panel;
    }

    public void restoreWindow()
    {
        this.mainWindow.setTitle("RSA Encryption system");
        this.mainWindow.setContentPane(new RSA(this.mainWindow));
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
