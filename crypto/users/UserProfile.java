package crypto.users;

import crypto.Main;
import crypto.session.UserSession;
import crypto.utils.DrawBackground;
import crypto.utils.ThemeManager;
import crypto.encryption_decryption.rsa.RSA;
import crypto.encryption_decryption.hill.Hill;
import crypto.encryption_decryption.cesar.Cesar;
import crypto.encryption_decryption.vernam.Vernam;
import crypto.encryption_decryption.affine.Affine;
import crypto.encryption_decryption.feistel.Feistel;
import crypto.encryption_decryption.vigenere.Vigenere;
import crypto.crypt_analyst_brute_force.CryptAnalyst;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserProfile extends JPanel 
{
    private Main mainWindow;
    private UserSession session;
    
    private JLabel welcomeLabel;
    private JLabel userInfoLabel;
    private JButton editProfileButton;
    private JButton logoutButton;
    
    public UserProfile(Main mainWindow) 
    {
        this.mainWindow = mainWindow;
        this.session = UserSession.getInstance();    
        initUI();
    }
    
    private void initUI() 
    {
        DrawBackground background = new DrawBackground("crypto/ressources/Screenshot 2025-12-04 at 11-12-56 (Image JPEG 564 × 564 pixels).png");
        background.setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setOpaque(false);
        
        JPanel headerPanel = createHeaderPanel();
        
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setOpaque(false);
        
        mainContentPanel.add(createWelcomeSection());
        mainContentPanel.add(Box.createVerticalStrut(30));
        
        mainContentPanel.add(createFeaturesOverview());
        mainContentPanel.add(Box.createVerticalStrut(30));
        
        mainContentPanel.add(createQuickAccessSection());
        mainContentPanel.add(Box.createVerticalStrut(30));
        
        mainContentPanel.add(createCryptanalysisSection());
        mainContentPanel.add(Box.createVerticalStrut(30));
        
        mainContentPanel.add(createHistorySection());
        
        JPanel paddedContent = new JPanel(new BorderLayout());
        paddedContent.setOpaque(false);
        paddedContent.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        paddedContent.add(mainContentPanel, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(paddedContent);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setWheelScrollingEnabled(true);
        
        scrollPane.addMouseWheelListener(e -> 
        {
            JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
            int value = verticalScrollBar.getValue();
            int extent = verticalScrollBar.getVisibleAmount();
            int maximum = verticalScrollBar.getMaximum();
            
            if(e.getWheelRotation() < 0)
                value = Math.max(0, value - 16);
            else
                value = Math.min(maximum - extent, value + 16);
            
            verticalScrollBar.setValue(value);
        });

        contentPanel.add(headerPanel, BorderLayout.CENTER);
        
        background.add(contentPanel, BorderLayout.NORTH);
        background.add(scrollPane, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(background, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() 
    {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, ThemeManager.ACCENT_CYAN),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        
        JLabel titleLabel = new JLabel("CryptoApp Pro");
        titleLabel.setFont(ThemeManager.FONT_TITLE);
        titleLabel.setForeground(ThemeManager.ACCENT_CYAN);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        editProfileButton = createCyberButton("Edit Profile", ThemeManager.ACCENT_BLUE);
        logoutButton = createCyberButton("Logout", new Color(231, 76, 60));
        
        editProfileButton.addActionListener(e -> showProfileEditor());
        logoutButton.addActionListener(e -> performLogout());
        
        buttonPanel.add(editProfileButton);
        buttonPanel.add(logoutButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createWelcomeSection() 
    {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setOpaque(false);
        
        String userName = session.getCurrentUser().getLogin();
        welcomeLabel = new JLabel("Bienvenue, " + userName + "!");
        welcomeLabel.setFont(ThemeManager.FONT_TITLE.deriveFont(32f));
        welcomeLabel.setForeground(ThemeManager.ACCENT_CYAN);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        userInfoLabel = new JLabel(
            "<html><div style='text-align: center; color: #AAAAAA;'>" +
            "Session active depuis " + session.getFormattedSessionDuration() + "<br>" +
            "Type de compte : Utilisateur Premium<br>" +
            "Acces complet a toutes les fonctionnalites" +
            "</div></html>"
        );
        userInfoLabel.setFont(ThemeManager.FONT_BODY);
        userInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createVerticalStrut(10));
        welcomePanel.add(userInfoLabel);
        
        return createStyledPanel(welcomePanel, "DashBoard");
    }
    
    private JPanel createFeaturesOverview() 
    {
        JPanel featuresPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        featuresPanel.setOpaque(false);
        
        JPanel encryptionCard = createFeatureCard(
            "🔐",
            "Chiffrement/Dechiffrement",
            "Protegez vos messages avec nos algorithmes cryptographiques avances. Supporte RSA, AES, Cesar, Vigenere, Hill et plus encore.",
            ThemeManager.ACCENT_BLUE
        );
        
        JPanel analysisCard = createFeatureCard(
            "🔍",
            "Cryptanalyse",
            "Analysez et cassez des codes cryptes avec nos outils de cryptanalyse avances. Inclut l'analyse de frequence et la recherche par force brute.",
            ThemeManager.ACCENT_PURPLE
        );
        
        JPanel historyCard = createFeatureCard(
            "📊",
            "Historique",
            "Consultez l'historique de vos operations de chiffrement et dechiffrement. Vos donnees sont sauvegardees automatiquement.",
            ThemeManager.ACCENT_GREEN
        );
        
        JPanel settingsCard = createFeatureCard(
            "⚙️",
            "Parametres",
            "Personnalisez votre experience. Modifiez vos informations personnelles, preferences de securite et parametres d'affichage.",
            ThemeManager.ACCENT_CYAN
        );
        
        featuresPanel.add(encryptionCard);
        featuresPanel.add(analysisCard);
        featuresPanel.add(historyCard);
        featuresPanel.add(settingsCard);
        
        return createStyledPanel(featuresPanel, "Fonctionnalites Disponibles");
    }
    
    private JPanel createQuickAccessSection() 
    {
        JPanel quickAccessPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        quickAccessPanel.setOpaque(false);
        
        String[] algorithms = {"RSA", "Cesar", "Vigenere", "Vernam", "Hill", "Affine", "Feistel", "Playfair"};
        Color[] colors = {
            ThemeManager.ACCENT_CYAN,
            ThemeManager.ACCENT_BLUE,
            ThemeManager.ACCENT_PURPLE,
            ThemeManager.ACCENT_GREEN,
            new Color(255, 165, 0),
            new Color(255, 105, 180),
            new Color(148, 0, 211),
            new Color(0, 191, 255)
        };

        for(int i = 0; i < algorithms.length; i++) 
        {
            JButton algoButton    = createAlgorithmButton(algorithms[i], colors[i]);
            final String algoName = algorithms[i];
            algoButton.addActionListener(e -> navigateToAlgorithm(algoName));
            quickAccessPanel.add(algoButton);
        }
        
        return createStyledPanel(quickAccessPanel, "Acces Rapide - Algorithmes");
    }
    
    private JPanel createCryptanalysisSection() 
    {
        JPanel analysisPanel = new JPanel(new BorderLayout(20, 0));
        analysisPanel.setOpaque(false);
        
        JTextArea description = new JTextArea(
            "Notre module de cryptanalyse vous permet d'analyser des textes cryptes sans connaître la cle. " +
            "Fonctionnalites inclues :\n" +
            "• Analyse de frequence des lettres\n" +
            "• Attaque par force brute sur Cesar\n" +
            "• Detection de motifs recurrents\n" +
            "• Analyse de la distribution statistique\n" +
            "• Support pour plusieurs algorithmes classiques"
        );
        description.setFont(ThemeManager.FONT_BODY);
        description.setForeground(ThemeManager.TEXT_SECONDARY);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setOpaque(false);
        description.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton analysisButton = createCyberButton("Acceder au Cryptanalyseur", ThemeManager.ACCENT_PURPLE);
        analysisButton.setPreferredSize(new Dimension(200, 50));
        analysisButton.addActionListener(e -> navigateToCryptanalysis());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(analysisButton);
        
        analysisPanel.add(description, BorderLayout.CENTER);
        analysisPanel.add(buttonPanel, BorderLayout.EAST);
        
        return createStyledPanel(analysisPanel, "Outils de Cryptanalyse");
    }
    
    private JPanel createHistorySection() 
    {
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setOpaque(false);
        
        String[] columnNames = {"Date", "Operation", "Algorithm", "Statut"};
        Object[][] data = {
            {"Aucune donnee disponible", "", "", ""}
        };
        
        JTable historyTable = new JTable(data, columnNames);
        historyTable.setFont(ThemeManager.FONT_MONO);
        historyTable.getTableHeader().setFont(ThemeManager.FONT_MONO_BOLD);
        historyTable.setForeground(ThemeManager.TEXT_PRIMARY);
        historyTable.setBackground(ThemeManager.DARK_BG_TERTIARY);
        historyTable.setGridColor(ThemeManager.ACCENT_CYAN);
        historyTable.setEnabled(false);
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        
        return createStyledPanel(historyPanel, "Historique des Operations");
    }
    
    private JPanel createFeatureCard(String emoji, String title, String description, Color color) 
    {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setOpaque(true);
        card.setBackground(new Color(30, 40, 60, 200));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ThemeManager.FONT_MONO_BOLD);
        titleLabel.setForeground(color);
        
        headerPanel.add(emojiLabel);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(titleLabel);
        
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(ThemeManager.FONT_BODY);
        descArea.setForeground(ThemeManager.TEXT_SECONDARY);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        descArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(descArea, BorderLayout.CENTER);
        
        return card;
    }
    
    private JButton createAlgorithmButton(String text, Color color) 
    {
        JButton button = new JButton(text) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if(getModel().isPressed())
                    g2.setColor(color.darker().darker());
                else if(getModel().isRollover()) 
                {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, color.brighter(),
                        0, getHeight(), color
                    );
                    g2.setPaint(gradient);
                } 
                else
                    g2.setColor(color);
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.setColor(new Color(255, 255, 255, 100));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setForeground(Color.WHITE);
        button.setFont(ThemeManager.FONT_MONO_BOLD);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() 
        {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent event) 
            {
                button.setForeground(Color.WHITE);
                button.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent event) 
            {
                button.setForeground(Color.WHITE);
                button.repaint();
            }
        });
        
        return button;
    }
    
    private JButton createCyberButton(String text, Color backgroundColor) 
    {
        JButton button = new JButton(text) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if(getModel().isPressed())
                    g2.setColor(backgroundColor.darker().darker());
                else if (getModel().isRollover()) 
                {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, backgroundColor.brighter(),
                        0, getHeight(), backgroundColor
                    );
                    g2.setPaint(gradient);
                } 
                else 
                    g2.setColor(backgroundColor);
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.setColor(new Color(255, 255, 255, 100));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(ThemeManager.FONT_MONO_BOLD);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(backgroundColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        
        return button;
    }
    
    private JPanel createStyledPanel(JPanel innerPanel, String title) 
    {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 2),
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            ThemeManager.FONT_SUBTITLE,
            ThemeManager.ACCENT_CYAN
        );
        
        container.setBorder(BorderFactory.createCompoundBorder(
            border,
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        container.add(innerPanel, BorderLayout.CENTER);
        return container;
    }
    
    private void showProfileEditor() 
    {
        JOptionPane.showMessageDialog(
            this,
            "<html><div style='color:#00ffff;'>" +
            "<b>editeur de Profil</b><br><br>" +
            "Cette fonctionnalite sera disponible dans la prochaine version.<br>" +
            "Vous pourrez modifier :<br>" +
            "• Votre nom d'utilisateur<br>" +
            "• Votre adresse email<br>" +
            "• Votre mot de passe<br>" +
            "• Votre photo de profil" +
            "</div></html>",
            "editeur de Profil",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void performLogout() 
    {
        int confirm = JOptionPane.showConfirmDialog(
            mainWindow,
            "<html><div style='color:#00ffff;'>" +
            "<b>Confirmation de deconnexion</b><br><br>" +
            "Êtes-vous sûr de vouloir vous deconnecter ?</div></html>",
            "Deconnexion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if(confirm == JOptionPane.YES_OPTION) {
            session.destroySession();
            mainWindow.showHome();
            
            JOptionPane.showMessageDialog(
                mainWindow,
                "<html><div style='color:#00ff00;'>Vous avez ete deconnecte avec succes.</div></html>",
                "Deconnexion",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    
    private void navigateToAlgorithm(String algorithmName) 
    {
        switch(algorithmName) 
        {
            case "RSA":
                mainWindow.setContentPane(new RSA(mainWindow));
                break;
            case "Cesar":
                mainWindow.setContentPane(new Cesar(mainWindow));
                break;
            case "Vigenere":
                mainWindow.setContentPane(new Vigenere(mainWindow));
                break;
            case "Vernam":
                mainWindow.setContentPane(new Vernam(mainWindow));
                break;
            case "Hill":
                mainWindow.setContentPane(new Hill(mainWindow));
                break;
            case "Affine":
                mainWindow.setContentPane(new Affine(mainWindow));
                break;
            case "Feistel":
                mainWindow.setContentPane(new Feistel(mainWindow));
                break;
            case "Playfair":
                JOptionPane.showMessageDialog(this, "Playfair - Fonctionnalite a venir");
                return;
        }
        mainWindow.revalidate();
        mainWindow.repaint();
    }
    
    private void navigateToCryptanalysis() 
    {
        mainWindow.setContentPane(new CryptAnalyst(mainWindow));
        mainWindow.revalidate();
        mainWindow.repaint();
    }
    
    public void refreshUserInfo() 
    {
        if (session.isLoggedIn()) 
        {
            String userName = session.getCurrentUser().getLogin();
            welcomeLabel.setText("Bienvenue, " + userName + "!");
            userInfoLabel.setText(
                "<html><div style='text-align: center; color: #AAAAAA;'>" +
                "Session active depuis " + session.getFormattedSessionDuration() + "<br>" +
                "Type de compte : Utilisateur Premium<br>" +
                "Acces complet a toutes les fonctionnalites" +
                "</div></html>"
            );
        }
    }
}