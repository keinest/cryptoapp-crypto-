package crypto.crypt_analys_brute_force.affine_brute_force;

import crypto.utils.DrawBackground;
import crypto.utils.Util;
import crypto.encryption_decryption.affine.Affine;
import crypto.Main;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.*;
import javax.imageio.*;

public class AffineBruterForce extends JPanel
{
    protected JTextField message;
    private JTextArea resultArea;
    private JProgressBar progressBar;
    private JButton copy;        
    private JButton clear;       

    protected JButton start;
    protected JButton back;
    private Main main_window;

    public AffineBruterForce(Main main_window)
    {
        this.main_window = main_window;
        this.message     = new JTextField();
        this.start       = Main.createStyledButton("Start", new Color(52, 152, 219), Color.WHITE, new Font("SansSerif", Font.PLAIN, 16));
        this.back        = Main.createStyledButton("Back", new Color(52, 154, 220), Color.WHITE, new Font("SansSerif", Font.PLAIN, 16));
        this.copy        = Main.createStyledButton("Copy", new Color(46, 204, 113), Color.WHITE, new Font("SansSerif", Font.PLAIN, 13));
        this.clear       = Main.createStyledButton("Clear", new Color(231, 76, 60), Color.WHITE, new Font("SansSerif", Font.PLAIN, 13));
        this.resultArea  = new JTextArea(8, 40);
        this.progressBar = new JProgressBar();
        this.progressBar.setIndeterminate(false);
        this.resultArea.setEditable(false);
        this.resultArea.setLineWrap(true);
        this.resultArea.setWrapStyleWord(true);

        this.message.setToolTipText("Enter the encrypted message");
        this.start.setToolTipText("Start the brute-force attempt (may take time)");
        this.back.setToolTipText("Back to main menu");

        DrawBackground background = new DrawBackground("crypto/ressources/IMG-20251026-WA0103.jpg");
        
        JPanel btn_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btn_panel.add(this.start);
        btn_panel.add(this.back);
        btn_panel.setOpaque(false);

        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        background.add(Box.createVerticalStrut(10));
        
        JLabel header = new JLabel("Affine Brute-Force", SwingConstants.CENTER);
        header.setFont(new Font("Montserrat", Font.BOLD, 28));
        header.setForeground(new Color(0, 0, 0));
        header.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        background.add(header);
        background.add(Box.createVerticalStrut(25));
        background.add(value_field_content());
        background.add(Box.createVerticalStrut(10));

        background.add(btn_panel);
        background.add(Box.createVerticalStrut(15));
        JPanel resultPanel = new JPanel(new BorderLayout(8, 8));
        resultPanel.setOpaque(false);
        progressBar.setStringPainted(true);
        progressBar.setString("");
        resultPanel.add(this.progressBar, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(this.resultArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setOpaque(false);
        resultPanel.add(sp, BorderLayout.CENTER);

        JPanel actionResultBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionResultBtns.setOpaque(false);
        actionResultBtns.add(this.copy);      
        actionResultBtns.add(this.clear);
        resultPanel.add(actionResultBtns, BorderLayout.SOUTH);

        background.add(resultPanel);

        this.start.addActionListener(e -> 
        {
            this.start.setEnabled(false);
            this.progressBar.setIndeterminate(true);
            this.progressBar.setString("Working...");

            SwingWorker<String, Void> worker = new SwingWorker<>() 
            {
                @Override
                protected String doInBackground() throws Exception 
                {
                    try
                    {
                        Thread.sleep(3000);
                        brute_force_process();
                        AffineBruterForce.this.progressBar.setIndeterminate(false);
                        AffineBruterForce.this.progressBar.setString("Done");
                    }
                    catch(Exception e)
                    {
                        AffineBruterForce.this.resultArea.setText(e.getMessage());
                    }
                    return null;
                }

            };
            worker.execute();
        });

        this.back.addActionListener(e -> main_window.showHome());

        this.copy.addActionListener(e -> 
        {
            String text = AffineBruterForce.this.resultArea.getText().trim();
            if(!text.isEmpty())
                Util.copyText(text);
        });
         
        this.clear.addActionListener(e -> 
        {
            this.resultArea.setText(" ");
            this.message.setText(" ");
            this.start.setEnabled(true);
        });

        this.setLayout(new BorderLayout());
        this.add(background, BorderLayout.CENTER);

    }

    private JPanel value_field_content()
    {
        JPanel content = new JPanel(new GridLayout(1, 1, 10, 10));
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 3, true), 
                BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20),
                "Datas for Brute-force",TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Montserrat", Font.BOLD, 22).deriveFont(Font.BOLD, 24f), new Color(34, 49, 63)
                )
            )
        );

        content.add(new JLabel("Enter the message "));
        content.add(this.message);

        return content;
    }

    private void brute_force_process()
    {
        String message_str = this.message.getText().trim();
        if(message_str.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Enter the message before continue !", "Error", JOptionPane.ERROR_MESSAGE);
            this.start.setEnabled(true);
            return;
        }

        java.util.List<int[]> key_pairs = new java.util.ArrayList<>();
        
        for(int i = 1; i < 26; i++)
        {
            if(Util.isPrime(i, 26))
            {
                for(int j = 0; j < 26; j++)
                    key_pairs.add(new int[]{i, j});
            }
        }
        
        this.resultArea.setText(" ");
        this.progressBar.setMaximum(key_pairs.size());
        this.progressBar.setValue(0);
        
        StringBuilder results = new StringBuilder();
        results.append("Brute-force results for: ").append(message_str).append("\n");
        results.append("=".repeat(70)).append("\n\n");
        
        for(int i = 0; i < key_pairs.size(); i++)
        {
            int[] keys = key_pairs.get(i);
            int a = keys[0];
            int b = keys[1];
            
            long[] uv = new long[2];
            long pgcd = Util.extendEuclide(a, 26, uv);
            long d = uv[0];
            
            if(d < 0)
                d += 26;
            
            String decrypted = decryptMessage(message_str, (int)d, b);
            
            results.append("Key pair (a=").append(a).append(", b=").append(b)
                   .append("): ").append(decrypted).append("\n");
            
            this.progressBar.setValue(i + 1);
            this.resultArea.setText(results.toString());
        }
        
        this.resultArea.append("\n" + "=".repeat(70) + "\n");
        this.resultArea.append("Total key pairs tested: " + key_pairs.size());
    }
    
    private String decryptMessage(String message, int d, int b)
    {
        StringBuilder decrypted = new StringBuilder();
        int length = message.length();
        
        for(int i = 0; i < length; i++)
        {
            char ch = message.charAt(i);
            
            if(!Character.isLetter(ch))
            {
                decrypted.append(ch);
                continue;
            }
            
            if(Character.isUpperCase(ch))
            {
                int charValue = ch - 'A';
                int decryptedValue = (int)((d * (charValue - b)) % 26);
                if(decryptedValue < 0)
                    decryptedValue += 26;
                decrypted.append((char)(decryptedValue + 'A'));
            }
            else
            {
                int charValue = ch - 'a';
                int decryptedValue = (int)((d * (charValue - b)) % 26);
                if(decryptedValue < 0)
                    decryptedValue += 26;
                decrypted.append((char)(decryptedValue + 'a'));
            }
        }
        
        return decrypted.toString();
    }
}
