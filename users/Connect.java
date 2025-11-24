package crypto.users;
import crypto.utils.DrawBackground;
import crypto.utils.Util;
import crypto.users.db.dbmanagement;
import crypto.Main;
import crypto.npk_datas.About;
import crypto.npk_datas.Service;
import crypto.npk_datas.Contact;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Connect extends JPanel
{
    private static final Color PRIMARY_COLOR = new Color(30, 40, 60);
    private static final Color ACCENT_COLOR  = new Color(0, 150, 255);
    private static final Color TEXT_LIGHT    = Color.WHITE;
    private static final Font TITLE_FONT     = new Font("SansSerif", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT  = new Font("SansSerif", Font.BOLD, 20);
    private static final Font BODY_FONT      = new Font("SansSerif", Font.PLAIN, 15);
   
    protected JButton home;
    protected JButton service;
    protected JButton contact;
    protected JButton about;
    protected JTextField login;
    protected JPasswordField password;
    protected UserStorage storage;
    protected JCheckBox remember;
    protected JButton forgot;
    protected JButton connect;
    public Main mainPage;

    public Connect(Main mainPage)
    {
        this.login    = new JTextField("login/email");
        this.password = new JPasswordField();
        this.remember = new JCheckBox("Remember me");
        this.forgot   = new JButton("Forgot password ?");
        this.connect  = createStyledButton("Login", new Color(46, 139, 87));

        this.mainPage = mainPage;
        this.storage  = new UserStorage();
        this.setLayout(new BorderLayout());

        JPanel connectPanel = connect();
        
        this.setFont(BODY_FONT);

        this.home    = createStyledButton("HOME", PRIMARY_COLOR);
        this.service = createStyledButton("Service", PRIMARY_COLOR);
        this.contact = createStyledButton("Contact", PRIMARY_COLOR);
        this.about   = createStyledButton("ABOUT", PRIMARY_COLOR);

        this.contact.addActionListener(event ->
        {
            Connect.this.mainPage.getContentPane().removeAll();
            Connect.this.mainPage.setContentPane(new Contact(Connect.this.mainPage));
            Connect.this.mainPage.revalidate();
            Connect.this.mainPage.repaint();
            
        });
        
        this.service.addActionListener(event -> 
        {
            Connect.this.mainPage.getContentPane().removeAll();
            Connect.this.mainPage.setContentPane(new Service(Connect.this.mainPage));
            Connect.this.mainPage.revalidate();
            Connect.this.mainPage.repaint();
            
        });

        this.connect.addActionListener(event ->
        {
            String email    = Connect.this.login.getText().trim();
            String password = new String(Connect.this.password.getPassword());
            
            if(email.isEmpty() || password.isEmpty() || email.equals("login/email"))
            {
                JOptionPane.showMessageDialog(Connect.this,"Please complete the form before continue!","Warning",JOptionPane.WARNING_MESSAGE);
                return;
            }

            if(!Util.mailVeri(email))
            {
                JOptionPane.showMessageDialog(Connect.this,"Please enter a valid email address!","Warning",JOptionPane.WARNING_MESSAGE);
                Connect.this.login.setText("");
                return;
            }

            dbmanagement.connect();
            if(dbmanagement.verifyUser(email, password)) 
            {
                JOptionPane.showMessageDialog(Connect.this,"Connexion successfull !","Success",JOptionPane.INFORMATION_MESSAGE);
                Connect.this.mainPage.navigateToHome(false);
            } 
            else 
                JOptionPane.showMessageDialog(Connect.this,"Incorrect information!","Connexion error",JOptionPane.ERROR_MESSAGE);
        
        });
        
        this.home.addActionListener(event ->
        {
            Connect.this.mainPage.showHome();
            Connect.this.mainPage.revalidate();
            Connect.this.mainPage.repaint();
            
        });

        this.about.addActionListener(event ->
        {
            Connect.this.mainPage.getContentPane().removeAll();
            Connect.this.mainPage.setContentPane(new About(Connect.this.mainPage));
            Connect.this.mainPage.revalidate();
            Connect.this.mainPage.repaint();
            
        });
        
        JPanel backPanel = new JPanel();
        backPanel.setOpaque(false);
        backPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        backPanel.add(this.home);
        backPanel.add(this.service);
        backPanel.add(this.contact);
        backPanel.add(this.about);
        
        JPanel contentHolder = new JPanel(new BorderLayout());
        contentHolder.setOpaque(false);
        contentHolder.add(backPanel, BorderLayout.NORTH); 
        contentHolder.add(connectPanel, BorderLayout.CENTER);

        DrawBackground backgroundPanel = new DrawBackground("crypto/ressources/login.jpg");
        backgroundPanel.setLayout(new GridBagLayout());
        
        backgroundPanel.add(contentHolder);
        
        this.add(backgroundPanel, BorderLayout.CENTER);
        this.setOpaque(true); 
    }

    private JButton createStyledButton(String text, Color background)
    {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(TEXT_LIGHT);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() 
        {
            public void mouseEntered(MouseEvent event) 
            {
                button.setBackground(background.darker());
            }
            public void mouseExited(MouseEvent event) 
            {
                button.setBackground(background);
            }
        });
        return button;
    }

    
    private JPanel connect()
    {
        JPanel data_panel = new JPanel();
        data_panel.setLayout(new GridLayout(2, 2, 10, 10));
        data_panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        data_panel.setOpaque(false);
        
        this.login.setFont(BODY_FONT);
        
        this.login.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent event)
            {
                Connect.this.login.setText("");
            }

            @Override
            public void focusLost(FocusEvent event)
            {
                if(Connect.this.login.getText().isEmpty())
                    Connect.this.login.setText("login/email");
            }
        });

        this.password.setFont(BODY_FONT);
        
        data_panel.add(new JLabel("Login/email"));
        data_panel.add(this.login);
        data_panel.add(new JLabel("Password"));
        data_panel.add(this.password);

        JLabel title = new JLabel("Connexion form");
        title.setFont(SUBTITLE_FONT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(ACCENT_COLOR.darker());
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER); 

        JPanel remember_forgot = new JPanel(new BorderLayout());
        remember_forgot.setOpaque(false);
        remember_forgot.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        
        this.remember.setContentAreaFilled(false);
        this.remember.setBorderPainted(false);
        this.remember.setFont(BODY_FONT);

        this.forgot.setForeground(ACCENT_COLOR);
        this.forgot.setContentAreaFilled(false);
        this.forgot.setBorderPainted(false);
        this.forgot.setFont(new Font("SansSerif", Font.ITALIC, 14));
        this.forgot.setCursor(new Cursor(Cursor.HAND_CURSOR));

        remember_forgot.add(this.remember,BorderLayout.WEST);
        remember_forgot.add(this.forgot,BorderLayout.EAST);

        Border main_border = BorderFactory.createLineBorder(PRIMARY_COLOR, 2);

        JPanel main_panel = new JPanel();
        main_panel.setLayout(new BoxLayout(main_panel,BoxLayout.Y_AXIS));
        main_panel.setOpaque(true);
        main_panel.setBackground(new Color(255, 255, 255, 230));
        main_panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30),
            BorderFactory.createLineBorder(PRIMARY_COLOR, 3)
        ));

        main_panel.add(title);
        main_panel.add(Box.createVerticalStrut(15));
        main_panel.add(data_panel);
        main_panel.add(remember_forgot);
        main_panel.add(this.connect);

        return main_panel;
    }
}
