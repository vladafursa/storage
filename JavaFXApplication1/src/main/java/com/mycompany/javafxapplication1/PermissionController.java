/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ntu-user
 */
public class PermissionController {
    private String userName;
    private String Filename="";
    private String sharedUserName="";
    public void setUserName(String userName) {
        this.userName = userName;
    }
  @FXML
  TableView fileSelectionTableView;
  @FXML
  private Button BackBtn;
  @FXML
  private Button allowReadBtn;
  @FXML
  private Button allowWriteBtn;
    @FXML
  private Button revokeReadBtn;
  @FXML
  private Button revokeWriteBtn;
  @FXML
  private Label status;
   @FXML
  private Label whatFile;
    @FXML
  private Label whatUser;
  @FXML
  private TextField selectUserField;
  private AuditTrail log;
  @FXML
private TableColumn<String, String> FileName;
  public void initialize() throws ClassNotFoundException{
   DB fileDB=new DB();
   try{
   ObservableList<String> data = fileDB.RetrieveNamesOfFilesOfUserThatOwnsThem(userName);
    fileSelectionTableView.setItems(data);
    FileName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));}
   catch(ClassNotFoundException e){
        java.util.logging.Logger.getLogger(PermissionController.class.getName()).log(Level.SEVERE, null, e);
   }
    
   }
  public void selectFile(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
           Filename = (String)fileSelectionTableView.getSelectionModel().getSelectedItem(); 
           whatFile.setText("You selected "+Filename);
           
    }
  }
  @FXML
   public void allowRead(ActionEvent event) throws IOException, InvalidKeySpecException, ClassNotFoundException, SQLException{
   DB FileDB= new DB();
   try{
        if(!Filename.isEmpty()){
             if(!sharedUserName.isEmpty()){
                 if(!FileDB.doesAnyPermissionExist(Filename, sharedUserName)){
                 FileDB.addDataToACL(Filename, sharedUserName, 0, 0);}
                 FileDB.grantReadPermission(Filename, sharedUserName);
                 status.setText("successfully granted permission to read");
                 log.ActionLog(userName + " allowed reading on " + Filename +" to "+sharedUserName);
             }
             else{
             status.setText("user wasn't chosen");}
        }
        else{
        status.setText("File wasn't chosen");}
       }
   catch(Exception e){
     java.util.logging.Logger.getLogger(PermissionController.class.getName()).log(Level.SEVERE, null, e);}
   }
   
   @FXML
   public void allowWrite(ActionEvent event) throws IOException, InvalidKeySpecException, ClassNotFoundException, SQLException{
  DB FileDB= new DB();
  try{
    if(!Filename.isEmpty()){
         if(!sharedUserName.isEmpty()){
             if(!FileDB.doesAnyPermissionExist(Filename, sharedUserName)){
             FileDB.addDataToACL(Filename, sharedUserName, 0, 0);}
             FileDB.grantWritePermission(Filename, sharedUserName);
             status.setText("successfully granted permission to write");
              log.ActionLog(userName + " allowed writing on " + Filename +" to "+sharedUserName);
         }
         else{
         status.setText("user wasn't chosen");}
    }
    else{
    status.setText("File wasn't chosen");}
    }
  catch(Exception e){
     java.util.logging.Logger.getLogger(PermissionController.class.getName()).log(Level.SEVERE, null, e);
  }
   }
   
    
   @FXML
   public void revokeRead(ActionEvent event) throws IOException, InvalidKeySpecException, ClassNotFoundException, SQLException{
     DB FileDB= new DB();
     try{
    if(!Filename.isEmpty()){
         if(!sharedUserName.isEmpty()){
             if(!FileDB.doesAnyPermissionExist(Filename, sharedUserName)){
             FileDB.addDataToACL(Filename, sharedUserName, 0, 0);}
             FileDB.revokeReadPermission(Filename, sharedUserName);
             status.setText("successfully revoked permission to read");
              log.ActionLog(userName + " revoked reading on " + Filename +" to "+sharedUserName);
         }
         else{
         status.setText("user wasn't chosen");}
    }
    else{
    status.setText("File wasn't chosen");}
    }
    catch(Exception e){
     java.util.logging.Logger.getLogger(PermissionController.class.getName()).log(Level.SEVERE, null, e);
  }
   }
   @FXML
   public void revokeWrite(ActionEvent event) throws IOException, InvalidKeySpecException, ClassNotFoundException, SQLException{
        DB FileDB= new DB();
   try{
    if(!Filename.isEmpty()){
         if(!sharedUserName.isEmpty()){
             if(!FileDB.doesAnyPermissionExist(Filename, sharedUserName)){
             FileDB.addDataToACL(Filename, sharedUserName, 0, 0);}
             FileDB.revokeWritePermission(Filename, sharedUserName);
             status.setText("successfully revoked permission to write");
              log.ActionLog(userName + " revoked writing on " + Filename +" to "+sharedUserName);
         }
         else{
         status.setText("user wasn't chosen");}
    }
    else{
    status.setText("File wasn't chosen");}
     }
    catch(Exception e){
     java.util.logging.Logger.getLogger(PermissionController.class.getName()).log(Level.SEVERE, null, e);
  }
  }
   @FXML
   public void selectUser(ActionEvent event) throws IOException, SQLException, ClassNotFoundException{
     DB fileDB=new DB();
     String checkName;
     checkName=selectUserField.getText();
   if(fileDB.isUserExists(checkName))
   {if(!checkName.equalsIgnoreCase(userName))
                {sharedUserName=checkName;
                whatUser.setText("You selected "+ sharedUserName);
                }
   else{whatUser.setText("user can't grant permission to itself");
   }
   }
   else{
   whatUser.setText("user doesn't exist");}
    }
   @FXML
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
        } catch (IOException ex) {
           java.util.logging.Logger.getLogger(PermissionController.class.getName()).log(Level.SEVERE, null, ex);
      
        }
    }
   
   
   
    private void dialogue(String headerMsg, String contentMsg) {
        
        Group root = new Group();
        Scene scene = new Scene(root, 300, 300, Color.DARKGRAY);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contentMsg);
        Optional<ButtonType> result = alert.showAndWait();
    }

}
