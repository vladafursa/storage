/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

/**
 *
 * @author ntu-user
 */
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
public class binController {
    
    private String userName;
    private String selectedFileName;
    // Other methods and fields...

    public void setUserName(String userName) {
        this.userName = userName;
    }
    private AuditTrail log;
    @FXML
  private TableView<bin> BinTable;
    @FXML
    private Label status;
     
  public void initialise() {
    DB binTable = new DB();
    ObservableList<bin> data;
//String FileName, long size, String creationDate,String modificationDate,String owner, String dateDeleted
    try {
        data = binTable.getDataFromBinTable();
      
        TableColumn<bin, String> FileName = new TableColumn<>("FileName");
        FileName.setCellValueFactory(new PropertyValueFactory<>("FileName"));

        TableColumn<bin, Float> size = new TableColumn<>("size");
        size.setCellValueFactory(new PropertyValueFactory<>("size"));

        TableColumn<bin, String> creationDate = new TableColumn<>("creationDate");
        creationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        TableColumn<bin, String> modificationDate = new TableColumn<>("modificationDate");
        modificationDate.setCellValueFactory(new PropertyValueFactory<>("modificationDate"));

        TableColumn<bin, String> owner = new TableColumn<>("owner");
        owner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        TableColumn<bin, String> dateDeleted = new TableColumn<>("dateDeleted");
        dateDeleted.setCellValueFactory(new PropertyValueFactory<>("dateDeleted"));
        BinTable.setItems(data);
        BinTable.refresh();
        BinTable.getColumns().addAll(FileName, size, creationDate,modificationDate,owner, dateDeleted);
        

    } catch (ClassNotFoundException e) {
      java.util.logging.Logger.getLogger(binController.class.getName()).log(Level.SEVERE, null, e);
       
    }
  
 
}
  
  @FXML
  private Button BackBtn;
  @FXML
  private Button recoverButton;
  @FXML
  private Button refreshBtn;
  @FXML
  private Button deleteBtn;
  @FXML
  public void recoverFile(ActionEvent event)  throws InvalidKeySpecException, IOException, SQLException, ClassNotFoundException{
      DB newDB=new DB();
      if(selectedFileName!="" &&  newDB.isDeletedFileOwnedByThisUser(selectedFileName, userName)){
     try {
         newDB.RecoverMetadata(selectedFileName);
         newDB.deleteRowBin(selectedFileName);
          log.ActionLog(userName + " recovered " + selectedFileName);
          status.setText(userName + " recovered " + selectedFileName);
    } catch (InvalidKeySpecException e) {
       java.util.logging.Logger.getLogger(binController.class.getName()).log(Level.SEVERE, null, e);
        // Handle the exception appropriately
       }
      }
      else{
      status.setText("you are not the owner or you haven't selected a file");}
      }
    
  public void selectFile(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
            // Get the selected item
            bin selectedDeletedFile = BinTable.getSelectionModel().getSelectedItem();

            // Pass the selected item to another function
            if (selectedDeletedFile != null) {
               
                selectedFileName=selectedDeletedFile.getFileName();
            }
        }
   }
   public void refresh(ActionEvent event) {

        initialise();
    }
   public void delete(ActionEvent event) throws InvalidKeySpecException, IOException, SQLException, ClassNotFoundException{
    DB newDB=new DB();
    DisrtibutionAroundContainers finalDeletion=new DisrtibutionAroundContainers();
    if(selectedFileName!="" && newDB.isDeletedFileOwnedByThisUser(selectedFileName, userName)){
         try {
         newDB.deleteRowBin(selectedFileName);
         finalDeletion.deleteChunks(selectedFileName);
         newDB.deleteRowACL(selectedFileName);
         newDB.deleteRowKey(selectedFileName);
         log.ActionLog(userName + " deleted " + selectedFileName);
         status.setText(userName + " deleted " + selectedFileName);
    } catch (InvalidKeySpecException e) {
      java.util.logging.Logger.getLogger(binController.class.getName()).log(Level.SEVERE, null, e);
 
    }
    }
    else{
    status.setText("you are not the owner or you haven't selected a file");}
    
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
            java.util.logging.Logger.getLogger(binController.class.getName()).log(Level.SEVERE, null, e);
       
        }
    }

}
