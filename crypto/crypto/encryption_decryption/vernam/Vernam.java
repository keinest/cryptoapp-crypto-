package crypto.encryption_decryption.vernam;

import crypto.view.MainWindow;
import crypto.view.AlgorithmBaseView;

/**
 * Selecteur de l algorithme Vernam.
 */
public class Vernam extends AlgorithmBaseView
{
    public Vernam(MainWindow window)
    {
        super(window, window.getNav());
    }

    @Override protected String getAlgorithmIcon()        { return "🎲"; }
    @Override protected String getAlgorithmName()        { return "Chiffrement Vernam"; }
    @Override protected String getAlgorithmDescription() {
        return "Masque jetable offrant une confidentialité théoriquement parfaite si la clé est aussi longue que le message.";
    }
    @Override protected String getKeyDescription() {
        return "Une clé aléatoire de même longueur que le message";
    }

    @Override
    protected void onEncryptClicked()
    {
        showInWindow(new VernamEncrypt(this), "Chiffrement Vernam - Chiffrement");
    }

    @Override
    protected void onDecryptClicked()
    {
        showInWindow(new VernamDecrypt(this), "Chiffrement Vernam - Dechiffrement");
    }

    public void restoreWindow()
    {
        restoreInWindow("Chiffrement Vernam");
    }
}
