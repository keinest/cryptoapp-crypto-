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

public class Main extends JFrame
{
    private static final Color PRIMARY_COLOR      = new Color(30, 40, 60);      
    private static final Color ACCENT_COLOR       = new Color(0, 150, 255);      
    private static final Color TEXT_LIGHT         = Color.WHITE;
    private static final Color BACKGROUND_OVERLAY = new Color(255, 255, 255, 200);
    private static final Font TITLE_FONT          = new Font("SansSerif", Font.BOLD, 30);
    private static final Font SUBTITLE_FONT       = new Font("SansSerif", Font.BOLD, 20);
    private static final Font BODY_FONT           = new Font("SansSerif", Font.PLAIN, 15);

    protected JButton _try;
    protected JButton sign;
    protected JButton login;
    protected JButton exit;
    protected Home window;
    protected Connect connexion;
    protected JButton start;

    public Main()
    {
        this.login = createStyledButton("Log In", new Color(46, 139, 87), TEXT_LIGHT, BODY_FONT);
        this._try  = createStyledButton("Try as guest", ACCENT_COLOR, TEXT_LIGHT, BODY_FONT);
        this.sign  = createStyledButton("Sign In", PRIMARY_COLOR, TEXT_LIGHT, BODY_FONT);
        this.exit  = createStyledButton("Exit", Color.RED, TEXT_LIGHT, BODY_FONT);
        this.start = createStyledButton("Get Started", Color.ORANGE, TEXT_LIGHT, new Font("SansSerif", Font.BOLD, 16));

        this.setLocationRelativeTo(null);
        this.setSize(950, 750); 
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
        try_layout.setBackground(new Color(0, 0, 0, 100));
        try_layout.add(this.exit);
        try_layout.add(this._try);
        try_layout.add(this.sign);
        try_layout.add(this.login);

        JPanel datas_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        Header header = new Header(this);
        header.removeBackBtn();
        header.removeLogOutBtn();
        datas_panel.add(header);
        datas_panel.add(try_layout);
        
        Border data_border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2), 
            "Description", TitledBorder.LEFT, TitledBorder.TOP, SUBTITLE_FONT, PRIMARY_COLOR
        );
        
        JLabel we_come = new JLabel("Bienvenue dans le monde du cryptage !");
        we_come.setFont(TITLE_FONT);
        we_come.setAlignmentX(Component.CENTER_ALIGNMENT);
        we_come.setForeground(ACCENT_COLOR); 

        JTextArea intro = new JTextArea("Les messages secrets sont notre specialite ! Nous sommes ravis de vous accueillir dans notre application de \n" + 
                                        "cryptographie, ou les codes et les chiffres n'ont plus de secrets pour vous.");
        intro.setEditable(false);
        intro.setOpaque(false);
        intro.setFont(BODY_FONT);
        intro.setLineWrap(true);
        intro.setWrapStyleWord(true);
        intro.setAlignmentX(Component.LEFT_ALIGNMENT); 

        JLabel dev = new JLabel("Decouvrez nos systemes de cryptage legendaires :");
        dev.setFont(SUBTITLE_FONT);
        dev.setAlignmentX(Component.CENTER_ALIGNMENT);
        dev.setForeground(PRIMARY_COLOR);

        JTextArea algoList = new JTextArea(
            "  * RSA: le roi des algorithmes asymetriques, pour des echanges securises.\n" +
            "  * Cesar: le decalage classique qui a trompe plus d'un espion.\n" +
            "  * Vigenere: le code polyalphabetique qui a resiste aux siecles.\n" +
            "  * Vernam: le masque jetable qui garantie la securite absolue (ou presque !).\n" +
            "  * Hill: le systeme matriciel qui chiffre vos messages en toute securite.\n" +
            "  * Affine: la combinaison parfaite de mathematiques et de cryptographie.\n" +
            "  * PlayFair: le code qui remplace les lettres par des paires de lettres.\n" +
            "  * Feistel: l'algorithme qui chiffre vos donnees en plusieurs tours.\n" +
            "  * Transposition rectangulaire: le systeme qui melange les lettres pour mieux les cacher."
        );
        
        algoList.setEditable(false);
        algoList.setOpaque(false);
        algoList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        algoList.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea invit = new JTextArea("Nous vous invitons a explorer nos fonctionnalites de chiffrement et de dechiffrement,\n" + 
                                        "et a decouvrir comment nos systemes peuvent proteger vos communications.");
        invit.setEditable(false);
        invit.setOpaque(false);
        invit.setFont(BODY_FONT);
        invit.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel join = new JLabel("Rejoignez la communaute!");
        join.setFont(TITLE_FONT);
        join.setAlignmentX(Component.CENTER_ALIGNMENT);
        join.setForeground(ACCENT_COLOR);

        JTextArea describe_join = new JTextArea(
            "Creez un compte pour acceder a toutes les fonctionnalites et profitez de nos services de cryptographie de pointe.\n" +
            "Nous sommes convaincus que vous apprecierez la securite et la facilite d'utilisation de notre application.\n" +
            "Alors qu'attendez-vous ? Inscrivez-vous des aujourd'hui et commencez a proteger vos secrets !"
        );

        describe_join.setFont(BODY_FONT);
        describe_join.setEditable(false);
        describe_join.setOpaque(false);
        describe_join.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel home_panel = new JPanel();
        home_panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30),
            data_border
        ));

        home_panel.setLayout(new BoxLayout(home_panel, BoxLayout.Y_AXIS));
        home_panel.setOpaque(true);
        home_panel.setBackground(BACKGROUND_OVERLAY); 

        JPanel start_con = new JPanel();
        start_con.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
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
        home_panel.add(Box.createVerticalStrut(20));
        home_panel.add(intro);
        home_panel.add(Box.createVerticalStrut(30));
        home_panel.add(dev);
        home_panel.add(Box.createVerticalStrut(15));
        home_panel.add(algoList);
        home_panel.add(Box.createVerticalStrut(30));
        home_panel.add(invit);
        home_panel.add(Box.createVerticalStrut(30));
        home_panel.add(join);
        home_panel.add(Box.createVerticalStrut(15));
        home_panel.add(describe_join); 
        home_panel.add(Box.createVerticalStrut(30));
        home_panel.add(start_con);

        JScrollPane scrollPane = new JScrollPane(home_panel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.addMouseWheelListener(new MouseWheelListener()
        {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event)
            {
                JScrollBar bar = scrollPane.getVerticalScrollBar();
                int value      = event.getWheelRotation() * bar.getUnitIncrement();
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
        main_panel.setOpaque(false);

        return main_panel;
    }

    public void navigateToHome(boolean isGuest)
    {
        JPanel panel = (JPanel)this.getContentPane();

        Border border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            isGuest ? "Guest mode" : "User mode", TitledBorder.LEFT, TitledBorder.TOP, SUBTITLE_FONT, PRIMARY_COLOR
        );
        DrawBackground background = new DrawBackground("crypto/ressources/img2.png");
        background.setOpaque(false);

        panel.setBorder(border);
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        JPanel navpanel = new Header(Main.this);

        Home homeContent = new Home(Main.this);
        JScrollPane homeScroll = new JScrollPane(homeContent);
        homeScroll.setBorder(null);
        homeScroll.getViewport().setOpaque(false);

        background.setLayout(new BorderLayout());
        background.add(navpanel,BorderLayout.NORTH);
        background.add(homeScroll, BorderLayout.CENTER);

        panel.add(background, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    public void showHome()
    {
        this.setTitle("Crypto Application");
        this.setContentPane(welcome());
        this.revalidate();
        this.repaint();
    }

    public static void main(String[] args) throws Exception
    {
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        Main window = new Main();
        window.setVisible(true);
    }
}
