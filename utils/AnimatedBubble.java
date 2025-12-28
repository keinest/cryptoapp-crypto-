package crypto.utils;

import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimatedBubble extends JPanel implements ActionListener 
{    
    private final ArrayList<Bubble> bubbles;
    private final Timer timer;
    private final Random random;
    private final Color baseColor;

    private static class Bubble 
    {
        public int x, y, size, speed;
        public Color color;
        public float alpha;
    }

    public AnimatedBubble(Color baseColor) 
    {
        this.baseColor = baseColor;
        this.bubbles   = new ArrayList<>();
        this.random    = new Random();
        this.timer     = new Timer(30, this); 
        this.timer.start();
        
        setOpaque(false);
        setBackground(ThemeManager.DARK_BG_PRIMARY); 

        for(int i = 0; i < 25; i++)
            bubbles.add(createRandomBubble());
    }

    private Bubble createRandomBubble() 
    {
        Bubble bubble = new Bubble();
        bubble.size   = random.nextInt(40) + 15;
        bubble.speed  = random.nextInt(10) + 1; 
        bubble.x      = random.nextInt(getWidth() > 0 ? getWidth() : 800);
        bubble.y      = getHeight() + random.nextInt(200); 
        bubble.alpha  = 0.3f + random.nextFloat() * 0.7f;
        bubble.color  = ThemeManager.getRandomBubbleColor();
        
        return bubble;
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Degrade de fond
        GradientPaint gradient = new GradientPaint(
            0, 0, ThemeManager.DARK_BG_PRIMARY,
            getWidth(), getHeight(), ThemeManager.DARK_BG_SECONDARY
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        for(Bubble bubble : bubbles) 
        {
            // Bulle avec degrade
            GradientPaint bubbleGradient = new GradientPaint(
                bubble.x - bubble.size / 2, bubble.y - bubble.size / 2,
                new Color(bubble.color.getRed(), bubble.color.getGreen(), 
                         bubble.color.getBlue(), (int)(bubble.alpha * 255)),
                bubble.x + bubble.size / 2, bubble.y + bubble.size / 2,
                new Color(bubble.color.getRed(), bubble.color.getGreen(), 
                         bubble.color.getBlue(), (int)(bubble.alpha * 100))
            );
            
            g2d.setPaint(bubbleGradient);
            g2d.fillOval(bubble.x - bubble.size / 2, bubble.y - bubble.size / 2, 
                        bubble.size, bubble.size);
        
            // Bordure lumineuse
            g2d.setColor(new Color(25, 200, 255, (int)(bubble.alpha * 180))); 
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(bubble.x - bubble.size / 2, bubble.y - bubble.size / 2, 
                        bubble.size, bubble.size);
        }
        g2d.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        int height = getHeight();
        int width  = getWidth();

        if(width <= 0 || height <= 0) return;

        for(int i = 0; i < bubbles.size(); i++) 
        {
            Bubble bubble = bubbles.get(i);
            bubble.y -= bubble.speed; 
            bubble.x += random.nextInt(3) - 1;

            if(bubble.y < -bubble.size)
                bubbles.set(i, resetBubble(width));
        }
        repaint();
    }
    
    private Bubble resetBubble(int width) 
    {
        Bubble bubble = new Bubble();
        bubble.size   = random.nextInt(40) + 15;
        bubble.speed  = random.nextInt(3) + 1;
        bubble.x      = random.nextInt(width);
        bubble.y      = getHeight() + bubble.size + random.nextInt(50);
        bubble.alpha  = 0.3f + random.nextFloat() * 0.7f;
        bubble.color  = ThemeManager.getRandomBubbleColor();
        return bubble;
    }
}