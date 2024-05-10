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

public class ACLController {
    
    private String userName;
    

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @FXML
  private TableView<ACL> ACLTableView;
     
  public void initialise() {
    ObservableList<ACL> data;
    DB myObj = new DB();
         try {
        data = myObj.getDataFromACL();
        TableColumn<ACL, String> FileName = new TableColumn<>("FileName");
        FileName.setCellValueFactory(new PropertyValueFactory<>("FileName"));

        TableColumn<ACL, String> SharedWith = new TableColumn<>("SharedWith");
        SharedWith.setCellValueFactory(new PropertyValueFactory<>("SharedWith"));

        TableColumn<ACL, Integer> ReadPerms = new TableColumn<>("ReadPerms");
        ReadPerms.setCellValueFactory(new PropertyValueFactory<>("ReadPerms"));

        TableColumn<ACL, Integer> WritePerms = new TableColumn<>("WritePerms");
        WritePerms.setCellValueFactory(new PropertyValueFactory<>("WritePerms"));

        
        ACLTableView.setItems(data);
        ACLTableView.refresh();
        ACLTableView.getColumns().addAll(FileName, SharedWith, ReadPerms,WritePerms);
        

    } catch (ClassNotFoundException ex) {
       Logger.getLogger(ACLController.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (IOException e) {
           Logger.getLogger(ACLController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}