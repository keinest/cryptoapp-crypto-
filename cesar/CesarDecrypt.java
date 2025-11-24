package crypto.cesar;
import crypto.cesar.Cesar;
import crypto.utils.Util;
import crypto.utils.DrawBackground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class CesarDecrypt extends JPanel
{
    protected JButton decrypt;
    protected Cesar cesar_window;
    protected JButton back;
    protected JTextField message_field;
    protected JTextField key_field;
    protected String message;

    protected String key_char; 

    public CesarDecrypt(Cesar cesar_window)
    {
        this.cesar_window = cesar_window;
        DrawBackground background = new DrawBackground("crypto/ressources/Gemini_Generated_Image_jyfixejyfixejyfi.png");

        this.setSize(new Dimension(500,500));
        background.setLayout(new GridBagLayout());
        background.setOpaque(true);
        
        this.decrypt = new JButton("Decrypt");
        this.decrypt.addActionListener(event -> cesar_decryption_condition());

        this.back = new JButton("Back");
        this.back.addActionListener(event -> CesarDecrypt.this.cesar_window.restoreWindow());

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                CesarDecrypt.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                CesarDecrypt.this.back.setBackground(Color.WHITE);
            }
        });

        background.add(this.back);
        background.add(cesar_form());
        background.add(this.decrypt);

        this.setLayout(new BorderLayout());
        this.add(background,BorderLayout.CENTER);
    }

    public int cesarDecrypt(int letter, int key)
    {
        int decrypted = (letter - key) % 26;
        if(decrypted < 0)
            decrypted += 26;

        return decrypted;
    }

    public void cesar_decryption_condition()
    {
        this.message = this.message_field.getText().trim();
        String key_str = this.key_field.getText().trim();

        if(this.message.isEmpty() || key_str.isEmpty())
        {
            JOptionPane.showMessageDialog(CesarDecrypt.this,"Empty value(s)!\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(key_str.length() != 1 || !Character.isLetter(key_str.charAt(0)))
        {
            JOptionPane.showMessageDialog(CesarDecrypt.this,"The key must be a single letter (A-Z or a-z)!\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        int real_key = 0;
        char key_char = key_str.charAt(0);
        
        if (Character.isUpperCase(key_char))
            real_key = key_char - 'A';
        else 
            real_key = key_char - 'a';

        char[] message_chars = this.message.toCharArray();
        int len = message_chars.length;
        int base = 0;
        int decrypted = 0;
        
        for(int i = 0; i < len; i++)
        {
            if(!Character.isLetter(message_chars[i]))
                continue;

            if(Character.isUpperCase(message_chars[i]))
                base = 'A';
            else
                base = 'a';
            
            decrypted = cesarDecrypt(message_chars[i] - base, real_key);
            
            message_chars[i] = (char)(decrypted + base);
        }

        String decrypted_message = new String(message_chars);

        JOptionPane.showMessageDialog(this,"Decrypted message : " + decrypted_message,"INFORMATION",JOptionPane.INFORMATION_MESSAGE);
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

        mainPanel.add(new JLabel("Enter the key (A-Z or a-z)"));
        key_field = new JTextField();
        mainPanel.add(key_field);
        
        return mainPanel;
    }
}