package crypto.hill;

import crypto.utils.DrawBackground;
import crypto.rsa.RsaEncrypt;
import crypto.Main;
import crypto.Home;
import crypto.Header;
import crypto.hill.HillEncrypt;
//import crypto.hill.HillDecrypt;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Hill extends JPanel
{
    protected JButton encrypt;
    protected JButton decrypt;
    protected JButton back;
    protected Main mainWindow;

    public Hill(Main window)
    {
        this.mainWindow = window;
        this.setSize(new Dimension(700,700));

        DrawBackground background = new DrawBackground("crypto/ressources/Pivot.png");
        background.setOpaque(false);
        background.setLayout(new GridBagLayout());

        this.encrypt = new JButton("Encrypt");
        this.decrypt = new JButton("Decrypt");
        this.back    = new JButton("Back");

        this.encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Hill.this.mainWindow.setTitle("Hill Encryption System");
                Hill.this.mainWindow.getContentPane().removeAll();
                Hill.this.mainWindow.setContentPane(new HillEncrypt(Hill.this));
                Hill.this.mainWindow.revalidate();
                Hill.this.mainWindow.repaint();
            }
        });

        /*this.decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Hill.mainWindow.setTitle("Hill Decryption System");
                Hill.this.mainWindow.setContentPane(new HillDecrypt(Hill.this));
                Hill.this.revalidate();
                Hill.this.repaint();
            }
        });*/

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(new Header(Hill.this.mainWindow),BorderLayout.NORTH);
                panel.add(new Home(Hill.this.mainWindow),BorderLayout.CENTER);

                Hill.this.mainWindow.setTitle("Crypto Application");
                Hill.this.mainWindow.getContentPane().removeAll();
                Hill.this.mainWindow.setContentPane(panel);
                Hill.this.mainWindow.getContentPane().revalidate();
                Hill.this.mainWindow.getContentPane().repaint();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                Hill.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                Hill.this.back.setBackground(Color.WHITE);
            }
        });

        JPanel btn_panel = new JPanel(new GridLayout(0,2,10,10));
        btn_panel.add(this.encrypt);
        btn_panel.add(this.decrypt);
        //btn_panel.setOpaque(false);

        this.back.setPreferredSize(new Dimension(250,30));
        JPanel content_panel = new JPanel();
        content_panel.setLayout(new BoxLayout(content_panel,BoxLayout.Y_AXIS));
        content_panel.add(btn_panel);
        content_panel.add(Box.createVerticalStrut(15));
        content_panel.add(this.back);

        background.add(content_panel);
        this.setLayout(new BorderLayout());
        this.add(background);
    }

    public void restoreWindow()
    {
        this.mainWindow.setTitle("Hill Encryption System");
        this.mainWindow.setContentPane(new Hill(this.mainWindow));
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
