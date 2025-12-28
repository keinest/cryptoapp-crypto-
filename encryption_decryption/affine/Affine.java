package crypto.encryption_decryption.affine;

import crypto.encryption_decryption.affine.AffineEncrypt;
import crypto.encryption_decryption.affine.AffineDecrypt;
import crypto.utils.DrawBackground;
import crypto.utils.Util;
import crypto.utils.ThemeManager;

import crypto.Main;
import crypto.Home;
import crypto.Header;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.*;
import javax.imageio.*;

public class Affine extends JPanel
{
    private final String MAIN_BG_PATH = "crypto/ressources/MITM.png";
    private final String PANE_BG_1    = "crypto/ressources/pixieDust.png";
    private final String PANE_BG_2    = "crypto/ressources/IMG-20251026-WA0102.jpg";
    private final String PANE_BG_3    = "crypto/ressources/evilTwin.png";
    private final String PANE_BG_4    = "crypto/ressources/IMG-20241009-WA0008.jpg";

    private static final Color PRIMARY_BUTTON_COLOR = new Color(0, 150, 255);
    private static final Color HOVER_COLOR          = new Color(0, 100, 200);
    private static final Color BACK_BUTTON_COLOR    = new Color(255, 50, 50);
    private static final Color SECTION_TEXT_COLOR   = Color.WHITE;
    private static final Color SECTION_TITLE_COLOR  = new Color(255, 255, 0);
    
    protected JButton       back;
    protected JButton       encrypt;
    protected JButton       decrypt;
    protected Main          mainWindow;
    
    public Affine(Main mainWindow)
    {
        this.mainWindow = mainWindow;
        this.setSize(new Dimension(750,750));
        
        DrawBackground background = new DrawBackground(MAIN_BG_PATH);
        this.setVisible(true);
        background.setOpaque(true);
        background.setLayout(new BorderLayout());
        
        this.encrypt = Main.createCyberButton("Encrypt", ThemeManager.BUTTON_BG);
        this.decrypt = Main.createCyberButton("Decrypt", ThemeManager.BUTTON_BG);
        this.back    = Main.createCyberButton("Back", ThemeManager.BUTTON_BG);

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
            Affine.this.mainWindow.setTitle("Système de Chiffrement Affine");
            Affine.this.mainWindow.setContentPane(new AffineEncrypt(Affine.this));
            Affine.this.mainWindow.revalidate();
            Affine.this.mainWindow.repaint();    
        });
        this.encrypt.addMouseListener(actionButtonHover);

        this.decrypt.addActionListener(event -> 
        {
            Affine.this.mainWindow.setTitle("Système de Déchiffrement Affine");
            Affine.this.mainWindow.setContentPane(new AffineDecrypt(Affine.this));
            Affine.this.mainWindow.revalidate();
            Affine.this.mainWindow.repaint();
            
        });
        this.decrypt.addMouseListener(actionButtonHover);

        this.back.addActionListener(event ->
        {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(new Header(Affine.this.mainWindow),BorderLayout.NORTH);
            panel.add(new Home(Affine.this.mainWindow),BorderLayout.CENTER);
            Affine.this.mainWindow.setTitle("Application Crypto");
            Affine.this.mainWindow.getContentPane().removeAll();
            Affine.this.mainWindow.setContentPane(panel);
            Affine.this.mainWindow.getContentPane().revalidate();
            Affine.this.mainWindow.getContentPane().repaint();
        });
        
        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                Affine.this.back.setBackground(Color.DARK_GRAY); 
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                Affine.this.back.setBackground(BACK_BUTTON_COLOR);
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
            "🔑 Le Cryptosystème Affine : Transformation Linéaire",
            "Le cryptosystème Affine est une méthode de chiffrement classique et simple qui utilise une transformation affine pour encoder et décoder les messages. Il sert souvent d'introduction aux concepts fondamentaux de la cryptographie par substitution."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_2,
            "🧮 Principe de Chiffrement",
            "Le message clair (caractère) est d'abord converti en un nombre x (ex: A=0, B=1, ..., Z=25). Le message chiffré y est obtenu par la formule :\n\n" + 
            "y = (ax + b) mod (n)\n" +
            "où a et b sont les clés (la clé a doit être coprime avec n), et n est la taille de l'alphabet (généralement 26)."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_3,
            "🔓 Principe de Déchiffrement",
            "Le déchiffrement utilise l'inverse modulaire de a, noté a^{-1}. L'opération inverse est effectuée pour retrouver x à partir de y :\n\n" + 
            "x = a^(-1)(y - b) mod(n)\n" +
            "L'existence de a^(-1) est la raison pour laquelle a doit être premier avec n (pgcd(a, n) = 1)."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_4,
            "🛡️ Sécurité et Vulnérabilités",
            "• **Clés** : Il y a n * phi(n) paires de clés possibles (pour n = 26, 26 x 12 = 312 clés).\n" +
            "• **Vulnérabilité** : Il est très **vulnérable** aux attaques par **force brute** (nombre limité de clés) et à l'**analyse de fréquence**.\n" +
            "• **Attaque par Texte Clair Connu** : Il suffit de connaître deux paires de lettres (clair/chiffré) pour déduire les clés a et b."
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

    public void showMe() 
    {
        this.mainWindow.setTitle("Système de Chiffrement Affine");
        this.mainWindow.setContentPane(this);
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
