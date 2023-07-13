package presence;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.text.MaskFormatter;

import dataBase.DataAcces;
import dataBase.Student;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class InsertStudent extends JFrame{
                JPanel pE,Basic;
                Toolkit tool;
                Image icone;
                StudentView studentView=new StudentView();
                final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                    public InsertStudent(int Largeur, int Hauteur, String title) throws ParseException{
                        super(title);
                        setSize(Largeur, Hauteur);
                        setLocationRelativeTo(null);
                        addWindowListener(new WindowAdapter(){
                            public void windowClosing(WindowEvent windowEvent){
                                setVisible(false);
                             } 
                        });
                        setVisible(true);
            
                        tool= Toolkit.getDefaultToolkit();
                        setIconImage(tool.getImage("src//main//java//images//insert.png"));

                        Basic = new JPanel(new BorderLayout());
                        pE = new JPanel(new GridLayout(9,2,5,5));
                        pE.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                        pE.setPreferredSize(new Dimension(200,100));
                        pE.setBackground(new Color(245,245,220));

                        JLabel prenom = new JLabel("  Fist Name");
                        prenom.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                        JTextField txtprenom = new JTextField();
                        pE.add(prenom);
                        pE.add(txtprenom);

                        JLabel nom = new JLabel("  Last Name");
                        nom.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                        JTextField txtnom = new JTextField();
                        pE.add(nom);
                        pE.add(txtnom);

                        JLabel uidL = new JLabel("  UID");
                        uidL.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                        JTextField txtE = new JTextField();
                        pE.add(uidL);
                        pE.add(txtE);


                        JLabel genderLabel = new JLabel("  Gender:");
                        genderLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                        JComboBox<?> genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
                        pE.add(genderLabel);
                        pE.add(genderComboBox);
                        
                        JLabel emailBox = new JLabel("  Email");
                        emailBox.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                        JTextField txtemailBox = new JTextField();
                        pE.add(emailBox);
                        pE.add(txtemailBox);
            
                        JLabel serial = new JLabel("  Mobile ");
                        serial.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                        MaskFormatter mask = new MaskFormatter("##########");
                        mask.setPlaceholderCharacter('*');
                        JFormattedTextField txtSer = new JFormattedTextField(mask);
                        pE.add(serial);
                        pE.add(txtSer);
            
                        JLabel check = new JLabel();
                        check.setHorizontalAlignment(SwingConstants.CENTER);
                        check.setForeground(Color.RED);
                        ImageIcon orImageIcon= new ImageIcon("src//main//java//images//insert.png");
                        Image originalImage = orImageIcon.getImage();
                        Image resizedImage = originalImage.getScaledInstance(300, 200, Image.SCALE_SMOOTH);

                        JLabel image= new JLabel(new ImageIcon(resizedImage));
                        
                        JButton SignUp = new JButton("  Add student ");
                        SignUp.setMnemonic(KeyEvent.VK_ENTER);
                        ImageIcon icon1 = new ImageIcon("src//main//java//images//add.jpg");
                        Image resizedSignUp = icon1.getImage().getScaledInstance(33, 33, java.awt.Image.SCALE_SMOOTH);
                        ImageIcon resizedIcon = new ImageIcon(resizedSignUp);
                        SignUp.setIcon(resizedIcon);
                        JButton close = new JButton("  Close ");
                        ImageIcon icon2 = new ImageIcon("src//main//java//images//close.png");
                        Image resizedClose = icon2.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
                        ImageIcon resizedIcon2 = new ImageIcon(resizedClose);
                        close.setIcon(resizedIcon2);
                        CustomButton(SignUp);
                        CustomButton(close);
                        pE.add(SignUp);
                        pE.add(close);
            
                        insertStudent(txtprenom, txtnom, txtE, genderComboBox, txtemailBox, txtSer, check, SignUp);
            
                        close.addActionListener(Event->{
                            setVisible(false);
                        });
            
                        Basic.add(check, BorderLayout.NORTH);
                        Basic.add(pE, BorderLayout.CENTER);
                        JSplitPane sep = new JSplitPane(SwingConstants.VERTICAL, image, Basic);
                        sep.setOrientation(SwingConstants.VERTICAL); 
                        add(sep);
                        setResizable(false);
                    }

    private void insertStudent(JTextField txtprenom, JTextField txtnom, JTextField txtE,
        JComboBox<?> genderComboBox, JTextField txtemailBox, JFormattedTextField txtSer,
        JLabel check, JButton SignUp) {
        SignUp.addActionListener(event -> {
        String username = txtE.getText();
        String mobile = txtSer.getText();
        String firstname = txtprenom.getText();
        String lastname = txtnom.getText();
        String gender = genderComboBox.getSelectedItem().toString();
        String email = txtemailBox.getText();

        if (username.isEmpty() || mobile.isEmpty() || firstname.isEmpty() || lastname.isEmpty()
                || gender.isEmpty() || email.isEmpty()) {
            check.setText(" Fields must be filled ");
            revalidate();
            repaint();
        } else {
            List<Student> UID;
            List<String> listUsers = new LinkedList<>();
            try {
                UID = new DataAcces().getUIDs();
                for (Student uids : UID) {
                    listUsers.add(uids.getId());
                }
                if (listUsers.contains(username)) {
                    check.setText(" Invalid UID ");
                    revalidate();
                    repaint();
                } else {
                    if (email.matches(EMAIL_PATTERN)) {
                        Student std = Student.getInstance(username, lastname + " " + firstname, gender, email, Integer.parseInt(mobile));
                        DataAcces.insert(std);
                        check.setText(" User data are successfully inserted ");
                        studentView.addToTable(std);
                        revalidate();
                        repaint();
                    } else {
                        check.setText(" Invalid email ");
                        revalidate();
                        repaint();
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                DataAcces.error(e);
            }
        }
        });
    }

    public void CustomButton(JButton btn){
        btn.setBackground(new Color(245,245,220));
        btn.setOpaque(false);
    }  
}
