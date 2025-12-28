package crypto.utils;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;
import java.awt.geom.Point2D;
import java.awt.GradientPaint;
import java.awt.LinearGradientPaint;

public class ThemeManager 
{
    // Couleurs principales cybersec theme
    public static final Color DARK_BG_PRIMARY = new Color(10, 20, 35);
    public static final Color DARK_BG_SECONDARY = new Color(15, 25, 40);
    public static final Color DARK_BG_TERTIARY = new Color(20, 30, 50);
    
    public static final Color ACCENT_CYAN = new Color(0, 255, 255);
    public static final Color ACCENT_BLUE = new Color(0, 150, 255);
    public static final Color ACCENT_GREEN = new Color(0, 255, 150);
    public static final Color ACCENT_PURPLE = new Color(150, 0, 255);
    
    public static final Color TEXT_PRIMARY = new Color(220, 220, 220);
    public static final Color TEXT_SECONDARY = new Color(180, 180, 180);
    public static final Color TEXT_MUTED = new Color(150, 150, 150);
    
    public static final Color BUTTON_BG = new Color(30, 40, 60, 220);
    public static final Color BUTTON_HOVER = new Color(40, 50, 80, 240);
    public static final Color BUTTON_ACTIVE = new Color(50, 60, 100, 255);
    
    // Polices
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 14);
    public static final Font FONT_MONO_BOLD = new Font("Consolas", Font.BOLD, 16);
    public static final Font FONT_TITLE = new Font("Consolas", Font.BOLD, 28);
    public static final Font FONT_SUBTITLE = new Font("Consolas", Font.BOLD, 20);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    
    // Couleurs pour les bulles animees
    public static final Color[] BUBBLE_COLORS = {
        new Color(0, 255, 255, 100),    // Cyan
        new Color(0, 200, 255, 80),     // Bleu clair
        new Color(100, 255, 200, 60),   // Vert cyan
        new Color(255, 100, 255, 40),   // Violet
        new Color(255, 255, 255, 30)    // Blanc translucide
    };
    
    // Effets de degrade
    public static GradientPaint createCyberGradient(int width, int height) 
    {
        return new GradientPaint(
            0, 0, ACCENT_CYAN,
            width, height, ACCENT_BLUE
        );
    }
    
    public static LinearGradientPaint createLinearGradient(int width, int height) 
    {
        Point2D start = new Point2D.Float(0, 0);
        Point2D end = new Point2D.Float(width, height);
        float[] fractions = {0.0f, 0.5f, 1.0f};
        Color[] colors = {DARK_BG_PRIMARY, DARK_BG_SECONDARY, DARK_BG_TERTIARY};
        
        return new LinearGradientPaint(start, end, fractions, colors);
    }
    
    // Generation de couleurs aleatoires pour les bulles
    public static Color getRandomBubbleColor() 
    {
        Random random = new Random();
        return BUBBLE_COLORS[random.nextInt(BUBBLE_COLORS.length)];
    }
    
    // Methode pour assombrir une couleur
    public static Color darken(Color color, float factor) 
    {
        int r = Math.max((int)(color.getRed() * factor), 0);
        int g = Math.max((int)(color.getGreen() * factor), 0);
        int b = Math.max((int)(color.getBlue() * factor), 0);
        return new Color(r, g, b, color.getAlpha());
    }
    
    // Methode pour eclaircir une couleur
    public static Color lighten(Color color, float factor) 
    {
        int r = Math.min((int)(color.getRed() * (1 + factor)), 255);
        int g = Math.min((int)(color.getGreen() * (1 + factor)), 255);
        int b = Math.min((int)(color.getBlue() * (1 + factor)), 255);
        return new Color(r, g, b, color.getAlpha());
    }
}