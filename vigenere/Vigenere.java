package crypto.vigenere;

import crypto.utils.DrawBackground;
import crypto.Main;
import crypto.Header;
import crypto.Home;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class Vigenere extends JPanel
{
    protected JButton encrypt;
    protected JButton decrypt;
    protected JButton back;
    private Main mainWindow;
    
    public Vigenere(Main mainWindow)
    {
        this.mainWindow = mainWindow;
        this.setSize(new Dimension(700,700));
        this.setLayout(new BorderLayout());

        DrawBackground background = new DrawBackground("crypto/ressources/evilTwin.png");
        background.setOpaque(true);
        background.setLayout(new GridBagLayout());
        
        this.encrypt = new JButton("Encrypt");
        this.decrypt = new JButton("Decrypt");

        encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Vigenere.this.mainWindow.setTitle("Vigenere Encryption system");
                Vigenere.this.mainWindow.setContentPane(new VigenereEncrypt(Vigenere.this));
                Vigenere.this.mainWindow.revalidate();
                Vigenere.this.mainWindow.repaint();
            }
        });

        this.decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Vigenere.this.mainWindow.setTitle("Vigenere Encryption System");
                Vigenere.this.mainWindow.setContentPane(new VigenereDecrypt(Vigenere.this));
                Vigenere.this.mainWindow.revalidate();
                Vigenere.this.mainWindow.repaint();
            }
        });
        
        this.back = new JButton("Back");

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.add(new Header(Vigenere.this.mainWindow),BorderLayout.NORTH);
                panel.add(new Home(Vigenere.this.mainWindow),BorderLayout.CENTER);

                Vigenere.this.mainWindow.getContentPane().removeAll();
                Vigenere.this.mainWindow.setTitle("Crypto Application");
                Vigenere.this.mainWindow.setContentPane(panel);
                Vigenere.this.mainWindow.getContentPane().revalidate();
                Vigenere.this.mainWindow.getContentPane().repaint();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                Vigenere.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                Vigenere.this.back.setBackground(Color.WHITE);
            }
        });
        
        JPanel btn_panel = new JPanel(new GridLayout(0,2,10,10));
        btn_panel.add(this.encrypt);
        btn_panel.add(this.decrypt);
        btn_panel.setOpaque(false);

        this.back.setPreferredSize(new Dimension(250,30));
        JPanel content_panel = new JPanel();
        content_panel.setLayout(new BoxLayout(content_panel,BoxLayout.Y_AXIS));
        content_panel.add(btn_panel);
        content_panel.add(Box.createVerticalStrut(15));
        content_panel.add(this.back);
        content_panel.setOpaque(false);

        this.setOpaque(true);
        background.add(content_panel);
        this.add(background);
    }
    
    public void restoreWindow()
    {
        this.mainWindow.setTitle("Vigenere Encryption system");
        this.mainWindow.setContentPane(new Vigenere(this.mainWindow));
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
