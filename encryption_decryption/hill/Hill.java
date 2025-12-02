package crypto.encryption_decryption.hill;

import crypto.utils.DrawBackground;
import crypto.Main;
import crypto.Home;
import crypto.Header;
import crypto.encryption_decryption.hill.HillEncrypt;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.*;

public class Hill extends JPanel
{
    private final String MAIN_BG_PATH = "crypto/ressources/Gemini_Generated_Image_jyfixfjyfixfjyfi.png";
    private final String PANE_BG_1    = "crypto/ressources/IMG-20241008-WA0039.jpg";
    private final String PANE_BG_2    = "crypto/ressources/logo.jpg";
    private final String PANE_BG_3    = "crypto/ressources/IMG-20251026-WA0096.jpg";
    private final String PANE_BG_4    = "crypto/ressources/phishing.png";

    private static final Color PRIMARY_BUTTON_COLOR = new Color(0, 150, 255);
    private static final Color HOVER_COLOR          = new Color(0, 100, 200);
    private static final Color BACK_BUTTON_COLOR    = new Color(255, 50, 50);
    private static final Color SECTION_TEXT_COLOR   = Color.WHITE;
    private static final Color SECTION_TITLE_COLOR  = new Color(255, 255, 0);

    protected JButton encrypt;
    protected JButton decrypt;
    protected JButton back;
    protected Main mainWindow;

    public Hill(Main window)
    {
        this.mainWindow = window;
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

        this.encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Hill.this.mainWindow.setTitle("Hill Encryption System");
                Hill.this.mainWindow.getContentPane().removeAll();
                Hill.this.mainWindow.setContentPane(new HillEncrypt(Hill.this));
                Hill.this.mainWindow.revalidate();
                Hill.this.mainWindow.repaint();
            }
        });
        this.encrypt.addMouseListener(actionButtonHover);

        this.decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Hill.this.mainWindow.setTitle("Hill Decryption System (Not implemented)");
                // Hill.this.mainWindow.setContentPane(new HillDecrypt(Hill.this));
                // Hill.this.mainWindow.revalidate();
                // Hill.this.mainWindow.repaint();
            }
        });
        this.decrypt.addMouseListener(actionButtonHover);

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(new Header(Hill.this.mainWindow),BorderLayout.NORTH);
                panel.add(new Home(Hill.this.mainWindow),BorderLayout.CENTER);

                Hill.this.mainWindow.setTitle("Crypto Application");
                Hill.this.mainWindow.getContentPane().removeAll();
                Hill.this.mainWindow.setContentPane(panel);
                Hill.this.mainWindow.getContentPane().revalidate();
                Hill.this.mainWindow.getContentPane().repaint();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                Hill.this.back.setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                Hill.this.back.setBackground(BACK_BUTTON_COLOR);
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
            "🔢 Le Chiffrement de Hill : Substitution Polygraphique",
            "Le chiffrement de Hill est un algorithme de substitution polygraphique, ce qui signifie qu'il chiffre des blocs de lettres ensemble, utilisant les concepts de l'algèbre linéaire (matrices) pour la substitution. Il est plus robuste que les chiffrements monoalphabétiques."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_2,
            "📐 Principe Clé : Opérations Matricielles",
            "Le chiffrement de Hill utilise une **matrice clé K** de taille m \times m. Un bloc de m lettres du message clair est converti en un vecteur colonne P de taille m. Le vecteur chiffré C est calculé par :\n\n" +
            "C = K P \\pmod{26}" +
            "Pour le déchiffrement, on utilise la matrice inverse de la clé, K^{-1}."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_3,
            "💡 Condition de Déchiffrement",
            "Pour que le déchiffrement soit possible, la matrice clé K doit être **inversible modulo 26**. Cela signifie que le **déterminant de la matrice clé** (det(K)) doit être **premier avec 26** (\text{pgcd}(det(K), 26) = 1)."
        ));

        main_panel_sections.add(createSectionPanel(
            PANE_BG_4,
            "⚔️ Sécurité et Limites",
            "• **Meilleure Résistance** : Le chiffrement de Hill résiste à l'analyse de fréquence car la fréquence d'une lettre dans le chiffré ne correspond pas à celle du clair.\n" +
            "• **Vulnérabilité** : Il est vulnérable à l'**attaque par texte clair connu**, car il suffit d'un certain nombre de paires (clair/chiffré) pour résoudre un système d'équations linéaires et trouver la matrice clé K."
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
        this.mainWindow.setTitle("Hill Encryption System");
        this.mainWindow.setContentPane(new Hill(this.mainWindow));
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
