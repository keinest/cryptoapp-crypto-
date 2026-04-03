package crypto.view;

import crypto.view.components.AppHeader;
import crypto.controller.NavigationController;
import crypto.utils.ThemeManager;

import javax.swing.*;
import java.awt.*;

/**
 * Panneau de base pour les selecteurs d'algorithme(Chiffrer / Dechiffrer).
 * Chaque algorithme l'etend et fournit son nom, icone, description et actions.
 */
public abstract class AlgorithmBaseView extends JPanel
{
    protected final MainWindow           window;
    protected final NavigationController nav;

    protected JButton btnEncrypt;
    protected JButton btnDecrypt;

    protected AlgorithmBaseView(MainWindow window, NavigationController nav)
    {
        this.window = window;
        this.nav    = nav;
        buildUI();
    }

    protected abstract String getAlgorithmIcon();
    protected abstract String getAlgorithmName();
    protected abstract String getAlgorithmDescription();
    protected abstract String getKeyDescription();
    protected abstract void   onEncryptClicked();
    protected abstract void   onDecryptClicked();

    protected final void showInWindow(JPanel view, String title)
    {
        window.showView(view, title);
    }

    protected final void restoreInWindow(String title)
    {
        window.showView(this, title);
    }

    // -- Construction UI ------------------

    private void buildUI()
    {
        setLayout(new BorderLayout());
        setBackground(ThemeManager.BG_APP);

        AppHeader header = new AppHeader(window, nav);
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(ThemeManager.BG_APP);
        center.add(buildCard());
        add(center, BorderLayout.CENTER);
    }

    private JPanel buildCard()
    {
        JPanel card = ThemeManager.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(480, 400));

        // Icone + titre
        JLabel iconLabel = new JLabel(getAlgorithmIcon());
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 44));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = ThemeManager.titleLabel(getAlgorithmName());
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Description

        JLabel descLabel = new JLabel(
            "<html><div align='center' style='width:380px'>" + getAlgorithmDescription() + "</div></html>");
        descLabel.setFont(ThemeManager.FONT_BODY);
        descLabel.setForeground(ThemeManager.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Info cle
        JLabel keyInfo = new JLabel(
            "<html><div align='center' style='width:380px'><b>Cle :</b> " + getKeyDescription() + "</div></html>");
        keyInfo.setFont(ThemeManager.FONT_SMALL);
        keyInfo.setForeground(ThemeManager.TEXT_MUTED);
        keyInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        keyInfo.setHorizontalAlignment(SwingConstants.CENTER);

        // Separateur
        JSeparator sep = ThemeManager.separator();
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // Boutons d'action
        btnEncrypt = ThemeManager.pillButton("🔒  Chiffrer");
        btnDecrypt = ThemeManager.pillButtonOutline("🔓  Dechiffrer");
        btnEncrypt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDecrypt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEncrypt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnDecrypt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnEncrypt.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDecrypt.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnEncrypt.addActionListener(e -> onEncryptClicked());
        btnDecrypt.addActionListener(e -> onDecryptClicked());

        // Bouton retour
        JButton btnBack = ThemeManager.pillButtonGhost("← Retour au tableau de bord");
        btnBack.setFont(ThemeManager.FONT_SMALL);
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.addActionListener(e -> nav.showDashboard());

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(descLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(keyInfo);
        card.add(Box.createVerticalStrut(28));
        card.add(sep);
        card.add(Box.createVerticalStrut(24));
        card.add(btnEncrypt);
        card.add(Box.createVerticalStrut(12));
        card.add(btnDecrypt);
        card.add(Box.createVerticalStrut(24));
        card.add(btnBack);

        return card;
    }

    /**
     * Construit le panneau Chiffrement/Dechiffrement avec un formulaire standard.
     * Utilise par CesarEncrypt, RsaEncrypt, etc.
     */
    public static JPanel buildCryptoFormPanel(
            String title,
            String[] fieldLabels,
            JComponent[] fields,
            JButton actionBtn,
            JTextArea resultArea,
            Runnable backAction)
    {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(ThemeManager.BG_APP);

        // Header minimal(titre + bouton retour)
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ThemeManager.BG_PANEL);
        topBar.setPreferredSize(new Dimension(0, 56));
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeManager.BORDER),
            BorderFactory.createEmptyBorder(0, 24, 0, 24)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(ThemeManager.TEXT_PRIMARY);

        JButton backBtn = ThemeManager.pillButtonGhost("← Retour");
        backBtn.addActionListener(e -> backAction.run());

        topBar.add(titleLbl, BorderLayout.WEST);
        topBar.add(backBtn,  BorderLayout.EAST);

        outer.add(topBar, BorderLayout.NORTH);

        // Corps
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(ThemeManager.BG_APP);

        JPanel card = ThemeManager.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(580, -1));

        for(int i = 0; i < fieldLabels.length; i++)
        {
            JLabel lbl = ThemeManager.fieldLabel(fieldLabels[i]);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(lbl);
            card.add(Box.createVerticalStrut(6));

            fields[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            if(fields[i].getMaximumSize().width == Integer.MAX_VALUE || fields[i].getMaximumSize().height > 60)
                fields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, fields[i].getPreferredSize().height));
            card.add(fields[i]);
            card.add(Box.createVerticalStrut(16));
        }

        actionBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        card.add(actionBtn);

        if(resultArea != null)
        {
            card.add(Box.createVerticalStrut(20));
            JLabel resLbl = ThemeManager.fieldLabel("Resultat");
            resLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(resLbl);
            card.add(Box.createVerticalStrut(6));

            JScrollPane scrollResult = new JScrollPane(resultArea);
            scrollResult.setBorder(new ThemeManager.RoundedBorder(ThemeManager.BORDER, ThemeManager.RADIUS_INPUT, 1));
            scrollResult.setAlignmentX(Component.LEFT_ALIGNMENT);
            scrollResult.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
            scrollResult.getViewport().setBackground(ThemeManager.BG_INPUT);
            card.add(scrollResult);
        }

        center.add(card);
        outer.add(center, BorderLayout.CENTER);
        return outer;
    }
}
