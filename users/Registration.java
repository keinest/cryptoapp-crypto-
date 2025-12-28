package crypto.users;
import crypto.utils.DrawBackground;
import crypto.utils.ThemeManager;
import crypto.utils.Util;
import crypto.users.db.dbmanagement;
import crypto.users.db.HASHCode;
import crypto.Main;
import crypto.npk_datas.About;
import crypto.npk_datas.Contact;
import crypto.npk_datas.Service;

import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Registration extends JPanel
{
    private static final Font TITLE_FONT     = ThemeManager.FONT_TITLE;
    private static final Font SUBTITLE_FONT  = ThemeManager.FONT_SUBTITLE;
    private static final Font BODY_FONT      = ThemeManager.FONT_BODY;
    
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
        this.login           = createCyberTextField();
        this.email           = createCyberTextField();
        this.confirmPassword = createCyberPasswordField();
        this.password        = createCyberPasswordField();
        this.submit          = Main.createCyberButton("Submit", ThemeManager.ACCENT_GREEN);
        this.main_window     = main_window;
        this.storage         = new UserStorage();
        this.setLayout(new BorderLayout());

        JPanel formPanel = register();
        this.setFont(BODY_FONT);
        
        this.back    = Main.createCyberButton("HOME", ThemeManager.ACCENT_BLUE);
        this.service = Main.createCyberButton("SERVICES", ThemeManager.ACCENT_BLUE);
        this.contact = Main.createCyberButton("CONTACT", ThemeManager.ACCENT_BLUE);
        this.about   = Main.createCyberButton("ABOUT", ThemeManager.ACCENT_BLUE);
        
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
    
    public JPanel register()
    {
        JPanel registrationPanel = new JPanel();
        registrationPanel.setLayout(new GridLayout(4, 2, 10, 10));
        registrationPanel.setOpaque(false);
        registrationPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JCheckBox showPassword = createCyberCheckBox("Show password");

        JLabel loginLabel = new JLabel("Login:");
        loginLabel.setFont(ThemeManager.FONT_MONO_BOLD);
        loginLabel.setForeground(ThemeManager.TEXT_PRIMARY);
        registrationPanel.add(loginLabel);
        
        this.login.setFont(BODY_FONT);
        this.login.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent event)
            {
                if(Registration.this.login.getText().equals("ex:jean dupont")) {
                    Registration.this.login.setText("");
                    Registration.this.login.setForeground(ThemeManager.TEXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(FocusEvent event)
            {
                if(Registration.this.login.getText().isEmpty()) {
                    Registration.this.login.setText("ex:jean dupont");
                    Registration.this.login.setForeground(ThemeManager.TEXT_MUTED);
                }
            }
        });
        registrationPanel.add(this.login);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(ThemeManager.FONT_MONO_BOLD);
        emailLabel.setForeground(ThemeManager.TEXT_PRIMARY);
        registrationPanel.add(emailLabel);
        
        this.email.setFont(BODY_FONT);
        this.email.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent event)
            {
                if(Registration.this.email.getText().equals("ex:jeandupont1@gmail.com")) {
                    Registration.this.email.setText("");
                    Registration.this.email.setForeground(ThemeManager.TEXT_PRIMARY);
                }
            }
            @Override
            public void focusLost(FocusEvent event)
            {
                if(Registration.this.email.getText().isEmpty()) {
                    Registration.this.email.setText("ex:jeandupont1@gmail.com");
                    Registration.this.email.setForeground(ThemeManager.TEXT_MUTED);
                }
            }
        });
        registrationPanel.add(this.email);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(ThemeManager.FONT_MONO_BOLD);
        passLabel.setForeground(ThemeManager.TEXT_PRIMARY);
        registrationPanel.add(passLabel);
        registrationPanel.add(this.password);

        JLabel confirmLabel = new JLabel("Confirm password:");
        confirmLabel.setFont(ThemeManager.FONT_MONO_BOLD);
        confirmLabel.setForeground(ThemeManager.TEXT_PRIMARY);
        registrationPanel.add(confirmLabel);
        registrationPanel.add(this.confirmPassword);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(20, 30, 50, 230));

        JLabel title = new JLabel("Registration Form"); 
        title.setFont(SUBTITLE_FONT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(ThemeManager.ACCENT_CYAN); 
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);  
          
        this.submit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                String log         = Registration.this.login.getText().trim();
                String mail        = Registration.this.email.getText().trim();
                String pass        = new String(Registration.this.password.getPassword());
                String confirmPass = new String(Registration.this.confirmPassword.getPassword());

                if(log.isEmpty() || mail.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || 
                   log.equals("ex:jean dupont") || mail.equals("ex:jeandupont1@gmail.com"))
                {
                    JOptionPane.showMessageDialog(Registration.this,
                        "<html><div style='color:#ff5555;'>Please complete the form before submitting.</div></html>",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(!pass.equals(confirmPass))
                {
                    JOptionPane.showMessageDialog(Registration.this,
                        "<html><div style='color:#ff5555;'>Passwords do not match! Please check and try again.</div></html>",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    Registration.this.password.setText("");
                    Registration.this.confirmPassword.setText("");
                    return;
                }

                if(!Util.mailVeri(mail))
                {
                    JOptionPane.showMessageDialog(Registration.this,
                        "<html><div style='color:#ff5555;'>Invalid email address! Please check and try again.</div></html>",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    Registration.this.email.setText("");
                    return;
                }

                final User newUser = new User(log, HASHCode.hash(mail), HASHCode.hash(pass));

                SwingWorker<String, Void> worker = new SwingWorker<>()
                {
                    @Override
                    protected String doInBackground()
                    {
                        try 
                        {
                            dbmanagement.connect();
                            dbmanagement.createTable();
                        } 
                        catch (Exception e){return "db_connect_error";}

                        if(dbmanagement.verifyUser(newUser.email, newUser.password) || dbmanagement.userExists(log,mail))
                        {
                            JOptionPane.showMessageDialog(Registration.this,
                                "<html><div style='color:#ffff55;'>This user already exists!</div></html>",
                                "Information", JOptionPane.INFORMATION_MESSAGE);
                            return null;
                        }

                        if(dbmanagement.registerUser(newUser) == false)
                        {
                            JOptionPane.showMessageDialog(Registration.this,
                                "<html><div style='color:#ff5555;'>Database error! Please restart.</div></html>",
                                "Database error", JOptionPane.ERROR_MESSAGE);
                            return null;
                        }

                        return "success";
                    }

                    @Override
                    protected void done()
                    {
                        try
                        {
                            String result = get();

                            if(result == null || result.equals("db_connect_error"))
                                return;

                            if(result.equals("success"))
                            {
                                storage.addUser(newUser);
                                JOptionPane.showMessageDialog(Registration.this,
                                    "<html><div style='color:#00ff00;'>Registration successful!</div></html>",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);

                                Registration.this.login.setText("");
                                Registration.this.email.setText("");
                                Registration.this.password.setText("");
                                Registration.this.confirmPassword.setText("");
                            }

                        } 
                        catch(InterruptedException e){Thread.currentThread().interrupt();} 
                        catch(ExecutionException e)
                        {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(Registration.this,
                                "<html><div style='color:#ff5555;'>An error occurred.</div></html>",
                                "Error" ,JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

                worker.execute();
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
        buttonPanel.add(this.submit, BorderLayout.EAST);
        buttonPanel.add(showPassword, BorderLayout.WEST);
        
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30),
            BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 3)
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
