package crypto.crypt_analys_brute_force.cesar_brute_force;

import crypto.utils.DrawBackground;
import crypto.utils.Util;
import crypto.encryption_decryption.cesar.Cesar;
import crypto.Main;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.border.*;
import javax.imageio.*;


public class CesarBruterForce extends JPanel
{
    protected JTextField message;
    private JTextArea resultArea;
    private JProgressBar progressBar;
    private JButton copy;        
    private JButton clear;       

    protected JButton start;
    protected JButton back;
    private Main main_window;

    public CesarBruterForce(Main main_window)
    {
        this.message     = new JTextField();
        this.resultArea  = new JTextArea();
        this.progressBar = new JProgressBar();
        this.start       = Main.createStyledButton("Start", new Color(52, 152, 219), Color.WHITE, new Font("SansSerif", Font.PLAIN, 16));
        this.back        = Main.createStyledButton("Back", new Color(52, 154, 220), Color.WHITE, new Font("SansSerif", Font.PLAIN, 16));
        this.copy        = Main.createStyledButton("Copy", new Color(46, 204, 113), Color.WHITE, new Font("SansSerif", Font.PLAIN, 13));
        this.clear       = Main.createStyledButton("Clear", new Color(231, 76, 60), Color.WHITE, new Font("SansSerif", Font.PLAIN, 13));
        
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
        
        JLabel header = new JLabel("Cesar Brute-Force", SwingConstants.CENTER);
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
                        CesarBruterForce.this.progressBar.setIndeterminate(false);
                        CesarBruterForce.this.progressBar.setString("Done");
                    }
                    catch(Exception e)
                    {
                        CesarBruterForce.this.resultArea.setText(e.getMessage());
                    }
                    return null;
                }

            };
            worker.execute();
        });

        this.back.addActionListener(e -> main_window.showHome());

        this.copy.addActionListener(e -> 
        {
            String text = CesarBruterForce.this.resultArea.getText().trim();
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

        this.resultArea.setText(" ");
        this.progressBar.setMaximum(message_str.length());
        this.progressBar.setValue(0);
        
        StringBuilder results = new StringBuilder();
        results.append("Brute-force results for: ").append(message_str).append("\n");
        results.append("=".repeat(70)).append("\n\n");
        
        String key_value = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        this.resultArea.append("Result for Cesar Brute force : \n\n");
        this.resultArea.append("=".repeat(70));
        this.resultArea.append("\n\n");

        for(int i = 0; i < key_value.length(); i++)
        {
            this.resultArea.append(String.valueOf(key_value.charAt(i)));
            this.resultArea.append(" : ");

            for(int j = 0; j < message_str.length(); j++)
            {
                if(!Character.isLetter(message_str.charAt(j)))
                {
                    this.resultArea.append(String.valueOf(message_str.charAt(j)));
                    continue;
                }

                int ch = message_str.charAt(j);
                int base = 0;
                int k = key_value.charAt(i);
                
                if(Character.isUpperCase(ch))
                    base = 'A';
                else
                    base = 'a';
                ch = (ch - k) % 26;
                if(ch < 0)
                    ch = (ch + 26) % 26; 
                String c = "" + (char)(ch + base);
                this.resultArea.append(c);
            }    
            this.resultArea.append("\n");
        }
        
        this.resultArea.append("\n" + "=".repeat(70) + "\n");
        this.resultArea.append("Total key pairs tested: " + key_value.length());
    } 
}
