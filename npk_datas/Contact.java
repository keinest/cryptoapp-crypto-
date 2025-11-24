package crypto.npk_datas;

import crypto.Main;
import crypto.utils.DrawBackground;
import crypto.utils.Util;
import crypto.users.Connect; 
import crypto.users.Registration; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Contact extends JPanel
{
    private static final Color PRIMARY_COLOR = new Color(30, 40, 60);
    private static final Color ACCENT_COLOR  = new Color(0, 150, 255);
    private static final Color TEXT_LIGHT    = Color.WHITE;
    private static final Color TEXT_DARK     = new Color(40, 40, 40);
    private static final Font TITLE_FONT     = new Font("SansSerif", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT  = new Font("SansSerif", Font.BOLD, 20);
    private static final Font BODY_FONT      = new Font("SansSerif", Font.PLAIN, 15);
    
    protected DrawBackground background;
    protected JButton back;
    protected Main main_window;
     
    public Contact(Main main_window) 
    {
        this.main_window = main_window;
        
        this.back = createStyledButton("Back",new Color(255,125, 60,255), TEXT_LIGHT, BODY_FONT);
        this.background = new DrawBackground("crypto/ressources/homes.jpg");
        this.setLayout(new BorderLayout());
        
        this.back.addActionListener(e -> Contact.this.main_window.showHome());

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setBackground(Color.BLACK); 
        navPanel.add(this.back);

        JScrollPane cont_scroll = new JScrollPane(contact_datas());
        cont_scroll.setOpaque(false);
        cont_scroll.getViewport().setOpaque(false);
        cont_scroll.setOpaque(false);
        cont_scroll.getVerticalScrollBar().setUnitIncrement(16);
        cont_scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cont_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        cont_scroll.setWheelScrollingEnabled(true);
        
        cont_scroll.addMouseWheelListener(new MouseWheelListener()
        {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event)
            {
                JScrollBar bar = cont_scroll.getVerticalScrollBar();
                int value      = event.getWheelRotation() * bar.getUnitIncrement();
                bar.setValue(bar.getValue() + value);
            }
        });

        this.background.setLayout(new BorderLayout());
        this.background.setOpaque(true);
        this.background.setBackground(new Color(255,255,255));
        this.background.add(navPanel,BorderLayout.NORTH);
        this.background.add(cont_scroll,BorderLayout.CENTER);
        this.add(background,BorderLayout.CENTER);
    }
    
    public void restoreWindow(JPanel panelToRestore)
    {
        this.main_window.getContentPane().removeAll();
        this.main_window.setContentPane(panelToRestore);
        panelToRestore.revalidate();
        panelToRestore.repaint();
        this.main_window.revalidate();
        this.main_window.repaint();
        this.main_window.setVisible(true);
    }

    private JButton createStyledButton(String text,Color background, Color foreground, Font font)
    {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFont(font);

        button.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent event)
            {
                button.setBackground(background == PRIMARY_COLOR ? ACCENT_COLOR : background.darker());
                button.setForeground(TEXT_LIGHT);
            }

            @Override
            public void mouseExited(MouseEvent event)
            {
                button.setBackground(background);
                button.setForeground(foreground);
            }
        });

        return button;
    }


    private boolean send_contact(String name, String phone, String email, String location)
    {
        if(name.isEmpty() || phone.isEmpty() || email.isEmpty() || location.isEmpty())
        {
            JOptionPane.showMessageDialog(Contact.this, "Please complete the form before continue !", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(!Util.mailVeri(email))
        {
            JOptionPane.showMessageDialog(Contact.this, "Invalid email address !\nCheck and retry", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JOptionPane.showMessageDialog(Contact.this, "Your informations has been successfully send", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    private JPanel contact_form()
    {
        JLabel cont_title  = new JLabel("Formulaire de contact !");
        cont_title.setFont(SUBTITLE_FONT);
        cont_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        cont_title.setForeground(TEXT_DARK);
        cont_title.setBackground(new Color(46, 139, 87));

        JTextArea cont_text = new JTextArea("Si vous preferez, vous pouvez nous envoyer un message en utilisant le formulaire ci dessous :");
        cont_text.setEditable(false);
        cont_text.setFont(BODY_FONT);
        cont_text.setLineWrap(true);
        cont_text.setWrapStyleWord(true);
        cont_text.setOpaque(false);

        JTextField name     = new JTextField(15);
        JTextField phone    = new JTextField(15);
        JTextField email    = new JTextField(15);
        JTextField location = new JTextField(15);

        JPanel data_panel   = new JPanel(new GridLayout(4,2,10,10));

        data_panel.setBackground(new Color(255,255,255));
        data_panel.add(new JLabel("Name "));
        data_panel.add(name);
        data_panel.add(new JLabel("Phone "));
        data_panel.add(phone);
        data_panel.add(new JLabel("Email "));
        data_panel.add(email);
        data_panel.add(new JLabel("Location "));
        data_panel.add(location);

        JPanel content_panel = new JPanel();
        content_panel.setLayout(new BoxLayout(content_panel, BoxLayout.Y_AXIS));
        content_panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(40, 40, 40, 40),BorderFactory.createTitledBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            "Network Protection Kit (NPK)", TitledBorder.LEFT, TitledBorder.TOP, SUBTITLE_FONT, PRIMARY_COLOR)
        ));
        content_panel.setOpaque(true);
        content_panel.setBackground(new Color(128,0,0));

        JButton send = createStyledButton("Submit",ACCENT_COLOR,Color.BLACK,BODY_FONT);
        send.setAlignmentX(Component.CENTER_ALIGNMENT);

        send.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                if(send_contact(name.getText().trim(),phone.getText().trim(),
                    email.getText().trim(),location.getText().trim()))
                {
                    name.setText("");
                    phone.setText("");
                    email.setText("");
                    location.setText("");
                }
            }
        });

        content_panel.add(cont_title);
        content_panel.add(Box.createVerticalStrut(15));
        content_panel.add(cont_text);
        content_panel.add(Box.createVerticalStrut(30));
        content_panel.add(data_panel);
        content_panel.add(Box.createVerticalStrut(45));
        content_panel.add(send);

        return content_panel;
    }

    private JPanel contact_datas()
    {
        JLabel contact_label = new JLabel("Contactez-nous pour proteger votre reseau !");
        contact_label.setFont(TITLE_FONT);
        contact_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        contact_label.setForeground(ACCENT_COLOR);
        contact_label.setBackground(new Color(90, 139, 87));

        JTextArea cont_describe = new JTextArea("Chez Network Protection Kit (NPK), nous sommes les gerants de votre securite\n" + 
                                                    " numerique. Nous sommes la pour vous aider a proteger votre reseau contre les \n" + 
                                                    "attaques et menaces , tout en vous offrant un service client exceptionnel."
                                                );

        cont_describe.setEditable(false);
        cont_describe.setFont(BODY_FONT);
        cont_describe.setOpaque(false);
        cont_describe.setLineWrap(true);
        cont_describe.setWrapStyleWord(true);
        cont_describe.setForeground(TEXT_DARK);

        //-----------------------------------------------------------

        JLabel coord = new JLabel("Nos coordonnees:");
        coord.setFont(SUBTITLE_FONT);
        coord.setAlignmentX(Component.CENTER_ALIGNMENT);
        coord.setForeground(Color.BLACK);

        JTextArea coord_data = new JTextArea("\t==> Adresse : 123, rue de la securite, 75001 Paris\n" +
                                              "\t==> Telephone : +237 6 73 26 28 34\n" +
                                              "\t==> Fax : +237 6 73 26 28 34\n" +
                                              "\t==> Email : linedevils271@gmail.com\n" +
                                              "\t==> Site web : thhps://npk_page.google.ki94935845/ghewiewrf/npk/prt.com"
                                            );

        coord_data.setEditable(false);
        coord_data.setFont(BODY_FONT);
        coord_data.setLineWrap(true);
        coord_data.setWrapStyleWord(true);
        coord_data.setForeground(TEXT_DARK);

        //------------------------------------------------------

        JLabel hour = new JLabel("Heures d'ouvrture : ");
        hour.setFont(SUBTITLE_FONT);
        hour.setAlignmentX(Component.CENTER_ALIGNMENT);
        hour.setForeground(new Color(46, 139, 87));

        JTextArea op_hour = new JTextArea("==> 24h/24 <----> 7j/7 en ligne");
        op_hour.setEditable(false);
        op_hour.setFont(BODY_FONT);
        op_hour.setLineWrap(true);
        op_hour.setWrapStyleWord(true);
        op_hour.setForeground(TEXT_DARK);

        //----------------------------------------------------------

        JLabel team_title = new JLabel("Notre equipe");
        team_title.setFont(SUBTITLE_FONT);
        team_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        team_title.setForeground(ACCENT_COLOR);

        JTextArea team = new JTextArea(".\n.\n.\n.\n.");
        team.setEditable(false);
        team.setFont(BODY_FONT);
        team.setLineWrap(true);
        team.setWrapStyleWord(true);
        team.setForeground(TEXT_DARK);

        //--------------------------------------------------------

        JLabel help_title = new JLabel("Nous sommes la pour aider");
        help_title.setFont(SUBTITLE_FONT);
        help_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        help_title.setForeground(new Color(50, 139, 100));

        JTextArea help = new JTextArea("Si vous avez des questions ou des preocupations concernant votre \n"+
                                        "securite,une application,un site web etc, n'hesitez pas a nous contacter.\n" + 
                                        "Nous sommes la pour aider a proteger votre reseau, developper vos logiciels\n" +
                                        ",stes web et surtout a vous offrir un service exceptionnel"
                                    );
        help.setEditable(false);
        help.setFont(BODY_FONT);
        help.setLineWrap(true);
        help.setWrapStyleWord(true);
        help.setForeground(TEXT_DARK);

        //-----------------------------------------------------

        JLabel how_help = new JLabel("Comment nous trouver ?");
        how_help.setFont(SUBTITLE_FONT);
        how_help.setAlignmentX(Component.CENTER_ALIGNMENT);
        how_help.setForeground(TEXT_DARK);

        JTextArea h_text = new JTextArea("Nous sommes situes au coeur du Cameroun a proximite de Bastos.\n" + 
                                        "Vous pouvez nous rejoindre a tout moment en ligne.\n"
                                        );

        h_text.setEditable(false);
        h_text.setFont(BODY_FONT);
        h_text.setLineWrap(true);
        h_text.setWrapStyleWord(true);
        h_text.setForeground(TEXT_DARK);

        //------------------------------------------------

        JLabel answer = new JLabel("Nous repondons a vos questions !");
        answer.setFont(SUBTITLE_FONT);
        answer.setAlignmentX(Component.CENTER_ALIGNMENT);
        answer.setForeground(ACCENT_COLOR);

        JTextArea answer_text = new JTextArea("Nous sommes la pour vous aider, alors n'hesitez pas a nous contacter.\n" + 
                                                "Nous repondons a vos questions et a vos preocupations dans les plus\n" + 
                                                "bref delais"
                                            );
        answer_text.setEditable(false);
        answer_text.setFont(BODY_FONT);
        answer_text.setLineWrap(true);
        answer_text.setWrapStyleWord(true);
        answer_text.setForeground(TEXT_DARK);

        //-----------------------------------------

        JLabel conf_title = new JLabel("Merci de votre confiance !");
        conf_title.setFont(SUBTITLE_FONT);
        conf_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        conf_title.setForeground(new Color(46, 87, 139));

        JTextArea conf_text = new JTextArea("Nous sommes fiers de vous compter parmi nos clients et nous nous \n" +
                                            "engageons a proteger votre reseau avec le plus grand soin. Merci de\n" + 
                                            "votre confiance en Network Protection Kit (NPK) ! "
                                            );
        conf_text.setEditable(false);
        conf_text.setFont(BODY_FONT);
        conf_text.setLineWrap(true);
        conf_text.setWrapStyleWord(true);
        conf_text.setForeground(TEXT_DARK);

        //--------------------------------------------

        JLabel follow_title = new JLabel("Suivez-nous sur les reseaux sociaux !");
        follow_title.setFont(SUBTITLE_FONT);
        follow_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        follow_title.setForeground(ACCENT_COLOR);

        JTextArea follow_text = new JTextArea("Facebook : https://www.facebook.com/profile.php?d=61560335830064\n"+
                                                "Twitter : @npkcyber\n" +
                                                "LinkedIn : NPK-Network Protection Kit"
                                                );
        follow_text.setEditable(false);
        follow_text.setFont(BODY_FONT);
        follow_text.setLineWrap(true);
        follow_text.setWrapStyleWord(true);
        follow_text.setForeground(TEXT_DARK);

        Border border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            "Network Protection Kit (NPK)", TitledBorder.LEFT, TitledBorder.TOP, SUBTITLE_FONT, PRIMARY_COLOR
        );

        JPanel contact_content = new JPanel();
        contact_content.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(40, 40, 40, 40),
            border
        ));
        contact_content.setOpaque(true);
        contact_content.setBackground(new Color(255,255,255,240));
        contact_content.setLayout(new BoxLayout(contact_content,BoxLayout.Y_AXIS));
        
        contact_content.add(contact_label);
        contact_content.add(Box.createVerticalStrut(30));
        contact_content.add(cont_describe);
        contact_content.add(Box.createVerticalStrut(15));
        contact_content.add(coord);
        contact_content.add(Box.createVerticalStrut(30));
        contact_content.add(coord_data);
        contact_content.add(Box.createVerticalStrut(30));
        contact_content.add(hour);
        contact_content.add(Box.createVerticalStrut(15));
        contact_content.add(op_hour);
        contact_content.add(Box.createVerticalStrut(30));
        contact_content.add(team_title);
        contact_content.add(Box.createVerticalStrut(15));
        contact_content.add(team);
        contact_content.add(Box.createVerticalStrut(30));
        contact_content.add(help_title);
        contact_content.add(Box.createVerticalStrut(15));
        contact_content.add(help);
        contact_content.add(Box.createVerticalStrut(30));
        contact_content.add(how_help);
        contact_content.add(Box.createVerticalStrut(15));
        contact_content.add(h_text);
        contact_content.add(Box.createVerticalStrut(30));
        contact_content.add(contact_form());
        contact_content.add(Box.createVerticalStrut(30));
        contact_content.add(answer);
        contact_content.add(Box.createVerticalStrut(15));
        contact_content.add(answer_text);
        contact_content.add(Box.createVerticalStrut(30));
        contact_content.add(conf_title);
        contact_content.add(Box.createVerticalStrut(15));
        contact_content.add(conf_text);
        contact_content.add(Box.createVerticalStrut(30));
        contact_content.add(follow_title);
        contact_content.add(Box.createVerticalStrut(15));
        contact_content.add(follow_text);
        contact_content.add(Box.createVerticalStrut(30));

        return contact_content;
    }
}