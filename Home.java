package crypto;

import crypto.encryption_decryption.rsa.RSA;
import crypto.encryption_decryption.hill.Hill;
import crypto.encryption_decryption.cesar.Cesar;
import crypto.encryption_decryption.vernam.Vernam;
import crypto.encryption_decryption.affine.Affine;
import crypto.encryption_decryption.feistel.Feistel;
import crypto.encryption_decryption.vigenere.Vigenere;

import crypto.Main;
import crypto.Header;
import crypto.users.Connect;
import crypto.users.Registration;
import crypto.utils.DrawBackground;
import crypto.utils.ThemeManager;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Home extends JPanel
{
    private static final Color PRIMARY_COLOR = ThemeManager.DARK_BG_PRIMARY;
    private static final Color ACCENT_COLOR  = ThemeManager.ACCENT_CYAN;
    private static final Color TEXT_DARK     = ThemeManager.TEXT_SECONDARY;
    private static final Font BUTTON_FONT    = ThemeManager.FONT_MONO_BOLD;
    
    private static final int ANIMATION_DURATION = 150; 
    
    protected JButton RsaEncrypt;
    protected JButton affineEncrypt;
    protected JButton cesar;
    protected JButton vigenere;
    protected JButton vernam;
    protected JButton hill;
    protected JButton feistel;
    protected JButton recTransposition;
    protected JButton playfair;
    protected Affine  affine;
    private Main window;

    public Home(Main window)
    {
        this.window = window;
        this.setLayout(new BorderLayout()); 
        JPanel HomePanel = createHomePanel();
        this.add(HomePanel, BorderLayout.CENTER);
        this.setOpaque(false);
    }
    
    public static void styleAlgorithmButton(JButton button) 
    {
        button.setBackground(ThemeManager.BUTTON_BG); 
        button.setForeground(ThemeManager.ACCENT_CYAN);
        button.setFocusPainted(false);
        button.setFont(BUTTON_FONT);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 1), 
            BorderFactory.createEmptyBorder(25, 15, 25, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() 
        {
            public void mouseEntered(MouseEvent eventvent) 
            {
                animateColorChange(button, ThemeManager.BUTTON_BG, ThemeManager.BUTTON_HOVER, 
                                 ThemeManager.ACCENT_CYAN, Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 2),
                    BorderFactory.createEmptyBorder(25, 15, 25, 15)
                ));
            }

            public void mouseExited(MouseEvent eventvent) 
            {
                animateColorChange(button, ThemeManager.BUTTON_HOVER, ThemeManager.BUTTON_BG, 
                                 Color.WHITE, ThemeManager.ACCENT_CYAN);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 1),
                    BorderFactory.createEmptyBorder(25, 15, 25, 15)
                ));
            }
        });
    }

    private static void animateColorChange(JButton button, Color fromBg, Color toBg, Color fromFg, Color toFg) 
    {
        Timer timer = new Timer(10, null);
        long startTime = System.currentTimeMillis();

        timer.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                long elapsed = System.currentTimeMillis() - startTime;
                double fraction = (double) elapsed / ANIMATION_DURATION;
                
                if(fraction > 1.0) 
                {
                    fraction = 1.0;
                    timer.stop();
                }

                Color newBg = interpolateColor(fromBg, toBg, fraction);
                Color newFg = interpolateColor(fromFg, toFg, fraction);
                
                button.setBackground(newBg);
                button.setForeground(newFg);
                button.repaint();
            }
        });
        timer.start();
    }

    private static Color interpolateColor(Color from, Color to, double fraction) 
    {
        int r = (int) (from.getRed() + (to.getRed() - from.getRed()) * fraction);
        int g = (int) (from.getGreen() + (to.getGreen() - from.getGreen()) * fraction);
        int b = (int) (from.getBlue() + (to.getBlue() - from.getBlue()) * fraction);
        int a = (int) (from.getAlpha() + (to.getAlpha() - from.getAlpha()) * fraction);
        return new Color(r, g, b, a);
    }

    private JPanel createHomePanel()
    {
        DrawBackground background = new DrawBackground(" ");
        background.setLayout(new GridBagLayout()); 

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 30, 30));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        this.RsaEncrypt       = createCyberAlgorithmButton("RSA");
        this.affineEncrypt    = createCyberAlgorithmButton("Affine");
        this.cesar            = createCyberAlgorithmButton("Cesar");
        this.vigenere         = createCyberAlgorithmButton("Vigenere");
        this.vernam           = createCyberAlgorithmButton("Vernam");
        this.hill             = createCyberAlgorithmButton("Hill");
        this.feistel          = createCyberAlgorithmButton("Feistel");
        this.recTransposition = createCyberAlgorithmButton("Rectangular Transposition");
        this.playfair         = createCyberAlgorithmButton("Playfair");

        buttonPanel.add(this.RsaEncrypt);
        buttonPanel.add(this.affineEncrypt);
        buttonPanel.add(this.cesar);
        buttonPanel.add(this.vigenere);
        buttonPanel.add(this.vernam);
        buttonPanel.add(this.hill);
        buttonPanel.add(this.feistel);
        buttonPanel.add(this.recTransposition);
        buttonPanel.add(this.playfair);

        this.RsaEncrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Home.this.window.setTitle("RSA Encryption System");
                Home.this.window.setContentPane(new RSA(Home.this.window));
                Home.this.window.revalidate();
                Home.this.window.repaint();
            }
        });

        this.affineEncrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Home.this.window.setTitle("Affine Encryption System");
                Home.this.window.setContentPane(new Affine(Home.this.window));
                Home.this.window.revalidate();
                Home.this.window.repaint();
            }
        });

        this.cesar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Home.this.window.setTitle("Cesar Encryption System");
                Home.this.window.setContentPane(new Cesar(Home.this.window));
                Home.this.window.revalidate();
                Home.this.window.repaint();
            }
        });
        
        this.vigenere.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Home.this.window.setTitle("Vigenere Encryption System");
                Home.this.window.setContentPane(new Vigenere(Home.this.window));
                Home.this.window.revalidate();
                Home.this.window.repaint();
            }
        });

        this.vernam.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Home.this.window.setTitle("Vernam Encryption System");
                Home.this.window.setContentPane(new Vernam(Home.this.window));
                Home.this.window.revalidate();
                Home.this.window.repaint();
            }
        });

        this.hill.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Home.this.window.setTitle("Hill Encryption System");
                Home.this.window.setContentPane(new Hill(Home.this.window));
                Home.this.window.revalidate();
                Home.this.window.repaint();
            }
        });
        
        this.feistel.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Home.this.window.setTitle("Feistel Encryption System");
                Home.this.window.setContentPane(new Feistel(Home.this.window));
                Home.this.window.revalidate();
                Home.this.window.repaint();
            }
        });

        background.add(buttonPanel);
        
        return background;
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
                
                if (getModel().isPressed())
                    g2.setColor(ThemeManager.BUTTON_ACTIVE); 
                else if (getModel().isRollover()) 
                {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, ThemeManager.BUTTON_HOVER,
                        0, getHeight(), ThemeManager.darken(ThemeManager.BUTTON_HOVER, 0.8f)
                    );
                    g2.setPaint(gradient);
                } 
                else 
                    g2.setColor(ThemeManager.BUTTON_BG);
                
                if (!getModel().isRollover() || getModel().isPressed()) 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.setColor(ThemeManager.ACCENT_CYAN);
                g2.setStroke(new BasicStroke(getModel().isRollover() ? 2.0f : 1.0f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setForeground(ThemeManager.ACCENT_CYAN);
        button.setFont(ThemeManager.FONT_MONO_BOLD);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(25, 15, 25, 15));
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
        this.removeAll();
        this.setLayout(new BorderLayout()); 
        JPanel HomePanel = createHomePanel();
        this.add(HomePanel, BorderLayout.CENTER);
        this.setOpaque(false);
    } 
}
