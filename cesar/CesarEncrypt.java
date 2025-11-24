package crypto.cesar;
import crypto.cesar.Cesar;
import crypto.utils.Util;
import crypto.utils.DrawBackground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class CesarEncrypt extends JPanel
{
    protected JButton encrypt;
    protected Cesar cesar_window;
    protected JButton back;
    protected JTextField message_field;
    protected JTextField key_field;
    protected String message;
    protected String key;

    public CesarEncrypt(Cesar cesar_window)
    {
        this.cesar_window = cesar_window;
        DrawBackground background = new DrawBackground("crypto/ressources/Gemini_Generated_Image_jyfixejyfixejyfi.png");

        this.setSize(new Dimension(500,500));
        background.setLayout(new GridBagLayout());
        background.setOpaque(true);
        
        this.encrypt = new JButton("Encrypt");
        this.encrypt.addActionListener(event -> cesar_encryption_condition());

        this.back = new JButton("Back");
        this.back.addActionListener(event -> CesarEncrypt.this.cesar_window.restoreWindow());

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                CesarEncrypt.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                CesarEncrypt.this.back.setBackground(Color.WHITE);
            }
        });

        background.add(this.back);
        background.add(cesar_form());
        background.add(this.encrypt);
        this.setLayout(new BorderLayout());

        this.add(background,BorderLayout.CENTER);
    }

    private int cesarEncrypt(int msg,int key)
    {
        return (msg + key) % 26;
    }

    private void cesar_encryption_condition()
    {
        message = this.message_field.getText().trim();
        key = this.key_field.getText().trim();

        if(message.isEmpty() || key.isEmpty())
        {
            JOptionPane.showMessageDialog(CesarEncrypt.this,"Empty value(s)!\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(key.length() > 1)
        {
            JOptionPane.showMessageDialog(CesarEncrypt.this,"The key must be a single character!\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        int len              = message.length();
        int base             = 0;
        int start            = 0;
        String clear_message = message;

        int real_key = 0;

        if(Character.isLetter(key.charAt(0)))
        {
            if(Character.isUpperCase(key.charAt(0)))
                real_key = key.charAt(0) - 'A';
            else
                real_key = key.charAt(0) - 'a';
        }

        else
            real_key = key.charAt(0) - '0';

        for(int i = 0; i < len; i++)
        {
            if(!Character.isLetter(message.charAt(i)))
                continue;
            if(Character.isUpperCase(message.charAt(i)))
                base = 'A';
            else
                base = 'a';
            
            start = cesarEncrypt(message.charAt(i) - base,real_key);

            message = message.substring(0,i) + (char)(start + base) + message.substring(i + 1);
        }

        JOptionPane.showMessageDialog(this,"Encrypted message: "  + message,"INFORMATION",JOptionPane.INFORMATION_MESSAGE);
        message_field.setText("");
        key_field.setText("");
    }

    private JPanel cesar_form()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2,2));
        
        mainPanel.add(new JLabel("Enter the message"));
        message_field = new JTextField();
        mainPanel.add(message_field);

        mainPanel.add(new JLabel("Enter the key"));
        key_field = new JTextField();
        mainPanel.add(key_field);

        return mainPanel;
    }
}