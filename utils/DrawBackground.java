package crypto.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class DrawBackground extends JPanel
{
    private Image backgroundImage;

    public DrawBackground(String imagePath)
    {
        try{backgroundImage = ImageIO.read(new File(imagePath));}
        catch(IOException e){e.printStackTrace();}
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
