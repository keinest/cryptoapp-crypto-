package crypto.crypt_analys_brute_force.rsa_brute_force;

import crypto.crypt_analyst_brute_force.CryptAnalyst;
import crypto.utils.DrawBackground;
import crypto.utils.ThemeManager;
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
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.*;
import javax.imageio.*;

public class RSABruterForce extends JPanel
{
    protected JTextField n_field;
    protected JTextField e_field;
    protected JTextField message;
    private JTextArea resultArea;
    private JProgressBar progressBar;
    private JButton copy;
    private JButton clear;

    protected JButton start;
    protected JButton back;
    private CryptAnalyst cryptanalyst_window;

    public RSABruterForce(CryptAnalyst cryptanalyst_window)
    {
        this.cryptanalyst_window = cryptanalyst_window;
        this.n_field             = createCyberTextField();
        this.e_field             = createCyberTextField();
        this.message             = createCyberTextField();
        this.start               = Main.createCyberButton("Start Brute Force", ThemeManager.ACCENT_CYAN);
        this.back                = Main.createCyberButton("Back", ThemeManager.ACCENT_BLUE);
        this.copy                = Main.createCyberButton("Copy", ThemeManager.ACCENT_GREEN);
        this.clear               = Main.createCyberButton("Clear", new Color(231, 76, 60));
        this.resultArea          = createCyberTextArea();
        this.progressBar         = createCyberProgressBar();

        this.progressBar.setIndeterminate(false);
        this.resultArea.setEditable(false);
        this.resultArea.setLineWrap(true);
        this.resultArea.setWrapStyleWord(true);

        this.n_field.setToolTipText("Enter the public modulus N");
        this.e_field.setToolTipText("Enter the public exponent E");
        this.message.setToolTipText("Enter the encrypted message");
        this.start.setToolTipText("Start the brute-force attempt (may take time)");
        this.back.setToolTipText("Back to main menu");

        DrawBackground background = new DrawBackground("crypto/ressources/avrotbvba.png");

        JPanel btn_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btn_panel.add(this.start);
        btn_panel.add(this.back);
        btn_panel.setOpaque(false);

        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        background.add(Box.createVerticalStrut(10));

        JLabel header = new JLabel("RSA Brute-Force", SwingConstants.CENTER);
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

            SwingWorker<Void, Void> worker = new SwingWorker<>()
            {
                @Override
                protected Void doInBackground() throws Exception
                {
                    try
                    {
                        brute_force_process();
                        RSABruterForce.this.progressBar.setIndeterminate(false);
                        RSABruterForce.this.progressBar.setString("Done");
                    }
                    catch(Exception ex)
                    {
                        RSABruterForce.this.resultArea.setText(ex.getMessage());
                    }
                    return null;
                }
                
                @Override
                protected void done() {
                    RSABruterForce.this.start.setEnabled(true);
                }
            };
            worker.execute();
        });

        this.back.addActionListener(e -> cryptanalyst_window.restoreHome());

        this.copy.addActionListener(e ->
        {
            String text = RSABruterForce.this.resultArea.getText().trim();
            if(!text.isEmpty())
            {
                Util.copyText(text);
                JOptionPane.showMessageDialog(this, 
                    "<html><div style='color:#00ff00;'>Text copied to clipboard!</div></html>",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        this.clear.addActionListener(e ->
        {
            this.resultArea.setText("");
            this.n_field.setText("");
            this.e_field.setText("");
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
        
        bar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() 
        {
            @Override
            protected Color getSelectionBackground() { return ThemeManager.TEXT_PRIMARY; }
            @Override
            protected Color getSelectionForeground() { return ThemeManager.DARK_BG_PRIMARY; }
        });
        
        return bar;
    }

    private JPanel value_field_content()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        
        JLabel nLabel = new JLabel("Public Modulus (n):");
        nLabel.setFont(ThemeManager.FONT_MONO_BOLD);
        nLabel.setForeground(ThemeManager.TEXT_PRIMARY);
        panel.add(nLabel);
        panel.add(this.n_field);
        
        JLabel eLabel = new JLabel("Public Exponent (e):");
        eLabel.setFont(ThemeManager.FONT_MONO_BOLD);
        eLabel.setForeground(ThemeManager.TEXT_PRIMARY);
        panel.add(eLabel);
        panel.add(this.e_field);
        
        JLabel msgLabel = new JLabel("Encrypted Message:");
        msgLabel.setFont(ThemeManager.FONT_MONO_BOLD);
        msgLabel.setForeground(ThemeManager.TEXT_PRIMARY);
        panel.add(msgLabel);
        panel.add(this.message);
        
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
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

        return panel;
    }

    private void brute_force_process()
    {
        String n_string = this.n_field.getText().trim();
        String e_string = this.e_field.getText().trim();
        String msg = this.message.getText().trim();

        if(n_string.isEmpty() || e_string.isEmpty() || msg.isEmpty())
        {
            JOptionPane.showMessageDialog(this, 
                "<html><div style='color:#ff5555;'>Please enter all data before continuing!</div></html>",
                "Error", JOptionPane.ERROR_MESSAGE);
            this.start.setEnabled(true);
            return;
        }

        if(!Util.veri_user_enter(n_string) || !Util.veri_user_enter(e_string))
        {
            JOptionPane.showMessageDialog(this, 
                "<html><div style='color:#ffff55;'>Values must be integers. Please check and retry!</div></html>",
                "Warning", JOptionPane.WARNING_MESSAGE);
            this.start.setEnabled(true);
            return;
        }

        int n = Util.convert_user_enter(n_string);
        int e = Util.convert_user_enter(e_string);

        List<int[]> p_q_pairs = new ArrayList<>();

        for(int i = 2; i * i <= n; i++)
        {
            if(n % i == 0)
            {
                int p = i;
                int q = n / i;

                if(i == q)
                    p_q_pairs.add(new int[]{p, q});
                else
                {
                    p_q_pairs.add(new int[]{p, q});
                    p_q_pairs.add(new int[]{q, p});
                }
            }
        }
        
        List<int[]> distinct_p_q_pairs = new ArrayList<>();
        for(int[] pair : p_q_pairs) 
        {
            boolean found = false;
            for(int[] distinctPair : distinct_p_q_pairs) 
            {
                if ((distinctPair[0] == pair[0] && distinctPair[1] == pair[1]) ||
                    (distinctPair[0] == pair[1] && distinctPair[1] == pair[0])) 
                {
                    found = true;
                    break;
                }
            }
            if(!found)
                distinct_p_q_pairs.add(pair);
        }

        if(distinct_p_q_pairs.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, 
                "<html><div style='color:#ffff55;'>N is not a product of two small factors in the search range!</div></html>",
                "Warning", JOptionPane.WARNING_MESSAGE);
            this.start.setEnabled(true);
            return;
        }

        this.resultArea.setText("");
        this.progressBar.setMaximum(distinct_p_q_pairs.size());
        this.progressBar.setValue(0);

        StringBuilder results = new StringBuilder();
        results.append("Brute-force results for N=").append(n).append(", E=").append(e).append("\n");
        results.append("Encrypted Message: ").append(msg).append("\n");
        results.append("=".repeat(70)).append("\n\n");

        for(int i = 0; i < distinct_p_q_pairs.size(); i++)
        {
            int[] pair = distinct_p_q_pairs.get(i);
            int p = pair[0];
            int q = pair[1];

            int phi = (p - 1) * (q - 1);

            long[] uv = new long[2];
            long pgcd = Util.extendEuclide(e, phi, uv);

            if(pgcd != 1) 
            {
                results.append("Key pair (p=").append(p).append(", q=").append(q).append("): E is not coprime to phi=").append(phi).append(". Skipping.\n");
                this.progressBar.setValue(i + 1);
                this.progressBar.setString(String.format("%d/%d", i + 1, distinct_p_q_pairs.size()));
                this.resultArea.setText(results.toString());
                continue;
            }

            long d_long = uv[0];
            int d = (int)(d_long % phi);
            if(d < 0)
                d += phi;

            String decrypted = decryptMessage(msg, d, n);

            results.append("Key pair (p=").append(p).append(", q=").append(q)
                   .append(", D=").append(d).append("): ").append(decrypted).append("\n");

            this.progressBar.setValue(i + 1);
            this.progressBar.setString(String.format("%d/%d", i + 1, distinct_p_q_pairs.size()));
            this.resultArea.setText(results.toString());
        }

        this.resultArea.append("\n" + "=".repeat(70) + "\n");
        this.resultArea.append("Total factor pairs (p, q) tested: " + distinct_p_q_pairs.size());
        this.start.setEnabled(true);
    }

    private String decryptMessage(String message, int d, int n)
    {
        StringBuilder decrypted = new StringBuilder();
        String[] cipherBlocks   = message.trim().split("\\s+");

        for(String block : cipherBlocks)
        {
            if(block.isEmpty()) continue;
            try 
            {
                long cipherValue = Long.parseLong(block);
                int clear_msg_int = RsaDecrypt.rsa_decrypt_mod_26((int)cipherValue, d, n);

                int clear_char_value = clear_msg_int % 26;
                decrypted.append((char)(clear_char_value + 'A'));

            } 
            catch (NumberFormatException e){decrypted.append("?");}
        }
        return decrypted.toString();
    }
}
