package crypto.encryption_decryption.feistel;

import crypto.encryption_decryption.feistel.Feistel;
import crypto.utils.DrawBackground;
import crypto.utils.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class FeistelEncrypt extends JPanel
{
    protected Feistel fwindow;
    protected JButton back;
    protected JButton process;
    protected DrawBackground background;

    protected List<Integer> message; 
    protected List<Integer> key;

    protected JTextField message_field; 
    protected JTextField key_field;
    protected int round;
    protected JTextField round_number;

    protected List<Integer> encrypted_message;

    public FeistelEncrypt(Feistel fwindow)
    {
        this.fwindow = fwindow;
        this.background = new DrawBackground("crypto/ressources/app.png");
        this.process = new JButton("Start");
        this.message = new ArrayList<>();
        this.key     = new ArrayList<>();

        this.background.setLayout(new GridBagLayout());

        this.back = new JButton("Back");
        this.back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                FeistelEncrypt.this.fwindow.restoreWindow();
            }
        });

        this.back.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                FeistelEncrypt.this.back.setBackground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                FeistelEncrypt.this.back.setBackground(Color.WHITE);
            }
        });

        this.process.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                FeistelEncrypt.this.encryption();
            }
        });

        this.background.add(this.back);
        this.background.add(encryption_datas());
        this.background.add(this.process);

        this.setLayout(new BorderLayout());
        this.add(this.background, BorderLayout.CENTER);

    }

    private JPanel encryption_datas()
    {
        JPanel content_panel = new JPanel();

        content_panel.setLayout(new GridLayout(3,2,10,10)); 
        content_panel.setOpaque(true);
        content_panel.setBackground(new Color(255,255,255,128));

        this.message_field = new JTextField(15);
        this.key_field     = new JTextField(15);
        this.round_number  = new JTextField(15);

        content_panel.add(new JLabel("Message"));
        content_panel.add(this.message_field);
        content_panel.add(new JLabel("Key"));
        content_panel.add(this.key_field);
        content_panel.add(new JLabel("Round(s)"));
        content_panel.add(this.round_number);

        return content_panel;
    }

    private void getValue()
    {
        String msg     = this.message_field.getText();
        String key_str = this.key_field.getText(); 

        if(msg.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"The message is empty","Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(key_str.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"The key is empty","Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.message.addAll(Util.convert_to_bin(msg));
        this.key.addAll(Util.convert_to_bin(key_str));
    }

    private void split_into_blocks(List<Integer> message,List<Integer> left_block,List<Integer> rigth_block)
    {
        if(message.size() % 2 != 0) 
        {
            JOptionPane.showMessageDialog(this, "The message size is not even. Adding a zero padding bit.", "Attention", JOptionPane.WARNING_MESSAGE);
            message.add(0, 0); 
        }

        int half_size = message.size() / 2;

        for(int i = 0; i < half_size; i++)
            left_block.add(message.get(i));

        for(int i = half_size; i < message.size(); i++)
            rigth_block.add(message.get(i));

    }

    
    private List<Integer> encrypt_process(List<Integer> message, List<Integer> key, String msg_operator, String key_operator)
    {
        List<Integer> left_block  = new ArrayList<>();
        List<Integer> rigth_block = new ArrayList<>();
        
        split_into_blocks(message,left_block,rigth_block);

        List<Integer> current_key = new ArrayList<>(key);
        if(rigth_block.size() < current_key.size())
            current_key = current_key.subList(0, rigth_block.size());

        else if(rigth_block.size() > current_key.size())
        {
            for(int i = current_key.size(); i < rigth_block.size(); i++)
                current_key.add(0,0);
        }
        
        // ----------------------------------------------------------------------------------------

        List<Integer> temp_block = new ArrayList<>();
        
        if(key_operator.equalsIgnoreCase("OR"))
        {
            for(int i = 0; i < current_key.size(); i++)
                temp_block.add(rigth_block.get(i) | current_key.get(i));
        }
        else if(key_operator.equalsIgnoreCase("AND"))
        {
            for(int i = 0; i < current_key.size(); i++)
                temp_block.add(rigth_block.get(i) & current_key.get(i));
        }
        else if(key_operator.equalsIgnoreCase("XOR"))
        {
            for(int i = 0; i < current_key.size(); i++)
                temp_block.add(rigth_block.get(i) ^ current_key.get(i));
        }
        else if(key_operator.equalsIgnoreCase("CMPL"))
        {
            for(int i = 0; i < current_key.size(); i++)
                temp_block.add(rigth_block.get(i) | (1 - current_key.get(i)));
        }
        else
        {
            JOptionPane.showMessageDialog(this,"Operator " + "\"" + key_operator + "\"" + " not found","Not Found",JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // ----------------------------------------------------------------------------------------

        if(msg_operator.equalsIgnoreCase("OR"))
        {
            for(int i = 0; i < temp_block.size(); i++)
                left_block.set(i, left_block.get(i) | temp_block.get(i));        
        }
        else if(msg_operator.equalsIgnoreCase("AND"))
        {
            for(int i = 0; i < temp_block.size(); i++)
                left_block.set(i, left_block.get(i) & temp_block.get(i));
        }
        else if(msg_operator.equalsIgnoreCase("XOR"))
        {
            for(int i = 0; i < temp_block.size(); i++)
                left_block.set(i, left_block.get(i) ^ temp_block.get(i));
        }
        else if(msg_operator.equalsIgnoreCase("CMPL"))
        {
            for(int i = 0; i < temp_block.size(); i++)
                left_block.set(i, left_block.get(i) | (1 - temp_block.get(i)));
        }
        else
        {
            JOptionPane.showMessageDialog(this,"Operator " + "\"" + msg_operator + "\"" + " not found");
            return null;
        }

        // ----------------------------------------------------------------------------------------

        List<Integer> next_round_msg = new ArrayList<>();

        for(int bit : rigth_block)
            next_round_msg.add(bit); 

        for(int bit : left_block)
            next_round_msg.add(bit); 
        
        return next_round_msg;
    }

    private void encryption()
    {
        FeistelEncrypt.this.back.setText("Cancel");
        FeistelEncrypt.this.message.clear();
        FeistelEncrypt.this.key.clear();
        FeistelEncrypt.this.getValue();

        try{FeistelEncrypt.this.round = Integer.parseInt(FeistelEncrypt.this.round_number.getText());}
                
        catch (NumberFormatException ex) 
        {
            JOptionPane.showMessageDialog(FeistelEncrypt.this, "The number of round(s) must be an integer!\nCheck and retry !", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
                
        if(FeistelEncrypt.this.message.isEmpty() || FeistelEncrypt.this.key.isEmpty())
            return; 

        List<Integer> current_message = new ArrayList<>(FeistelEncrypt.this.message);

        for(int i = 0; i < FeistelEncrypt.this.round; i++)
        {
            this.background.removeAll();
                    
            JPanel output_panel = new JPanel();
            output_panel.setLayout(new GridLayout(2,2,10,10));
            JTextField output_field = new JTextField(15);
            JTextField key_field    = new JTextField(15);
                    
            output_panel.add(new JLabel("Message Operator"));
            output_panel.add(output_field);
            output_panel.add(new JLabel("Function Operator"));
            output_panel.add(key_field);
            output_panel.setOpaque(true);
            output_panel.setBackground(new Color(255,255,255,128));

            JPanel box_panel = new JPanel();
            box_panel.setLayout(new BoxLayout(box_panel,BoxLayout.Y_AXIS));
            box_panel.add(output_panel);
            box_panel.add(Box.createVerticalStrut(15));
            box_panel.add(FeistelEncrypt.this.back);

            this.background.add(box_panel);
            this.background.revalidate();
            this.background.repaint();

            JOptionPane.showMessageDialog(FeistelEncrypt.this,output_panel,"Round " + (i + 1),JOptionPane.INFORMATION_MESSAGE);
            current_message = FeistelEncrypt.this.encrypt_process(
                current_message, 
                FeistelEncrypt.this.key,
                output_field.getText().trim(),
                key_field.getText().trim()
            );
                    
            if(current_message == null)
                return;
            
            if(i == FeistelEncrypt.this.round - 1)
                FeistelEncrypt.this.encrypted_message = current_message;
        }

        FeistelEncrypt.this.back.setText("Back");
        JOptionPane.showMessageDialog(
            FeistelEncrypt.this,
            "Encrypted Message: " + Util.convert_to_string(FeistelEncrypt.this.encrypted_message),
            "Result",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}