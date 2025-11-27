package crypto.cesar;
import crypto.Main;
import crypto.Home;
import crypto.Header;
import crypto.cesar.CesarDecrypt;
import crypto.utils.DrawBackground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;


public class Cesar extends JPanel
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
    protected Main mainWindow;

    public Cesar(Main mainWindow)
    {
        this.mainWindow = mainWindow;
        this.setSize(new Dimension(500,500));
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

        this.encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Cesar.this.mainWindow.setTitle("Cesar Encryption system");
                Cesar.this.mainWindow.setContentPane(new CesarEncrypt(Cesar.this)); 
                Cesar.this.mainWindow.revalidate();
                Cesar.this.mainWindow.repaint();
            }
        });
        this.encrypt.addMouseListener(actionButtonHover);


        this.decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Cesar.this.mainWindow.setTitle("Cesar Decryption system");
                Cesar.this.mainWindow.setContentPane(new CesarDecrypt(Cesar.this));
                Cesar.this.mainWindow.revalidate();
                Cesar.this.mainWindow.repaint();
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
                
                panel.add(new Header(Cesar.this.mainWindow),BorderLayout.NORTH);
                panel.add(new Home(Cesar.this.mainWindow),BorderLayout.CENTER);
                
                Cesar.this.mainWindow.setTitle("Crypto Application");
                Cesar.this.mainWindow.getContentPane().removeAll();
                Cesar.this.mainWindow.setContentPane(panel);
                Cesar.this.mainWindow.getContentPane().revalidate();
                Cesar.this.mainWindow.getContentPane().repaint();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                Cesar.this.back.setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                Cesar.this.back.setBackground(BACK_BUTTON_COLOR);
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
        doc_panel.add(btn_panel,BorderLayout.NORTH);

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
            "🏛️ Le Chiffrement de César : Substitution par Décalage",
            "Le chiffrement de César (ou code de César) est une méthode de cryptographie très ancienne qui fait partie de la catégorie des chiffrements par substitution monoalphabétique. Il est nommé d'après Jules César, qui l'utilisait pour communiquer avec ses généraux."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_2,
            "\uD83C\uDF10 Principe Clé : Décalage Constant",
            "Le principe du chiffrement de César est remarquablement simple : chaque lettre du message en clair est remplacée par une lettre située un certain nombre de positions plus loin dans l'alphabet.\n\n" +
            "• **La Clé** : La clé est un simple nombre entier (un décalage) compris entre 1 et 25.\n" +
            "• **Substitution** : Chaque lettre est décalée de ce nombre de positions vers la droite (ou vers la gauche pour le déchiffrement). Si le décalage dépasse 'Z', il revient au début de l'alphabet ('A') (principe du modulo).\n\n" +
            "**Exemple Classique (Décalage 3)** :\n" +
            "  'A' devient 'D'\n" +        
            "  'B' devient 'E'\n" +
            "  ...\n" +
            "  'X' devient 'A'"
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_3,
            "🧮 Méthode de Chiffrement (Modulo 26)",
            "Le chiffrement de César est une opération d'arithmétique modulaire simple (modulo 26).\n" +
            "Chaque lettre est convertie en un nombre (A=0, B=1, ..., Z=25).\n\n" +
            "• **P** : Valeur numérique de la lettre du clair (Plaintext).\n" +
            "• **K** : Valeur du décalage (la Clé).\n" +
            "• **C** : Valeur numérique de la lettre chiffrée (Ciphertext).\n\n" +
            "Formule de Chiffrement : C = (P + K) mod 26\n\n" + 
            "Formule de Déchiffrement : P = (C - K) mod 26"
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_4,
            "📉 Sécurité (Très Faible)",
            "Le chiffrement de César est très facile à casser dans le contexte moderne.\n\n" +
            "• **Faible Espace de Clé** : Il n'y a que 25 clés possibles. On trouve la bonne clé par simple force brute (tester les 25 décalages).\n" +
            "• **Analyse Fréquentielle** : L'analyse des fréquences des lettres ('E' est souvent la plus fréquente) permet de déduire rapidement le décalage.\n\n" +
            "Il est aujourd'hui utilisé principalement comme introduction aux concepts de la cryptographie."
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
        this.mainWindow.setTitle("Cesar Encryption system");
        this.mainWindow.setContentPane(new Cesar(this.mainWindow));
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
