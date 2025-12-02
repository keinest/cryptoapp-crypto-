package crypto;
import crypto.Main;
import crypto.utils.DrawBackground;
import crypto.users.Registration;
import crypto.users.Connect;
import crypto.Home;
import crypto.Header;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.plaf.basic.BasicButtonUI;
import java.util.Objects;

public class Main extends JFrame
{
    private static final Color PRIMARY_COLOR        = new Color(34, 49, 63);       
    private static final Color ACCENT_COLOR         = new Color(52, 152, 219);     
    private static final Color SECONDARY_ACCENT     = new Color(243, 156, 18);      
    private static final Color TEXT_LIGHT           = Color.WHITE;
    private static final Color TEXT_DARK            = new Color(44, 62, 80);       
    private static final Color BACKGROUND_OVERLAY   = new Color(255, 255, 255, 245); 
    
    private static final Font TITLE_FONT            = new Font("Montserrat", Font.BOLD, 38);
    private static final Font SUBTITLE_FONT         = new Font("Montserrat", Font.BOLD, 22);
    private static final Font BODY_FONT             = new Font("SansSerif", Font.PLAIN, 16);
    private static final Font MONO_FONT             = new Font("Consolas", Font.PLAIN, 15);
    
    protected JButton _try;
    protected JButton sign;
    protected JButton login;
    protected JButton exit;
    protected Home window;
    protected Connect connexion;
    protected JButton start;

    public Main()
    {
        this.login = createStyledButton("Log In", new Color(39, 174, 96), TEXT_LIGHT, BODY_FONT);
        this._try  = createStyledButton("Try as guest", ACCENT_COLOR, TEXT_LIGHT, BODY_FONT);
        this.sign  = createStyledButton("Sign In", PRIMARY_COLOR, TEXT_LIGHT, BODY_FONT);
        this.exit  = createStyledButton("Exit", new Color(231, 76, 60), TEXT_LIGHT, BODY_FONT);
        this.start = createStyledButton("Get Started", SECONDARY_ACCENT, TEXT_LIGHT, new Font("Montserrat", Font.BOLD, 18));

        this.setLocationRelativeTo(null);
        this.setSize(1000, 800);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Crypto Application");

        this.showHome();
    }
    
