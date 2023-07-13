package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class DataAcces{

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3306;
    private static final String BD_NAME = "gestion";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static Connection connect;
    private static final Logger log=LogManager.getLogger(DataAcces.class);

    public static Connection getConnection(){

        try{
            connect = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, BD_NAME), USERNAME, PASSWORD);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage());
            error(e);
        }
        return connect;
    }
 
    public static void update(Student data){
        Connection con = DataAcces.getConnection();
        if(con==null) return;
        String query = "UPDATE `table_the_iot_projects` SET `name`='"+data.getName()+"',`gender`='"+data.getGender()+"',`email`='"+data.getEmail()+"',`mobile`='"+data.getMobile()+"' WHERE `id`='"+data.getId()+"'" ;
        try(Statement stat=con.createStatement()) {
            int countUp= stat.executeUpdate(query);
            if(countUp>0) JOptionPane.showMessageDialog(null, "Student data are successfully updated");  
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            error(e);
        }finally{
            try {
                con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                error(e);
            }
        }
    }

    public static void insert(Student data){
        Connection con = DataAcces.getConnection();
        if(con==null) return;
        String query = "INSERT INTO table_the_iot_projects (name, id, gender, email, mobile) VALUES ('"+data.getName()+"','"+data.getId()+"','"+data.getGender()+"' ,'"+data.getEmail()+"' , '"+data.getMobile()+"')";    
        try(Statement stat=con.createStatement()){
            int count= stat.executeUpdate(query);
            if(count>0) JOptionPane.showMessageDialog(null, "Student data are successfully inserted");     
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            error(e);
        }finally{
        try {
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            error(e);
        }
        }
    }

    
    public static int deleteById(String id) {
            int count= 0;
            Connection con = DataAcces.getConnection();
            if(con==null) return 0;
            String query = "DELETE FROM table_the_iot_projects WHERE id=?";
            try (PreparedStatement prestat = con.prepareStatement(query)) {
                prestat.setString(1, id);
                if(JOptionPane.showConfirmDialog(null, "   Are you sure you want to delete this Student ", " Confirm the deletion", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                count = prestat.executeUpdate();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null,e.getMessage());
                error(e);
            }finally{
                try {
                    con.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    error(e);
                }
            }
            return count;    
    }
      
    public static List<Student> findAll(){
        Connection con = DataAcces.getConnection();
        if(con==null) return null;
        List<Student> students = new LinkedList<>();
        String query = "SELECT * FROM table_the_iot_projects";
        try(PreparedStatement prestat = con.prepareStatement(query)) {
            ResultSet result = prestat.executeQuery();
            while (result.next()) {
                Student data=Student.getInstance(result.getString("id"), result.getString("name"), result.getString("gender"), result.getString("email"), Integer.parseInt(result.getString("mobile")));
                students.add(data);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            error(e);
        }finally{
            try {
                con.close();
            } catch (SQLException ec) {
                JOptionPane.showMessageDialog(null, ec.getMessage());
                error(ec);
            }
        }
        return students;
    }

    public List<Student> getUIDs() throws SQLException {
        Connection con = DataAcces.getConnection();
        if(con==null) return null;
        List<Student> list = new LinkedList<>();
        String query = "SELECT * FROM table_the_iot_projects";
        try(PreparedStatement prestat = con.prepareStatement(query)){
            ResultSet result = prestat.executeQuery();
            while (result.next()) {
                Student data=Student.getInstance(result.getString("id"), result.getString("name"), result.getString("gender"), result.getString("email"), Integer.parseInt(result.getString("mobile")));
                list.add(data);
            }
        }
        return list;
    }

    public static Student findById(String id) throws SQLException {
        Connection con = DataAcces.getConnection();
        if(con==null) return null;
        Student student=null;
        String query = "SELECT * FROM table_the_iot_projects WHERE id=?";
        try(PreparedStatement prestat = con.prepareStatement(query)){
            prestat.setString(1, id);
            ResultSet result = prestat.executeQuery();
            while (result.next()) {
                student=Student.getInstance(result.getString("id"), result.getString("name"), result.getString("gender"), result.getString("email"), Integer.parseInt(result.getString("mobile")));
            }
        }
        return student;
    }

    public static int getRowNumber(String idTofind){
        int rowNumber = -1;
        Connection connection = DataAcces.getConnection();
        if(connection==null) return -1;
        String query="SELECT row_number FROM (SELECT @row_number:=@row_number+1 AS row_number, id FROM table_the_iot_projects, (SELECT @row_number:=0) AS t) AS temp WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, idTofind);
            ResultSet resultSet = statement.executeQuery();        
            if (resultSet.next()) {
                rowNumber = resultSet.getInt("row_number");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            error(e);
        }finally{
            try {
                connection.close();
            } catch (SQLException ec) {
                JOptionPane.showMessageDialog(null, ec.getMessage());
                error(ec);
            }
        }
        return rowNumber;
    }

    public static void error(Exception e){
        log.info(e.getMessage());
        log.error(e.getMessage());
        log.debug(e.getMessage());
    }
}