package crypto.vernam;

import crypto.Main;
import crypto.Home;
import crypto.Header;
import crypto.utils.DrawBackground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class Vernam extends JPanel
{
    protected JButton encrypt;
    protected JButton decrypt;
    protected JButton back;
    private Main mainWindow;

    public Vernam(Main mainWindow)
    {
        this.mainWindow = mainWindow;
        this.encrypt = new JButton("Encrypt");
        this.decrypt = new JButton("Decrypt");

        this.encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Vernam.this.mainWindow.setTitle("Vernam Encryption system");
                Vernam.this.mainWindow.setContentPane(new VernamEncrypt(Vernam.this));
                Vernam.this.mainWindow.revalidate();
                Vernam.this.mainWindow.repaint();
            }
        });

        this.decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Vernam.this.mainWindow.setTitle("Vernam Decryption System");
                Vernam.this.mainWindow.setContentPane(new VernamDecrypt(Vernam.this));
                Vernam.this.mainWindow.revalidate();
                Vernam.this.mainWindow.repaint();
            }
        });

        this.back = new JButton("Back");

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(new Header(Vernam.this.mainWindow),BorderLayout.NORTH);
                panel.add(new Home(Vernam.this.mainWindow),BorderLayout.CENTER);

                Vernam.this.mainWindow.getContentPane().removeAll();
                Vernam.this.mainWindow.setTitle("Crypto Application");
                Vernam.this.mainWindow.setContentPane(panel);
                Vernam.this.mainWindow.getContentPane().revalidate();
                Vernam.this.mainWindow.getContentPane().repaint();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                Vernam.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                Vernam.this.back.setBackground(Color.WHITE);
            }
        });

        JPanel back_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        back_panel.add(this.back);

        DrawBackground background = new DrawBackground("/home/blackblade/Bureau/Programmation/Java/ProgrammeJava/EvenementJava/cryptoapp/crypto/ressources/img6.png");
        background.setLayout(new GridBagLayout());
        
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

        this.setLayout(new BorderLayout());
        background.add(content_panel);
        this.add(background);
    }

    public void restoreWindow()
    {
        this.mainWindow.setTitle("Vernam Encryption System");
        this.mainWindow.setContentPane(this);
        this.mainWindow.revalidate();
        this.mainWindow.repaint();
    }
}
