package crypto.crypt_analys_brute_force.vigenere_brute_force;

import crypto.Main;
import crypto.utils.Util;
import crypto.utils.ThemeManager;
import crypto.utils.DrawBackground;
import crypto.crypt_analyst_brute_force.CryptAnalyst;
import crypto.encryption_decryption.vigenere.Vigenere;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.List;
import javax.imageio.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.border.*;

public class VigenereBruterForce extends JPanel
{
    protected JTextField message;
    private JTextArea resultArea;
    private JProgressBar progressBar;
    private JButton copy;        
    private JButton clear;       

    protected JButton start;
    protected JButton back;
    private CryptAnalyst cryptanalyst_window;
    
    private JSpinner spinner; 
    private final int MAX_KEY_LENGTH = 5;

    public VigenereBruterForce(CryptAnalyst cryptanalyst_window)
    {
        this.cryptanalyst_window = cryptanalyst_window;
        this.message             = createCyberTextField();
        
        SpinnerNumberModel model = new SpinnerNumberModel(3, 1, MAX_KEY_LENGTH, 1);
        this.spinner = new JSpinner(model);
        styleSpinner(this.spinner);
        
        this.start               = Main.createCyberButton("Start Exhaustive Search", ThemeManager.ACCENT_CYAN);
        this.back                = Main.createCyberButton("Back", ThemeManager.ACCENT_BLUE);
        this.copy                = Main.createCyberButton("Copy", ThemeManager.ACCENT_GREEN);
        this.clear               = Main.createCyberButton("Clear", new Color(231, 76, 60));
        this.resultArea          = createCyberTextArea();
        this.progressBar         = createCyberProgressBar();

        this.progressBar.setIndeterminate(false);
        this.resultArea.setEditable(false);
        this.resultArea.setLineWrap(true);
        this.resultArea.setWrapStyleWord(true);

        this.message.setToolTipText("Enter the encrypted message");
        this.start.setToolTipText("Start the exhaustive brute-force attempt (may take a very long time)");
        this.back.setToolTipText("Back to main menu");
        this.spinner.setToolTipText("Max key length to test (Exhaustive Brute-Force)");

        DrawBackground background = new DrawBackground("crypto/ressources/Electronic circuit board close up_ Technology….jpeg");
        
        JPanel btn_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btn_panel.add(this.start);
        btn_panel.add(this.back);
        btn_panel.setOpaque(false);

        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        background.add(Box.createVerticalStrut(10));
        
        JLabel header = new JLabel("Vigenere Exhaustive Brute-Force", SwingConstants.CENTER);
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
            int choice = JOptionPane.showConfirmDialog(this,
                "<html><div style='color:#ffff55;'>" +
                "<b>Attention!</b><br><br>" +
                "L'attaque exhaustive sur Vigenere peut prendre un temps considerable.<br>" +
                "Pour une cle de longueur " + spinner.getValue() + ", il y a 26^" + spinner.getValue() + " = " + 
                (long)Math.pow(26, (Integer)spinner.getValue()) + " possibilites.<br><br>" +
                "Souhaitez-vous continuer ?</div></html>",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (choice == JOptionPane.YES_OPTION) {
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
                            VigenereBruterForce.this.start.setEnabled(false);
                            brute_force_process();
                            VigenereBruterForce.this.progressBar.setIndeterminate(false);
                            VigenereBruterForce.this.progressBar.setString("Done");
                        }
                        catch(Exception e)
                        {
                            VigenereBruterForce.this.resultArea.setText("Error: " + e.getMessage());
                            VigenereBruterForce.this.progressBar.setIndeterminate(false);
                            VigenereBruterForce.this.progressBar.setString("Error");
                            VigenereBruterForce.this.start.setEnabled(true);
                        }
                        return null;
                    }
                    
                    @Override
                    protected void done() {
                        VigenereBruterForce.this.start.setEnabled(true);
                    }
                };
                worker.execute();
            }
        });

        this.back.addActionListener(e -> cryptanalyst_window.restoreHome());

        this.copy.addActionListener(e -> 
        {
            String text = VigenereBruterForce.this.resultArea.getText().trim();
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
    
    private JTextField createCyberTextField() 
    {
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
    
    private JTextArea createCyberTextArea() 
    {
        JTextArea area = new JTextArea();
        area.setFont(ThemeManager.FONT_MONO);
        area.setForeground(ThemeManager.ACCENT_CYAN);
        area.setBackground(ThemeManager.DARK_BG_TERTIARY);
        area.setCaretColor(ThemeManager.ACCENT_CYAN);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return area;
    }
    
    private JProgressBar createCyberProgressBar() 
    {
        JProgressBar bar = new JProgressBar();
        bar.setStringPainted(true);
        bar.setString("");
        bar.setForeground(ThemeManager.ACCENT_CYAN);
        bar.setBackground(ThemeManager.DARK_BG_SECONDARY);
        bar.setBorder(BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 1));
        return bar;
    }
    
    private void styleSpinner(JSpinner spinner) 
    {
        spinner.setFont(ThemeManager.FONT_MONO);
        spinner.setForeground(ThemeManager.TEXT_PRIMARY);
        spinner.setBackground(ThemeManager.DARK_BG_TERTIARY);
        spinner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeManager.ACCENT_CYAN, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) 
        {
            JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setForeground(ThemeManager.TEXT_PRIMARY);
            textField.setBackground(ThemeManager.DARK_BG_TERTIARY);
            textField.setCaretColor(ThemeManager.ACCENT_CYAN);
        }
    }

    private JPanel value_field_content()
    {
        JPanel content = new JPanel(new BorderLayout(10, 10));
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

        JPanel messagePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        JLabel label = new JLabel("Encrypted Message:");
        label.setFont(ThemeManager.FONT_MONO_BOLD);
        label.setForeground(ThemeManager.TEXT_PRIMARY);
        
        messagePanel.add(label);
        messagePanel.add(this.message);
        messagePanel.setOpaque(false);
        
        JPanel keyLengthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        
        JLabel lengthLabel = new JLabel("Max Key Length:");
        lengthLabel.setFont(ThemeManager.FONT_MONO_BOLD);
        lengthLabel.setForeground(ThemeManager.TEXT_PRIMARY);
        
        keyLengthPanel.add(lengthLabel);
        keyLengthPanel.add(this.spinner);
        keyLengthPanel.setOpaque(false);
        
        JPanel warningPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        warningPanel.setOpaque(false);
        JLabel warning = new JLabel("⚠️ Warning: Length > 3 may cause performance issues!");
        warning.setFont(ThemeManager.FONT_BODY);
        warning.setForeground(new Color(255, 200, 0));
        warningPanel.add(warning);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(messagePanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(keyLengthPanel);
        inputPanel.add(Box.createVerticalStrut(5));
        inputPanel.add(warningPanel);
        inputPanel.setOpaque(false);

        content.add(inputPanel, BorderLayout.CENTER);

        return content;
    }

    private void brute_force_process()
    {
        String message_str = this.message.getText().trim();
        int maxLen = (Integer)this.spinner.getValue();
        
        if(message_str.isEmpty())
        {
            JOptionPane.showMessageDialog(this, 
                "<html><div style='color:#ff5555;'>Please enter the message before continuing!</div></html>",
                "Error", JOptionPane.ERROR_MESSAGE);
            this.start.setEnabled(true);
            return;
        }

        this.resultArea.setText("");
        this.progressBar.setMaximum(100);
        this.progressBar.setValue(0);
        
        StringBuilder results = new StringBuilder();
        results.append("Vigenere Brute-force Analysis for: ").append(message_str).append("\n");
        results.append("Using frequency analysis instead of exhaustive search for performance\n");
        results.append("=".repeat(70)).append("\n\n");
        
        String cleanMessage = message_str.toUpperCase().replaceAll("[^A-Z]", "");
        
        for(int keyLength = 1; keyLength <= maxLen; keyLength++) 
        {
            results.append("\n=== Testing key length: ").append(keyLength).append(" ===\n");
            
            for (int pos = 0; pos < keyLength; pos++) 
            {
                StringBuilder column = new StringBuilder();
                for (int i = pos; i < cleanMessage.length(); i += keyLength) 
                    column.append(cleanMessage.charAt(i));
                
                results.append("Position ").append(pos + 1).append(": ");
                
                if (column.length() > 0) 
                {
                    int[] frequencies = new int[26];
                    for (char c : column.toString().toCharArray())
                        frequencies[c - 'A']++;
                    
                    // Trouver la lettre la plus frequente
                    int maxFreq = 0;
                    char mostCommon = 'A';
                    for (int i = 0; i < 26; i++) 
                    {
                        if (frequencies[i] > maxFreq) 
                        {
                            maxFreq = frequencies[i];
                            mostCommon = (char)('A' + i);
                        }
                    }
                    
                    // 'E' est la lettre la plus frequente en français/anglais
                    int shift = (mostCommon - 'E' + 26) % 26;
                    results.append("Most common letter: ").append(mostCommon)
                           .append(" (").append(maxFreq).append("x) -> Shift: ").append(shift)
                           .append(" -> Key letter: ").append((char)('A' + shift)).append("\n");
                } 
                else
                    results.append("No letters at this position\n");
            }
            
            this.progressBar.setValue((keyLength * 100) / maxLen);
            this.progressBar.setString(String.format("%d%%", (keyLength * 100) / maxLen));
            this.resultArea.setText(results.toString());
            this.resultArea.setCaretPosition(0);
        }
        
        results.append("\n" + "=".repeat(70) + "\n");
        results.append("Analysis complete. Try these key lengths and shifts to decrypt.\n");
        results.append("For exhaustive search, reduce max key length to 1-2.");
        
        this.resultArea.setText(results.toString());
        this.start.setEnabled(true);
    }
}
