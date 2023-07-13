package presence;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.logging.log4j.core.config.Configurator;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;

import dataBase.DataAcces;
import dataBase.Student;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.awt.Toolkit;


public class StudentView extends JFrame {
    private JPanel pD, read;
    private Toolkit tool;
    private JButton insert, update, delete;
    private JTabbedPane tp;
    private TableRowSorter<TableModel> sort;
    private static JTable table;
    private JTextField find;
    private static JLabel idLabel, nameLabel, genderLabel, emailLabel, mobileLabel, msgLabel;
    private static DefaultTableModel model;
      
    public StudentView(){}
    public StudentView(int Largeur, int Hauteur, String title) throws SQLException{
        super(title);
        setSize(Largeur, Hauteur);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        tool= Toolkit.getDefaultToolkit();
        setIconImage(tool.getImage("src//main//java//images//insert.png"));

        ImageIcon orImageIcon= new ImageIcon("src//main//java//images//insert.png");
        Image originalImage = orImageIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(300, 250, Image.SCALE_SMOOTH);
        JLabel image= new JLabel(new ImageIcon(resizedImage));
    
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalStrut(120));             
        
        pD = new JPanel(new BorderLayout());
        
        String columns[] = {"present","id", "name", "gender", "email", "mobile"};          
        
        model = new DefaultTableModel();
        for (int index = 0; index < columns.length; index++) {
          model.addColumn(columns[index]);
        }
        List<Student> students= DataAcces.findAll();
        Optional<List<Student>> studentOptional = Optional.ofNullable(students);
        if(studentOptional.isPresent()){
          for(Student data: students){
            model.addRow(new Object[]{
              false,
              data.getId(),
              data.getName(),
              data.getGender(),
              data.getEmail(),
              data.getMobile()
            });
          }
        }

