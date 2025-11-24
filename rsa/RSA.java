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
    private static final Color TEXT_LIGHT         = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(72, 118, 163);  
    private static final Color ACCENT_COLOR  = new Color(255, 102, 102);  
    private static final Color TEXT_COLOR    = new Color(50, 50, 50);      
    private static final Color BG_OVERLAY    = new Color(255, 255, 255, 230);
    private static final Font TITLE_FONT     = new Font("Arial", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT  = new Font("Georgia", Font.BOLD, 20);
    private static final Font BODY_FONT      = new Font("Georgia", Font.PLAIN, 16);
    
    
    protected JButton       back;
    protected JButton       encrypt;
    protected JButton       decrypt;
    protected Main          mainWindow;
    
    public RSA(Main mainWindow)
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
            RSA.this.mainWindow.setTitle("RSA encryption system");
            RSA.this.mainWindow.setContentPane(new RsaEncrypt(RSA.this));
            RSA.this.mainWindow.revalidate();
            RSA.this.mainWindow.repaint();    
        });

        this.decrypt.addActionListener(event -> 
        {
            RSA.this.mainWindow.setTitle("RSA decryption system");
            RSA.this.mainWindow.setContentPane(new RsaDecrypt(RSA.this));
            RSA.this.mainWindow.revalidate();
            RSA.this.mainWindow.repaint();
            
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
            panel.add(new Header(RSA.this.mainWindow),BorderLayout.NORTH);
            panel.add(new Home(RSA.this.mainWindow),BorderLayout.CENTER);
            RSA.this.mainWindow.setTitle("Crypto Application");
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
                RSA.this.back.setBackground(ACCENT_COLOR.brighter()); 
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                RSA.this.back.setBackground(new Color(220, 220, 220, 180));
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
        center_panel.add(desc, gbc);

        gbc.gridy   = 1;
        gbc.insets  = new Insets(40, 0, 0, 0);
        center_panel.add(btn_panel, gbc);

        background.add(this.back,BorderLayout.NORTH);
        background.add(center_panel, BorderLayout.CENTER);
        
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(background));
    }

    private JPanel describe()
    {
        Border line = BorderFactory.createLineBorder(PRIMARY_COLOR, 2, true);
        Border describe_border = BorderFactory.createTitledBorder(line,
         "Crypto systeme RSA", TitledBorder.CENTER, TitledBorder.TOP, TITLE_FONT, PRIMARY_COLOR.darker());

        JTextArea intro = new JTextArea("Le crypto systeme RSA(Rivest-Shamir-Adelman) est un algorithme de chiffrement asymetrique largement utilise pour securiser les communications sur internet");
        intro.setEditable(false);
        intro.setFont(BODY_FONT.deriveFont(Font.ITALIC, 16f)); 
        intro.setOpaque(false);
        intro.setLineWrap(true);
        intro.setWrapStyleWord(true);
        intro.setForeground(TEXT_COLOR.darker());

        JLabel base_princ = new JLabel("🔑 Principe de base");
        base_princ.setFont(SUBTITLE_FONT);
        base_princ.setAlignmentX(Component.CENTER_ALIGNMENT);
        base_princ.setForeground(ACCENT_COLOR);

        JTextArea base_text = new JTextArea("Le RSA repose sur la difficulte de factoriser un grand nombre\n" +
                                        "entier en ses facteurs premiers. Il utilise une paire de cle : \n" +
                                        "\t* Cle publique : utilisee pour chiffrer les donnees\n" + 
                                        "\t* Cle privee : utilisee pour dechiffrer les donnees");
                        
        base_text.setEditable(false);
        base_text.setFont(BODY_FONT.deriveFont(Font.ITALIC, 16f)); 
        base_text.setOpaque(false);
        base_text.setLineWrap(true);
        base_text.setWrapStyleWord(true);
        base_text.setForeground(TEXT_COLOR.darker()); 

        JLabel fonct_label = new JLabel("🔑 Fonctionnement");
        fonct_label.setFont(SUBTITLE_FONT);
        fonct_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        fonct_label.setForeground(ACCENT_COLOR);

        JTextArea fonct_text = new JTextArea("1. Generation des cles : On choisit deux grands nombres premiers p et q, puis on calcule \n" +
                                            "n = p * q. La cle publique est (n, e) et la cle privee est (n, d), ou e et d sont des entiers\n" +
                                            "tels que e*d ≡ 1(mod(p - 1) (q - 1)).\n\n" + 
                                            "2. Chiffrement : Pour chiffrer um message m, on calcule c = (m ^ e) mod n.\n\n" +
                                            "3. Dechiffrement : Pour dechiffrer on le message c, on calcule m = (c ^ d) mod n");
        fonct_text.setEditable(false);
        fonct_text.setFont(BODY_FONT.deriveFont(Font.ITALIC, 16f)); 
        fonct_text.setOpaque(false);
        fonct_text.setLineWrap(true);
        fonct_text.setWrapStyleWord(true);
        fonct_text.setForeground(TEXT_COLOR.darker()); 

        JLabel sec_label = new JLabel("Securite");
        sec_label.setFont(SUBTITLE_FONT);
        sec_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        sec_label.setForeground(ACCENT_COLOR);

        JTextArea sec_text = new JTextArea("Le securite du RSA repose sur la difficulte de factoriser n en p et q. Si un attaquqnt peut factoriser n, il peut calculuer la cle prive d et dechiffrer le message");

        sec_text.setEditable(false);
        sec_text.setFont(BODY_FONT.deriveFont(Font.ITALIC, 16f)); 
        sec_text.setOpaque(false);
        sec_text.setLineWrap(true);
        sec_text.setWrapStyleWord(true);
        sec_text.setForeground(TEXT_COLOR.darker()); 

        JLabel av = new JLabel("Avantage");
        av.setFont(SUBTITLE_FONT);
        av.setAlignmentX(Component.CENTER_ALIGNMENT);
        av.setForeground(ACCENT_COLOR);

        JTextArea av_text = new JTextArea("\n==> Securite elevee : Le RSA est considere comme un algorithme de chiffrement tres securise\n" +
                                        "==> Large utilisation : le RSA est largement utilise pour securiser les communication sur internet");

        av_text.setEditable(false);
        av_text.setFont(BODY_FONT.deriveFont(Font.ITALIC, 16f)); 
        av_text.setOpaque(false);
        av_text.setLineWrap(true);
        av_text.setWrapStyleWord(true);
        av_text.setForeground(TEXT_COLOR.darker());

        JLabel incv = new JLabel("Inconveniants");

        incv.setFont(SUBTITLE_FONT);
        incv.setAlignmentX(Component.CENTER_ALIGNMENT);
        incv.setForeground(ACCENT_COLOR);
                
        JTextArea incv_text = new JTextArea("\n==> Lent : le RSA est plus lent que les algorithmes de chiffrements symetrique\n" +
                                            "==> Taille des cles : les cles RSA doivent etre tres grandes pour etre securisees ce qui peut etre un inconvenient pour certaines applications");
        
        incv_text.setEditable(false);
        incv_text.setFont(BODY_FONT.deriveFont(Font.ITALIC, 16f)); 
        incv_text.setOpaque(false);
        incv_text.setLineWrap(true);
        incv_text.setWrapStyleWord(true);
        incv_text.setForeground(TEXT_COLOR.darker());

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
        
        describe_panel.add(Box.createVerticalStrut(35));
        describe_panel.add(intro);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(base_princ);
        describe_panel.add(Box.createVerticalStrut(35));
        describe_panel.add(base_text);
        describe_panel.add(Box.createVerticalStrut(35));
        describe_panel.add(fonct_label);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(fonct_text);
        describe_panel.add(Box.createVerticalStrut(35));
        describe_panel.add(sec_label);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(sec_text);
        describe_panel.add(Box.createVerticalStrut(35));
        describe_panel.add(av);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(av_text);
        describe_panel.add(Box.createVerticalStrut(35));
        describe_panel.add(incv);
        describe_panel.add(Box.createVerticalStrut(15));
        describe_panel.add(incv_text);
        describe_panel.add(Box.createVerticalStrut(35));
        
        return describe_panel;
    }

    private JButton createStyledButton(String text, Color background, Color foreground, Font font)
    {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
                button.setBackground(background);
                button.setForeground(foreground);
            }
        });
        return button;
    }

    public void restoreWindow()
    {
        this.mainWindow.setTitle("RSA Encryption system");
        this.mainWindow.setContentPane(new RSA(this.mainWindow));
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
