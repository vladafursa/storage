/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;
import java.nio.file.*;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.FileWriter;
import java.io.FileNotFoundException;  
import java.util.Date;
import java.util.Scanner;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
/**
 *
 * @author ntu-user
 */
public class fileManagementController {
    private String userName;
  

    public void setUserName(String userName) {
        this.userName = userName;
    }
    private AuditTrail log;
    
    @FXML
    private Button createButton;
       
    @FXML
    private Button updateButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button BackBtn;
    
    @FXML
    private TextField fileNameField;
    
    @FXML
    private TextArea statusArea;
    
    @FXML
    private TextArea contentArea;
    
    @FXML
    private Button saveButton;
    
     public void createFileHandler(ActionEvent event)  throws InvalidKeySpecException, IOException, SQLException, ClassNotFoundException{
         String fileName = fileNameField.getText();  
         if(!fileName.isEmpty()){
                File myObj = new File(fileName);
                DB fileDB=new DB();  
                try {
                   if (!fileDB.isFileExists(fileName)) {
                       myObj.createNewFile();
                       fileDB.addDataToKeys(fileName, fileName);
                       fileDB.addDataToFileDB(myObj.getName(), (float) myObj.length(), new Date().toString(), new Date().toString(),userName);
                       DisrtibutionAroundContainers containersManager1=new DisrtibutionAroundContainers();
                       containersManager1.sendFileToContainers(myObj);
                       myObj.delete();
                       //sendFileToContainers(myObj);
                       statusArea.setText("File created: " + myObj.getName());
                       log.FileLog(userName, "created", myObj.getName());

                   } else {
                       statusArea.setText("File already exists.");
                    }
                } 

                catch (InvalidKeySpecException e) {
                statusArea.setText("Invalid key specification error occurred.");
               // Handle the exception, log it, or take appropriate action
                java.util.logging.Logger.getLogger(fileManagementController.class.getName()).log(Level.SEVERE, null, e);}
                }
        else{
          statusArea.setText("you didn't typed in a name of the file");
        }
     }
     
    public void updateFileHandler(ActionEvent event)  throws InvalidKeySpecException, IOException, SQLException, ClassNotFoundException{
        String fileName = fileNameField.getText();  
         DB fileDB=new DB();  
         if(!fileName.isEmpty()){
            try {
             if (fileDB.isFileExists(fileName)) {
                 if(fileDB.isFileOwnedByThisUser(fileName, userName) || fileDB.canUserRead(fileName, userName)){
                         //File myObj =retrieveFileFromContainers(fileName);
                         //testunZip
                         DisrtibutionAroundContainers containersManager1=new DisrtibutionAroundContainers();
                         File myObj =containersManager1.retrieveFileFromContainers(fileName);
                         Scanner myReader = new Scanner(myObj);
                         String data="";
                         while (myReader.hasNextLine()) {
                             data += myReader.nextLine()+"\n";
                         }
                         contentArea.setText(data);
                         myReader.close();
                         log.FileLog(userName, "opened", myObj.getName());
                         myObj.delete();
                  statusArea.setText("File opened: " + myObj.getName());
                 }
                 else{
                 statusArea.setText("You are not the owner and you don't have the permission to read");}
             }
             else {
                 statusArea.setText("File doesn't exist.");
             }
            }
           catch (FileNotFoundException e) {
             statusArea.setText("An error occurred.");
             java.util.logging.Logger.getLogger(fileManagementController.class.getName()).log(Level.SEVERE, null, e);
            }
         }
        else{ 
             statusArea.setText("you didn't typed in a name of the file");
         }

    }
     
    public void deleteFileHandler(ActionEvent event) throws InvalidKeySpecException, IOException, SQLException, ClassNotFoundException{
    String fileName = fileNameField.getText(); 
     if(!fileName.isEmpty()){
        File myObj = new File(fileName); 
        DB fileDB=new DB();  
            if (fileDB.isFileExists(fileName)) { 
                 if(fileDB.isFileOwnedByThisUser(fileName, userName)){
                        statusArea.setText("Deleted the file: " + myObj.getName());
                         log.FileLog(userName, "deleted", myObj.getName());
                         contentArea.clear();
                         try{
                         fileDB.addDataToBin(fileName);
                         fileDB.deleteRowFileDB(fileName);

                        // containersManager.deleteChunks(fileName);
                         }
                         catch (InvalidKeySpecException e) {
                          statusArea.setText("Invalid key specification error occurred.");
                          // Handle the exception, log it, or take appropriate action
                         java.util.logging.Logger.getLogger(fileManagementController.class.getName()).log(Level.SEVERE, null, e);}
                    }
                    else{
                     statusArea.setText("You are not the owner of the file");
                     }
            }
            else {
              statusArea.setText("File doesn't exist.");
            } 
        }
     else{statusArea.setText("you didn't typed in a name of the file");}
    }
     
     public void saveContent(ActionEvent event) throws  InvalidKeySpecException, IOException, SQLException, ClassNotFoundException{
       String fileName = fileNameField.getText(); 
       String content=contentArea.getText();
       if(!fileName.isEmpty()){
        File myObj=new File(fileName);
        DB fileDB=new DB();  
         if(fileDB.isFileOwnedByThisUser(fileName, userName) || fileDB.canUserWrite(fileName, userName)){
             try {
                FileWriter myWriter = new FileWriter(fileName);
                myWriter.write(content);
                myWriter.close();
                statusArea.setText("Successfully updated the file.");
                contentArea.clear();
                //sendFileToContainers(myObj);
                fileDB.updateFileDB(fileName, new Date().toString(),(float) myObj.length());
                DisrtibutionAroundContainers containersManager1=new DisrtibutionAroundContainers();
                containersManager1.sendFileToContainers(myObj);
                 myObj.delete();
                // sendFileToContainers(myObj);
                log.FileLog(userName, "updated", fileName);


             } catch (IOException e) {
                statusArea.setText("An error occurred.");
                java.util.logging.Logger.getLogger(fileManagementController.class.getName()).log(Level.SEVERE, null, e);
             }
              catch (InvalidKeySpecException e) {
             statusArea.setText("Invalid key specification error occurred.");
            // Handle the exception, log it, or take appropriate action
             java.util.logging.Logger.getLogger(fileManagementController.class.getName()).log(Level.SEVERE, null, e);}
             }
         else{
             statusArea.setText("The user is not owner of the file and doesn't have permission to write");
         }

      }
       else{statusArea.setText("you didn't typed in a name of the file");}
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
            e.printStackTrace();
        }
    }
 
 
}
