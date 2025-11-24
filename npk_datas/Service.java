package crypto.npk_datas;

import crypto.users.Connect;
import crypto.users.Registration;
import crypto.Main;
import crypto.utils.DrawBackground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Service extends JPanel
{
    private static final Color PRIMARY_COLOR = new Color(30, 40, 60);
    private static final Color ACCENT_COLOR  = new Color(0, 150, 255);
    private static final Color TEXT_LIGHT    = Color.WHITE;
    private static final Color TEXT_DARK     = new Color(40, 40, 40);
    private static final Font TITLE_FONT     = new Font("SansSerif", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT  = new Font("SansSerif", Font.BOLD, 20);
    private static final Font BODY_FONT      = new Font("SansSerif", Font.PLAIN, 15);
    
    protected Main main_window;
    protected DrawBackground background;
    protected JButton back;
    
    public Service(Main main_window)
    {
        this.main_window    = main_window; 
        this.back           = createStyledButton("Back",new Color(255,30,30,200), TEXT_LIGHT, BODY_FONT);
        this.background     = new DrawBackground("crypto/ressources/IMG-20251027-WA0032.jpg");

        JPanel backPanel    = new JPanel();
        backPanel.setOpaque(false);
        backPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        backPanel.add(this.back);

        this.back.addActionListener(event -> Service.this.main_window.showHome());

        this.setLayout(new BorderLayout());
        this.add(backPanel,BorderLayout.NORTH);
        this.add(background, BorderLayout.CENTER);
    }
    
    private JButton createStyledButton(String text, Color background, Color foreground, Font font)
    {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBackground(background);
        button.setForeground(foreground);
        
        button.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                button.setBackground(background == PRIMARY_COLOR ? ACCENT_COLOR : background.darker());
                button.setForeground(TEXT_LIGHT);
            }

            @Override 
            public void mouseExited(MouseEvent event)
            {
                button.setForeground(foreground);
                button.setBackground(background);
            }
        });

        return button;
    } 
    
    public void restoreWindow(JPanel panelToRestore)
    {
        this.main_window.getContentPane().removeAll();
        this.main_window.setContentPane(panelToRestore);
        panelToRestore.revalidate();
        panelToRestore.repaint();
        this.main_window.revalidate();
        this.main_window.repaint();
        this.main_window.setVisible(true);
    }

}