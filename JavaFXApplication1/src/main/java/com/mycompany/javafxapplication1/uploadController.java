/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.FileWriter;
import java.io.FileNotFoundException;  
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
/**
 *
 * @author ntu-user
 */
public class uploadController {
    private String userName;
    private DisrtibutionAroundContainers containersManager;
    
    private FileChunking chunkingMechanism;
    public void setUserName(String userName) {
        this.userName = userName;
    }
    private String pathOfSelectedFile=""; 
    private AuditTrail log;
   @FXML
    private Button selectFileButton;
       
    @FXML
    private Button uploadFileButton;
    
    
    @FXML
    private Button BackBtn;
    
    @FXML
    private Label statusLabel;
    @FXML
    private Label  selectedLabel;
    
     @FXML
    public void selectFile(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) selectFileButton.getScene().getWindow();
        primaryStage.setTitle("Select a File");
        FileChooser fileChooser = new FileChooser();
        File initialDirectory = new File("/home/ntu-user/App");
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        
        if(selectedFile!=null){
            pathOfSelectedFile=(String)selectedFile.getCanonicalPath();
            selectedLabel.setText("you selected "+ selectedFile.getName());
        }
        
    }

    public File creatingFileForUploading(String pathToTheFile) throws InvalidKeySpecException, IOException, SQLException, ClassNotFoundException{
        File selectedFile=new File(pathToTheFile);
        return selectedFile;
    }
    public void uploadFile(ActionEvent event) throws InvalidKeySpecException, IOException, SQLException, ClassNotFoundException{
       if(!pathOfSelectedFile.isEmpty()){
        
        try{  File uploadedFile=new File(pathOfSelectedFile);
         DB fileDB=new DB();
         fileDB.addDataToKeys(uploadedFile.getName(), uploadedFile.getName());
         fileDB.addDataToFileDB(uploadedFile.getName(), (float) uploadedFile.length(), new Date().toString(), new Date().toString(),userName);
         log.FileLog(userName, "uploaded", uploadedFile.getName());
         DisrtibutionAroundContainers containersManager1=new DisrtibutionAroundContainers();
         containersManager1.sendFileToContainers(uploadedFile);
         statusLabel.setText("successfully uploaded "+ uploadedFile.getName());}
         catch (InvalidKeySpecException | IOException | ClassNotFoundException e) {
          java.util.logging.Logger.getLogger(uploadController.class.getName()).log(Level.SEVERE, null, e);
       }
       }
       else{
           statusLabel.setText("You didn't select a file");}
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
            java.util.logging.Logger.getLogger(uploadController.class.getName()).log(Level.SEVERE, null, e);
       
        }
    }

}
