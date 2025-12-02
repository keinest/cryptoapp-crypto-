package crypto.encryption_decryption.rsa;
import crypto.utils.Util;
import crypto.Main;
import crypto.utils.DrawBackground;
import crypto.encryption_decryption.rsa.RSA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class RsaEncrypt extends JPanel
{
    protected int        phi;
    protected String     key;
    protected String     message;
    protected int        module;
    protected String     p;
    protected String     q;
    protected JButton    encrypt;
    protected JTextField keyMessage ;
    protected JTextField message_field;
    protected JTextField pfield;
    protected JTextField qfield;
    protected JButton    back;
    protected RSA       rsaWindow;

    public RsaEncrypt(RSA rsaWindow)
    {
        this.rsaWindow     = rsaWindow;
        DrawBackground background = new DrawBackground("crypto/ressources/change.png");
        
        this.setSize(500,500);
        
        background.setLayout(new GridBagLayout());

        this.encrypt = new JButton("Encrypt");
        
        this.encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                rsa_encryption_condition();
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
                RsaEncrypt.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                RsaEncrypt.this.back.setBackground(Color.WHITE);
            }
        });
        
        background.add(this.back);
        background.add(rsa_form());
        background.add(this.encrypt);

        this.setLayout(new BorderLayout());
        this.add(background,BorderLayout.CENTER);
        this.setOpaque(false);
    }

    private int encryptRSA(int m,int e,int module) // Ecncryption
    {
        return Util.power(m,e) % module;
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

        rsa_panel.add(new JLabel("Enter the key "));
        keyMessage = new JTextField();
        rsa_panel.add(keyMessage);

        rsa_panel.add(new JLabel("Enter the message "));
        message_field = new JTextField();
        rsa_panel.add(message_field);
        
        return rsa_panel;
    }

    private void rsa_encryption_condition()
    {
        RsaEncrypt.this.p       = pfield.getText().trim();
        RsaEncrypt.this.q       = qfield.getText().trim();
        RsaEncrypt.this.key     = keyMessage.getText();
        RsaEncrypt.this.message = message_field.getText().trim();

        if(p.isEmpty() || q.isEmpty() || key.isEmpty() || message.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Empty value ! Check and retry","Error",JOptionPane.ERROR_MESSAGE);
            keyMessage.setText("");
            message_field.setText("");
            pfield.setText("");
            qfield.setText("");

            return;
        }

        if(!Util.veri_user_enter(p) || !Util.veri_user_enter(q) || !Util.veri_user_enter(key) || 
           !Util.veri_user_enter(message))
        {
            JOptionPane.showMessageDialog(this,"The values of your are not the integers ! Check and retry","Error",JOptionPane.ERROR_MESSAGE);
            keyMessage.setText("");
            message_field.setText("");
            pfield.setText("");
            qfield.setText("");

            return;
        }

        int p_int       = 0;
        int q_int       = 0;
        int message_int = 0;
        int key_int     = 0;

        p_int           = Util.convert_user_enter(p);
        q_int           = Util.convert_user_enter(q);

        if(Util.isPrime(p_int,q_int) == false)
        {
            JOptionPane.showMessageDialog(this,"Your integers are not coprime !","Error",JOptionPane.ERROR_MESSAGE);
            keyMessage.setText("");
            message_field.setText("");
            pfield.setText("");
            qfield.setText("");

            return;
        }

        message_int = Util.convert_user_enter(message);
        key_int     = Util.convert_user_enter(key);

        RsaEncrypt.this.module = p_int * q_int;
        RsaEncrypt.this.phi    = (p_int - 1) * (q_int - 1);
        
        if(!Util.isPrime(RsaEncrypt.this.phi,key_int))
        {
            JOptionPane.showMessageDialog(this,"The Euler function and encryption key have to be coprime !\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            keyMessage.setText("");
            message_field.setText("");
            pfield.setText("");
            qfield.setText("");

            return;
        }

        int encrypt_message = encryptRSA(message_int,key_int,RsaEncrypt.this.module);

        JOptionPane.showMessageDialog(this, "The encrypted message of " + message_int + " is (M = " + message_int + "^" + key_int + " mod " + module + " = " + encrypt_message + ")", "Success", JOptionPane.INFORMATION_MESSAGE);
        keyMessage.setText("");
        message_field.setText("");
        pfield.setText("");
        qfield.setText("");
   
    }
}
