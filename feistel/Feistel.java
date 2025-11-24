package crypto.feistel;

import crypto.utils.DrawBackground;
import crypto.Main;
import crypto.Header;
import crypto.Home;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;

public class Feistel extends JPanel
{
    protected JButton encrypt;
    protected JButton decrypt;
    protected JButton back;
    protected Main mainWindow;

    public Feistel(Main mainWindow)
    {
        this.mainWindow = mainWindow;
        this.setSize(new Dimension(700,700));
        this.setLayout(new BorderLayout());

        DrawBackground background = new DrawBackground("crypto/ressources/IMG-20251026-WA0102.jpg");
        background.setLayout(new GridBagLayout());

        this.encrypt = new JButton("Encrypt");
        this.encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Feistel.this.mainWindow.setTitle("Feistel Encryption System");
                Feistel.this.mainWindow.getContentPane().removeAll();
                Feistel.this.mainWindow.setContentPane(new FeistelEncrypt(Feistel.this));
                Feistel.this.mainWindow.revalidate();
                Feistel.this.mainWindow.repaint();
            }
        });
        
        this.decrypt = new JButton("Decrypt");
        this.decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Feistel.this.mainWindow.setTitle("Feistel Decryption System");
                Feistel.this.mainWindow.getContentPane().removeAll();
                Feistel.this.mainWindow.setContentPane(new FeistelDecrypt(Feistel.this));
                Feistel.this.mainWindow.revalidate();
                Feistel.this.mainWindow.repaint();
            }
        });
        
        this.back = new JButton("Back");

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Feistel.this.mainWindow.getContentPane().removeAll();

                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.add(new Header(Feistel.this.mainWindow),BorderLayout.NORTH);
                panel.add(new Home(Feistel.this.mainWindow),BorderLayout.CENTER);

                Feistel.this.mainWindow.getContentPane().removeAll();
                Feistel.this.mainWindow.setTitle("Crypto Application");
                Feistel.this.mainWindow.setContentPane(panel);
                Feistel.this.mainWindow.getContentPane().revalidate();
                Feistel.this.mainWindow.getContentPane().repaint();
            }
        });

        JPanel button_panel = new JPanel();
        button_panel.setLayout(new GridLayout(0,2,10,10));
        button_panel.add(this.encrypt);
        button_panel.add(this.decrypt);
        button_panel.setOpaque(false);

        JPanel content_panel = new JPanel();
        content_panel.setLayout(new BoxLayout(content_panel,BoxLayout.Y_AXIS));
        content_panel.setOpaque(false);
        
        this.back.setPreferredSize(new Dimension(250,30));
        content_panel.add(button_panel);
        content_panel.add(Box.createVerticalStrut(15));
        content_panel.add(this.back);
        background.add(content_panel);
        this.add(background);
    }

    public void restoreWindow()
    {
        this.mainWindow.setTitle("Feistel Encryption system");
        this.mainWindow.setContentPane(new Feistel(this.mainWindow));
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
