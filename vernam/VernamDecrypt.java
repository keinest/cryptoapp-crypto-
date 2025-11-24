package crypto.vernam;

import crypto.utils.DrawBackground;
import crypto.vernam.Vernam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class VernamDecrypt extends JPanel
{
    protected JTextField message_field;
    protected JTextField key_field;
    protected JButton back;
    protected JButton decrypt;
    private Vernam verWind;

    public VernamDecrypt(Vernam window)
    {
        this.verWind = window;
        DrawBackground background = new DrawBackground("/home/blackblade/Bureau/Programmation/Java/ProgrammeJava/EvenementJava/cryptoapp/crypto/ressources/pixieDust.png");

        background.setLayout(new GridBagLayout());

        this.setLayout(new BorderLayout());

        this.decrypt = new JButton("Decrypt");
        this.back = new JButton("Back");

        this.decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                vernam_decryption_condition();
            }
        });

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                VernamDecrypt.this.verWind.restoreWindow();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                VernamDecrypt.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                VernamDecrypt.this.back.setBackground(Color.WHITE);
            }
        });

        background.add(this.back);
        background.add(vernam_form());
        background.add(this.decrypt);

        this.add(background,BorderLayout.CENTER);
    }

    private JPanel vernam_form()
    {
        JPanel form_datas = new JPanel();
        form_datas.setLayout(new GridLayout(2,2,10,10));

        this.message_field = new JTextField();
        form_datas.add(new JLabel("Enter the message"));
        form_datas.add(this.message_field);

        this.key_field = new JTextField();
        form_datas.add(new JLabel("Enter the key"));
        form_datas.add(this.key_field);

        return form_datas;
    }

    private int vernam_decrypt(int message, int key)
    {
        return (message - key) % 26;
    }

    private void vernam_decryption_condition()
    {
        String message = this.message_field.getText().trim();
        String key     = this.key_field.getText().trim();

        if(message.isEmpty() || key.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Empty values !\nCheck and continue !\n","Warming",JOptionPane.INFORMATION_MESSAGE);
            return;
        } 

        if(key.length() != message.length())
        {
            JOptionPane.showMessageDialog(this,"The length of message must be same with the message length !\nCheck and continue !","Warming",JOptionPane.ERROR_MESSAGE);
            return;
        }

        int int_key = 0;
        int msg     = 0;
        int start   = 0;
        int base    = 0;

        String clear_message = message;

        for(int i = 0; i < message.length(); i++)
        {
            if(!Character.isLetter(message.charAt(i)) || !Character.isLetter(key.charAt(i)))
                continue;

            if(Character.isUpperCase(key.charAt(i)))
                int_key = key.charAt(i) - 'A';
            else
                int_key = key.charAt(i) - 'a';
            
            if(Character.isUpperCase(message.charAt(i)))
                base = 'A';
            else
                base = 'a';
            
            start = vernam_decrypt(message.charAt(i) - base, int_key);
            message = message.substring(0,i) + (char)start + message.substring(i + 1);
        }

        JOptionPane.showMessageDialog(this,"The decrypted message of " + clear_message + " is " + message,"Result",JOptionPane.INFORMATION_MESSAGE);
        this.message_field.setText("");
        this.key_field.setText("");
    }
}