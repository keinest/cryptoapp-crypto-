package crypto.encryption_decryption.vigenere;

import crypto.view.MainWindow;
import crypto.view.AlgorithmBaseView;

/**
 * Selecteur de l algorithme Vigenere.
 */
public class Vigenere extends AlgorithmBaseView
{
    public Vigenere(MainWindow window)
    {
        super(window, window.getNav());
    }

    @Override protected String getAlgorithmIcon()        { return "🔑"; }
    @Override protected String getAlgorithmName()        { return "Chiffrement Vigenère"; }
    @Override protected String getAlgorithmDescription() {
        return "Chiffrement polyalphabétique utilisant un mot-clé répété sur la longueur du message.";
    }
    @Override protected String getKeyDescription() {
        return "Un mot-clé alphabétique (ex: SECRET)";
    }

    @Override
    protected void onEncryptClicked()
    {
        showInWindow(new VigenereEncrypt(this), "Chiffrement Vigenère - Chiffrement");
    }

    @Override
    protected void onDecryptClicked()
    {
        showInWindow(new VigenereDecrypt(this), "Chiffrement Vigenère - Dechiffrement");
    }

    public void restoreWindow()
    {
        restoreInWindow("Chiffrement Vigenère");
    }
}
