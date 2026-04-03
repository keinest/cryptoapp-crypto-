package crypto.encryption_decryption.rsa;

import crypto.view.MainWindow;
import crypto.view.AlgorithmBaseView;

/**
 * Selecteur de l algorithme RSA.
 */
public class RSA extends AlgorithmBaseView
{
    public RSA(MainWindow window)
    {
        super(window, window.getNav());
    }

    @Override protected String getAlgorithmIcon()        { return "🔐"; }
    @Override protected String getAlgorithmName()        { return "Chiffrement RSA"; }
    @Override protected String getAlgorithmDescription() {
        return "Algorithme asymétrique à clés publique/privée. Repose sur la difficulté de factoriser de grands entiers premiers.";
    }
    @Override protected String getKeyDescription() {
        return "Deux nombres premiers p et q, une clé publique e";
    }

    @Override
    protected void onEncryptClicked()
    {
        showInWindow(new RsaEncrypt(this), "Chiffrement RSA - Chiffrement");
    }

    @Override
    protected void onDecryptClicked()
    {
        showInWindow(new RsaDecrypt(this), "Chiffrement RSA - Dechiffrement");
    }

    public void restoreWindow()
    {
        restoreInWindow("Chiffrement RSA");
    }
}
