package crypto;

import crypto.encryption_decryption.rsa.RSA;
import crypto.encryption_decryption.cesar.Cesar;
import crypto.encryption_decryption.vigenere.Vigenere;
import crypto.encryption_decryption.vernam.Vernam;
import crypto.encryption_decryption.hill.Hill;
import crypto.encryption_decryption.feistel.Feistel;
//import crypto.encryption_decryption.rsa.RsaEncrypt;
import crypto.encryption_decryption.affine.Affine;

import crypto.users.Registration;
import crypto.users.Connect;
import crypto.utils.DrawBackground;
import crypto.Main;
import crypto.Header;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Home extends JPanel
{
    private static final Color PRIMARY_COLOR = new Color(30, 40, 60);
    private static final Color ACCENT_COLOR  = new Color(0, 150, 255);
    private static final Color TEXT_DARK     = new Color(40, 40, 40);
    private static final Font BUTTON_FONT    = new Font("SansSerif", Font.BOLD, 16);
    
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
    
    private void styleAlgorithmButton(JButton button) 
    {
        button.setBackground(Color.WHITE); 
        button.setForeground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setFont(BUTTON_FONT);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), 
            BorderFactory.createEmptyBorder(25, 15, 25, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() 
        {
            public void mouseEntered(MouseEvent event) 
            {
                button.setBackground(ACCENT_COLOR);
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_COLOR.darker(), 2),
                    BorderFactory.createEmptyBorder(25, 15, 25, 15)
                ));
            }

            public void mouseExited(MouseEvent event) 
            {
                button.setBackground(Color.WHITE);
                button.setForeground(PRIMARY_COLOR);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                    BorderFactory.createEmptyBorder(25, 15, 25, 15)
                ));
            }
        });
    }

    private JPanel createHomePanel()
    {
        DrawBackground background = new DrawBackground("crypto/ressources/IOT.png");
        background.setLayout(new GridBagLayout()); 

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 30, 30));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        this.RsaEncrypt       = new JButton("RSA");
        this.affineEncrypt    = new JButton("Affine");
        this.cesar            = new JButton("Cesar");
        this.vigenere         = new JButton("Vigenere");
        this.vernam           = new JButton("Vernam");
        this.hill             = new JButton("Hill");
        this.feistel          = new JButton("Feistel");
        this.recTransposition = new JButton("Rectangular Transposition");
        this.playfair         = new JButton("Playfair");

        styleAlgorithmButton(this.RsaEncrypt);
        styleAlgorithmButton(this.affineEncrypt);
        styleAlgorithmButton(this.cesar);
        styleAlgorithmButton(this.vigenere);
        styleAlgorithmButton(this.vernam);
        styleAlgorithmButton(this.hill);
        styleAlgorithmButton(this.feistel);
        styleAlgorithmButton(this.recTransposition);
        styleAlgorithmButton(this.playfair);

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

    public void restoreHome()
    {
        this.removeAll();
        this.setLayout(new BorderLayout()); 
        JPanel HomePanel = createHomePanel();
        this.add(HomePanel, BorderLayout.CENTER);
        this.setOpaque(false);
    } 
}
