package crypto.affine;
import crypto.utils.Util;
import crypto.affine.AffineEncrypt;
import crypto.affine.AffineDecrypt;
import crypto.Main;
import crypto.Home;
import crypto.Header;
import crypto.utils.DrawBackground;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.*;
import javax.imageio.*;

public class Affine extends JPanel
{
    protected JButton       back;
    protected JButton       encrypt;
    protected JButton       decrypt;
    protected Main          mainWindow;
    
    private static final Color PRIMARY_COLOR = new Color(72, 118, 163);  
    private static final Color ACCENT_COLOR  = new Color(255, 102, 102);  
    private static final Color TEXT_COLOR    = new Color(50, 50, 50);      
    private static final Color BG_OVERLAY    = new Color(255, 255, 255, 230);
    private static final Font TITLE_FONT     = new Font("Arial", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT  = new Font("Georgia", Font.BOLD, 20);
    private static final Font BODY_FONT      = new Font("Georgia", Font.PLAIN, 16);
    
    public Affine(Main mainWindow)
    {
        this.mainWindow = mainWindow;
        this.setSize(new Dimension(750,750));
        
        DrawBackground background = new DrawBackground("crypto/ressources/downgrade.png");
        this.setVisible(true);
        background.setOpaque(true);
        background.setLayout(new BorderLayout(20, 20));
        
        this.encrypt = Main.createStyledButton("Encrypt", PRIMARY_COLOR, ACCENT_COLOR, SUBTITLE_FONT.deriveFont(Font.BOLD, 24f));
        this.decrypt = Main.createStyledButton("Decrypt", PRIMARY_COLOR, ACCENT_COLOR, SUBTITLE_FONT.deriveFont(Font.BOLD, 24f));

        this.encrypt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 20, 10, 20),
            BorderFactory.createRaisedBevelBorder() 
        ));
        this.decrypt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 20, 10, 20),
            BorderFactory.createRaisedBevelBorder() 
        ));


        this.encrypt.addActionListener(event -> 
        {
            Affine.this.mainWindow.setTitle("Système de Chiffrement Affine");
            Affine.this.mainWindow.setContentPane(new AffineEncrypt(Affine.this));
            Affine.this.mainWindow.revalidate();
            Affine.this.mainWindow.repaint();    
        });

        this.decrypt.addActionListener(event -> 
        {
            Affine.this.mainWindow.setTitle("Système de Déchiffrement Affine");
            Affine.this.mainWindow.setContentPane(new AffineDecrypt(Affine.this));
            Affine.this.mainWindow.revalidate();
            Affine.this.mainWindow.repaint();
            
        });

        this.back = Main.createStyledButton("Back", TEXT_COLOR, new Color(220, 220, 220, 180), SUBTITLE_FONT.deriveFont(Font.PLAIN, 18f));
        
        this.back.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEXT_COLOR.brighter(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10) 
        ));
        
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
                Affine.this.back.setBackground(ACCENT_COLOR.brighter()); 
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                Affine.this.back.setBackground(new Color(220, 220, 220, 180));
            }
        });
        
        JPanel btn_panel = new JPanel();
        btn_panel.setLayout(new GridLayout(1, 2, 30, 10));
        btn_panel.add(this.encrypt);
        btn_panel.add(this.decrypt);
        btn_panel.setOpaque(false);

        JPanel desc = describe();
        
        JPanel center_panel = new JPanel(new GridBagLayout());
        center_panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx              = 0;
        gbc.gridy              = 0;
        gbc.weightx            = 1.0;
        gbc.fill               = GridBagConstraints.HORIZONTAL;
        JScrollPane scroll     = new JScrollPane(desc);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        center_panel.add(scroll, gbc);

        gbc.gridy  = 1;
        gbc.insets = new Insets(40, 0, 0, 0);
        center_panel.add(btn_panel, gbc);


        background.add(this.back,BorderLayout.NORTH);
        background.add(center_panel, BorderLayout.CENTER);
        this.setLayout(new BorderLayout());
        this.add(background);  
    }

    private JPanel describe()
    {
        Border line = BorderFactory.createLineBorder(PRIMARY_COLOR, 2, true);
        Border describe_border = BorderFactory.createTitledBorder(line,
         "Crypto systeme Affine", TitledBorder.CENTER, TitledBorder.TOP, TITLE_FONT, PRIMARY_COLOR.darker());

        JTextArea intro = new JTextArea("Le cryptosystème Affine est une méthode de chiffrement classique et simple qui utilise une transformation affine pour encoder et décoder les messages. Il sert souvent d'introduction aux concepts fondamentaux de la cryptographie."
                                );
        intro.setEditable(false);
        intro.setFont(BODY_FONT.deriveFont(Font.ITALIC, 16f)); 
        intro.setOpaque(false);
        intro.setLineWrap(true);
        intro.setWrapStyleWord(true);
        intro.setForeground(TEXT_COLOR.darker());

        JLabel fonct_label = new JLabel("🔑 Fonctionnement");
        fonct_label.setFont(SUBTITLE_FONT);
        fonct_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        fonct_label.setForeground(ACCENT_COLOR); 
        
        JTextArea fonct = new JTextArea("• Le message clair (caractère) est d'abord converti en un nombre x.\n" + 
            "• Le message chiffré y est obtenu par la formule : y = (ax + b) mod(n), où a et b sont les clés, et n est la taille de l'alphabet.\n" +
            "• Le déchiffrement utilise l'inverse modulaire de a : x =  a^(-1)(y - b) mod(n).");
        fonct.setEditable(false);
        fonct.setFont(BODY_FONT);
        fonct.setOpaque(false);
        fonct.setLineWrap(true);
        fonct.setWrapStyleWord(true);
        fonct.setForeground(TEXT_COLOR);

        JLabel sec_label = new JLabel("🛡️ Sécurité et Vulnérabilités");
        sec_label.setFont(SUBTITLE_FONT);
        sec_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        sec_label.setForeground(ACCENT_COLOR);

        JTextArea secu_des = new JTextArea("• La sécurité repose sur la difficulté de trouver les constantes a et b à partir du texte chiffré.\n" +
                                        "• Il est très **vulnérable** aux attaques par **force brute** (nombre limité de clés possibles).\n" +
                                        "• Il est également sensible à l'**analyse de fréquence** et aux **attaques par texte clair connu** (nécessite seulement deux paires de lettres).");
        secu_des.setEditable(false);
        secu_des.setFont(BODY_FONT);
        secu_des.setOpaque(false);
        secu_des.setLineWrap(true);
        secu_des.setWrapStyleWord(true);
        secu_des.setForeground(TEXT_COLOR);
        
        JPanel describe_panel = new JPanel();
        
        describe_panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(30, 40, 30, 40), 
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5), 
                describe_border 
            )
        ));

        describe_panel.setLayout(new BoxLayout(describe_panel,BoxLayout.Y_AXIS));
        describe_panel.setOpaque(true);
        describe_panel.setBackground(BG_OVERLAY); 

        describe_panel.add(intro);
        describe_panel.add(Box.createVerticalStrut(35));
        describe_panel.add(fonct_label);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(fonct);
        describe_panel.add(Box.createVerticalStrut(35));
        describe_panel.add(sec_label);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(secu_des);

        fonct_label.setAlignmentX(Component.LEFT_ALIGNMENT);
        sec_label.setAlignmentX(Component.LEFT_ALIGNMENT);
        intro.setAlignmentX(Component.LEFT_ALIGNMENT);
        fonct.setAlignmentX(Component.LEFT_ALIGNMENT);
        secu_des.setAlignmentX(Component.LEFT_ALIGNMENT);


        return describe_panel;
    }

    public void showMe() 
    {
        this.mainWindow.setTitle("Système de Chiffrement Affine");
        this.mainWindow.setContentPane(this);
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
