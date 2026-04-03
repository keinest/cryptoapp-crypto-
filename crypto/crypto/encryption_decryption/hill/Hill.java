package crypto.encryption_decryption.hill;

import crypto.view.MainWindow;
import crypto.view.AlgorithmBaseView;

/**
 * Selecteur de l algorithme Hill.
 */
public class Hill extends AlgorithmBaseView
{
    public Hill(MainWindow window)
    {
        super(window, window.getNav());
    }

    @Override protected String getAlgorithmIcon()        { return "M"; }
    @Override protected String getAlgorithmName()        { return "Chiffrement Hill"; }
    @Override protected String getAlgorithmDescription() {
        return "Chiffrement par blocs basé sur la multiplication matricielle modulo 26.";
    }
    @Override protected String getKeyDescription() {
        return "Une matrice carrée n×n dont le déterminant est copremier avec 26";
    }

    @Override
    protected void onEncryptClicked()
    {
        showInWindow(new HillEncrypt(this), "Chiffrement Hill - Chiffrement");
    }

    @Override
    protected void onDecryptClicked()
    {
        showInWindow(new HillDecrypt(this), "Chiffrement Hill - Dechiffrement");
    }

    public void restoreWindow()
    {
        restoreInWindow("Chiffrement Hill");
    }
}
