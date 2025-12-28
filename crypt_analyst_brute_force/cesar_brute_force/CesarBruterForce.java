package crypto.crypt_analys_brute_force.cesar_brute_force;

import crypto.crypt_analyst_brute_force.CryptAnalyst;
import crypto.utils.DrawBackground;
import crypto.utils.ThemeManager;
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
    private CryptAnalyst cryptanalyst_window;

    public CesarBruterForce(CryptAnalyst cryptanalyst_window)
    {
        this.cryptanalyst_window = cryptanalyst_window;
        this.message             = createCyberTextField();
        this.resultArea          = createCyberTextArea();
        this.progressBar         = createCyberProgressBar();
        this.start               = Main.createCyberButton("Start Brute Force", ThemeManager.ACCENT_CYAN);
        this.back                = Main.createCyberButton("Back", ThemeManager.ACCENT_BLUE);
        this.copy                = Main.createCyberButton("Copy", ThemeManager.ACCENT_GREEN);
        this.clear               = Main.createCyberButton("Clear", new Color(231, 76, 60));
        
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
        header.setFont(ThemeManager.FONT_TITLE);
        header.setForeground(ThemeManager.ACCENT_CYAN);
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
        sp.setBorder(BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 1));
        sp.getViewport().setBackground(ThemeManager.DARK_BG_TERTIARY);
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
                        Thread.sleep(1000); 
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
                
                @Override
                protected void done() {
                    CesarBruterForce.this.start.setEnabled(true);
                }
            };
            worker.execute();
        });

        this.back.addActionListener(e -> cryptanalyst_window.restoreHome());

        this.copy.addActionListener(e -> 
        {
            String text = CesarBruterForce.this.resultArea.getText().trim();
            if(!text.isEmpty())
                Util.copyText(text);
        });
         
        this.clear.addActionListener(e -> 
        {
            this.resultArea.setText("");
            this.message.setText("");
            this.start.setEnabled(true);
            this.progressBar.setString("");
            this.progressBar.setValue(0);
        });

        this.setLayout(new BorderLayout());
        this.add(background, BorderLayout.CENTER);
    }
    
    private JTextField createCyberTextField() {
        JTextField field = new JTextField();
        field.setFont(ThemeManager.FONT_MONO);
        field.setForeground(ThemeManager.TEXT_PRIMARY);
        field.setBackground(ThemeManager.DARK_BG_TERTIARY);
        field.setCaretColor(ThemeManager.ACCENT_CYAN);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }
    
    private JTextArea createCyberTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(ThemeManager.FONT_MONO);
        area.setForeground(ThemeManager.ACCENT_CYAN);
        area.setBackground(ThemeManager.DARK_BG_TERTIARY);
        area.setCaretColor(ThemeManager.ACCENT_CYAN);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return area;
    }
    
    private JProgressBar createCyberProgressBar() {
        JProgressBar bar = new JProgressBar();
        bar.setStringPainted(true);
        bar.setString("");
        bar.setForeground(ThemeManager.ACCENT_CYAN);
        bar.setBackground(ThemeManager.DARK_BG_SECONDARY);
        bar.setBorder(BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 1));
        return bar;
    }

    private JPanel value_field_content()
    {
        JPanel content = new JPanel(new GridLayout(1, 2, 10, 10));
        
        JLabel label = new JLabel("Encrypted Message:");
        label.setFont(ThemeManager.FONT_MONO_BOLD);
        label.setForeground(ThemeManager.TEXT_PRIMARY);
        
        content.add(label);
        content.add(this.message);
        content.setOpaque(false);
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 3, true), 
                BorderFactory.createTitledBorder(
                    BorderFactory.createEmptyBorder(20, 20, 20, 20),
                    "Brute-force Parameters",
                    TitledBorder.CENTER, 
                    TitledBorder.TOP,
                    ThemeManager.FONT_SUBTITLE,
                    ThemeManager.ACCENT_CYAN
                )
            )
        );

        return content;
    }
    
    private void brute_force_process()
    {
        String message_str = this.message.getText().trim();
        if(message_str.isEmpty())
        {
            JOptionPane.showMessageDialog(this, 
                "<html><div style='color:#ff5555;'>Please enter the message before continuing!</div></html>",
                "Error", JOptionPane.ERROR_MESSAGE);
            this.start.setEnabled(true);
            return;
        }

        this.resultArea.setText("");
        this.progressBar.setMaximum(26);
        this.progressBar.setValue(0);
        
        StringBuilder results = new StringBuilder();
        results.append("Cesar Brute-force results for: ").append(message_str).append("\n");
        results.append("=".repeat(70)).append("\n\n");
        
        for(int shift = 0; shift < 26; shift++)
        {
            StringBuilder decrypted = new StringBuilder();
            for(int i = 0; i < message_str.length(); i++)
            {
                char ch = message_str.charAt(i);
                
                if(Character.isLetter(ch))
                {
                    char base = Character.isUpperCase(ch) ? 'A' : 'a';
                    int originalPosition = ch - base;
                    int newPosition = (originalPosition - shift + 26) % 26;
                    decrypted.append((char)(base + newPosition));
                }
                else
                {
                    decrypted.append(ch);
                }
            }
            
            results.append(String.format("Shift %2d: %s\n", shift, decrypted.toString()));
            
            this.progressBar.setValue(shift + 1);
            this.progressBar.setString(String.format("%d/26", shift + 1));
            this.resultArea.setText(results.toString());
            this.resultArea.setCaretPosition(0);
        }
        
        this.resultArea.append("\n" + "=".repeat(70) + "\n");
        this.resultArea.append("Total shifts tested: 26");
        this.start.setEnabled(true);
    } 
}
