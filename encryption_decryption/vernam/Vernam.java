package crypto.encryption_decryption.vernam;

import crypto.Main;
import crypto.Home;
import crypto.Header;
import crypto.utils.ThemeManager;
import crypto.utils.DrawBackground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.border.*;

public class Vernam extends JPanel
{
    private final String MAIN_BG_PATH = "crypto/ressources/IMG-20251026-WA0106.jpg";
    private final String PANE_BG_1    = "crypto/ressources/Gemini_Generated_Image_jyfixejyfixejyfi.png";
    private final String PANE_BG_2    = "crypto/ressources/Gemini_Generated_Image_jyfixdjyfixdjyfi.png";
    private final String PANE_BG_3    = "crypto/ressources/Gemini_Generated_Image_jyfixcjyfixcjyfi.png";
    private final String PANE_BG_4    = "crypto/ressources/Gemini_Generated_Image_jyfixajyfixajyfi.png";

    private static final Color PRIMARY_BUTTON_COLOR = new Color(0, 150, 255);
    private static final Color HOVER_COLOR          = new Color(0, 100, 200);
    private static final Color BACK_BUTTON_COLOR    = new Color(255, 50, 50);
    private static final Color SECTION_TEXT_COLOR   = Color.WHITE;
    private static final Color SECTION_TITLE_COLOR  = new Color(255, 255, 0);
    
    protected JButton encrypt;
    protected JButton decrypt;
    protected JButton back;
    private Main mainWindow;

    public Vernam(Main mainWindow)
    {
        this.mainWindow = mainWindow;
        this.setSize(new Dimension(700,700));

        DrawBackground background = new DrawBackground(MAIN_BG_PATH);
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

        this.encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Vernam.this.mainWindow.setTitle("Système de Chiffrement de Vernam");
                Vernam.this.mainWindow.setContentPane(new VernamEncrypt(Vernam.this));
                Vernam.this.mainWindow.revalidate();
                Vernam.this.mainWindow.repaint();
            }
        });
        this.encrypt.addMouseListener(actionButtonHover);

        this.decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Vernam.this.mainWindow.setTitle("Système de Déchiffrement de Vernam");
                Vernam.this.mainWindow.setContentPane(new VernamDecrypt(Vernam.this));
                Vernam.this.mainWindow.revalidate();
                Vernam.this.mainWindow.repaint();
            }
        });
        this.decrypt.addMouseListener(actionButtonHover);

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(new Header(Vernam.this.mainWindow),BorderLayout.NORTH);
                panel.add(new Home(Vernam.this.mainWindow),BorderLayout.CENTER);

                Vernam.this.mainWindow.getContentPane().removeAll();
                Vernam.this.mainWindow.setTitle("Application Crypto");
                Vernam.this.mainWindow.setContentPane(panel);
                Vernam.this.mainWindow.getContentPane().revalidate();
                Vernam.this.mainWindow.getContentPane().repaint();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                Vernam.this.back.setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                Vernam.this.back.setBackground(BACK_BUTTON_COLOR);
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
            "🛡️ Le Chiffrement de Vernam : L'OTP Parfait",
            "Le chiffrement de Vernam, ou One-Time Pad (OTP), est la seule méthode de chiffrement qui a été mathématiquement prouvée comme étant **incassable** (sécurité parfaite), à condition de respecter des conditions très strictes."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_2,
            "💡 Principe Clé : XOR et Clé Aléatoire",
            "Le chiffrement s'effectue par une simple opération **OU exclusif (XOR)** entre chaque bit (ou caractère) du message clair P et un bit (ou caractère) correspondant de la clé K :\n\n" +
            "C = P XOR K\n\n" +
            "Le déchiffrement utilise la même opération :\n" +
            "P = C XOR K\n" +
            "L'opération XOR est sa propre inverse."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_3,
            "✅ Conditions pour la Sécurité Parfaite",
            "La sécurité incassable de l'OTP dépend de trois conditions essentielles :\n" +
            "1. **Aléatoire** : La clé K doit être absolument aléatoire (vraie source de hasard).\n" +
            "2. **Unique** : La clé ne doit être utilisée **qu'une seule fois** (d'où le nom 'One-Time Pad').\n" +
            "3. **Taille** : La clé doit être **au moins aussi longue que le message clair**."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_4,
            "❌ Inconvénients Pratiques",
            "Bien qu'incassable, le Vernam est difficile à utiliser en pratique :\n" +
            "• **Distribution de la Clé** : Le problème de l'échange de la clé est le même que celui du message.\n" +
            "• **Gestion des Clés** : Nécessite de générer et stocker une quantité immense de clés aléatoires, une pour chaque message."
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
        this.mainWindow.setTitle("Vernam Encryption System");
        this.mainWindow.setContentPane(this);
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
