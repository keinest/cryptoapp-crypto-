package crypto.crypt_analyst_brute_force;

import crypto.Main;
import crypto.Home;
import crypto.users.User;
import crypto.utils.Util;
import crypto.session.UserSession;
import crypto.utils.DrawBackground;
import crypto.utils.ThemeManager;
import crypto.session.SessionInterceptor;
import crypto.crypt_analys_brute_force.rsa_brute_force.RSABruterForce;
import crypto.crypt_analys_brute_force.cesar_brute_force.CesarBruterForce;
import crypto.crypt_analys_brute_force.affine_brute_force.AffineBruterForce;
import crypto.crypt_analys_brute_force.vernam_brute_force.VernamBruterForce;
import crypto.crypt_analys_brute_force.vigenere_brute_force.VigenereBruterForce;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;
import javax.swing.border.*;

public class CryptAnalyst extends JPanel
{
    protected JButton rsa;
    protected JButton affine;
    protected JButton cesar;
    protected JButton vigenere;
    protected JButton vernam;
    protected JButton hill;
    protected JButton feistel;
    protected JButton recTransposition;
    protected JButton playfair;
    private JPanel btn_panel;

    JButton back;
    private Main window;   

    public CryptAnalyst(Main window)
    {
        if(!SessionInterceptor.checkSessionAndRedirect(window)) 
            return;

        UserSession session = UserSession.getInstance();
        session.updateActivity();
        
        this.window = window;
        this.setLayout(new BorderLayout());
        JPanel homePanel = createHomePanel();
        this.btn_panel   = new JPanel(new FlowLayout(FlowLayout.LEFT)); 

        this.back = Main.createCyberButton("Back to Home", ThemeManager.ACCENT_BLUE);
        this.back.setToolTipText("Retourner à la page d'accueil");
        
        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                CryptAnalyst.this.window.showHome();
            }
        });
        
        this.btn_panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 15, 10, 15),
            BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 1)
        ));
        this.btn_panel.setOpaque(false);
        this.btn_panel.setPreferredSize(new Dimension(200,60));
        this.btn_panel.add(this.back);

        this.add(btn_panel, BorderLayout.NORTH);
        this.add(homePanel, BorderLayout.CENTER);
        this.setOpaque(false);
    }

    private JPanel createHomePanel()
    {
        this.rsa              = createCyberAlgorithmButton("RSA Brute Force");
        this.affine           = createCyberAlgorithmButton("Affine Brute Force");
        this.cesar            = createCyberAlgorithmButton("Cesar Brute Force");
        this.vigenere         = createCyberAlgorithmButton("Vigenere Brute Force");
        this.vernam           = createCyberAlgorithmButton("Vernam Brute Force");
        this.hill             = createCyberAlgorithmButton("Hill Brute Force");
        this.feistel          = createCyberAlgorithmButton("Feistel Brute Force");
        this.recTransposition = createCyberAlgorithmButton("Recursive Transposition Brute Force");
        this.playfair         = createCyberAlgorithmButton("Playfair Brute Force");

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 30, 30));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        buttonPanel.add(this.rsa);
        buttonPanel.add(this.affine);
        buttonPanel.add(this.cesar);
        buttonPanel.add(this.vigenere);
        buttonPanel.add(this.vernam);
        buttonPanel.add(this.hill);
        buttonPanel.add(this.feistel);
        buttonPanel.add(this.recTransposition);
        buttonPanel.add(this.playfair);

        this.rsa.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                CryptAnalyst.this.window.setTitle("RSA Brute Force Cryptanalysis");
                CryptAnalyst.this.window.setContentPane(new RSABruterForce(CryptAnalyst.this));
                CryptAnalyst.this.window.revalidate();
                CryptAnalyst.this.window.repaint();
            }
        });

        this.cesar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                CryptAnalyst.this.window.setTitle("Cesar Brute Force Cryptanalysis");
                CryptAnalyst.this.window.setContentPane(new CesarBruterForce(CryptAnalyst.this));
                CryptAnalyst.this.window.revalidate();
                CryptAnalyst.this.window.repaint();
            }
        });

        this.affine.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                CryptAnalyst.this.window.setTitle("Affine Brute Force Cryptanalysis");
                CryptAnalyst.this.window.setContentPane(new AffineBruterForce(CryptAnalyst.this));
                CryptAnalyst.this.window.revalidate();
                CryptAnalyst.this.window.repaint();
            }
        });

        this.vigenere.addActionListener(e ->
        {
            CryptAnalyst.this.window.setTitle("Vigenere Brute Force Cryptanalysis");
            CryptAnalyst.this.window.setContentPane(new VigenereBruterForce(CryptAnalyst.this));
            CryptAnalyst.this.window.revalidate();
            CryptAnalyst.this.window.repaint();
        });

        this.vernam.addActionListener(e -> 
        {
            CryptAnalyst.this.window.setTitle("Vernam Brute Force Cryptanalysis");
            CryptAnalyst.this.window.setContentPane(new VernamBruterForce(CryptAnalyst.this));
            CryptAnalyst.this.window.revalidate();
            CryptAnalyst.this.window.repaint();
        });

        this.hill.addActionListener(e -> showNotImplemented());
        this.feistel.addActionListener(e -> showNotImplemented());
        this.recTransposition.addActionListener(e -> showNotImplemented());
        this.playfair.addActionListener(e -> showNotImplemented());

        DrawBackground background = new DrawBackground("crypto/ressources/Screenshot 2025-12-04 at 11-12-56 (Image JPEG 564 × 564 pixels).png");
        background.setLayout(new GridBagLayout()); 

        background.add(buttonPanel);

        return background;
    }
    
    private void showNotImplemented() 
    {
        JOptionPane.showMessageDialog(this,
            "<html><div style='color:#ffff55;'>" +
            "<b>Fonctionnalité en développement</b><br><br>" +
            "Cette fonctionnalité n'est pas encore implémentée.<br>" +
            "Elle sera disponible dans une future mise à jour.</div></html>",
            "En développement",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private JButton createCyberAlgorithmButton(String text) 
    {
        JButton button = new JButton(text) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = ThemeManager.BUTTON_BG;
                Color borderColor = ThemeManager.ACCENT_CYAN;
                
                if(getModel().isPressed()) 
                {
                    bgColor = ThemeManager.BUTTON_ACTIVE;
                    borderColor = ThemeManager.ACCENT_BLUE;
                } 
                else if(getModel().isRollover()) 
                {
                    bgColor = ThemeManager.BUTTON_HOVER;
                    borderColor = Color.WHITE;
                }
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(getModel().isRollover() ? 2.0f : 1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setForeground(ThemeManager.ACCENT_CYAN);
        button.setFont(ThemeManager.FONT_MONO_BOLD);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
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
                button.setForeground(ThemeManager.ACCENT_CYAN);
                button.repaint();
            }
        });
        
        return button;
    }

    public void restoreHome()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(this.btn_panel, BorderLayout.NORTH);
        panel.add(createHomePanel(), BorderLayout.CENTER);
        this.window.setTitle("Cryptanalysis Brute Force Methods");
        this.window.setContentPane(panel);
        this.window.revalidate();
        this.window.repaint();
    }
}
