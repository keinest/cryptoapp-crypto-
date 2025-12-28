package crypto.users;

import crypto.Main;
import crypto.utils.Util;
import crypto.npk_datas.About;
import crypto.users.db.HASHCode;
import crypto.npk_datas.Service;
import crypto.npk_datas.Contact;
import crypto.utils.ThemeManager;
import crypto.session.UserSession;
import crypto.utils.DrawBackground;
import crypto.users.db.dbmanagement;
import crypto.session.SessionInterceptor;
import crypto.crypt_analyst_brute_force.CryptAnalyst;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Connect extends JPanel
{
    private static final Font TITLE_FONT     = ThemeManager.FONT_TITLE;
    private static final Font SUBTITLE_FONT  = ThemeManager.FONT_SUBTITLE;
    private static final Font BODY_FONT      = ThemeManager.FONT_BODY;
   
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
        this.login    = createCyberTextField();
        this.password = createCyberPasswordField();
        this.remember = createCyberCheckBox("Remember me");
        this.forgot   = createCyberLinkButton("Forgot password?");
        this.connect  = Main.createCyberButton("Login", ThemeManager.ACCENT_GREEN);

        this.mainPage = mainPage;
        this.storage  = new UserStorage();
        this.setLayout(new BorderLayout());

        JPanel connectPanel = connect();
        
        this.setFont(BODY_FONT);

        this.home    = Main.createCyberButton("HOME", ThemeManager.ACCENT_BLUE);
        this.service = Main.createCyberButton("Service", ThemeManager.ACCENT_BLUE);
        this.contact = Main.createCyberButton("Contact", ThemeManager.ACCENT_BLUE);
        this.about   = Main.createCyberButton("ABOUT", ThemeManager.ACCENT_BLUE);

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
            String pass     = new String(Connect.this.password.getPassword());
            
            if(email.isEmpty() || pass.isEmpty())
            {
                JOptionPane.showMessageDialog(Connect.this,
                    "<html><div style='color:#ff5555;'>Please complete the form before continuing!</div></html>",
                    "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            SwingWorker <String, Void> worker = new SwingWorker<>() 
            {
                @Override
                protected String doInBackground() throws Exception 
                {
                    dbmanagement.connect();
                    if(dbmanagement.verifyUser(HASHCode.hash(email), HASHCode.hash(pass))) 
                    {
                        User user = dbmanagement.getUserByEmail(HASHCode.hash(email));
                        if(user != null) 
                        {
                            UserSession session = UserSession.getInstance();
                            session.createUserSession(user, false);
                            return "success";
                        } else {
                            return "user_not_found";
                        }
                    } 
                    else 
                        return "auth_failed";
                }

                @Override
                protected void done() 
                {
                    try 
                    {
                        String result = get();
                        
                        if("success".equals(result)) 
                        {
                            JOptionPane.showMessageDialog(Connect.this,
                                "<html><div style='color:#00ff00;'>Login successful!</div></html>",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                            Connect.this.mainPage.getContentPane().removeAll();
                            Connect.this.mainPage.navigateToHome(false);
                            Connect.this.mainPage.revalidate();
                            Connect.this.mainPage.repaint();
                        } 
                        else if("user_not_found".equals(result)) 
                            JOptionPane.showMessageDialog(Connect.this,
                                "<html><div style='color:#ff5555;'>User not found in database</div></html>",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        else 
                            JOptionPane.showMessageDialog(Connect.this,
                                "<html><div style='color:#ff5555;'>Incorrect email or password!</div></html>",
                                "Login error", JOptionPane.ERROR_MESSAGE);
                        
                    } 
                    catch(Exception e) 
                    {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(Connect.this,
                            "<html><div style='color:#ff5555;'>Error during authentication: " + e.getMessage() + "</div></html>",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        });
        
        this.home.addActionListener(event -> Connect.this.mainPage.navigateToHome(false));

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

    private JTextField createCyberTextField() 
    {
        JTextField field = new JTextField();
        field.setFont(ThemeManager.FONT_MONO);
        field.setForeground(ThemeManager.TEXT_PRIMARY);
        field.setBackground(ThemeManager.DARK_BG_TERTIARY);
        field.setCaretColor(ThemeManager.ACCENT_CYAN);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }
    
    private JPasswordField createCyberPasswordField() 
    {
        JPasswordField field = new JPasswordField();
        field.setFont(ThemeManager.FONT_MONO);
        field.setForeground(ThemeManager.TEXT_PRIMARY);
        field.setBackground(ThemeManager.DARK_BG_TERTIARY);
        field.setCaretColor(ThemeManager.ACCENT_CYAN);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }
    
    private JCheckBox createCyberCheckBox(String text) 
    {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setFont(ThemeManager.FONT_BODY);
        checkBox.setForeground(ThemeManager.TEXT_PRIMARY);
        checkBox.setBackground(new Color(0, 0, 0, 0));
        checkBox.setFocusPainted(false);
        return checkBox;
    }
    
    private JButton createCyberLinkButton(String text) 
    {
        JButton button = new JButton(text);
        button.setFont(ThemeManager.FONT_BODY);
        button.setForeground(ThemeManager.ACCENT_CYAN);
        button.setBackground(new Color(0, 0, 0, 0));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseEntered(MouseEvent e) 
            {
                button.setForeground(ThemeManager.ACCENT_BLUE);
                button.setText("<html><u>" + text + "</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) 
            {
                button.setForeground(ThemeManager.ACCENT_CYAN);
                button.setText(text);
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
                if(Connect.this.login.getText().equals("johdoh@gmail.com")) 
                {
                    Connect.this.login.setText("");
                    Connect.this.login.setForeground(ThemeManager.TEXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(FocusEvent event)
            {
                if(Connect.this.login.getText().isEmpty()) 
                {
                    Connect.this.login.setText("johdoh@gmail.com");
                    Connect.this.login.setForeground(ThemeManager.TEXT_MUTED);
                }
            }
        });

        this.password.setFont(BODY_FONT);
        
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(ThemeManager.FONT_MONO_BOLD);
        emailLabel.setForeground(ThemeManager.TEXT_PRIMARY);
        data_panel.add(emailLabel);
        data_panel.add(this.login);
        
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(ThemeManager.FONT_MONO_BOLD);
        passLabel.setForeground(ThemeManager.TEXT_PRIMARY);
        data_panel.add(passLabel);
        data_panel.add(this.password);

        JLabel title = new JLabel("Login Form");
        title.setFont(SUBTITLE_FONT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(ThemeManager.ACCENT_CYAN);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER); 

        JPanel remember_forgot = new JPanel(new BorderLayout());
        remember_forgot.setOpaque(false);
        remember_forgot.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        remember_forgot.add(this.remember, BorderLayout.WEST);
        remember_forgot.add(this.forgot, BorderLayout.EAST);

        JPanel main_panel = new JPanel();
        main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
        main_panel.setOpaque(true);
        main_panel.setBackground(new Color(20, 30, 50, 230));
        main_panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30),
            BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 3)
        ));

        main_panel.add(title);
        main_panel.add(Box.createVerticalStrut(15));
        main_panel.add(data_panel);
        main_panel.add(remember_forgot);
        main_panel.add(this.connect);

        return main_panel;
    }
}
