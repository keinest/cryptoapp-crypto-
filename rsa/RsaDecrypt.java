package crypto.rsa;
import crypto.utils.Util;
import crypto.utils.DrawBackground;
import crypto.rsa.RSA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class RsaDecrypt extends JPanel
{
    protected int        phi;
    protected String     key;
    protected String     message;
    protected int        module;
    protected String     p;
    protected String     q;
    protected JButton    decrypt;
    protected JTextField keyMessage ;
    protected JTextField message_field;
    protected JTextField pfield;
    protected JTextField qfield;
    protected JButton    back;
    protected RSA       rsaWindow;

    public RsaDecrypt(RSA rsaWindow)
    {
        this.rsaWindow = rsaWindow;

        DrawBackground background = new DrawBackground("crypto/ressources/logo.jpg");

        this.decrypt = new JButton("Decrypt");
        decrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                rsa_decryption_condition();
            }
        });

        this.back = new JButton("Back");

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                rsaWindow.restoreWindow();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                RsaDecrypt.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                RsaDecrypt.this.back.setBackground(Color.WHITE);
            }
        });

        background.setLayout(new GridBagLayout());

        background.add(this.back);
        background.add(rsa_form());
        background.add(this.decrypt);
        background.setOpaque(true);

        this.setLayout(new BorderLayout());
        this.add(background,BorderLayout.CENTER);
    }

    private int rsa_decrypt(int message,int privateKey,int module)
    {
        return Util.power(message,privateKey) % module;
    }

    private void rsa_decryption_condition()
    {
        RsaDecrypt.this.p = pfield.getText().trim();
        RsaDecrypt.this.q = qfield.getText().trim();
        RsaDecrypt.this.key = keyMessage.getText().trim();
        RsaDecrypt.this.message = message_field.getText().trim();

        if(p.isEmpty() || q.isEmpty() || key.isEmpty() || message.isEmpty())
        {
            JOptionPane.showMessageDialog(RsaDecrypt.this,"Empty values ! Check and retry !","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!Util.veri_user_enter(p) || !Util.veri_user_enter(q) 
            || !Util.veri_user_enter(key) || !Util.veri_user_enter(message))
        {
            JOptionPane.showMessageDialog(RsaDecrypt.this,"The values are not the integers !\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            keyMessage.setText("");
            message_field.setText("");
            pfield.setText("");
            qfield.setText("");

            return;
        }

        int p_int = 0;
        int q_int = 0;
        int key_int = 0;
        int message_int = 0;

        p_int = Util.convert_user_enter(p);
        q_int = Util.convert_user_enter(q);
        key_int = Util.convert_user_enter(key);

        if(!Util.isPrime(p_int,q_int))
        {
            JOptionPane.showMessageDialog(RsaDecrypt.this,"Your integers are not coprime !\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            keyMessage.setText("");
            message_field.setText("");
            pfield.setText("");
            qfield.setText("");

            return;
        }

        RsaDecrypt.this.module = p_int * q_int;
        RsaDecrypt.this.phi = (p_int - 1) * (q_int - 1);

        if(!Util.isPrime(RsaDecrypt.this.phi,key_int))
        {
            JOptionPane.showMessageDialog(RsaDecrypt.this,"The Euler function and encryption key have to be coprime !\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            keyMessage.setText("");
            message_field.setText("");
            pfield.setText("");
            qfield.setText("");

            return;
        }

        int u = 0;
        int v = 0; 
        long[] uv = new long[2];

        int d = 0;
        long pgcd = Util.extendEuclide((long)key_int,(long)RsaDecrypt.this.phi,uv);

        u = (int)uv[0];
        v = (int)uv[1];

        d = u;

        if(d < 0)
            d += RsaDecrypt.this.phi;
        int decrypt_message = rsa_decrypt(message_int,d,RsaDecrypt.this.module);

        JOptionPane.showMessageDialog(RsaDecrypt.this,"The decrypted message of " + RsaDecrypt.this.message + " is (" + message_int + " ^ " + d +") mod " + RsaDecrypt.this.module + "= " + decrypt_message,"INFORMATION",JOptionPane.INFORMATION_MESSAGE);
        keyMessage.setText("");
        message_field.setText("");
        pfield.setText("");
        qfield.setText("");
    }

    private JPanel rsa_form()
    {
        JPanel rsa_panel = new JPanel();
        rsa_panel.setLayout(new GridLayout(4,2,10,10));
        
        rsa_panel.add(new JLabel("Enter the first integer "));
        pfield = new JTextField();
        rsa_panel.add(pfield);

        rsa_panel.add(new JLabel("Enter the second integer "));
        qfield = new JTextField();
        rsa_panel.add(qfield);

        rsa_panel.add(new JLabel("Enter the encryption key "));
        keyMessage = new JTextField();
        rsa_panel.add(keyMessage);

        rsa_panel.add(new JLabel("Enter the message "));
        message_field = new JTextField();
        rsa_panel.add(message_field);
        
        return rsa_panel;
    }

}