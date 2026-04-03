package crypto.encryption_decryption.hill;

import crypto.utils.ThemeManager;

import javax.swing.*;
import java.awt.*;

/**
 * Dechiffrement Hill — inverser la multiplication matricielle modulo 26.
 * Ce fichier etait absent de l'original (bug signale).
 */
public class HillDecrypt extends JPanel
{
    private final Hill     parent;
    private JTextArea      cipherArea;
    private JTextArea      resultArea;
    private JTextField[][] keyFields;
    private JTextField     sizeField;
    private JButton        btnValidate;
    private JButton        btnDecrypt;
    private int            matSize = 2;

    public HillDecrypt(Hill parent)
    {
        this.parent = parent;
        buildUI();
    }

    private void buildUI()
    {
        setLayout(new BorderLayout());
        setBackground(ThemeManager.BG_APP);

        // Barre du haut
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ThemeManager.BG_PANEL);
        topBar.setPreferredSize(new Dimension(0, 56));
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.BORDER),
            BorderFactory.createEmptyBorder(0, 24, 0, 24)
        ));
        JLabel titleLbl = new JLabel("Hill — Dechiffrement");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(ThemeManager.TEXT_PRIMARY);
        JButton backBtn = ThemeManager.pillButtonGhost("<- Retour");
        backBtn.addActionListener(e -> parent.restoreWindow());
        topBar.add(titleLbl, BorderLayout.WEST);
        topBar.add(backBtn,  BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Contenu central
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(ThemeManager.BG_APP);

        JPanel card = ThemeManager.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(620, -1));

        // Taille matrice
        JLabel sizeLbl = ThemeManager.fieldLabel("Taille de la matrice (ex : 2 pour 2x2, 3 pour 3x3)");
        sizeLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        sizeField = ThemeManager.styledField("2");
        sizeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        sizeField.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnValidate = ThemeManager.pillButtonOutline("Definir la matrice");
        btnValidate.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnValidate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnValidate.addActionListener(e -> buildKeyGrid(card));

        // Message chiffre
        JLabel cipherLbl = ThemeManager.fieldLabel("Message chiffre");
        cipherLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        cipherArea = ThemeManager.styledTextArea(3, 40);
        JScrollPane cipherScroll = new JScrollPane(cipherArea);
        cipherScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        cipherScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        cipherScroll.setBorder(new ThemeManager.RoundedBorder(ThemeManager.BORDER, ThemeManager.RADIUS_INPUT, 1));
        cipherScroll.getViewport().setBackground(ThemeManager.BG_INPUT);

        // Resultat
        JLabel resLbl = ThemeManager.fieldLabel("Resultat");
        resLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultArea = ThemeManager.styledTextArea(3, 40);
        resultArea.setEditable(false);
        JScrollPane resScroll = new JScrollPane(resultArea);
        resScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        resScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        resScroll.setBorder(new ThemeManager.RoundedBorder(ThemeManager.BORDER, ThemeManager.RADIUS_INPUT, 1));
        resScroll.getViewport().setBackground(ThemeManager.BG_INPUT);

        btnDecrypt = ThemeManager.pillButtonOutline("Dechiffrer");
        btnDecrypt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDecrypt.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDecrypt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnDecrypt.setEnabled(false);
        btnDecrypt.addActionListener(e -> performDecryption());

        card.add(sizeLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(sizeField);
        card.add(Box.createVerticalStrut(10));
        card.add(btnValidate);
        card.add(Box.createVerticalStrut(20));
        card.add(cipherLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(cipherScroll);
        card.add(Box.createVerticalStrut(16));
        card.add(btnDecrypt);
        card.add(Box.createVerticalStrut(20));
        card.add(resLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(resScroll);

        center.add(card);
        add(center, BorderLayout.CENTER);
    }

    private void buildKeyGrid(JPanel card)
    {
        String sizeStr = sizeField.getText().trim();
        if (!sizeStr.matches("[2-4]"))
        {
            resultArea.setText("Taille invalide. Entrez 2, 3 ou 4.");
            resultArea.setForeground(ThemeManager.ERROR);
            return;
        }
        matSize   = Integer.parseInt(sizeStr);
        keyFields = new JTextField[matSize][matSize];

        // Chercher et supprimer l'ancienne grille si elle existe
        Component[] comps = card.getComponents();
        for (Component c : comps)
        {
            if ("keyGrid".equals(c.getName()))
            {
                card.remove(c);
                break;
            }
        }

        JPanel keyGrid = new JPanel(new GridLayout(matSize, matSize, 8, 8));
        keyGrid.setName("keyGrid");
        keyGrid.setOpaque(false);
        keyGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        keyGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, matSize * 48));

        for (int r = 0; r < matSize; r++)
            for (int c = 0; c < matSize; c++)
            {
                keyFields[r][c] = ThemeManager.styledField("0");
                keyGrid.add(keyFields[r][c]);
            }

        JLabel keyLbl = ThemeManager.fieldLabel("Matrice cle (" + matSize + "x" + matSize + ")");
        keyLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        keyLbl.setName("keyLabel");

        // Ajouter apres le bouton Valider (index 4)
        card.add(keyLbl,  4);
        card.add(Box.createVerticalStrut(6), 5);
        card.add(keyGrid, 6);
        card.add(Box.createVerticalStrut(16), 7);

        btnDecrypt.setEnabled(true);
        card.revalidate();
        card.repaint();
    }

    private void performDecryption()
    {
        String cipher = cipherArea.getText().trim().toUpperCase().replaceAll("[^A-Z]", "");

        if (cipher.isEmpty())
        { showError("Entrez un message chiffre."); return; }

        if (keyFields == null)
        { showError("Definissez d'abord la matrice cle."); return; }

        // Lire la matrice
        int[][] key = new int[matSize][matSize];
        try
        {
            for (int r = 0; r < matSize; r++)
                for (int c = 0; c < matSize; c++)
                    key[r][c] = Integer.parseInt(keyFields[r][c].getText().trim()) % 26;
        }
        catch (NumberFormatException ex)
        { showError("La matrice ne doit contenir que des entiers."); return; }

        // Calculer la matrice inverse modulo 26
        int[][] inv = invertMatrix(key, matSize);
        if (inv == null)
        { showError("La matrice n'est pas inversible modulo 26 (det non copremier avec 26)."); return; }

        // Padding si necessaire
        while (cipher.length() % matSize != 0) cipher += "X";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cipher.length(); i += matSize)
        {
            int[] block = new int[matSize];
            for (int j = 0; j < matSize; j++)
                block[j] = cipher.charAt(i + j) - 'A';

            for (int r = 0; r < matSize; r++)
            {
                int val = 0;
                for (int c = 0; c < matSize; c++)
                    val += inv[r][c] * block[c];
                sb.append((char)(((val % 26) + 26) % 26 + 'A'));
            }
        }

        resultArea.setText(sb.toString());
        resultArea.setForeground(ThemeManager.TEXT_PRIMARY);
    }

    /** Calcule l'inverse d'une matrice modulo 26 via la méthode des cofacteurs. */
    private int[][] invertMatrix(int[][] m, int n)
    {
        int det = determinant(m, n);
        det = ((det % 26) + 26) % 26;

        // Chercher l'inverse de det modulo 26
        int detInv = -1;
        for (int i = 1; i < 26; i++)
            if ((det * i) % 26 == 1) { detInv = i; break; }

        if (detInv == -1) return null; // non inversible

        // Matrice des cofacteurs transposee
        int[][] adj = new int[n][n];
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
            {
                int[][] sub = subMatrix(m, r, c, n);
                int cof = (int) Math.round(Math.pow(-1, r + c)) * determinant(sub, n - 1);
                adj[c][r] = (((cof % 26) + 26) % 26);
            }

        // Multiplier par detInv
        int[][] inv = new int[n][n];
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                inv[r][c] = (adj[r][c] * detInv) % 26;

        return inv;
    }

    private int determinant(int[][] m, int n)
    {
        if (n == 1) return m[0][0];
        if (n == 2) return m[0][0] * m[1][1] - m[0][1] * m[1][0];
        int det = 0;
        for (int c = 0; c < n; c++)
            det += (int) Math.round(Math.pow(-1, c)) * m[0][c] * determinant(subMatrix(m, 0, c, n), n - 1);
        return det;
    }

    private int[][] subMatrix(int[][] m, int excludeRow, int excludeCol, int n)
    {
        int[][] sub = new int[n - 1][n - 1];
        int ri = 0;
        for (int r = 0; r < n; r++)
        {
            if (r == excludeRow) continue;
            int ci = 0;
            for (int c = 0; c < n; c++)
            {
                if (c == excludeCol) continue;
                sub[ri][ci++] = m[r][c];
            }
            ri++;
        }
        return sub;
    }

    private void showError(String msg)
    {
        resultArea.setText("Erreur : " + msg);
        resultArea.setForeground(ThemeManager.ERROR);
    }
}