        table = new JTable(model){
          public Class<?> getColumnClass(int column) {
            //renvoie Boolean.class
            return getValueAt(0, column).getClass(); 
          }
        };
        find = new JTextField();
        sort = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sort);
        table.setShowGrid(true);
        table.setShowVerticalLines(true);
        table.setFillsViewportHeight(true);
        table.setCellSelectionEnabled(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setEnabled(true);
        table.setBackground(UIManager.getColor("Button.hightlight"));
        table.setSurrendersFocusOnKeystroke(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setColumnSelectionAllowed(true);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane pane = new JScrollPane(table);
        pD.add(pane, BorderLayout.CENTER);

        finder();
        pD.add(find, BorderLayout.NORTH);

        insert = new JButton();
        ImageIcon icon1 = new ImageIcon("src//main//java//images//added.png");
        Image resizedInsert = icon1.getImage().getScaledInstance(33, 33, java.awt.Image.SCALE_SMOOTH);
        ImageIcon resizedIcon1 = new ImageIcon(resizedInsert);
        insert.setIcon(resizedIcon1);
        update = new JButton();
        ImageIcon icon2 = new ImageIcon("src//main//java//images//update.png");
        Image resizedUpdate = icon2.getImage().getScaledInstance(33, 33, java.awt.Image.SCALE_SMOOTH);
        ImageIcon resizedIcon2 = new ImageIcon(resizedUpdate);
        update.setIcon(resizedIcon2);
        delete = new JButton();
        ImageIcon icon3 = new ImageIcon("src//main//java//images//delete.png");
        Image resizedDelete = icon3.getImage().getScaledInstance(33, 33, java.awt.Image.SCALE_SMOOTH);
        ImageIcon resizedIcon3 = new ImageIcon(resizedDelete);
        delete.setIcon(resizedIcon3);
        CustomButton(insert);
        CustomButton(update);
        CustomButton(delete);


        insert.addActionListener(event->{
          try {
            new InsertStudent(700, 370,"Insert Student");
          } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            DataAcces.error(e);
          }
        });

        delete(model);

        update(model);

        box.add(insert);
        box.add(update);
        box.add(delete);


        pD.add(box, BorderLayout.SOUTH);

        read = new JPanel(null);
        JLabel titleLabel = new JLabel("Student Data");
        titleLabel.setBounds(0, 0, 375, 40);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.decode("#10a0c5"));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        read.add(titleLabel);

        idLabel = new JLabel("  ID: " );
        idLabel.setBounds(50, 70, 400, 20);
        read.add(idLabel);

        nameLabel = new JLabel("  Name: ");
        nameLabel.setBounds(50, 100, 400, 20);
        read.add(nameLabel);

        genderLabel = new JLabel("  Gender: ");
        genderLabel.setBounds(50, 130, 400, 20);
        read.add(genderLabel);

        emailLabel = new JLabel("  Email: ");
        emailLabel.setBounds(50, 160, 400, 20);
        read.add(emailLabel);

        mobileLabel = new JLabel("  Mobile Number: ");
        mobileLabel.setBounds(50, 190, 400, 20);
        read.add(mobileLabel);

        msgLabel = new JLabel();
        msgLabel.setBounds(50, 230, 400, 20);
        msgLabel.setForeground(Color.RED);
        read.add(msgLabel);

        tp=new JTabbedPane();
        tp.setBounds(5,5,800,600);  
        tp.add("Student",pD);  
        tp.add("Read",read);  
        JSplitPane sep = new JSplitPane(SwingConstants.VERTICAL, image, tp);
        sep.setOrientation(SwingConstants.VERTICAL); 
        add(sep);
        setResizable(false);
    }

    private void delete(DefaultTableModel model) {
      delete.addActionListener(event->{
        int i= table.getSelectedRow();
        if(i!=-1){
          String id= (String) model.getValueAt(i, 1);
          int count= DataAcces.deleteById(id);
          if(count>0){
            model.removeRow(i);
            model.fireTableDataChanged();
          }
        }
      });
    }

    private void update(DefaultTableModel model) {
      update.addActionListener(event->{
        int i= table.getSelectedRow();
        if(i!=-1){
          String id= (String) model.getValueAt(i, 1);
          String name= (String) model.getValueAt(i, 2);
          String gender= (String) model.getValueAt(i, 3);
          String email= (String) model.getValueAt(i, 4);
          int mobile= (int) model.getValueAt(i, 5);
          DataAcces.update(Student.getInstance(id, name, gender, email, mobile));
        }
      });
    }

    private void finder() {
      find.getDocument().addDocumentListener(new DocumentListener()
      {
              @Override
              public void insertUpdate(DocumentEvent e) {
                  String str = find.getText();
                  if (str.trim().length() == 0) {
                      sort.setRowFilter(null);
                  } else {
                      sort.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                  }
              }
              @Override
              public void removeUpdate(DocumentEvent e) {
                  String str = find.getText();
                  if (str.trim().length() == 0) {
                      sort.setRowFilter(null);
                  } else {
                      sort.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                  }
              }
              @Override
              public void changedUpdate(DocumentEvent e) {}
              
          });
    }

    private void CustomButton(JButton btn){
      btn.setBackground(new Color(245,245,220));
      btn.setBorderPainted(false);
    } 

    protected void setInfo(Student student){
      StudentView.idLabel.setText("  ID:  "+student.getId());
      StudentView.nameLabel.setText("  Name:  "+student.getName());
      StudentView.genderLabel.setText("  Gender:  "+student.getGender());
      StudentView.emailLabel.setText("  Email:  "+student.getEmail());
      StudentView.mobileLabel.setText("  Mobile Number:  "+student.getMobile());
      StudentView.msgLabel.setText("");
      for (int i = 0; i < StudentView.table.getRowCount(); i++) {
        if(StudentView.model.getValueAt(i, 1).equals(student.getId())) StudentView.table.setValueAt(true, i, 0);
      }
    }

    protected void eraseInfo(String uid){
      StudentView.idLabel.setText("  ID:  "+uid);
      StudentView.nameLabel.setText("  Name:  ");
      StudentView.genderLabel.setText("  Gender:  ");
      StudentView.emailLabel.setText("  Email:  ");
      StudentView.mobileLabel.setText("  Mobile Number:  ");
      StudentView.msgLabel.setText("The person with this ID "+uid+" \n is not registered in this class");
    }

    protected void addToTable(Student student){
      StudentView.model.addRow(
        new Object[]{
          true,
          student.getId(),
          student.getName(),
          student.getGender(),
          student.getEmail(),
          student.getMobile()
        }
      );
      StudentView.table.revalidate();
      StudentView.table.repaint();
    }  

    private void nodeMCU_Server(){
        ServerWorker serverWorker = new ServerWorker();
        serverWorker.execute();
    }

    public static void main(String[] args) {

      // Chemin de votre fichier de configuration
      String log4jConfPath = "src/main/java/configuration/log4j.xml";
      // Chargement de la configuration log4j
      Configurator.initialize(null, log4jConfPath);

      java.awt.EventQueue.invokeLater(new Runnable() {
          @Override
          public void run(){
              try {
                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated( true );
                FlatLaf.install(new FlatArcOrangeIJTheme());                        
                StudentView student=new StudentView(700, 395,"Students");
                student.nodeMCU_Server();
              } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                DataAcces.error(e);
              }
          }
      });
  }
}
