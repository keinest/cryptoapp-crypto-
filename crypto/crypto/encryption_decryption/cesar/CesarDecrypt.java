package crypto.encryption_decryption.cesar;

import crypto.utils.ThemeManager;
import crypto.utils.Util;
import crypto.view.AlgorithmBaseView;

import javax.swing.*;
import java.awt.*;

/**
 * Formulaire de déchiffrement César avec résultat inline.
 */
public class CesarDecrypt extends JPanel
{
    private final Cesar    parent;
    private JTextField     messageField;
    private JTextField     keyField;
    private JTextArea      resultArea;
    private JButton        btnDecrypt;

    public CesarDecrypt(Cesar parent)
    {
        this.parent = parent;
        buildUI();
    }

    private void buildUI()
    {
        messageField = ThemeManager.styledField("Entrez le message chiffré");
        messageField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        keyField = ThemeManager.styledField("Ex : D  ou  3");
        keyField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        resultArea = ThemeManager.styledTextArea(3, 40);
        resultArea.setEditable(false);

        btnDecrypt = ThemeManager.pillButtonOutline("🔓  Déchiffrer");
        btnDecrypt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDecrypt.addActionListener(e -> performDecryption());

        JPanel panel = AlgorithmBaseView.buildCryptoFormPanel(
            "César — Déchiffrement",
            new String[]{"Message chiffré", "Clé (lettre A–Z ou chiffre 0–9)"},
            new JComponent[]{messageField, keyField},
            btnDecrypt,
            resultArea,
            parent::restoreWindow
        );

        setLayout(new BorderLayout());
        setBackground(ThemeManager.BG_APP);
        add(panel, BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
    }

    private JPanel buildBottomBar()
    {
        JPanel bar = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 16, 10));
        bar.setBackground(ThemeManager.BG_PANEL);
        bar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, ThemeManager.BORDER));

        JButton btnCopy  = ThemeManager.pillButtonOutline("📋 Copier le résultat");
        JButton btnClear = ThemeManager.pillButtonGhost("Effacer");

        btnCopy.addActionListener(e ->
        {
            String res = resultArea.getText().trim();
            if (!res.isEmpty()) Util.copyText(res);
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

    private void performDecryption()
    {
        String message = messageField.getText().trim();
        String keyStr  = keyField.getText().trim();

        if (message.isEmpty() || keyStr.isEmpty())
        { showError("Message et clé sont obligatoires."); return; }

        if (keyStr.length() != 1)
        { showError("La clé doit être un seul caractère."); return; }

        char kc = keyStr.charAt(0);
        int realKey;
        if (Character.isUpperCase(kc))      realKey = kc - 'A';
        else if (Character.isLowerCase(kc)) realKey = kc - 'a';
        else if (Character.isDigit(kc))     realKey = kc - '0';
        else { showError("Clé invalide."); return; }

        char[] chars = message.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            if (!Character.isLetter(chars[i])) continue;
            int base = Character.isUpperCase(chars[i]) ? 'A' : 'a';
            int dec  = ((chars[i] - base - realKey) % 26 + 26) % 26;
            chars[i] = (char)(dec + base);
        }

        resultArea.setText(new String(chars));
        resultArea.setForeground(ThemeManager.TEXT_PRIMARY);
    }

    private void showError(String msg)
    {
        resultArea.setText("⚠ " + msg);
        resultArea.setForeground(ThemeManager.ERROR);
    }
}
