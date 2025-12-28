package crypto;

import crypto.session.UserSession;
import crypto.session.SessionInterceptor;
import crypto.users.Connect;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EnhancedHeader extends JPanel 
{
    private Main window;
    private JLabel userInfoLabel;
    private JButton sessionInfoButton;
    private JButton logoutButton;
    private JButton homeButton;
    private Timer sessionTimer;
    
    public EnhancedHeader(Main window) 
    {
        this.window = window;
        this.initUI();
        this.setupSessionTimer();
    }
    
    private void initUI() 
    {
        this.setLayout(new BorderLayout());
        
        this.setOpaque(true);
        
        this.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, crypto.utils.ThemeManager.ACCENT_CYAN),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("CryptoApp");
        titleLabel.setFont(crypto.utils.ThemeManager.FONT_TITLE);
        titleLabel.setForeground(crypto.utils.ThemeManager.ACCENT_CYAN);
        leftPanel.add(titleLabel);
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centerPanel.setOpaque(false);
        
        userInfoLabel = new JLabel();
        updateUserInfo();
        userInfoLabel.setForeground(crypto.utils.ThemeManager.TEXT_PRIMARY);
        userInfoLabel.setFont(crypto.utils.ThemeManager.FONT_MONO);
        
        sessionInfoButton = new JButton("ⓘ");
        styleInfoButton(sessionInfoButton);
        
        sessionInfoButton.addActionListener(e -> SessionInterceptor.showSessionInfo(window));
        
        centerPanel.add(userInfoLabel);
        centerPanel.add(sessionInfoButton);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        
        homeButton = createCyberHeaderButton("Home", crypto.utils.ThemeManager.ACCENT_BLUE);
        logoutButton = createCyberHeaderButton("Log Out", new Color(231, 76, 60));
        
        homeButton.addActionListener(e -> window.showHome());
        logoutButton.addActionListener(e -> performLogout());
        
        rightPanel.add(homeButton);
        rightPanel.add(Box.createHorizontalStrut(10));
        rightPanel.add(logoutButton);
        
        this.add(leftPanel, BorderLayout.WEST);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.EAST);
    }
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        GradientPaint gradient = new GradientPaint(
            0, 0, crypto.utils.ThemeManager.DARK_BG_PRIMARY,
            getWidth(), 0, crypto.utils.ThemeManager.DARK_BG_TERTIARY
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setColor(new Color(0, 255, 255, 30));
        g2d.fillRect(0, 0, getWidth(), 2);
        
        g2d.dispose();
    }
    
    private void updateUserInfo() 
    {
        UserSession session = UserSession.getInstance();
        
        SwingUtilities.invokeLater(() -> 
        {
            if(session.isLoggedIn()) 
            {
                String userName = session.getCurrentUser().getLogin();
                String userType = session.isGuest() ? "Invite" : "Utilisateur";
                String duration = session.getFormattedSessionDuration();
                
                userInfoLabel.setText("<html><font color='#00ffff'><b>" + userName + "</b></font>(" + userType + ") • " + duration + "</html>");
                userInfoLabel.setToolTipText("Session active depuis " + duration);
                logoutButton.setEnabled(true);
                logoutButton.setVisible(true);
            } 
            else 
            {
                userInfoLabel.setText("<html><i><font color='#aaaaaa'>Non connecte</font></i></html>");
                userInfoLabel.setToolTipText("Cliquez pour vous connecter");
                logoutButton.setEnabled(false);
                logoutButton.setVisible(false);
            }
        });
    }
    
    private void setupSessionTimer() 
    {
        sessionTimer = new Timer(1000, e -> 
        {
            updateUserInfo();
            
            UserSession session = UserSession.getInstance();
            if(session.isLoggedIn() && !session.isSessionValid()) 
            {
                sessionTimer.stop();
                handleSessionExpiration();
            }
        });
        sessionTimer.start();
    }
    
    private void handleSessionExpiration() 
    {
        UserSession session = UserSession.getInstance();
        
        int choice = JOptionPane.showConfirmDialog(
            window,
            "<html><div style='color:#ff5555;'>" +
            "<b>Session expiree</b><br><br>" +
            "Votre session a expire pour des raisons de securite.<br>" +
            "Souhaitez-vous vous reconnecter ?</div></html>",
            "Session expiree",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        session.destroySession();
        
        if(choice == JOptionPane.YES_OPTION) 
        {
            window.getContentPane().removeAll();
            window.setContentPane(new Connect(window));
            window.revalidate();
            window.repaint();
        } 
        else
            window.showHome();
    }
    
    private void performLogout() 
    {
        UserSession session = UserSession.getInstance();
        
        if(session.isLoggedIn()) 
        {
            int confirm = JOptionPane.showConfirmDialog(
                window,
                "<html><div style='color:#00ffff;'>" +
                "<b>Confirmation de deconnexion</b><br><br>" +
                "Êtes-vous sûr de vouloir vous deconnecter ?</div></html>",
                "Deconnexion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if(confirm == JOptionPane.YES_OPTION) 
            {
                session.destroySession();
                window.showHome();
                
                JOptionPane.showMessageDialog(
                    window,
                    "<html><div style='color:#00ff00;'>Vous avez ete deconnecte avec succès.</div></html>",
                    "Deconnexion",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }
    
    private JButton createCyberHeaderButton(String text, Color backgroundColor) 
    {
        JButton button = new JButton(text) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed())
                    g2.setColor(backgroundColor.darker().darker());
                else if (getModel().isRollover()) {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, backgroundColor.brighter(),
                        0, getHeight(), backgroundColor
                    );
                    g2.setPaint(gradient);
                } 
                else
                    g2.setColor(backgroundColor);
                
                if (!getModel().isRollover() || getModel().isPressed()) 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.setColor(new Color(255, 255, 255, 100));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(crypto.utils.ThemeManager.FONT_MONO_BOLD);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(backgroundColor.darker(), 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        
        button.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseEntered(MouseEvent event) 
            {
                button.setForeground(Color.WHITE);
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent event) 
            {
                button.setForeground(Color.WHITE);
                button.repaint();
            }
        });
        
        return button;
    }
    
    private void styleInfoButton(JButton button) 
    {
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(crypto.utils.ThemeManager.FONT_MONO_BOLD);
        button.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseEntered(MouseEvent event) 
            {
                button.setBackground(new Color(52, 152, 219));
            }
            
            @Override
            public void mouseExited(MouseEvent event) 
            {
                button.setBackground(new Color(41, 128, 185));
            }
        });
    }
    
    public void stopTimer() 
    {
        if(sessionTimer != null && sessionTimer.isRunning()) 
            sessionTimer.stop();
    }
    
    @Override
    protected void finalize() throws Throwable 
    {
        stopTimer();
        super.finalize();
    }
}
