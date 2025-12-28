package crypto.encryption_decryption.vigenere;

import crypto.encryption_decryption.vigenere.Vigenere;
import crypto.utils.DrawBackground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VigenereDecrypt extends JPanel
{
    private Vigenere vigWindow;
    private JButton back;
    private JButton decrypt;
    protected JTextField message_field;
    protected JTextField key_field;
    protected String message;
    protected String key;

    public VigenereDecrypt(Vigenere vigWindow)
    {
        this.vigWindow = vigWindow;
        this.setVisible(true);
        this.setSize(new Dimension(700,700));

        DrawBackground background = new DrawBackground("crypto/ressources/IMG-20241008-WA0039.jpg");
        background.setLayout(new GridBagLayout());

        this.decrypt = new JButton("Decrypt");
        this.decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                vigenere_decryption_condition();
            }
        });

        this.back = new JButton("Back");
        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                VigenereDecrypt.this.vigWindow.restoreWindow();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                VigenereDecrypt.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                VigenereDecrypt.this.back.setBackground(Color.WHITE);
            }
        });

        background.add(back);
        background.add(vigenere_form());
        background.add(decrypt);

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

    private int vigenere_decrypt(int message, int key)
    {
        int decrypted = (message - key) % 26;

        if (decrypted < 0)
            decrypted += 26;
        return decrypted;
    }

    private void vigenere_decryption_condition()
    {
        VigenereDecrypt.this.message = message_field.getText().trim();
        VigenereDecrypt.this.key     = key_field.getText().trim();

        if(VigenereDecrypt.this.message.isEmpty() || VigenereDecrypt.this.key.isEmpty())
        {
            JOptionPane.showMessageDialog(VigenereDecrypt.this,"Empty value(s)!\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!VigenereDecrypt.this.key.matches("[a-zA-Z]+")) 
        {
            JOptionPane.showMessageDialog(VigenereDecrypt.this, "The key must contain only letters!\nCheck and retry !", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String clear_message = message;
        char[] message_chars = message.toCharArray(); 
        
        int len_m     = message_chars.length;
        int len_k     = VigenereDecrypt.this.key.length();
        int countk    = 0;
        int base      = 0;
        int decrypted = 0;
        int real_key  = 0;

        for(int countm = 0; countm < len_m; countm++)
        {
            char char_m = message_chars[countm];

            if(!Character.isLetter(char_m))
                continue; 

            char char_k = VigenereDecrypt.this.key.charAt(countk % len_k); 
            if(Character.isUpperCase(char_k))
                real_key = char_k - 'A';
            else
                real_key = char_k - 'a';

            if(Character.isUpperCase(char_m))
                base = 'A';
            else
                base = 'a';

            decrypted = vigenere_decrypt(char_m - base, real_key);

            message_chars[countm] = (char)(decrypted + base);

            countk++;
        }
        
        String decrypted_message = new String(message_chars);

        JOptionPane.showMessageDialog(VigenereDecrypt.this,"Decrypted message : " + decrypted_message,"INFORMATION",JOptionPane.INFORMATION_MESSAGE);
        message_field.setText("");
        key_field.setText("");
    }
}