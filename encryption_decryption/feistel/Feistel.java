package crypto.encryption_decryption.feistel;

import crypto.utils.DrawBackground;
import crypto.Main;
import crypto.Header;
import crypto.Home;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;

public class Feistel extends JPanel
{
    private final String MAIN_BG_PATH = "crypto/ressources/MITM.png";
    private final String PANE_BG_1    = "crypto/ressources/IOT.png";
    private final String PANE_BG_2    = "crypto/ressources/img6.png";
    private final String PANE_BG_3    = "crypto/ressources/IMG-20251026-WA0106.jpg";
    private final String PANE_BG_4    = "crypto/ressources/IMG-20251026-WA0096.jpg";

    private static final Color PRIMARY_BUTTON_COLOR = new Color(0, 150, 255);
    private static final Color HOVER_COLOR          = new Color(0, 100, 200);
    private static final Color BACK_BUTTON_COLOR    = new Color(255, 50, 50);
    private static final Color SECTION_TEXT_COLOR   = Color.WHITE;
    private static final Color SECTION_TITLE_COLOR  = new Color(255, 255, 0);

    protected JButton encrypt;
    protected JButton decrypt;
    protected JButton back;
    protected Main mainWindow;

    public Feistel(Main mainWindow)
    {
        this.mainWindow = mainWindow;
        this.setSize(new Dimension(700,700));
        this.setLayout(new BorderLayout());

        DrawBackground background = new DrawBackground("crypto/ressources/IMG-20251026-WA0102.jpg");
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
                Feistel.this.mainWindow.setTitle("Feistel Encryption System");
                Feistel.this.mainWindow.getContentPane().removeAll();
                Feistel.this.mainWindow.setContentPane(new FeistelEncrypt(Feistel.this));
                Feistel.this.mainWindow.revalidate();
                Feistel.this.mainWindow.repaint();
            }
        });
        this.encrypt.addMouseListener(actionButtonHover);
        
        this.decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Feistel.this.mainWindow.setTitle("Feistel Decryption System");
                Feistel.this.mainWindow.getContentPane().removeAll();
                Feistel.this.mainWindow.setContentPane(new FeistelDecrypt(Feistel.this));
                Feistel.this.mainWindow.revalidate();
                Feistel.this.mainWindow.repaint();
            }
        });
        this.decrypt.addMouseListener(actionButtonHover);

        Dimension btnSize = new Dimension(280, 55); 
        this.back.setPreferredSize(btnSize);
        this.encrypt.setPreferredSize(btnSize);
        this.decrypt.setPreferredSize(btnSize);
        
        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Feistel.this.mainWindow.getContentPane().removeAll();

                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.add(new Header(Feistel.this.mainWindow),BorderLayout.NORTH);
                panel.add(new Home(Feistel.this.mainWindow),BorderLayout.CENTER);

                Feistel.this.mainWindow.getContentPane().removeAll();
                Feistel.this.mainWindow.setTitle("Crypto Application");
                Feistel.this.mainWindow.setContentPane(panel);
                Feistel.this.mainWindow.getContentPane().revalidate();
                Feistel.this.mainWindow.getContentPane().repaint();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                Feistel.this.back.setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                Feistel.this.back.setBackground(BACK_BUTTON_COLOR);
            }
        });

        JPanel button_panel = new JPanel();
        button_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        button_panel.add(this.encrypt);
        button_panel.add(this.decrypt);
        button_panel.add(this.back);
        button_panel.setOpaque(false);

        JPanel doc_pane = doc_panel();
        doc_pane.add(button_panel, BorderLayout.NORTH);
        
        JScrollPane scroll_pane = new JScrollPane(doc_pane);
        scroll_pane.getViewport().setOpaque(false);
        scroll_pane.setBorder(null);
        scroll_pane.getHorizontalScrollBar().setUnitIncrement(16);
        scroll_pane.getVerticalScrollBar().setUnitIncrement(16);
        scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        scroll_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        scroll_pane.addMouseWheelListener(new MouseWheelListener()
        {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event)
            {
                JScrollBar vbar = scroll_pane.getVerticalScrollBar();
                int value = event.getWheelRotation() * vbar.getUnitIncrement();
                vbar.setValue(vbar.getValue() + value);
            }
        });
        
        background.add(scroll_pane);
        this.setLayout(new BorderLayout());
        this.add(background);
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

    private JPanel doc_panel()
    {
        JPanel main_panel = new JPanel(new GridLayout(2, 2, 15, 15));
        main_panel.setOpaque(false);

        main_panel.add(createSectionPanel(PANE_BG_1,"🧱 Le Réseau de Feistel : Architecture par Bloc",
            "Le chiffrement de Feistel (ou réseau de Feistel) est une structure symétrique\n" + 
            "qui sert de base à de nombreux algorithmes de chiffrement par bloc (comme DES, Blowfish, et GOST). \n" +
            "Il permet de construire un algorithme de chiffrement réversible (où le processus de déchiffrement est\n" + 
            "presque identique au chiffrement) même si les opérations internes sont non-réversibles. Ce design est la clé de son efficacité."
        ));

        main_panel.add(createSectionPanel(PANE_BG_2, "🔄 Principe de Base : Division et Itération",
            "Au cœur du chiffrement de Feistel se trouve le concept d'itération (ou round).\n\n" +
            "**Division du Bloc** : Le bloc de données en clair est divisé en deux moitiés de taille égale :\n" + 
            "  - Une gauche (L_i) et une droite (R_i).\n" + 
            "  - Le bloc initial est (L_0, R_0).\n" +
            "**Itération** : Chaque tour utilise une fonction de tour (F) et une sous-clé (K_i) dérivée de la clé principale.\n" +
            "Le nombre de tours (N) détermine la sécurité."
        ));
        
        main_panel.add(createSectionPanel(PANE_BG_3, "⚙️ La Fonction de Tour (F) et l'Opération XOR",
            "La force du réseau réside dans la fonction de tour (F) et la manière dont les moitiés interagissent :\n\n" +
            "**Échange de Données** :\n" + 
            "L_{i+1} = R_i\n\n" +
            "**Combinaison (Opération XOR oplus)** :\n" +
            "R_{i+1} = L_i oplus F(R_i, K_i)\n\n" +
            "C'est l'opération oplus qui garantit la **réversibilité**. Pour déchiffrer, on refait \n" +
            "essentiellement les mêmes étapes en utilisant les sous-clés dans l'ordre inverse (K_{N}, K_{N-1}, ...). Cette structure est dite **symétrique**."
        ));
        
        main_panel.add(createSectionPanel(PANE_BG_4, "🌟 Avantages et Sécurité",
            "**Réversibilité Intégrée** : Le déchiffrement est très similaire au chiffrement, ne nécessitant qu'une\n" + 
            "inversion de l'ordre des sous-clés, simplifiant grandement l'implémentation matérielle et logicielle.\n\n" +
            "**Sécurité** : L'alternance des opérations non-linéaires (dans F) et de la diffusion (via l'échange et le oplus) \n" + 
            "assure une sécurité robuste après plusieurs tours. Même si la fonction F n'est pas réversible, le réseau de Feistel dans son ensemble l'est."
        ));

        JPanel return_panel = new JPanel(new BorderLayout());
        return_panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return_panel.add(main_panel, BorderLayout.CENTER);
        return_panel.setOpaque(false);

        return return_panel;
    }
    public void restoreWindow()
    {
        this.mainWindow.setTitle("Feistel Encryption system");
        this.mainWindow.setContentPane(new Feistel(this.mainWindow));
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
