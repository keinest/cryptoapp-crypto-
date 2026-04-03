package crypto.encryption_decryption.affine;

import crypto.view.MainWindow;
import crypto.view.AlgorithmBaseView;

/**
 * Selecteur de l algorithme Affine.
 */
public class Affine extends AlgorithmBaseView
{
    public Affine(MainWindow window)
    {
        super(window, window.getNav());
    }

    @Override protected String getAlgorithmIcon()        { return "➗"; }
    @Override protected String getAlgorithmName()        { return "Chiffrement Affine"; }
    @Override protected String getAlgorithmDescription() {
        return "Variante du chiffrement par substitution combinant multiplication et addition modulo 26.";
    }
    @Override protected String getKeyDescription() {
        return "Deux entiers a (copremier avec 26) et b (décalage)";
    }

    @Override
    protected void onEncryptClicked()
    {
        showInWindow(new AffineEncrypt(this), "Chiffrement Affine - Chiffrement");
    }

    @Override
    protected void onDecryptClicked()
    {
        showInWindow(new AffineDecrypt(this), "Chiffrement Affine - Dechiffrement");
    }

    public void restoreWindow()
    {
        restoreInWindow("Chiffrement Affine");
    }

    /** Alias pour AffineEncrypt et AffineDecrypt. */
    public void showMe() { restoreWindow(); }
}
