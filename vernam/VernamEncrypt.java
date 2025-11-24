package crypto.vernam;

import crypto.utils.DrawBackground;
import crypto.vernam.Vernam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;


public class VernamEncrypt extends JPanel
{
    protected JTextField message_field;
    protected JTextField key_field;
    protected JButton back;
    protected JButton encrypt;
    private Vernam verWind;

    public VernamEncrypt(Vernam window)
    {

        DrawBackground background = new DrawBackground("/home/blackblade/Bureau/Programmation/Java/ProgrammeJava/EvenementJava/cryptoapp/crypto/ressources/img3.png");

        this.verWind = window;
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.back = new JButton("Back");
        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                VernamEncrypt.this.verWind.restoreWindow();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                VernamEncrypt.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                VernamEncrypt.this.back.setBackground(Color.WHITE);
            }
        });

        this.encrypt = new JButton("Encrypt");

        this.encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                vernam_encryption_condition();
            }
        });

        background.setLayout(new GridBagLayout());
        background.add(this.back);
        background.add(form_datas());
        background.add(this.encrypt);

        this.add(background,BorderLayout.CENTER);
    }

    private int vernam_encrypt(int message, int key)
    {
        return (message + key) % 26;
    }

    private JPanel form_datas()
    {
        JPanel datas_form = new JPanel();
        datas_form.setLayout(new GridLayout(2,2,10,10));
        datas_form.add(new JLabel("Enter the message"));
        this.message_field = new JTextField();
        this.key_field = new JTextField();

        datas_form.add(this.message_field);
        datas_form.add(new JLabel("Enter the key"));
        datas_form.add(this.key_field);

        return datas_form;
    }

    private void vernam_encryption_condition()
    {
        String message = this.message_field.getText().trim();
        String key = this.key_field.getText().trim();

        int start   = 0;
        int base    = 0;
        int int_key = 0;

        if(message.isEmpty() || key.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Empty values!\nCheck and retry!","Warming",JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String clear_message = message;

        if(message.length() != key.length())
        {
            JOptionPane.showMessageDialog(this,"he length of message must be same with the message length !\nCheck and continue !","Warming",JOptionPane.ERROR_MESSAGE);
            return;
        }

        for(int i = 0; i < message.length(); i++)
        {
            if(!Character.isLetter(message.charAt(i)) || Character.isLetter(key.charAt(i)))
                continue;
            
            if(Character.isUpperCase(message.charAt(i)))
                base = 'A';
            else
                base = 'a';
            
            if(Character.isUpperCase(key.charAt(i)))
                int_key = key.charAt(i) - 'A';
            else
                int_key = key.charAt(i) - 'a';

            start = vernam_encrypt(message.charAt(i) - base, int_key);
            message = message.substring(0,i) + (char)start + message.substring(i + 1);
        }

        JOptionPane.showMessageDialog(this,"The encrypted message of " + clear_message + " is " + message,"INFORMATION",JOptionPane.INFORMATION_MESSAGE);
        this.message_field.setText("");
        this.key_field.setText("");
    }
}