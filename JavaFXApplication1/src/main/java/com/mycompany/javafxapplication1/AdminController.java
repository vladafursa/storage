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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;

/**
 *
 * @author ntu-user
 */
public class AdminController {
      private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }
 private AuditTrail log;
    @FXML 
    private Button RtnButton;
  
      @FXML 
    private Button metadata;
    @FXML 
    private Button permissionsBtn;
    @FXML
    private TableView<User> adminTableView;
     @FXML
     private Button auditTrailBtn;
    @FXML
     private Button KeyBtn;
    public void initialise(){
    DB DB = new DB();
    ObservableList<User> data;

    try {
        data = DB.getDataFromTable();
        TableColumn user = new TableColumn("User");
        user.setCellValueFactory(
        new PropertyValueFactory<>("user"));
        TableColumn pass = new TableColumn("Pass");
        pass.setCellValueFactory(
        new PropertyValueFactory<>("pass"));
        adminTableView.setItems(data);
        adminTableView.getColumns().addAll(user, pass);
        

    } catch (ClassNotFoundException ex) {
       Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
    }
  
 
}
     
  @FXML
  private void RtnHandler(ActionEvent a){
  Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) RtnButton.getScene().getWindow();
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
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
        }
  }
   @FXML
  private void openMetadata(ActionEvent a){
  Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) metadata.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("metadata.fxml"));
            Parent root = loader.load();
            MetadataController controller = loader.getController();
            controller.setUserName(userName);
            controller.initialise();
            log.ActionLog(userName + " accessed Metadata");
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Metadata");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException e) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
        }
  }
     @FXML
  private void openAuditTrail(ActionEvent a){
      Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) auditTrailBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("auditTrail.fxml"));
            Parent root = loader.load();
            auditTrailController controller = loader.getController();
            controller.setUserName(userName);
             log.ActionLog(userName + " accessed audit trail");
            controller.initialise();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Audit trail");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException e) {
           Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
        }
  }
  

  
     @FXML
  private void openPermissions(ActionEvent a){
     Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) permissionsBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ACL.fxml"));
            Parent root = loader.load();
            ACLController controller = loader.getController();
            controller.setUserName(userName);
            controller.initialise();
            log.ActionLog(userName + " accessed ACL");
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("ACL");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException e) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
        }
  } 
      @FXML
  private void openKeys(ActionEvent a){
     Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) permissionsBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("keys.fxml"));
            Parent root = loader.load();
            KeyController controller = loader.getController();
            controller.setUserName(userName);
            controller.initialise();
            log.ActionLog(userName + " accessed Key table");
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Keys");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException e) {
             Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
        }
  }
}

