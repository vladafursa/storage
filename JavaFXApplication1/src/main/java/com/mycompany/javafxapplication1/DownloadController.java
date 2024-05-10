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
import java.util.List;
import java.util.Scanner;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
/**
 *
 * @author ntu-user
 */
public class DownloadController {
    private DisrtibutionAroundContainers containersManager;
    private String userName;
    private FileChunking chunkingMechanism;
    private String Filename="";
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    private String pathOfSelectedFile; 
    private AuditTrail log;
 
    
    @FXML
    private Button BackBtn;
    
    @FXML
    private Button DownloadBtn;
    
    @FXML
    private Label status;
    @FXML
    TableView filesForDownload;
  @FXML
  private TableColumn<String, String> FileName;
  public void initialize() throws ClassNotFoundException{
   DB fileDB=new DB();
   ObservableList<String> data = fileDB.RetrieveNamesOfFilesThatUserCanDownload(userName);
    // Setting items to the TableView
    filesForDownload.setItems(data);
    FileName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
   }
  public void selectFile(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
             Filename = (String)filesForDownload.getSelectionModel().getSelectedItem();
           
    }
  }
    public void Download(ActionEvent event) throws IOException, ClassNotFoundException{
   if(!Filename.isEmpty()){
    DisrtibutionAroundContainers containersManager1=new DisrtibutionAroundContainers();  
    File fileThatWillBeMoved = containersManager1.retrieveFileFromContainers(Filename);
    File destinationDirectory = new File("/home/ntu-user/App/UserDownloads");

            // Move the file to the destination directory
            try {
                Path sourcePath = fileThatWillBeMoved.toPath();
                Path destinationPath = Paths.get("/home/ntu-user/App/UserDownloads", fileThatWillBeMoved.getName());

                // Copy the file to the destination directory
                Files.copy(sourcePath, destinationPath);
                fileThatWillBeMoved.delete();

                status.setText("File downloaded successfully to: " + destinationPath.toAbsolutePath());
            } catch (IOException e) {
                     java.util.logging.Logger.getLogger(DownloadController.class.getName()).log(Level.SEVERE, null, e);
       
            }
             log.FileLog(userName, " downloaded ", Filename);
    }
    else{
        status.setText("File wasn't selected");
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
          java.util.logging.Logger.getLogger(DownloadController.class.getName()).log(Level.SEVERE, null, e);
       
        }
    }

}
