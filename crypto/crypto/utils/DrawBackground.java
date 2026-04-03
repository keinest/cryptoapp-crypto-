package crypto.utils;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.imageio.*;

public class DrawBackground extends JPanel
{
    private Image backgroundImage;
    private Color overlayColor;

    public DrawBackground(String imagePath)
    {
        this(imagePath, new Color(0, 0, 0, 100));
    }

    public DrawBackground(String imagePath, Color overlay)
    {
        if (imagePath != null && !imagePath.isBlank())
        {
            try { backgroundImage = ImageIO.read(new File(imagePath)); }
            catch (IOException e)
            {
                backgroundImage = null;
            }
        }
        this.overlayColor = overlay;
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (backgroundImage != null)
        {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.setColor(overlayColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        else
        {
            // Dégradé de secours corporate sombre
            
            GradientPaint gradient = new GradientPaint(
                0, 0,            ThemeManager.BG_APP,
                getWidth(), getHeight(), ThemeManager.BG_PANEL
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Grille subtile
            g2d.setColor(new Color(92, 107, 192, 8));
            g2d.setStroke(new BasicStroke(1));
            for (int x = 0; x < getWidth();  x += 40) g2d.drawLine(x, 0, x, getHeight());
            for (int y = 0; y < getHeight(); y += 40) g2d.drawLine(0, y, getWidth(), y);
        }

        g2d.dispose();
    }
}
