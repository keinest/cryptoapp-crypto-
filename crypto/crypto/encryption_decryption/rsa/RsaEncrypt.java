package crypto.encryption_decryption.rsa;

import crypto.utils.Util;
import crypto.utils.DrawBackground;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.concurrent.ExecutionException;

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
    protected JTextArea  message_area;
    protected JTextArea  result_area;
    protected JTextField pfield;
    protected JTextField qfield;
    protected JButton    back;
    protected RSA       rsaWindow;
    protected JProgressBar progressBar;
    
    protected JButton    copy;        
    protected JButton    clear;       

    public RsaEncrypt(RSA rsaWindow)
    {
        this.rsaWindow     = rsaWindow;
        
        this.pfield        = new JTextField();
        this.qfield        = new JTextField();
        this.keyMessage    = new JTextField();
        this.message_area  = new JTextArea(3, 20);
        this.result_area   = new JTextArea(8, 40);
        this.progressBar   = new JProgressBar();

        this.encrypt = createStyledButton("Start", new Color(52, 152, 219), Color.WHITE, new Font("SansSerif", Font.PLAIN, 16));
        this.back    = createStyledButton("Back", new Color(52, 154, 220), Color.WHITE, new Font("SansSerif", Font.PLAIN, 16));
        this.copy    = createStyledButton("Copy", new Color(46, 204, 113), Color.WHITE, new Font("SansSerif", Font.PLAIN, 13));
        this.clear   = createStyledButton("Clear", new Color(231, 76, 60), Color.WHITE, new Font("SansSerif", Font.PLAIN, 13));

        this.progressBar.setIndeterminate(false);
        this.result_area.setEditable(false);
        this.result_area.setLineWrap(true);
        this.result_area.setWrapStyleWord(true);
        this.message_area.setLineWrap(true);
        this.message_area.setWrapStyleWord(true);
        
        this.pfield.setToolTipText("Enter the first prime number (p)");
        this.qfield.setToolTipText("Enter the second prime number (q)");
        this.keyMessage.setToolTipText("Enter the encryption key (e)");
        this.encrypt.setToolTipText("Start RSA encryption");
        this.back.setToolTipText("Back to RSA main menu");

        
        DrawBackground background = new DrawBackground("crypto/ressources/IMG-20251026-WA0103.jpg");
        
        JPanel btn_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btn_panel.add(this.encrypt);
        btn_panel.add(this.back);
        btn_panel.setOpaque(false);

        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        background.add(Box.createVerticalStrut(10));
        
        JLabel header = new JLabel("RSA String Encryption", SwingConstants.CENTER);
        header.setFont(new Font("Montserrat", Font.BOLD, 28));
        header.setForeground(new Color(0, 0, 0));
        header.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        background.add(header);
        background.add(Box.createVerticalStrut(25));
        
        background.add(rsa_form());
        background.add(Box.createVerticalStrut(10));

        background.add(btn_panel);
        background.add(Box.createVerticalStrut(15));
        
        JPanel resultPanel = new JPanel(new BorderLayout(8, 8));
        resultPanel.setOpaque(false);
        
        progressBar.setStringPainted(true);
        progressBar.setString("0%");
        resultPanel.add(this.progressBar, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(this.result_area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setOpaque(false);
        resultPanel.add(sp, BorderLayout.CENTER);

        JPanel actionResultBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionResultBtns.setOpaque(false);
        actionResultBtns.add(this.copy);      
        actionResultBtns.add(this.clear);
        resultPanel.add(actionResultBtns, BorderLayout.SOUTH);

        background.add(resultPanel);
        
        background.add(Box.createVerticalGlue());

        this.encrypt.addActionListener(e -> rsa_encryption_condition());

        this.back.addActionListener(e -> rsaWindow.restoreWindow());

        this.copy.addActionListener(e -> 
        {
            String text = RsaEncrypt.this.result_area.getText().trim();
            if(!text.isEmpty())
                Util.copyText(text);
        });
         
        this.clear.addActionListener(e -> 
        {
            this.result_area.setText("");
            this.message_area.setText("");
            this.pfield.setText("");
            this.qfield.setText("");
            this.keyMessage.setText("");
            this.progressBar.setValue(0);
            this.progressBar.setString("0%");
            this.encrypt.setEnabled(true);
        });

        this.setLayout(new BorderLayout());
        this.add(background, BorderLayout.CENTER);
    }
    
    private JButton createStyledButton(String text, Color background, Color foreground, Font font) 
    {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private int encryptAndReduce(int m, int e, int module)
    {
        int intermediateResult = Util.power(m, e) % module; 
        return intermediateResult % 26;
    }

    private JPanel rsa_form()
    {
        JPanel rsa_panel = new JPanel(new GridLayout(4, 2, 10, 10));
        rsa_panel.setOpaque(true);
        rsa_panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 3, true), 
                BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15),
                "RSA Parameters and Message",TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Montserrat", Font.BOLD, 22).deriveFont(Font.BOLD, 20f), new Color(34, 49, 63)
                )
            )
        );

        rsa_panel.add(new JLabel("Enter the first prime (p) "));
        rsa_panel.add(pfield);

        rsa_panel.add(new JLabel("Enter the second prime (q) "));
        rsa_panel.add(qfield);

        rsa_panel.add(new JLabel("Enter the encryption key (e) "));
        rsa_panel.add(keyMessage);

        rsa_panel.add(new JLabel("Enter the message (string) "));
        JScrollPane messageScrollPane = new JScrollPane(message_area);
        messageScrollPane.setPreferredSize(new Dimension(200, 60)); 
        rsa_panel.add(messageScrollPane);
        
        return rsa_panel;
    }

    private void rsa_encryption_condition()
    {
        RsaEncrypt.this.p       = pfield.getText().trim();
        RsaEncrypt.this.q       = qfield.getText().trim();
        RsaEncrypt.this.key     = keyMessage.getText().trim();
        RsaEncrypt.this.message = message_area.getText();
        
        result_area.setText("");
        progressBar.setValue(0);
        progressBar.setString("0%");
        encrypt.setEnabled(false);

        if(p.isEmpty() || q.isEmpty() || key.isEmpty() || message.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Empty value ! Check and retry","Error",JOptionPane.ERROR_MESSAGE);
            encrypt.setEnabled(true);
            return;
        }

        if(!Util.veri_user_enter(p) || !Util.veri_user_enter(q) || !Util.veri_user_enter(key))
        {
            JOptionPane.showMessageDialog(this,"p, q, and key must be integers ! Check and retry","Error",JOptionPane.ERROR_MESSAGE);
            encrypt.setEnabled(true);
            return;
        }

        int p_int       = 0;
        int q_int       = 0;
        int key_int     = 0;

        try {
            p_int           = Util.convert_user_enter(p);
            q_int           = Util.convert_user_enter(q);
            key_int         = Util.convert_user_enter(key);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Conversion error in p, q, or key. Ensure they are valid integers.", "Error", JOptionPane.ERROR_MESSAGE);
            encrypt.setEnabled(true);
            return;
        }

        if(!Util.isPrime(p_int,q_int))
        {
            JOptionPane.showMessageDialog(this,"p and q should be distinct prime numbers !","Error",JOptionPane.ERROR_MESSAGE);
            encrypt.setEnabled(true);
            return;
        }

        RsaEncrypt.this.module = p_int * q_int;
        RsaEncrypt.this.phi    = (p_int - 1) * (q_int - 1);
        
        if(!Util.isPrime(RsaEncrypt.this.phi,key_int))
        {
            JOptionPane.showMessageDialog(this,"The Euler function (phi) and encryption key (e) have to be coprime !\nCheck and retry !","Error",JOptionPane.ERROR_MESSAGE);
            encrypt.setEnabled(true);
            return;
        }
        
        if (RsaEncrypt.this.module <= 26) 
        {
            JOptionPane.showMessageDialog(this, "The modulus N (p*q = " + RsaEncrypt.this.module + ") must be greater than 26 for the base encryption step.", "Error", JOptionPane.ERROR_MESSAGE);
            encrypt.setEnabled(true);
            return;
        }

        RsaEncryptWorker worker = new RsaEncryptWorker(message, key_int, RsaEncrypt.this.module);
        worker.execute();
    }

    private class RsaEncryptWorker extends SwingWorker<String, Integer> 
    {
        
        private final String message;
        private final int key_int;
        private final int module;

        public RsaEncryptWorker(String message, int key_int, int module) 
        {
            this.message = message;
            this.key_int = key_int;
            this.module  = module;
        }

        @Override
        protected String doInBackground() throws Exception 
        {
            StringBuilder encryptedResult = new StringBuilder();
            
            String upperCaseMessage = message.toUpperCase();
            int totalLength = upperCaseMessage.length();
            int charsProcessed = 0;

            for (int i = 0; i < totalLength; i++) 
            {
                char character = upperCaseMessage.charAt(i);
                
                if (character >= 'A' && character <= 'Z') 
                {
                    int char_index = character - 'A';
                    int encrypted_index = encryptAndReduce(char_index, key_int, module);
                    char encrypted_char = (char) (encrypted_index + 'A');
                    encryptedResult.append(encrypted_char);
                    charsProcessed++;
                    int progress = (int) (((double) charsProcessed / totalLength) * 100);
                    publish(progress);
                }
            }
            return encryptedResult.toString();
        }

        @Override
        protected void process(java.util.List<Integer> chunks) 
        {
            if (!chunks.isEmpty()) 
            {
                int progress = chunks.get(chunks.size() - 1);
                progressBar.setValue(progress);
                progressBar.setString(progress + "%");
            }
        }

        @Override
        protected void done() 
        {
            encrypt.setEnabled(true);
            try 
            {
                String result = get();
                result_area.setText(result);
                JOptionPane.showMessageDialog(RsaEncrypt.this, "Message successfully encrypted! The result is displayed below.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } 
            catch (InterruptedException | ExecutionException e) {
                JOptionPane.showMessageDialog(RsaEncrypt.this, "An error occurred during encryption: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}