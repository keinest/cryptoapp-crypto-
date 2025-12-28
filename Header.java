package crypto;

import crypto.Main;
import crypto.users.Connect;
import crypto.users.Registration;
import crypto.crypt_analyst_brute_force.CryptAnalyst;
import crypto.session.UserSession;
import crypto.utils.ThemeManager;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Header extends JPanel
{
    protected JButton sigin;
    protected JButton connect;
    protected JButton signOut;
    protected JButton back;
    protected JButton cryptanalys;
    private Main window;

    public Header(Main window)
    {
        this.window = window;
        
        this.setOpaque(false);
        this.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 15, 10, 15),
            BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 1)
        ));
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 5));

        this.sigin       = Main.createCyberButton("Signup", ThemeManager.ACCENT_PURPLE);
        this.connect     = Main.createCyberButton("Login", ThemeManager.ACCENT_GREEN);
        this.signOut     = Main.createCyberButton("Log Out", new Color(231, 76, 60));
        this.back        = Main.createCyberButton("Back", ThemeManager.ACCENT_BLUE);
        this.cryptanalys = Main.createCyberButton("Crypt-analysis", ThemeManager.ACCENT_CYAN);

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Header.this.window.getContentPane().removeAll();
                Header.this.window.showHome();
                Header.this.window.revalidate();
                Header.this.window.repaint();
            }
        });

        this.sigin.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Header.this.window.getContentPane().removeAll();
                Header.this.window.setContentPane(new Registration(Header.this.window));
                Header.this.window.revalidate();
                Header.this.window.repaint();
            }
        });

        this.connect.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                JPanel connection = new Connect(Header.this.window);
                Header.this.window.getContentPane().removeAll();
                Header.this.window.setContentPane(connection);
                Header.this.window.revalidate();
                Header.this.window.repaint();
            }
        });

        this.cryptanalys.addActionListener(e -> 
        {
            UserSession session = UserSession.getInstance();
            if(!session.isLoggedIn()) 
            {
                int choice = JOptionPane.showConfirmDialog(
                    Header.this.window,
                    "<html><div style='color:#ffff55;'>" +
                    "Cryptanalysis is only available for logged in users.<br>" +
                    "Would you like to login now?</div></html>",
                    "Login Required",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                if(choice == JOptionPane.YES_OPTION) 
                {
                    Header.this.window.getContentPane().removeAll();
                    Header.this.window.setContentPane(new Connect(Header.this.window));
                    Header.this.window.revalidate();
                    Header.this.window.repaint();
                }
                return;
            }
            
            Header.this.window.getContentPane().removeAll();
            Header.this.window.setContentPane(new CryptAnalyst(Header.this.window));
            Header.this.window.revalidate();
            Header.this.window.repaint();
        });

        this.signOut.addActionListener(e -> {
            UserSession session = UserSession.getInstance();
            if(session.isLoggedIn()) {
                int confirm = JOptionPane.showConfirmDialog(
                    window,
                    "<html><div style='color:#ffff55;'>Are you sure you want to logout?</div></html>",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
                );
                
                if(confirm == JOptionPane.YES_OPTION) {
                    session.destroySession();
                    window.getContentPane().removeAll();
                    window.showHome();
                }
            } else {
                JOptionPane.showMessageDialog(
                    window,
                    "<html><div style='color:#ffff55;'>You are not logged in.</div></html>",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        this.add(this.sigin);
        this.add(this.connect);
        this.add(this.signOut);
        this.add(this.back);
        this.add(this.cryptanalys);
        
        updateButtonVisibility();
    }

    public void removeBackBtn()
    {
        this.remove(this.back);
        this.revalidate();
        this.repaint();
    }

    public void removeLogOutBtn()
    {
        this.remove(this.signOut);
        this.revalidate();
        this.repaint();
    }

    public void removeAnalystBtn()
    {
        this.remove(this.cryptanalys);
        this.revalidate();
        this.repaint();
    }
    
    public void removeSignupBtn()
    {
        this.remove(this.sigin);
        this.revalidate();
        this.repaint();
    }
    
    public void removeLoginBtn()
    {
        this.remove(this.connect);
        this.revalidate();
        this.repaint();
    }
    
    public JButton getCryptanalysButton()
    {
        return this.cryptanalys;
    }
    
    public JButton getSignOutButton()
    {
        return this.signOut;
    }
    
    public JButton getConnectButton()
    {
        return this.connect;
    }
    
    public JButton getSignupButton()
    {
        return this.sigin;
    }
    
    public void updateButtonVisibility()
    {
        UserSession session = UserSession.getInstance();
        
        if(session.isLoggedIn()) {
            if(session.isGuest()) {
                this.cryptanalys.setVisible(false);
                this.signOut.setVisible(true);
                this.sigin.setVisible(false);
                this.connect.setVisible(false);
            } else {
                this.cryptanalys.setVisible(true);
                this.signOut.setVisible(true);
                this.sigin.setVisible(false);
                this.connect.setVisible(false);
            }
        } else {
            this.cryptanalys.setVisible(false);
            this.signOut.setVisible(false);
            this.sigin.setVisible(true);
            this.connect.setVisible(true);
        }
        
        this.revalidate();
        this.repaint();
    }
    
    public void refreshButtons()
    {
        updateButtonVisibility();
    }
}
