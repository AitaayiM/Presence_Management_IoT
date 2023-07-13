package presence;

import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.SwingWorker;
import javax.swing.JOptionPane;

import dataBase.DataAcces;
import dataBase.Student;

import java.sql.SQLException;
import java.util.Optional;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerWorker extends SwingWorker<Void, Void> {
    StudentView studentView=new StudentView();
    @Override
    protected Void doInBackground() throws Exception {
        int port = 1234;
        try (ServerSocket serverSocket = new ServerSocket(port)){
            while(true){
                if(netIsAvailable()){
                    Socket clientSocket = serverSocket.accept();                  
                    Thread clientThread = new Thread(() -> {
                        try {
                            // Traitez les demandes du client
                            handleClientRequest(clientSocket);
                        } catch (IOException | SQLException e) {
                            JOptionPane.showMessageDialog(null, e.getMessage());
                            DataAcces.error(e);
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                JOptionPane.showMessageDialog(null, e.getMessage());
                                DataAcces.error(e);
                            }
                        }
                    });
                    clientThread.start();
                }
            }
        } catch (IOException e) {
          JOptionPane.showMessageDialog(null, e.getMessage());
          DataAcces.error(e);
        }
        return null;
    }

    private void handleClientRequest(Socket clientSocket) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String data = reader.readLine();
        Student student=DataAcces.findById(data);
        Optional<Student> studentOptional = Optional.ofNullable(student);
        if(studentOptional.isPresent()){
          studentView.setInfo(student);
        }else{
          studentView.eraseInfo(data);
        }
        clientSocket.close();
    }

    private boolean netIsAvailable() {
        try {
            Process process = Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Error checking internet connection: " + e.getMessage());
            DataAcces.error(e);
        }
        return false;
    }

}

