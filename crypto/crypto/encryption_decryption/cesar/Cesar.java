package crypto.encryption_decryption.cesar;

import crypto.view.MainWindow;
import crypto.view.AlgorithmBaseView;

/**
 * Sélecteur de l'algorithme César.
 */
public class Cesar extends AlgorithmBaseView
{
    public Cesar(MainWindow window)
    {
        super(window, window.getNav());
    }

    @Override protected String getAlgorithmIcon()        { return "🔤"; }
    @Override protected String getAlgorithmName()        { return "Chiffrement César"; }
    @Override protected String getAlgorithmDescription() {
        return "Chiffrement par substitution monoalphabétique classique, inventé par Jules César. " +
               "Chaque lettre est décalée d'un nombre fixe de positions dans l'alphabet.";
    }
    @Override protected String getKeyDescription() {
        return "Un caractère (lettre A–Z ou chiffre 0–9) définissant le décalage";
    }

    @Override
    protected void onEncryptClicked()
    {
        showInWindow(new CesarEncrypt(this), "César — Chiffrement");
    }

    @Override
    protected void onDecryptClicked()
    {
        showInWindow(new CesarDecrypt(this), "César — Déchiffrement");
    }

    /** Restaure la fenêtre de sélection César. */
    public void restoreWindow()
    {
        restoreInWindow("Chiffrement César");
    }
}
