/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MetadataController {
    
    private String userName;
    

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @FXML
  private TableView<metadata> metadataTable;
     
  public void initialise() {
    DB metadataDB = new DB();
    ObservableList<metadata> data;

    try {
        data = metadataDB.getDataFromFileTable();
        TableColumn<metadata, String> FileName = new TableColumn<>("FileName");
        FileName.setCellValueFactory(new PropertyValueFactory<>("FileName"));

        TableColumn<metadata, Float> size = new TableColumn<>("size");
        size.setCellValueFactory(new PropertyValueFactory<>("size"));

        TableColumn<metadata, String> creationDate = new TableColumn<>("creationDate");
        creationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        TableColumn<metadata, String> modificationDate = new TableColumn<>("modificationDate");
        modificationDate.setCellValueFactory(new PropertyValueFactory<>("modificationDate"));

        TableColumn<metadata, String> owner = new TableColumn<>("owner");
        owner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        
        metadataTable.setItems(data);
        metadataTable.refresh();
        metadataTable.getColumns().addAll(FileName, size, creationDate,modificationDate,owner);
        

    } catch (ClassNotFoundException ex) {
          java.util.logging.Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
    }
 
}
  
  @FXML
  private Button BackBtn;
  
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
           java.util.logging.Logger.getLogger(MetadataController.class.getName()).log(Level.SEVERE, null, ex);
      
        }
    }

}