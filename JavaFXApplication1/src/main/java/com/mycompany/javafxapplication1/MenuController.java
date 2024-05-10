/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;
import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
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
 *
 * @author ntu-user
 */
public class MenuController {
     private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }
  
    @FXML private Button TerminalBtn;
    
    @FXML private Button DownloadBtn;
    @FXML private Button shareFileButton;
    @FXML private Button CreateBtn;
    @FXML private Button uploadBtn;
    @FXML private Button AdminBtn;
    @FXML private Button RtrnBtn;
    @FXML private Button RemoteTerminalBtn;
    @FXML private Button binBtn;
    @FXML private Button myAccount;
 
    @FXML
public void returner(ActionEvent action){
     Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) RtrnBtn.getScene().getWindow();
        try {
            log.LogoffLog(userName);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Login");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
      
        }
}
@FXML
    
public void creator(ActionEvent action){
     Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) CreateBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fileswindow.fxml"));
            Parent root = loader.load();
            fileManagementController controller = loader.getController();
            controller.setUserName(userName);
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("File management");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
      
        }
}
@FXML
public void AdminHandler(ActionEvent action) throws ClassNotFoundException, SQLException, InvalidKeySpecException, IOException{
     Stage secondaryStage = new Stage();
     
        Stage primaryStage = (Stage) AdminBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Admin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Admin Access");
            AdminController controller=loader.getController();
            controller.setUserName(userName);
            controller.initialise();
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException ex) {
           java.util.logging.Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
      
        }
}
public void openBin(ActionEvent action){
     Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) binBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("bin.fxml"));
            Parent root = loader.load();
            binController controller = loader.getController();
            controller.initialise();
            controller.setUserName(userName);
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Bin");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException ex) {
              java.util.logging.Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
      
        }
}
@FXML

    
public void TerminalSwitch(ActionEvent action){
     Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) TerminalBtn.getScene().getWindow();
        try {
            log.TerminalLog(userName);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("terminal.fxml"));
            Parent root = loader.load();
            TerminalController controller = loader.getController();
            controller.setUserName(userName);
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Terminal");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException ex) {
           java.util.logging.Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
      
        }
}
@FXML
public void UploadSwitch(ActionEvent action){
     Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) uploadBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("upload.fxml"));
            Parent root = loader.load();
            uploadController controller = loader.getController();
            controller.setUserName(userName);
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Upload file");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException ex) {
              java.util.logging.Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
      
        }
}
@FXML
public void shareSwitch(ActionEvent action) throws ClassNotFoundException{
  Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) shareFileButton.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("permission.fxml"));
            Parent root = loader.load();
            PermissionController controller = loader.getController();
            controller.setUserName(userName);
            controller.initialize();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Grant/revoke permissions");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException ex) {
               java.util.logging.Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
      
        }
}
@FXML
public void DownloadSwitch(ActionEvent action) throws ClassNotFoundException{
       Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) DownloadBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("download.fxml"));
            Parent root = loader.load();
            DownloadController controller = loader.getController();
            controller.setUserName(userName);
            controller.initialize();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Download file");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
      
        } 
}
@FXML
public void RemoteTerminalSwitch(ActionEvent action){
     Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) RemoteTerminalBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("containerSelection.fxml"));
            Parent root = loader.load();
            RemoteTerminalController controller = loader.getController();
            controller.setUserName(userName);
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Remote Terminal");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException ex) {
              java.util.logging.Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
      
        }
}

@FXML
public void openMyAccount(ActionEvent action){
     Stage secondaryStage = new Stage();
        Stage primaryStage = (Stage) RemoteTerminalBtn.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Update.fxml"));
            Parent root = loader.load();
            Update controller = loader.getController();
            controller.setUserName(userName);
            controller.initialise();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("User details");
            secondaryStage.show();
            primaryStage.close();
        } catch (IOException ex) {
              java.util.logging.Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
      
        }
}
private AuditTrail log;
    
}
