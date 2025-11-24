package crypto.cesar;
import crypto.Main;
import crypto.Home;
import crypto.Header;
import crypto.cesar.CesarDecrypt;
import crypto.utils.DrawBackground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;


public class Cesar extends JPanel
{
    protected JButton encrypt;
    protected JButton decrypt;
    protected JButton back;
    protected Main mainWindow;

    public Cesar(Main mainWindow)
    {
        this.mainWindow = mainWindow;
        this.setSize(new Dimension(500,500));
        DrawBackground background = new DrawBackground("crypto/ressources/Gemini_Generated_Image_jyfixajyfixajyfi.png");
        
        background.setOpaque(true);
        background.setLayout(new GridBagLayout());

        this.encrypt = new JButton("Encrypt");
        this.decrypt = new JButton("Decrypt");
        this.back = new JButton("Back");

        this.encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Cesar.this.mainWindow.setTitle("Cesar Encryption system");
                Cesar.this.mainWindow.setContentPane(new CesarEncrypt(Cesar.this));
                Cesar.this.mainWindow.revalidate();
                Cesar.this.mainWindow.repaint();
            }
        });

        this.decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Cesar.this.mainWindow.setTitle("Cesar Decryption system");
                Cesar.this.mainWindow.setContentPane(new CesarDecrypt(Cesar.this));
                Cesar.this.mainWindow.revalidate();
                Cesar.this.mainWindow.repaint();
            }
        });

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                
                panel.add(new Header(Cesar.this.mainWindow),BorderLayout.NORTH);
                panel.add(new Home(Cesar.this.mainWindow),BorderLayout.CENTER);
                
                Cesar.this.mainWindow.setTitle("Crypto Application");
                Cesar.this.mainWindow.getContentPane().removeAll();
                Cesar.this.mainWindow.setContentPane(panel);
                Cesar.this.mainWindow.getContentPane().revalidate();
                Cesar.this.mainWindow.getContentPane().repaint();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                Cesar.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                Cesar.this.back.setBackground(Color.WHITE);
            }
        });
        
        JPanel btn_panel = new JPanel();
        btn_panel.setLayout(new GridLayout(0,2,10,10));
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

        background.add(content_panel);

        this.setLayout(new BorderLayout());
        this.add(background);
    }

    public void restoreWindow()
    {
        this.mainWindow.setTitle("Cesar Encryption system");
        this.mainWindow.setContentPane(new Cesar(this.mainWindow));
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
