package crypto.encryption_decryption.affine;

import crypto.utils.Util;
import crypto.Main;
import crypto.utils.DrawBackground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class AffineEncrypt extends JPanel
{
    protected JTextField fkey_field;
    protected JTextField skey_field;
    protected JTextField message_field;
    protected String     message;
    protected int        fkey;
    protected int        skey;
    protected JButton    back;
    protected Affine     affineWindow;

    public AffineEncrypt(Affine affineWindow)
    {
        DrawBackground background = new DrawBackground("crypto/ressources/MITM.png");
        this.affineWindow = affineWindow;
        //this.affineWindow.setTitle("Affine Encryption system");
        this.setSize(500,500);
        background.setLayout(new GridBagLayout());
        background.setOpaque(true);

        JButton encrypt = new JButton("Encrypt");
        encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                affine_encryption_condition();
            }
        });

        this.back = new JButton("Back");
        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                affineWindow.showMe();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                AffineEncrypt.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                AffineEncrypt.this.back.setBackground(Color.WHITE);
            }
        });

        background.add(this.back);
        background.add(affine_form());
        background.add(encrypt);

        this.setLayout(new BorderLayout());
        this.add(background,BorderLayout.CENTER);
    }

    private int encryptAffine(int message,int fkey,int skey)
    {
        return (message * fkey + skey) % 26;
    }

    private JPanel affine_form()
    {
        JPanel affine_panel = new JPanel();
        affine_panel.setLayout(new GridLayout(3,2,10,10));
        
        affine_panel.add(new JLabel("Enter the first key"));
        fkey_field  = new JTextField();
        affine_panel.add(fkey_field);

        affine_panel.add(new JLabel("Enter the second key"));
        skey_field  = new JTextField();
        affine_panel.add(skey_field);

        affine_panel.add(new JLabel("Enter the message"));
        message_field = new JTextField();
        affine_panel.add(message_field);

        return affine_panel;
    }

    private void affine_encryption_condition()
    {
        String fkeyStr = fkey_field.getText();
        String skeyStr = skey_field.getText();
        message        = message_field.getText();

        if(fkeyStr.isEmpty() || skeyStr.isEmpty() || message.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Empty values !\n Check and retry","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!Util.veri_user_enter(fkeyStr) || !Util.veri_user_enter(skeyStr))
        {
            JOptionPane.showMessageDialog(this,"The values are not the integers !\n Check and retry","Error",JOptionPane.ERROR_MESSAGE);
            fkey_field.setText("");
            skey_field.setText("");
            message_field.setText("");
            return;
        }

        fkey    = Util.convert_user_enter(fkeyStr);
        skey    = Util.convert_user_enter(skeyStr);

        if(!Util.isPrime(fkey,26))
        {
            JOptionPane.showMessageDialog(this,"The first key has to be prime with the alphabet lenght !\n Check and retry !","Error",JOptionPane.ERROR_MESSAGE);
            fkey_field.setText("");
            return;
        }

        String clear_message = message;
        int lenght           = message.length();
        
        for(int i = 0; i < lenght; i++)
        {
            if(!Character.isLetter(message.charAt(i)))
                continue;

            int encrypt_message;
            if(Character.isUpperCase(message.charAt(i)))
                encrypt_message = encryptAffine(message.charAt(i) - 'A', fkey, skey) + 'A';
            
            else
                encrypt_message = encryptAffine(message.charAt(i) - 'a', fkey, skey) + 'a';
            
            message = message.substring(0, i) + (char)encrypt_message + message.substring(i + 1);
        }
        
        JOptionPane.showMessageDialog(this, "The encrypted message of (" + clear_message + ") is M = (" + clear_message + " * " + fkey + " + " + skey + ") % 26 = " + message, "Result", JOptionPane.INFORMATION_MESSAGE);
        fkey_field.setText("");
        skey_field.setText("");
        message_field.setText("");
    }
}