    public static JButton createStyledButton(String text, Color background, Color foreground, Font font)
    {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setFont(font);
        
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(background.darker(), 1, true),
            BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(false);

        button.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseEntered(MouseEvent event) 
            {
                button.setBackground(background.brighter());
                button.setForeground(TEXT_LIGHT);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(background.brighter().darker(), 1, true),
                    BorderFactory.createEmptyBorder(12, 25, 12, 25)
                ));
            }

            @Override
            public void mouseExited(MouseEvent event) 
            {
                button.setBackground(background);
                button.setForeground(foreground);
                 button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(background.darker(), 1, true),
                    BorderFactory.createEmptyBorder(12, 25, 12, 25)
                ));
            }
        });
        
        button.setUI(new BasicButtonUI() 
        {
            @Override
            public void paint(Graphics g, JComponent c) 
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(button.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
                
                super.paint(g2, c);
                g2.dispose();
            }
        });

        return button;
    }

    private JPanel welcome()
    {
        this._try.addActionListener(event -> Main.this.navigateToHome(true));

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

        this.exit.addActionListener(event -> Main.this.dispose());

        JPanel try_layout = new JPanel();
        try_layout.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        try_layout.setBackground(PRIMARY_COLOR);
        try_layout.setOpaque(true);
        try_layout.add(this._try);
        try_layout.add(this.sign);
        try_layout.add(this.login);
        try_layout.add(this.exit);

        JPanel datas_panel = new JPanel(new BorderLayout());
        Header header = new Header(this);
        header.removeBackBtn();
        header.removeLogOutBtn();
        header.removeAnalystBtn();
        
        header.setBackground(PRIMARY_COLOR);
        datas_panel.setBackground(PRIMARY_COLOR);
        datas_panel.add(header, BorderLayout.WEST);
        datas_panel.add(try_layout, BorderLayout.EAST);
        
        Border data_border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 3, true),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );
        
        JLabel we_come = new JLabel("Bienvenue dans le monde du cryptage !");
        we_come.setFont(TITLE_FONT);
        we_come.setAlignmentX(Component.CENTER_ALIGNMENT);
        we_come.setForeground(ACCENT_COLOR); 
       
        JTextArea intro = new JTextArea("Les messages secrets sont notre spécialité ! Nous sommes ravis de vous accueillir dans notre application de cryptographie, où les codes et les chiffres n'ont plus de secrets pour vous.");
        intro.setEditable(false);
        intro.setOpaque(false);
        intro.setFont(BODY_FONT);
        intro.setLineWrap(true);
        intro.setWrapStyleWord(true);
        intro.setAlignmentX(Component.LEFT_ALIGNMENT); 
        intro.setForeground(TEXT_DARK);

        JLabel dev = new JLabel("Découvrez nos systèmes de cryptage légendaires :");
        dev.setFont(SUBTITLE_FONT);
        dev.setAlignmentX(Component.CENTER_ALIGNMENT);
        dev.setForeground(SECONDARY_ACCENT);

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
        algoList.setAlignmentX(Component.LEFT_ALIGNMENT);
        algoList.setForeground(TEXT_DARK.darker());
       
        JTextArea invit = new JTextArea("Nous vous invitons à explorer nos fonctionnalités de chiffrement et de déchiffrement, et à découvrir comment nos systèmes peuvent protéger vos communications.");
        invit.setEditable(false);
        invit.setOpaque(false);
        invit.setFont(BODY_FONT);
        invit.setLineWrap(true);
        invit.setWrapStyleWord(true);
        invit.setAlignmentX(Component.LEFT_ALIGNMENT);
        invit.setForeground(TEXT_DARK);
       
        JLabel join = new JLabel("Rejoignez la communauté !");
        join.setFont(TITLE_FONT.deriveFont(Font.BOLD, 30f));
        join.setAlignmentX(Component.CENTER_ALIGNMENT);
        join.setForeground(PRIMARY_COLOR); 
        
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
        describe_join.setAlignmentX(Component.LEFT_ALIGNMENT);
        describe_join.setForeground(TEXT_DARK);
       
        JPanel home_panel = new JPanel();
        home_panel.setBorder(data_border);
        home_panel.setLayout(new BoxLayout(home_panel, BoxLayout.Y_AXIS));
        home_panel.setOpaque(true);
        home_panel.setBackground(BACKGROUND_OVERLAY);

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

        DrawBackground background = new DrawBackground("crypto/ressources/img2.png");
        background.setOpaque(true);
        background.setLayout(new BorderLayout());
        background.add(scrollPane,BorderLayout.CENTER);
        
        JPanel main_panel = new JPanel();
        main_panel.setLayout(new BorderLayout());
        main_panel.add(background,BorderLayout.CENTER);
        main_panel.add(datas_panel,BorderLayout.NORTH);
        main_panel.setOpaque(true);

        return main_panel;
    }

    public void navigateToHome(boolean isGuest)
    {
        Container container = this.getContentPane();

        Border lineBorder = BorderFactory.createLineBorder(ACCENT_COLOR, 3, true); 
        
        Border border = BorderFactory.createTitledBorder(
            lineBorder,
            isGuest ? "Guest Mode" : "User Mode", 
            TitledBorder.CENTER, TitledBorder.TOP, 
            SUBTITLE_FONT.deriveFont(Font.BOLD, 24f), 
            PRIMARY_COLOR 
        );
        
        DrawBackground background = new DrawBackground("crypto/ressources/img2.png");
        background.setOpaque(true);
        
        container.removeAll();
        container.setLayout(new BorderLayout());
        
        Border marginBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);

        JPanel navpanel = new Header(Main.this);
        navpanel.setBackground(PRIMARY_COLOR.darker().darker()); 

        Home homeContent = new Home(Main.this);
        JScrollPane homeScroll = new JScrollPane(homeContent);
        homeScroll.setBorder(null);
        homeScroll.getViewport().setOpaque(false);
        homeScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        homeScroll.getVerticalScrollBar().setUnitIncrement(20);

        background.setLayout(new BorderLayout());
        background.add(navpanel,BorderLayout.NORTH);
        background.add(homeScroll, BorderLayout.CENTER);
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        
        contentWrapper.setBorder(BorderFactory.createCompoundBorder(marginBorder, border));
        contentWrapper.add(background, BorderLayout.CENTER);

        container.add(contentWrapper, BorderLayout.CENTER);
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
        try{UIManager.setLookAndFeel(new NimbusLookAndFeel());} 
        catch(UnsupportedLookAndFeelException e) {System.out.println("Nimbus Look and Feel not supported. Using default.");}
        
        Main window = new Main();
        window.setVisible(true);
    }
}
