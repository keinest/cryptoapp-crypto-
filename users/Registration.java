package crypto.users;
import crypto.utils.DrawBackground;
import crypto.utils.Util;
import crypto.users.db.dbmanagement;
import crypto.Main;
import crypto.npk_datas.About;
import crypto.npk_datas.Contact;
import crypto.npk_datas.Service;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Registration extends JPanel
{
    private static final Color PRIMARY_COLOR = new Color(30, 40, 60); 
    private static final Color ACCENT_COLOR  = new Color(0, 150, 255);     
    private static final Color TEXT_LIGHT    = Color.WHITE;
    private static final Color TEXT_DARK     = new Color(40, 40, 40); 
    private static final Font TITLE_FONT     = new Font("SansSerif", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT  = new Font("SansSerif", Font.BOLD, 20);
    private static final Font BODY_FONT      = new Font("SansSerif", Font.PLAIN, 15);
    
    protected JButton back;
    protected JTextField login;
    protected JTextField email;
    protected JPasswordField password;
    protected JPasswordField confirmPassword;
    protected UserStorage storage;
    protected JButton service;
    protected JButton contact;
    protected JButton about;
    protected JButton submit;
    public Main main_window;

    public Registration(Main main_window)
    {
        this.login           = new JTextField("ex:jean dupont");
        this.email           = new JTextField("ex:jeandupont1@gmail.com");
        this.confirmPassword = new JPasswordField();
        this.password        = new JPasswordField();
        this.submit          = createStyledButton("Submit", new Color(46, 139, 87));
        this.main_window     = main_window;
        this.storage         = new UserStorage();
        this.setLayout(new BorderLayout());

        JPanel formPanel = register();
        this.setFont(BODY_FONT);
        
        this.back    = createStyledButton("HOME", PRIMARY_COLOR);
        this.service = createStyledButton("SERVICES", PRIMARY_COLOR);
        this.contact = createStyledButton("CONTACT", PRIMARY_COLOR);
        this.about   = createStyledButton("ABOUT", PRIMARY_COLOR);
        
        this.service.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Registration.this.main_window.getContentPane().removeAll();
                Registration.this.main_window.setContentPane(new Service(Registration.this.main_window));
                Registration.this.main_window.revalidate();
                Registration.this.main_window.repaint();
            }
        });

        this.contact.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Registration.this.main_window.getContentPane().removeAll();
                Registration.this.main_window.setContentPane(new Contact(Registration.this.main_window));
                Registration.this.main_window.revalidate();
                Registration.this.main_window.repaint();
            }
        });

        this.about.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Registration.this.main_window.getContentPane().removeAll();
                Registration.this.main_window.setContentPane(new About(Registration.this.main_window));
                Registration.this.main_window.revalidate();
                Registration.this.main_window.repaint();
            }
        });

        DrawBackground backgroundPanel = new DrawBackground("crypto/ressources/Gemini_Generated_Image_jyfixfjyfixfjyfi.png");
        backgroundPanel.setLayout(new GridBagLayout());
        
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(false);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Registration.this.main_window.getContentPane().removeAll();
                Registration.this.main_window.showHome();
                Registration.this.main_window.revalidate();
                Registration.this.main_window.repaint();
            }
        });

        JPanel backPanel = new JPanel();
        backPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        backPanel.setOpaque(false);
        backPanel.add(this.back);
        backPanel.add(this.service);
        backPanel.add(this.contact);
        backPanel.add(this.about);

        contentPane.add(backPanel, BorderLayout.NORTH);
        backgroundPanel.add(formPanel);
        contentPane.add(backgroundPanel, BorderLayout.CENTER);

        this.add(contentPane, BorderLayout.CENTER);
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
    
    public JPanel register()
    {
        JPanel registrationPanel = new JPanel();
        registrationPanel.setLayout(new GridLayout(4, 2, 10, 10));
        registrationPanel.setOpaque(false);
        registrationPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JCheckBox showPassword = new JCheckBox("Show password");
        showPassword.setOpaque(false);

        registrationPanel.add(new JLabel("Login"));
        this.login.setFont(BODY_FONT);
        
        this.login.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent event)
            {
                Registration.this.login.setText("");
            }

            @Override
            public void focusLost(FocusEvent event)
            {
                if(Registration.this.login.getText().isEmpty())
                   Registration.this.login.setText("ex:jean dupont");
            }
        });
        registrationPanel.add(this.login);

        registrationPanel.add(new JLabel("Email"));
        this.email.setFont(BODY_FONT);
        
        this.email.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent event)
            {
                Registration.this.email.setText("");
            }
            @Override
            public void focusLost(FocusEvent event)
            {
                if(Registration.this.email.getText().isEmpty())
                   Registration.this.email.setText("ex:jeandupont1@gmail.com");
            }
        });
        registrationPanel.add(this.email);

        registrationPanel.add(new JLabel("Password"));
        registrationPanel.add(this.password);

        registrationPanel.add(new JLabel("Confirm password"));
        registrationPanel.add(this.confirmPassword);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(255, 255, 255, 230));

        JLabel title = new JLabel("Registration Form"); 
        title.setFont(SUBTITLE_FONT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(ACCENT_COLOR.darker()); 
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);  
          
        Border border = BorderFactory.createLineBorder(PRIMARY_COLOR, 3);

        this.submit.addActionListener(new ActionListener()
        {
           @Override
            public void actionPerformed(ActionEvent event)
            {
                String log         = Registration.this.login.getText().trim();
                String mail        = Registration.this.email.getText().trim();
                String pass        = new String(Registration.this.password.getPassword());
                String confirmPass = new String(Registration.this.confirmPassword.getPassword());

                if(log.isEmpty() || mail.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || log.equals("ex:jean dupont") || mail.equals("ex:jeandupont1@gmail.com"))
                {
                    JOptionPane.showMessageDialog(Registration.this,"Please complete the form before submit.","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if(!pass.equals(confirmPass))
                {
                    JOptionPane.showMessageDialog(Registration.this,"Incorrect Password ! Check and retry.","Error",JOptionPane.ERROR_MESSAGE);
                    Registration.this.password.setText("");
                    Registration.this.confirmPassword.setText("");
                    return;
                }

                if(Util.mailVeri(mail) == false)
                {
                    JOptionPane.showMessageDialog(Registration.this,"Email incorrect ! Vérifiez et réessayez.","Erreur",JOptionPane.ERROR_MESSAGE);
                    Registration.this.email.setText("");
                    return;
                }

                User newUser = new User(log,mail,pass);
                dbmanagement.connect();
                dbmanagement.createTable();
                
                if(dbmanagement.verifyUser(log,pass) || dbmanagement.userExists(log,mail))
                {
                    JOptionPane.showMessageDialog(Registration.this,"This user is already exist !","INFORMATION",JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if(dbmanagement.registerUser(newUser) == false)
                {
                    JOptionPane.showMessageDialog(Registration.this,"Error ! Please restart.","Data base error",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                storage.addUser(newUser); 

                JOptionPane.showMessageDialog(Registration.this,"Inscription réussie !","Succès",JOptionPane.INFORMATION_MESSAGE);
                
                Registration.this.login.setText("");
                Registration.this.email.setText("");
                Registration.this.password.setText("");
                Registration.this.confirmPassword.setText("");
            }
        }); 

        showPassword.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                if(showPassword.isSelected())
                {
                    password.setEchoChar((char)0);
                    confirmPassword.setEchoChar((char)0);
                    return;
                }

                password.setEchoChar('*');
                confirmPassword.setEchoChar('*');
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(this.submit,BorderLayout.EAST);
        buttonPanel.add(showPassword,BorderLayout.WEST);
        
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30),
            border
        ));
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(registrationPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(buttonPanel);
        mainPanel.setOpaque(true);

        return mainPanel;
    }

    public void restoreWindow(JPanel panelToRestore)
    {
        this.main_window.getContentPane().removeAll();
        this.main_window.setContentPane(panelToRestore);
        panelToRestore.revalidate();
        panelToRestore.repaint();
        
        this.main_window.revalidate();
        this.main_window.repaint();
        this.main_window.setVisible(true);
    }
}