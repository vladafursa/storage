/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.util.logging.Level;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;



public class TerminalController {
    private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @FXML
    private Button submitTerminalButton;
    
    @FXML
    private Button BackBtn;
    
    
    @FXML
    private TextField terminalTextField;
    
    @FXML
    private TextArea terminalTextArea;
    
     public void submitTerminalButtonHandler(ActionEvent event) throws IOException{
       TerminalCommandsImplementation obj = new TerminalCommandsImplementation();
       String terminalCommand=terminalTextField.getText();
       terminalTextField.clear();
       String result=obj.runCommand(terminalCommand);
       terminalTextArea.setText(result);
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
            java.util.logging.Logger.getLogger(TerminalController.class.getName()).log(Level.SEVERE, null, e);
       
        }
    }
    
}
