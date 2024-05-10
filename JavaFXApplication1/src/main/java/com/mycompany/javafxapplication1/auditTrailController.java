/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.FileNotFoundException; 
import java.util.logging.Level;

/**
 *
 * @author ntu-user
 */
public class auditTrailController {
       private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private AuditTrail log;
    @FXML
    private Button BackBtn;
    
     
    @FXML
    private TextArea auditTrailArea;
    public void initialise() throws FileNotFoundException, IOException{
    File logFile=new File("logs.txt"); 
    if (logFile.exists()) {
        Scanner myReader = new Scanner(logFile);
        String data="";
        while (myReader.hasNextLine()) {
        data += myReader.nextLine()+"\n";
        }
        auditTrailArea.setText(data);
        myReader.close();
    }
    else{
             logFile.createNewFile();
        }
    } 
     public void Back(ActionEvent event) throws IOException{
      Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) BackBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Menu.fxml"));
            Parent root = loader.load();
            MenuController controller = loader.getController();
            controller.setUserName(userName);
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Menu");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException e) {
           java.util.logging.Logger.getLogger(auditTrailController.class.getName()).log(Level.SEVERE, null, e);

        }
    }  
}
