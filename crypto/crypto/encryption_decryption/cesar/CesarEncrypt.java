package crypto.encryption_decryption.cesar;

import crypto.utils.ThemeManager;
import crypto.utils.Util;
import crypto.view.AlgorithmBaseView;

import javax.swing.*;
import java.awt.*;

/**
 * Formulaire de chiffrement César avec résultat inline.
 */
public class CesarEncrypt extends JPanel
{
    private final Cesar       parent;
    private JTextField        messageField;
    private JTextField        keyField;
    private JTextArea         resultArea;
    private JButton           btnEncrypt;

    public CesarEncrypt(Cesar parent)
    {
        this.parent = parent;
        buildUI();
    }

    private void buildUI()
    {
        messageField = ThemeManager.styledField("Entrez le message à chiffrer");
        messageField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        keyField = ThemeManager.styledField("Ex : D  ou  3");
        keyField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        resultArea = ThemeManager.styledTextArea(3, 40);
        resultArea.setEditable(false);

        btnEncrypt = ThemeManager.pillButton("🔒  Chiffrer");
        btnEncrypt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEncrypt.addActionListener(e -> performEncryption());

        JPanel panel = AlgorithmBaseView.buildCryptoFormPanel(
            "César — Chiffrement",
            new String[]{"Message en clair", "Clé (lettre A–Z ou chiffre 0–9)"},
            new JComponent[]{messageField, keyField},
            btnEncrypt,
            resultArea,
            parent::restoreWindow
        );

        setLayout(new BorderLayout());
        setBackground(ThemeManager.BG_APP);
        add(panel, BorderLayout.CENTER);

        // Bouton copier dans un panneau bas
        JPanel bottomBar = buildBottomBar();
        add(bottomBar, BorderLayout.SOUTH);
    }

    private JPanel buildBottomBar()
    {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        bar.setBackground(ThemeManager.BG_PANEL);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, ThemeManager.BORDER));

        JButton btnCopy  = ThemeManager.pillButtonOutline("📋 Copier le résultat");
        JButton btnClear = ThemeManager.pillButtonGhost("Effacer");

        btnCopy.addActionListener(e ->
        {
            String res = resultArea.getText().trim();
            if (!res.isEmpty()) { Util.copyText(res); }
        });

        btnClear.addActionListener(e ->
        {
            messageField.setText("");
            keyField.setText("");
            resultArea.setText("");
        });

        bar.add(btnClear);
        bar.add(btnCopy);
        return bar;
    }

    private void performEncryption()
    {
        String message = messageField.getText().trim();
        String keyStr  = keyField.getText().trim();

        if (message.isEmpty() || keyStr.isEmpty())
        {
            showError("Message et clé sont obligatoires.");
            return;
        }
        if (keyStr.length() != 1)
        {
            showError("La clé doit être un seul caractère (lettre ou chiffre).");
            return;
        }

        int realKey;
        char kc = keyStr.charAt(0);
        if (Character.isUpperCase(kc))      realKey = kc - 'A';
        else if (Character.isLowerCase(kc)) realKey = kc - 'a';
        else if (Character.isDigit(kc))     realKey = kc - '0';
        else { showError("Clé invalide — utilisez une lettre (A–Z) ou un chiffre (0–9)."); return; }

        StringBuilder sb  = new StringBuilder();
        for (int i = 0; i < message.length(); i++)
        {
            char c = message.charAt(i);
            if (!Character.isLetter(c)) { sb.append(c); continue; }
            int base = Character.isUpperCase(c) ? 'A' : 'a';
            sb.append((char)(((c - base + realKey) % 26) + base));
        }

        resultArea.setText(sb.toString());
        resultArea.setForeground(ThemeManager.TEXT_PRIMARY);
    }

    private void showError(String msg)
    {
        resultArea.setText("⚠ " + msg);
        resultArea.setForeground(ThemeManager.ERROR);
    }
}
