package crypto;

import crypto.Main;
import crypto.users.Registration;
import crypto.users.Connect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Header extends JPanel
{
    private static final Color PRIMARY_COLOR = new Color(30, 40, 60);
    private static final Color ACCENT_COLOR  = new Color(0, 150, 255);
    private static final Color TEXT_LIGHT    = Color.WHITE;

    protected JButton sigin;
    protected JButton connect;
    protected JButton signOut;
    protected JButton back;
    private Main window;

    public Header(Main window)
    {
        this.window = window;
        
        this.setOpaque(true);
        this.setBackground(new Color(240, 240, 240, 200)); 
        this.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 15, 10, 15),
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)
        ));
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 5));

        this.sigin   = createHeaderButton("Sigin", ACCENT_COLOR);
        this.connect = createHeaderButton("Login", new Color(46, 139, 87));
        this.signOut = createHeaderButton("Log Out", Color.GRAY);
        this.back    = createHeaderButton("Back", PRIMARY_COLOR);

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Header.this.window.getContentPane().removeAll();
                Header.this.window.showHome();
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

        this.add(this.sigin);
        this.add(this.connect);
        this.add(this.signOut);
        this.add(this.back);
    }

    private JButton createHeaderButton(String text, Color background)
    {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(TEXT_LIGHT);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseEntered(MouseEvent event) 
            {
                button.setBackground(background.brighter());
            }

            @Override
            public void mouseExited(MouseEvent event) 
            {
                button.setBackground(background);
            }
        });
        return button;
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
}