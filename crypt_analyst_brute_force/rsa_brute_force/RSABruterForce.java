package crypto.crypt_analys_brute_force.rsa_brute_force;

import crypto.utils.DrawBackground;
import crypto.utils.Util;
import crypto.encryption_decryption.rsa.RSA;
import crypto.encryption_decryption.rsa.RsaDecrypt;
import crypto.Header;
import crypto.Home;
import crypto.Main;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.*;
import javax.imageio.*;

public class RSABruterForce extends JPanel
{
    protected JTextField n_field;
    protected JTextField e_field;
    protected JTextField message;

    protected JButton start;
    protected JButton back;
    private Main main_window;

    public RSABruterForce(Main main_window)
    {
        this.main_window = main_window;
        this.n_field = new JTextField();
        this.e_field = new JTextField();
        this.message = new JTextField();
        this.start = Main.createStyledButton("Start", new Color(52, 152, 219), Color.WHITE, new Font("SansSerif", Font.PLAIN, 16));
        this.back = Main.createStyledButton("Back", new Color(52, 154, 220), Color.WHITE, new Font("SansSerif", Font.PLAIN, 16));
        
        DrawBackground background = new DrawBackground("ressources/avrotbvba.png");
        
        JPanel btn_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btn_panel.add(this.start);
        btn_panel.add(this.back);
        btn_panel.setOpaque(false);

        background.setLayout(new BoxLayout(back, BoxLayout.Y_AXIS));
        background.add(Box.createVerticalStrut(35));

        background.add(value_field_content());

        background.add(Box.createVerticalStrut(15));
        background.add(btn_panel);

        this.add(background);
    }

    private JPanel value_field_content()
    {
        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(3, 3, 10, 10));
        panel.add(new JLabel("Enter the public value(n) "));
        panel.add(this.n_field);
        panel.add(new JLabel("Enter the encryption key(e) "));
        panel.add(this.e_field);
        panel.add(new JLabel("Enter the message to brute-force"));
        panel.add(this.message);
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 3, true), 
                BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20),
                "Datas for Brute-force",TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Montserrat", Font.BOLD, 22).deriveFont(Font.BOLD, 24f), new Color(34, 49, 63)
                )
            )
        );

        return panel;
    }

    private JPanel brute_force_process()
    {
        String n_string = this.n_field.getText().trim();
        String e_string = this.e_field.getText().trim();

        if(n_string.isEmpty() || e_string.isEmpty() || this.message.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please enter the datas and try again !", "Error", JOptionPane.ERROR_MESSAGE);
            return this;
        }

        if(!Util.veri_user_enter(n_string) || Util.veri_user_enter(e_string))
        {
            JOptionPane.showMessageDialog(this, "The value(s) must be the integer(s), check and retury !", "Warning", JOptionPane.WARNING_MESSAGE);
            return this;
        }

        int n = Util.convert_user_enter(n_string);
        int e = Util.convert_user_enter(e_string);

        JTextArea p_q_values = new JTextArea();

        for(int i = 2; i < n; i++)
        {
            for(int j = 2; j < n; j++)
            {
                if(Util.isPrime(i, j) && ((i * j) == n))
                {
                    p_q_values.append(String.valueOf(i));
                    p_q_values.append(String.valueOf(j));
                }
            }
        }

        char [] p_q_array = p_q_values.getText().trim().toCharArray();
        int p_length = p_q_array.length;

        JTextArea private_key = new JTextArea();

        for(int i = 0; i < p_length - 1; i++)
        {
            if(Util.isPrime((p_q_array[i] - 1) * (p_q_array[i + 1] - 1), e))
            {
                long[] uv = new long[2];
                long pgcd = Util.extendEuclide((p_q_array[i] - 1) * (p_q_array[i + 1] - 1), e, uv);
                int u = (int)uv[0];
                private_key.append(String.valueOf(u));
            }
        }

        String msg = this.message.getText().trim();
        int msg_len = msg.length();

        char[] private_key_str = private_key.getText().trim().toCharArray();
        int p_k_lenht = private_key_str.length;

        char [][] brt_msg = new char[msg_len][p_k_lenht];
        
        for(int i = 0; i < msg_len; i++)
        {
            for(int j = 0; j < p_k_lenht; j++)
            {
                if(i < msg_len && j < p_k_lenht)
                {
                    int clear_msg = RsaDecrypt.rsa_decrypt((int)msg.charAt(i), Util.convert_user_enter(String.valueOf(private_key_str[j])), n) % 26;
                    brt_msg[i][j] = (char)(clear_msg + 'A');
                }
            }
        }
        return null;
    }
}
