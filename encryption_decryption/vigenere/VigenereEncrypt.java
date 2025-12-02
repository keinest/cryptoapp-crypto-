package crypto.encryption_decryption.vigenere;

import crypto.encryption_decryption.vigenere.Vigenere;
import crypto.utils.DrawBackground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class VigenereEncrypt extends JPanel
{
    private Vigenere vigWindow;
    private JButton back;
    private JButton encrypt;
    protected JTextField message_field;
    protected JTextField key_field;
    protected String message;
    protected String key;

    public VigenereEncrypt(Vigenere vigWindow)
    {
        this.vigWindow = vigWindow;
        this.setVisible(true);
        this.setSize(new Dimension(700,700));

        DrawBackground background = new DrawBackground("crypto/ressources/IMG-20241008-WA0039.jpg");
        background.setLayout(new GridBagLayout());

        this.encrypt = new JButton("Encrypt");
        this.encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                vigenere_encryption_condition();
            }
        });

        this.back = new JButton("Back");

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                VigenereEncrypt.this.vigWindow.restoreWindow();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                VigenereEncrypt.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                VigenereEncrypt.this.back.setBackground(Color.WHITE);
            }
        });

        background.add(back);
        background.add(vigenere_form());
        background.add(encrypt);

        this.setLayout(new BorderLayout());
        this.add(background,BorderLayout.CENTER);
        this.setOpaque(true);
    }

    private JPanel vigenere_form()
    {
        JPanel vigenere_panel = new JPanel();
        vigenere_panel.setLayout(new GridLayout(2,2,10,10));

        message_field = new JTextField();
        key_field     = new JTextField();

        vigenere_panel.add(new JLabel("Enter the message"));
        vigenere_panel.add(message_field);

        vigenere_panel.add(new JLabel("Enter the key (Letters only)"));
        vigenere_panel.add(key_field);

        return vigenere_panel;
    }

    private int vigenere_encrypt(int message,int key)
    {
        return (message + key) % 26;
    }

    private void vigenere_encryption_condition()
    {
        VigenereEncrypt.this.message = message_field.getText().trim();
        VigenereEncrypt.this.key     = key_field.getText().trim();

        if(VigenereEncrypt.this.message.isEmpty() || VigenereEncrypt.this.key.isEmpty())
        {
            JOptionPane.showMessageDialog(VigenereEncrypt.this,"Empty value(s)!\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!VigenereEncrypt.this.key.matches("[a-zA-Z]+")) 
        {
            JOptionPane.showMessageDialog(VigenereEncrypt.this, "The key must contain only letters!\nCheck and retry !", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String clear_message = message;
        
        char[] message_chars = message.toCharArray(); 
        
        int len_m    = message_chars.length;
        int len_k    = VigenereEncrypt.this.key.length();
        int countk   = 0;
        int base     = 0;
        int start    = 0;
        int real_key = 0;

        for(int countm = 0; countm < len_m; countm++)
        {
            char char_m = message_chars[countm];
            if(!Character.isLetter(char_m))
                continue; 
            
            char char_k = VigenereEncrypt.this.key.charAt(countk % len_k); 

            if(Character.isUpperCase(char_k))
                real_key = char_k - 'A';
            else
                real_key = char_k - 'a';

            if(Character.isUpperCase(char_m))
                base = 'A';
            else
                base = 'a';
            start = vigenere_encrypt(char_m - base, real_key);

            message_chars[countm] = (char)(start + base);

            countk++;
        }

        String encrypted_message = new String(message_chars);

        JOptionPane.showMessageDialog(VigenereEncrypt.this,"Encrypted message : "+ encrypted_message,"INFORMATION",JOptionPane.INFORMATION_MESSAGE);
        message_field.setText("");
        key_field.setText("");
    }
}