package crypto;

import crypto.Main;
import crypto.Home;
import crypto.Header;
import crypto.users.UserProfile;
import crypto.session.UserSession;
import crypto.users.Connect;
import crypto.users.Registration;
import crypto.utils.AnimatedBubble;
import crypto.utils.DrawBackground;
import crypto.utils.ThemeManager;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Main extends JFrame
{
    private static final Color PRIMARY_COLOR      = ThemeManager.DARK_BG_PRIMARY;       
    private static final Color ACCENT_COLOR       = ThemeManager.ACCENT_CYAN;     
    private static final Color SECONDARY_ACCENT   = ThemeManager.ACCENT_BLUE;      
    private static final Color TEXT_LIGHT         = ThemeManager.TEXT_PRIMARY;
    private static final Color TEXT_DARK          = ThemeManager.TEXT_SECONDARY;       
    private static final Color BACKGROUND_OVERLAY = new Color(255, 255, 255, 210); 
    
    private static final Font TITLE_FONT          = ThemeManager.FONT_TITLE;
    private static final Font SUBTITLE_FONT       = ThemeManager.FONT_SUBTITLE;
    private static final Font BODY_FONT           = ThemeManager.FONT_BODY;
    private static final Font MONO_FONT           = ThemeManager.FONT_MONO;
    
    private static final int ANIMATION_DURATION = 150; 
    private static final int ELEVATION_PX       = 5;    
    
    protected JButton _try;
    protected JButton sign;
    protected JButton login;
    protected JButton exit;
    protected Home    window;
    protected Connect connexion;
    protected JButton start;

    public Main()
    {
        this.login = createCyberButton("Log In", ThemeManager.ACCENT_GREEN);
        this._try  = createCyberButton("Try as guest", ThemeManager.ACCENT_BLUE);
        this.sign  = createCyberButton("Sign In", ThemeManager.ACCENT_PURPLE);
        this.exit  = createCyberButton("Exit", new Color(231, 76, 60));
        this.start = createCyberButton("Get Started", ThemeManager.ACCENT_CYAN);

        this.setLocationRelativeTo(null);
        this.setSize(1000, 800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Crypto Application - Secure Cryptography Suite");

        crypto.session.SessionInterceptor.startInactivityMonitor(this);
        
        this.showHome();
    }
    
    public static JButton createCyberButton(String text, Color background)
    {
        JButton button = new JButton(text) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int elevation = 0;
                if (getModel().isArmed()) {
                    elevation = 2;
                } else if (getModel().isRollover()) {
                    elevation = 5;
                }
                
                int width  = getWidth();
                int height = getHeight();
                int arc    = 15;

                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(elevation, elevation, width - elevation, height - elevation, arc, arc);

                GradientPaint gradient = new GradientPaint(
                    0, 0, background,
                    0, height, ThemeManager.darken(background, 0.7f)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, width - elevation, height - elevation, arc, arc);
                
                g2.setColor(new Color(255, 255, 255, 100));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, width - elevation - 1, height - elevation - 1, arc, arc);

                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setForeground(ThemeManager.TEXT_PRIMARY);
        button.setFont(ThemeManager.FONT_MONO_BOLD);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        button.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseEntered(MouseEvent event) 
            {
                button.setForeground(Color.WHITE);
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent event) 
            {
                button.setForeground(ThemeManager.TEXT_PRIMARY);
                button.repaint();
            }
        });
        
        return button;
    }

    private JPanel welcome()
    {
        this._try.addActionListener(event -> 
        {
            UserSession session = UserSession.getInstance();
            if(session.isLoggedIn()) 
            {
                int choice = JOptionPane.showConfirmDialog(
                    Main.this,
                    "<html><div style='color:#00ffff;'>" +
                    "Vous avez déjà une session active.<br>" +
                    "Voulez-vous continuer en mode invité ?<br>" +
                    "(Votre session actuelle sera fermée)</div></html>",
                    "Session existante",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if(choice == JOptionPane.YES_OPTION) 
                {
                    session.destroySession();
                    Main.this.navigateToHome(true);
                }
            }
            else
                Main.this.navigateToHome(true);
        });

        this.sign.addActionListener(evant ->
        {
            Main.this.getContentPane().removeAll();
            Main.this.setContentPane(new Registration(Main.this));
            Main.this.revalidate();
            Main.this.repaint(); 
        });

        this.login.addActionListener(event -> 
        {
            Main.this.getContentPane().removeAll();
            Main.this.setContentPane(new Connect(Main.this));
            Main.this.revalidate();
            Main.this.repaint();
        });

        this.exit.addActionListener(event -> {
            crypto.session.SessionInterceptor.stopInactivityMonitor();
            Main.this.dispose();
        });

        JPanel try_layout = new JPanel();
        try_layout.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        try_layout.setOpaque(false);
        try_layout.add(this._try);
        try_layout.add(this.sign);
        try_layout.add(this.login);
        try_layout.add(this.exit);

        JPanel datas_panel = new JPanel(new BorderLayout());
        Header header = new Header(this);
        header.removeBackBtn();
        header.removeLogOutBtn();
        header.removeAnalystBtn();
        
        datas_panel.setOpaque(false);
        datas_panel.add(header, BorderLayout.WEST);
        datas_panel.add(try_layout, BorderLayout.EAST);
        
        Border data_border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 3, true),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );
        
        JLabel we_come = new JLabel("Bienvenue dans le monde du cryptage !");
        we_come.setFont(TITLE_FONT);
        we_come.setAlignmentX(Component.CENTER_ALIGNMENT);
        we_come.setForeground(ThemeManager.ACCENT_CYAN);
       
        JTextArea intro = new JTextArea("Les messages secrets sont notre spécialité ! Nous sommes ravis de vous accueillir dans notre application de cryptographie, où les codes et les chiffres n'ont plus de secrets pour vous.");
        intro.setEditable(false);
        intro.setOpaque(false);
        intro.setFont(BODY_FONT);
        intro.setLineWrap(true);
        intro.setWrapStyleWord(true);
        intro.setAlignmentX(Component.CENTER_ALIGNMENT); 
        intro.setForeground(ThemeManager.TEXT_SECONDARY);

        JLabel dev = new JLabel("Découvrez nos systèmes de cryptage légendaires :");
        dev.setFont(SUBTITLE_FONT);
        dev.setAlignmentX(Component.CENTER_ALIGNMENT);
        dev.setForeground(ThemeManager.ACCENT_BLUE);

        JTextArea algoList = new JTextArea(
            "  * RSA: le roi des algorithmes asymétriques.\n" +
            "  * Cesar: le décalage classique qui a trompé plus d'un espion.\n" +
            "  * Vigenere: le code polyalphabétique qui a résisté aux siècles.\n" +
            "  * Vernam: le masque jetable pour la sécurité absolue.\n" +
            "  * Hill: le système matriciel pour chiffrer vos messages.\n" +
            "  * Affine: la combinaison parfaite de mathématiques et de cryptographie.\n" +
            "  * PlayFair: le code qui remplace les lettres par des paires.\n" +
            "  * Feistel: l'algorithme qui chiffre vos données en plusieurs tours.\n" +
            "  * Transposition rectangulaire: le système qui mélange les lettres."
        );
        
        algoList.setEditable(false);
        algoList.setOpaque(false);
        algoList.setFont(MONO_FONT); 
        algoList.setAlignmentX(Component.CENTER_ALIGNMENT);
        algoList.setForeground(ThemeManager.TEXT_SECONDARY);
       
        JTextArea invit = new JTextArea("Nous vous invitons à explorer nos fonctionnalités de chiffrement et de déchiffrement, et à découvrir comment nos systèmes peuvent protéger vos communications.");
        invit.setEditable(false);
        invit.setOpaque(false);
        invit.setFont(BODY_FONT);
        invit.setLineWrap(true);
        invit.setWrapStyleWord(true);
        invit.setAlignmentX(Component.CENTER_ALIGNMENT);
        invit.setForeground(ThemeManager.TEXT_SECONDARY);
       
        JLabel join = new JLabel("Rejoignez la communauté !");
        join.setFont(TITLE_FONT.deriveFont(Font.BOLD, 30f));
        join.setAlignmentX(Component.CENTER_ALIGNMENT);
        join.setForeground(ThemeManager.ACCENT_PURPLE); 
        
        JTextArea describe_join = new JTextArea(
            "Créez un compte pour accéder à toutes les fonctionnalités et profitez de nos services de cryptographie de pointe.\n" +
            "Nous sommes convaincus que vous apprécierez la sécurité et la facilité d'utilisation de notre application.\n" +
            "Alors qu'attendez-vous ? Inscrivez-vous dès aujourd'hui et commencez à protéger vos secrets !"
        );

        describe_join.setFont(BODY_FONT);
        describe_join.setEditable(false);
        describe_join.setOpaque(false);
        describe_join.setLineWrap(true);
        describe_join.setWrapStyleWord(true);
        describe_join.setAlignmentX(Component.CENTER_ALIGNMENT);
        describe_join.setForeground(ThemeManager.TEXT_SECONDARY);
       
        JPanel home_panel = new JPanel();
        home_panel.setBorder(data_border);
        home_panel.setLayout(new BoxLayout(home_panel, BoxLayout.Y_AXIS));
        home_panel.setOpaque(true);
        home_panel.setBackground(new Color(15, 25, 40, 240));

        JPanel start_con = new JPanel();
        start_con.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
        start_con.setOpaque(false);

        this.start.addActionListener(event -> 
        {
            Main.this.getContentPane().removeAll();
            Registration sigin_window = new Registration(Main.this);
            Main.this.setContentPane(sigin_window);
            Main.this.revalidate();
        });

        start_con.add(this.start);
        start_con.add(this.login);

        home_panel.add(we_come);
        home_panel.add(Box.createVerticalStrut(25));
        home_panel.add(intro);
        home_panel.add(Box.createVerticalStrut(40));
        home_panel.add(dev);
        home_panel.add(Box.createVerticalStrut(15));
        home_panel.add(algoList);
        home_panel.add(Box.createVerticalStrut(40));
        home_panel.add(invit);
        home_panel.add(Box.createVerticalStrut(40));
        home_panel.add(join);
        home_panel.add(Box.createVerticalStrut(15));
        home_panel.add(describe_join); 
        home_panel.add(Box.createVerticalStrut(30));
        home_panel.add(start_con);
        
        JScrollPane scrollPane = new JScrollPane(home_panel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setWheelScrollingEnabled(true);
        
        scrollPane.addMouseWheelListener(new MouseWheelListener()
        {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event)
            {
                JScrollBar bar  = scrollPane.getVerticalScrollBar();
                int value       = event.getWheelRotation() * bar.getUnitIncrement();
                bar.setValue(bar.getValue() + value);
            }
        });

        AnimatedBubble animatedBackground = new AnimatedBubble(ThemeManager.ACCENT_CYAN);
        animatedBackground.setLayout(new BorderLayout());
        animatedBackground.add(scrollPane, BorderLayout.CENTER);
        animatedBackground.setOpaque(false);
        
        JPanel main_panel = new JPanel();
        main_panel.setLayout(new BorderLayout());
        main_panel.add(animatedBackground, BorderLayout.CENTER);
        main_panel.add(datas_panel, BorderLayout.NORTH);
        main_panel.setOpaque(false);
        
        DrawBackground container = new DrawBackground("crypto/ressources/Perfect Designer - Tech Innovation.jpeg");
        container.setLayout(new BorderLayout());
        container.add(main_panel, BorderLayout.CENTER);
        
        return container;
    }

    public void navigateToHome(boolean isGuest) {
    Container container = this.getContentPane();
    
    Border lineBorder = BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 3, true); 
    
    String guest = "Guest Mode";
    String user = "User Mode";
    
    Border border = BorderFactory.createTitledBorder(
        lineBorder,
        isGuest ? guest : user, 
        TitledBorder.CENTER, TitledBorder.TOP, 
        SUBTITLE_FONT.deriveFont(Font.BOLD, 24f), 
        ThemeManager.ACCENT_CYAN 
    );
    
    DrawBackground background = new DrawBackground("crypto/ressources/Screenshot 2025-12-04 at 11-12-56 (Image JPEG 564 × 564 pixels).png");
    background.setOpaque(false);
    
    container.removeAll();
    container.setLayout(new BorderLayout());
    
    Border marginBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
    
    if (isGuest) {
        UserSession session = UserSession.getInstance();
        session.createGuestSession();
        
        // Mode invité - Home normal
        EnhancedHeader navpanel = new EnhancedHeader(this);
        navpanel.setBackground(ThemeManager.DARK_BG_PRIMARY); 
        
        Home homeContent = new Home(this);
        JScrollPane homeScroll = new JScrollPane(homeContent);
        homeScroll.setBorder(null);
        homeScroll.getViewport().setOpaque(false);
        homeScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        homeScroll.getVerticalScrollBar().setUnitIncrement(20);
        
        background.setLayout(new BorderLayout());
        background.add(navpanel, BorderLayout.NORTH);
        background.add(homeScroll, BorderLayout.CENTER);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        
        contentWrapper.setBorder(BorderFactory.createCompoundBorder(marginBorder, border));
        contentWrapper.add(background, BorderLayout.CENTER);
    
        container.add(contentWrapper, BorderLayout.CENTER);
    } else {
        // Mode utilisateur connecté - UserProfile
        UserProfile userProfile = new UserProfile(this);
        JScrollPane profileScroll = new JScrollPane(userProfile);
        profileScroll.setBorder(null);
        profileScroll.getViewport().setOpaque(false);
        profileScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        profileScroll.getVerticalScrollBar().setUnitIncrement(20);
        
        background.setLayout(new BorderLayout());
        background.add(profileScroll, BorderLayout.CENTER);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        
        contentWrapper.setBorder(BorderFactory.createCompoundBorder(marginBorder, border));
        contentWrapper.add(background, BorderLayout.CENTER);
        
        container.add(contentWrapper, BorderLayout.CENTER);
    }
    
    container.revalidate();
    container.repaint();
}

    public void showHome()
    {
        this.setTitle("Crypto Application - Welcome");
        this.setContentPane(welcome());
        this.revalidate();
        this.repaint();
    }

    public static void main(String[] args) throws Exception
    {
        try
        {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            UIManager.put("control", ThemeManager.DARK_BG_SECONDARY);
            UIManager.put("info", ThemeManager.DARK_BG_TERTIARY);
            UIManager.put("nimbusBase", ThemeManager.DARK_BG_PRIMARY);
            UIManager.put("nimbusFocus", ThemeManager.ACCENT_CYAN);
            UIManager.put("nimbusLightBackground", ThemeManager.DARK_BG_SECONDARY);
            UIManager.put("text", ThemeManager.TEXT_PRIMARY);
        } 
        catch(UnsupportedLookAndFeelException e) 
        {
            System.out.println("Nimbus Look and Feel not supported. Using default.");
        }
        
        SwingUtilities.invokeLater(() -> 
        {
            Main window = new Main();
            window.setVisible(true);
        });
    }
}
