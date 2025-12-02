package crypto.encryption_decryption.hill;

import crypto.utils.DrawBackground;
import crypto.Main;
import crypto.utils.Util;
import crypto.encryption_decryption.hill.Hill;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class HillEncrypt extends JPanel
{
    protected JTextField [][] message;
    protected JTextField [][] key;
    protected JTextField message_rows;
    protected JTextField message_columns;
    protected double [][] message_values;
    protected double [][] encrypted_message;
    protected int rows_m;
    protected int columns_m;
    protected JButton reset;

    protected JTextField key_rows;
    protected JTextField key_columns;
    protected double [][] key_values;
    protected int rows_k;
    protected int columns_k;

    protected JButton back;
    protected JButton encrypt;

    private JButton validate;
    private Hill hwindow;

    public HillEncrypt(Hill hwindow)
    {
        DrawBackground background = new DrawBackground("crypto/ressources/IMG-20251026-WA0092.jpg");

        this.hwindow = hwindow;
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.back     = new JButton("Back");
        this.encrypt  = new JButton("Encrypt");
        this.validate = new JButton("Validate");

        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                HillEncrypt.this.hwindow.restoreWindow();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                HillEncrypt.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                HillEncrypt.this.back.setBackground(Color.WHITE);
            }
        });

        this.encrypt.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                if(get_key_values() && get_message_values())
                    hill_encryption();
            }
        });

        this.reset = new JButton("Reset all");
        this.reset.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                for(int i = 0; i < HillEncrypt.this.rows_k; i++)
                {
                    for(int j = 0; j < HillEncrypt.this.columns_k; j++)
                        HillEncrypt.this.key[i][j].setText("");
                }

                for(int i = 0; i < HillEncrypt.this.rows_m; i++)
                {
                    for(int j = 0; j < HillEncrypt.this.columns_m; j++)
                        HillEncrypt.this.message[i][j].setText("");
                }
            }
        });

        this.validate.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                String key_r = HillEncrypt.this.key_rows.getText().trim();
                String msg_r = HillEncrypt.this.message_rows.getText().trim();
                String key_c = HillEncrypt.this.key_columns.getText().trim();
                String msg_c = HillEncrypt.this.message_columns.getText().trim();

                if(!Util.veri_user_enter(key_r) || key_r.isEmpty() || !Util.veri_user_enter(msg_r) || msg_r.isEmpty()
                    || !Util.veri_user_enter(key_c) || key_c.isEmpty() || !Util.veri_user_enter(msg_c) || msg_c.isEmpty())
                {
                    JOptionPane.showMessageDialog(HillEncrypt.this,"Number format error","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                HillEncrypt.this.rows_k     = Util.convert_user_enter(key_r);
                HillEncrypt.this.columns_k  = Util.convert_user_enter(key_c);
                HillEncrypt.this.rows_m     = Util.convert_user_enter(msg_r);
                HillEncrypt.this.columns_m  = Util.convert_user_enter(msg_c);

                if(HillEncrypt.this.columns_k != HillEncrypt.this.rows_m)
                {
                    JOptionPane.showMessageDialog(HillEncrypt.this,"It is impossible to perform a linear combination with these two matrices.\nMake sure that the number of rows of your key matrix is equal to the number of columns of \nyour message matrix, then try again!\n","Warning",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                JSplitPane split_data = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                
                JPanel key_panel = new JPanel();
                key_panel.setLayout(new BoxLayout(key_panel,BoxLayout.Y_AXIS));

                JLabel k_label = new JLabel("Key");
                k_label.setForeground(Color.WHITE);
                k_label.setFont(new Font("sans-serif",Font.BOLD,15));
                k_label.setForeground(Color.BLUE);

                key_panel.add(k_label);
                key_panel.add(Box.createVerticalStrut(10));
                key_panel.add(key_form());

                JPanel message_panel = new JPanel();
                message_panel.setLayout(new BoxLayout(message_panel,BoxLayout.Y_AXIS));
                
                JLabel m_label = new JLabel("Message");
                m_label.setForeground(Color.WHITE);
                m_label.setForeground(Color.BLUE);
                m_label.setFont(new Font("sans-serif",Font.BOLD,15));

                message_panel.add(m_label);
                message_panel.add(Box.createVerticalStrut(15));
                message_panel.add(message_form());

                key_panel.setOpaque(false);
                message_panel.setOpaque(false);
                
                split_data.add(key_panel);
                split_data.add(message_panel);
                split_data.setOpaque(false);

                HillEncrypt.this.removeAll();
                HillEncrypt.this.setLayout(new BorderLayout());
                
                background.removeAll();
                JPanel data_panel = new JPanel(new GridBagLayout());
                data_panel.add(HillEncrypt.this.back);
                data_panel.add(split_data);
                data_panel.add(HillEncrypt.this.encrypt);

                JPanel content_panel = new JPanel();
                content_panel.setLayout(new BoxLayout(content_panel,BoxLayout.Y_AXIS));
                content_panel.add(data_panel);
                content_panel.add(Box.createVerticalStrut(15));
                content_panel.add(HillEncrypt.this.reset);
                split_data.setDividerLocation(200);
                background.add(content_panel);

                background.revalidate();
                background.repaint();
                
                HillEncrypt.this.add(background);
                HillEncrypt.this.revalidate();
                HillEncrypt.this.repaint();
            }
        });

        background.setLayout(new GridBagLayout());

        //this.setLayout(new GridBagLayout());
        background.add(this.back);
        background.add(data_length_form());
        background.add(this.validate);
        background.setOpaque(false);
        this.add(background);
    }

    // Matrice cle

    private JPanel key_form()
    {
        JPanel form_k = new JPanel();

        this.key = new JTextField[this.rows_k][this.columns_k];

        form_k.setLayout(new GridLayout(this.rows_k,this.columns_k,5,5));

        for(int i = 0; i < this.rows_k; i++)
        {
            for(int j = 0; j < this.columns_k; j++)
            {
                this.key[i][j] = new JTextField(3);
                form_k.add(this.key[i][j]);
            }
        }
        return form_k;
    }

    // Recuperation des valeurs de la matrice cle en format de nombres reels

    private boolean get_key_values()
    {
        this.key_values = new double[this.rows_k][this.columns_k];
        
        for(int i = 0; i < this.rows_k; i++)
        {
            for(int j = 0; j < this.columns_k; j++)
            {
                String cell = this.key[i][j].getText().trim();
                if(cell.isEmpty())
                {
                    JOptionPane.showMessageDialog(this,"The value of this cell of key is empty( " + (i + 1) + "," + (j + 1) + " ) !","Warning",JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                if(!Util.veri_number_format(cell))
                {
                    JOptionPane.showMessageDialog(this,"Some values of your key are not the numbers !","Error",JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                this.key_values[i][j] = Double.parseDouble(cell);
            }
        }
        return true;
    }

    // Matrice message

    private JPanel message_form()
    {
        JPanel form_m = new JPanel();
        form_m.setLayout(new GridLayout(this.rows_m,this.columns_m,5,5));

        this.message = new JTextField[this.rows_m][this.columns_m];

        for(int i = 0; i < this.rows_m; i++)
        {
            for(int j = 0; j < this.columns_m; j++)
            {
                final int lgn = i;
                final int col = j;
                JTextField field = new JTextField(3);
                field.addKeyListener(new KeyAdapter()
                {
                    @Override
                    public void keyPressed(KeyEvent event)
                    {
                        if(event.getKeyCode() == KeyEvent.VK_ENTER)
                        {
                            int line = lgn;
                            int cl = col + 1;
                            if(cl >= HillEncrypt.this.rows_m)
                            {
                                line++;
                                cl = 0;
                            }
                            if(line >= HillEncrypt.this.rows_k * HillEncrypt.this.columns_m)
                                line = 0;
                        }
                        HillEncrypt.this.message[lgn][col].requestFocusInWindow();
                    }
                });
                this.message[i][j] = field;
                form_m.add(this.message[i][j]);
            }
        }

        return form_m;
    }

    // Recuperation des valeurs de la matrice message en format de nombres reels

    private boolean get_message_values()
    {
        this.message_values = new double[this.rows_m][this.columns_m];

        for(int i = 0; i < this.rows_m; i++)
        {
            for(int j = 0; j < this.columns_m; j++)
            {
                String cell = this.message[i][j].getText().trim();
                if(cell.isEmpty())
                {
                    JOptionPane.showMessageDialog(this,"The value of this cell of message is empty( " + (i + 1) + "," + (j + 1) + " ) !","Warning",JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                if(!Util.veri_number_format(cell))
                {
                    JOptionPane.showMessageDialog(this,"Some values of your message are not the numbers","Error",JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                this.message_values[i][j] = Double.parseDouble(this.message[i][j].getText().trim());
            }
        }
        return true;
    }

    // Chifrement

    private void hill_encryption()
    {
        this.encrypted_message = new double[this.rows_k][this.columns_m];

        for(int i = 0; i < this.rows_k; i++)
        {
            for(int j = 0; j < this.columns_m; j++)
            {
                this.encrypted_message[i][j] = 0;
                for(int k = 0; k < this.columns_k; k++)
                    this.encrypted_message[i][j] += this.key_values[i][k] * this.message_values[k][j];
            }
        }

        String encrypt_result = " ";
        int length = 0;
        for(int i = 0; i < this.rows_k; i++)
        {
            for(int j = 0; j < this.columns_m; j++)
                encrypt_result += (encrypted_message[i][j]) % 26 + "  "; 

            encrypt_result+= "\n";
        }

        JOptionPane.showMessageDialog(this,"The encrypted message is \n" + encrypt_result, "Result", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel data_length_form()
    {
        JPanel main_panel = new JPanel();
        main_panel.setLayout(new BoxLayout(main_panel,BoxLayout.Y_AXIS));

        JPanel key_panel = new JPanel();

        this.key_rows = new JTextField(7);
        this.key_columns = new JTextField(7); 

        key_panel.setLayout(new BorderLayout());
        JPanel key_values_panel = new JPanel();
        
        key_values_panel.setLayout(new GridBagLayout());
        key_values_panel.add(new JLabel("Rows"));
        key_values_panel.add(this.key_rows);
        key_values_panel.add(new JLabel("Columns"));
        key_values_panel.add(this.key_columns);

        key_panel.add(key_values_panel,BorderLayout.CENTER);

        this.message_rows = new JTextField(7);
        this.message_columns = new JTextField(7);

        JPanel message_panel = new JPanel();

        JPanel message_values_panel = new JPanel();
        message_values_panel.setLayout(new GridBagLayout());
        message_values_panel.add(new JLabel("Rows"));
        message_values_panel.add(this.message_rows);
        message_values_panel.add(new JLabel("Colunms"));
        message_values_panel.add(this.message_columns);

        message_panel.add(message_values_panel,BorderLayout.CENTER);

        JLabel key_label = new JLabel("Key datas");
        key_label.setBackground(Color.WHITE);
        key_label.setForeground(Color.WHITE);

        main_panel.add(key_label);
        main_panel.add(Box.createVerticalStrut(15));
        main_panel.add(key_panel);
        main_panel.add(Box.createVerticalStrut(25));
        main_panel.add(new JSeparator());
        main_panel.add(Box.createVerticalStrut(10));
        
        JLabel msg_label = new JLabel("Message datas");
        msg_label.setBackground(Color.WHITE);
        msg_label.setForeground(Color.WHITE);
        main_panel.add(msg_label);
        main_panel.add(Box.createVerticalStrut(15));
        main_panel.add(message_panel);
        main_panel.setOpaque(true);
        main_panel.setBackground(new Color(255,255,255,(int)0.4 * 255));

        return main_panel;
    }
}
