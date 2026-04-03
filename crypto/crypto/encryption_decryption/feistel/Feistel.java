package crypto.encryption_decryption.feistel;

import crypto.view.MainWindow;
import crypto.view.AlgorithmBaseView;

/**
 * Selecteur de l algorithme Feistel.
 */
public class Feistel extends AlgorithmBaseView
{
    public Feistel(MainWindow window)
    {
        super(window, window.getNav());
    }

    @Override protected String getAlgorithmIcon()        { return "??"; }
    @Override protected String getAlgorithmName()        { return "Chiffrement Feistel"; }
    @Override protected String getAlgorithmDescription() {
        return "Réseau de Feistel structurant de nombreux chiffrements modernes (DES, Blowfish).";
    }
    @Override protected String getKeyDescription() {
        return "Une clé textuelle et un nombre de tours";
    }

    @Override
    protected void onEncryptClicked()
    {
        showInWindow(new FeistelEncrypt(this), "Chiffrement Feistel - Chiffrement");
    }

    @Override
    protected void onDecryptClicked()
    {
        showInWindow(new FeistelDecrypt(this), "Chiffrement Feistel - Dechiffrement");
    }

    public void restoreWindow()
    {
        restoreInWindow("Chiffrement Feistel");
    }
}
