package crypto.encryption_decryption.vigenere;

import crypto.utils.DrawBackground;
import crypto.Main;
import crypto.Header;
import crypto.Home;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.border.*;

public class Vigenere extends JPanel
{
    private final String MAIN_BG_PATH = "crypto/ressources/IMG-20251023-WA0023.jpg";
    private final String PANE_BG_1    = "crypto/ressources/code1.png";
    private final String PANE_BG_2    = "crypto/ressources/app.png";
    private final String PANE_BG_3    = "crypto/ressources/change.png";
    private final String PANE_BG_4    = "crypto/ressources/coal.png";

    private static final Color PRIMARY_BUTTON_COLOR = new Color(0, 150, 255);
    private static final Color HOVER_COLOR          = new Color(0, 100, 200);
    private static final Color BACK_BUTTON_COLOR    = new Color(255, 50, 50);
    private static final Color SECTION_TEXT_COLOR   = Color.WHITE;
    private static final Color SECTION_TITLE_COLOR  = new Color(255, 255, 0);

    protected JButton encrypt;
    protected JButton decrypt;
    protected JButton back;
    private Main mainWindow;
    
    public Vigenere(Main mainWindow)
    {
        this.mainWindow = mainWindow;
        this.setSize(new Dimension(700,700));

        DrawBackground background = new DrawBackground(MAIN_BG_PATH);
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

        encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Vigenere.this.mainWindow.setTitle("Système de Chiffrement de Vigenère");
                Vigenere.this.mainWindow.setContentPane(new VigenereEncrypt(Vigenere.this));
                Vigenere.this.mainWindow.revalidate();
                Vigenere.this.mainWindow.repaint();
            }
        });
        this.encrypt.addMouseListener(actionButtonHover);

        this.decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Vigenere.this.mainWindow.setTitle("Système de Déchiffrement de Vigenère");
                Vigenere.this.mainWindow.setContentPane(new VigenereDecrypt(Vigenere.this));
                Vigenere.this.mainWindow.revalidate();
                Vigenere.this.mainWindow.repaint();
            }
        });
        this.decrypt.addMouseListener(actionButtonHover);
        
        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.add(new Header(Vigenere.this.mainWindow),BorderLayout.NORTH);
                panel.add(new Home(Vigenere.this.mainWindow),BorderLayout.CENTER);

                Vigenere.this.mainWindow.getContentPane().removeAll();
                Vigenere.this.mainWindow.setTitle("Application Crypto");
                Vigenere.this.mainWindow.setContentPane(panel);
                Vigenere.this.mainWindow.getContentPane().revalidate();
                Vigenere.this.mainWindow.getContentPane().repaint();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                Vigenere.this.back.setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                Vigenere.this.back.setBackground(BACK_BUTTON_COLOR);
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
            "📜 Le Chiffrement de Vigenère : Le Chiffre Indéchiffrable",
            "Le chiffrement de Vigenère est un algorithme de substitution polyalphabétique, souvent considéré historiquement comme le 'chiffre indéchiffrable' avant qu'une méthode d'attaque (méthode de Kasiski) ne soit découverte."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_2,
            "🗝️ Principe Clé : La Clé Répétitive",
            "Contrairement à César, Vigenère utilise une **phrase clé** ou un **mot clé**. Chaque lettre de la clé détermine un décalage différent pour chaque lettre du message clair. La clé est répétée pour correspondre à la longueur du message.\n" +
            "• **Exemple** : Message 'ATTAQUE', Clé 'CLE'\n" +
            "  Clair:  A T T A Q U E\n" +
            "  Clé:    C L E C L E C"
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_3,
            "🧮 Méthode de Chiffrement (Modulo 26)",
            "Le chiffrement utilise l'arithmétique modulaire, comme le chiffrement de César, mais le décalage (la clé K_i) change à chaque position i.\n\n" +
            "• P_i : Valeur numérique de la lettre du clair à la position i.\n" +
            "• K_i : Valeur numérique de la lettre de la clé à la position i.\n" +
            "• C_i : Valeur numérique de la lettre chiffrée.\n\n" +
            "Formule de Chiffrement : C_i = (P_i + K_i) mod{26}"
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_4,
            "⚔️ Sécurité et Analyse",
            "• **Meilleure Sécurité** : La polyalphabétisation masque les fréquences des lettres, rendant l'analyse de fréquence simple inefficace.\n" +
            "• **Vulnérabilité** : Il est vulnérable à l'**analyse de Kasiski** et à l'**indice de coïncidence**, qui permettent de retrouver la longueur de la clé, réduisant ensuite le problème à plusieurs chiffrements de César."
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
        this.mainWindow.setTitle("Vigenere Encryption system");
        this.mainWindow.setContentPane(new Vigenere(this.mainWindow));
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
