/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.util.logging.Level;
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
import javafx.stage.Stage;

/**
 *
 * @author ntu-user
 */
public class KeyController {
        
    private String userName;
    

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @FXML
  private TableView<Key> keyTable;
     
  public void initialise() {
   DB DB = new DB();
    ObservableList<Key> data;
    try {
        data = DB.getDataFromKeys();
        TableColumn FileName = new TableColumn("FileName");
        FileName.setCellValueFactory(new PropertyValueFactory<>("FileName"));
        TableColumn secretKey = new TableColumn("secretKey");
        secretKey.setCellValueFactory(new PropertyValueFactory<>("secretKey"));
        keyTable.setItems(data);
        keyTable.getColumns().addAll(FileName, secretKey);
        

    } catch (ClassNotFoundException ex) {
         java.util.logging.Logger.getLogger(KeyController.class.getName()).log(Level.SEVERE, null, ex);
       
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
               java.util.logging.Logger.getLogger(KeyController.class.getName()).log(Level.SEVERE, null, ex);
      
        }
    }
}
