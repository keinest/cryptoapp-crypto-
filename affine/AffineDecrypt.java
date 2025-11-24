package crypto.affine;
import crypto.utils.Util;
import crypto.affine.Affine;
import crypto.utils.DrawBackground;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class AffineDecrypt extends JPanel
{
    protected JTextField fkey_field;
    protected JTextField skey_field;
    protected JTextField encrypt_message_field;
    protected JButton    back;
    protected int        fkey;
    protected int        skey;
    protected JButton    decrypt;
    protected Affine     affineWindow;

    public AffineDecrypt(Affine affineWindow)
    {
        this.setSize(505,505);
        this.affineWindow = affineWindow;
        DrawBackground background = new DrawBackground("crypto/ressources/img2.png");
        this.setOpaque(true);

        this.decrypt = new JButton("Decrypt");

        this.decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                affine_decrypt_condition();
            }
        });

        this.back = new JButton("Back");
        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                AffineDecrypt.this.affineWindow.showMe();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                AffineDecrypt.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                AffineDecrypt.this.back.setBackground(Color.WHITE);
            }
        });

        background.setLayout(new GridBagLayout());
        background.add(this.back);
        background.add(affine_decrypt_form());
        background.add(this.decrypt);

        this.setLayout(new BorderLayout());
        this.add(background,BorderLayout.CENTER);
    }

    public JPanel affine_decrypt_form()
    {
        JPanel decrypt_panel = new JPanel();
        decrypt_panel.setLayout(new GridLayout(3,2));

        decrypt_panel.add(new JLabel("Enter the first key"));
        this.fkey_field = new JTextField();
        decrypt_panel.add(this.fkey_field);

        decrypt_panel.add(new JLabel("Enter the second key")); 
        this.skey_field = new JTextField();
        decrypt_panel.add(this.skey_field);

        decrypt_panel.add(new JLabel("Enter the message"));
        encrypt_message_field = new JTextField();
        decrypt_panel.add(encrypt_message_field);

        return decrypt_panel;
    }

    private int decrypt_affine(int dkey,int skey,int ch)
    {
        return (dkey * (ch - skey)) % 26; 
    }

    private void affine_decrypt_condition()
    { 
        String fkey_text        = this.fkey_field.getText().trim();
        String skey_text        = this.skey_field.getText().trim();
        String encrypt_message  = this.encrypt_message_field.getText().trim();

        if(fkey_text.isEmpty() || skey_text.isEmpty() || encrypt_message.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Empty value(s) !\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!Util.veri_user_enter(fkey_text) || !Util.veri_user_enter(skey_text))
        {
            JOptionPane.showMessageDialog(AffineDecrypt.this,"The values of key have to be integers !\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.fkey = Util.convert_user_enter(fkey_text);
        this.skey = Util.convert_user_enter(skey_text);

        if(!Util.isPrime(this.fkey,26))
        {
            JOptionPane.showMessageDialog(AffineDecrypt.this,"The first key has to be prime with the alphabet lenght !\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        long u = 0,v = 0,d = 0;
        long[] uv = new long[2];
        long pgcd = Util.extendEuclide(fkey,26,uv);
        u = uv[0];
        v = uv[1];
        d = u;

        if(d < 0)
            d += 26;
        
        int lenght = encrypt_message.length();
        
        String decrypt_hide = encrypt_message;

        for(int i = 0; i < lenght; i++)
        {
            int decrypt = 0;
            if(!Character.isLetter(encrypt_message.charAt(i)))
                continue;
            
            if(Character.isUpperCase(encrypt_message.charAt(i)))
                decrypt = decrypt_affine((int)d,skey,encrypt_message.charAt(i) - 'A') + 'A';

            else
                decrypt = decrypt_affine((int)d,skey,encrypt_message.charAt(i) - 'a') + 'a';

            encrypt_message = encrypt_message.substring(0,i) + (char)decrypt + encrypt_message.substring(i + 1);
        }

        JOptionPane.showMessageDialog(AffineDecrypt.this,"The decrypted message of " + decrypt_hide + " is " + d + " (" + decrypt_hide + " - " + this.skey + " mod 26) = " + encrypt_message,"Success",JOptionPane.INFORMATION_MESSAGE);
        this.fkey_field.setText("");
        this.skey_field.setText("");
        encrypt_message_field.setText("");
    }
}
