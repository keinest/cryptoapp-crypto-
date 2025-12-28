package crypto.utils;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.imageio.*;

public class DrawBackground extends JPanel
{
    private Image backgroundImage;
    private Color overlayColor;
    private boolean showGrid;

    public DrawBackground(String imagePath)
    {
        this(imagePath, new Color(0, 0, 0, 80), true);
    }
    
    public DrawBackground(String imagePath, Color overlay, boolean showGrid)
    {
        try{backgroundImage = ImageIO.read(new File(imagePath));} 
        
        catch(IOException e)
        {
            backgroundImage = null;
            System.err.println("Image non trouvée: " + imagePath);
        }
        
        this.overlayColor = overlay;
        this.showGrid     = showGrid;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        if(backgroundImage == null) 
        {
            GradientPaint gradient = new GradientPaint(
                0, 0, ThemeManager.DARK_BG_PRIMARY,
                getWidth(), getHeight(), ThemeManager.DARK_BG_SECONDARY
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
         
            if(showGrid) 
            {
                g2d.setColor(new Color(0, 255, 255, 10));
                g2d.setStroke(new BasicStroke(1));
                
                for(int x = 0; x < getWidth(); x += 30) 
                    g2d.drawLine(x, 0, x, getHeight());
                
                for(int y = 0; y < getHeight(); y += 30) 
                    g2d.drawLine(0, y, getWidth(), y);
                
                g2d.setColor(new Color(0, 150, 255, 20));
                for (int x = 30; x < getWidth(); x += 30) 
                {
                    for (int y = 30; y < getHeight(); y += 30)
                        g2d.fillOval(x - 1, y - 1, 3, 3);
                }
            }
        } 
        else 
        {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            
            g2d.setColor(overlayColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            if (showGrid) {
                g2d.setColor(new Color(0, 255, 255, 5));
                g2d.setStroke(new BasicStroke(1));
                
                for (int y = 0; y < getHeight(); y += 4) {
                    g2d.drawLine(0, y, getWidth(), y);
                }
            }
        }
        
        g2d.setColor(new Color(0, 255, 255, 30));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
        
        g2d.dispose();
    }
}
