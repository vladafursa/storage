/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;
import java.io.IOException;
import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.ResourceBundle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;


/**
 *
 * @author ntu-user
 */
public class Update {
    
   private String userName;
    public void setUserName(String userName) {
        this.userName = userName;
    }
   private String Filename;
    @FXML 
            private TextField textfield;
    @FXML
    private PasswordField pass1;
    @FXML
    private PasswordField pass2;
    @FXML 
    private Button returnTo;
    @FXML 
    private Button confirm;
    @FXML 
    private Button deleteMyAccount;
    @FXML
    private Label userNameLabel;
    
    private AuditTrail log;
    

public void initialise(){
userNameLabel.setText(userName);
}
    
@FXML  
 public void updateform(ActionEvent event) throws SQLException, ClassNotFoundException, InvalidKeySpecException{
     DB myobj=new DB();
     if(!pass1.getText().isEmpty()){
        if(pass1.getText().equals(pass2.getText())){
        myobj.UpdateTable(userName, pass1.getText());
        dialogue("Succesfully Updated  Profile","Close");
        log.ActionLog(userName + "changed password ");
        }
        else{
        dialogue("passwords do not match","Close");
        }
    }
    else{
        dialogue("password cannot be empty","Close");
        }
}
 
@FXML
private void backToLogin(ActionEvent event){
    Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) returnTo.getScene().getWindow();
        DB myObj = new DB();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Login");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException e) {
           java.util.logging.Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, e);
        }
    
}
 @FXML
private void DeleteMyself(ActionEvent event)  throws SQLException, ClassNotFoundException, InvalidKeySpecException{
    DB myObj=new DB();
    myObj.deleteRow(userName);
       dialogue("Succesfully Deleted  Profile","Return to Login");
        log.ActionLog(userName + " deleted account");
       Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) deleteMyAccount.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Login");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException e) {
           java.util.logging.Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, e);
        }
    
}
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

    
}