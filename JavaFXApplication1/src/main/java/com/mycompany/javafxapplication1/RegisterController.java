/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ntu-user
 */
public class RegisterController {

     private String userName;
    
     private AuditTrail log;  
  
    public void setUserName(String userName) {
        this.userName = userName;
    }
    @FXML
    private Button registerBtn;

    @FXML
    private Button backLoginBtn;

    @FXML
    private PasswordField passPasswordField;

    @FXML
    private PasswordField rePassPasswordField;

    @FXML
    private TextField userTextField;
    
    @FXML
    private Button selectBtn;
  

    private void dialogue(String headerMsg, String contentMsg) {
        Stage secondaryStage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 300, 300, Color.DARKGRAY);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contentMsg);
        Optional<ButtonType> result = alert.showAndWait();
    }

    @FXML
    private void registerBtnHandler(ActionEvent event) {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) registerBtn.getScene().getWindow();
        if(!userTextField.getText().isEmpty()){
             if(!passPasswordField.getText().isEmpty()){
                try {
                    FXMLLoader loader = new FXMLLoader();
                    DB myObj = new DB();
                    if(myObj.isUserExists(userTextField.getText())==false){
                        if (passPasswordField.getText().equals(rePassPasswordField.getText())) {
                            myObj.addDataToDB(userTextField.getText(), passPasswordField.getText());
                            log.RegisterLog(userTextField.getText());
                            log.LoginLog(userTextField.getText());
                            dialogue("Adding information to the database", "Successful!");
                            loader.setLocation(getClass().getResource("Menu.fxml"));
                            Parent root = loader.load();
                            Scene scene = new Scene(root, 640, 480);
                            secondaryStage.setScene(scene);
                            MenuController controller = loader.getController();
                            controller.setUserName(userTextField.getText());
                            secondaryStage.setTitle("Menu");

                        } else {
                            loader.setLocation(getClass().getResource("register.fxml"));
                            Parent root = loader.load();
                            Scene scene = new Scene(root, 640, 480);
                            secondaryStage.setScene(scene);
                            secondaryStage.setTitle("Register a new User");
                            dialogue("Passwords do not match!","Try again!");
                        }
                        secondaryStage.show();
                        primaryStage.close();
                    }else{
                         dialogue("user already exists","try again");
                    }            
                } catch (Exception e) {
                java.util.logging.Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, e);
       
                    }
             }
             else{
             dialogue("password cannot be empty","try again");}
        }
        else{
        dialogue("user name cannot be empty","try again");}
    }

    @FXML
    private void backLoginBtnHandler(ActionEvent event) {
        Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) backLoginBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Login");
            secondaryStage.show();
            primaryStage.close();  
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
