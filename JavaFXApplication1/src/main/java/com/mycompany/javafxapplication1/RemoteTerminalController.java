/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.IOException;
import java.util.logging.Level;
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
public class RemoteTerminalController {
    private String userName;
    public void setUserName(String userName) {
        this.userName = userName;
    }
    private AuditTrail log;
  @FXML
  private TableView<Container> containerSelection;
     
  @FXML 
  private Label statusOfSelection;
  @FXML
  private TableColumn<Container, String> containerName;
  ObservableList<Container> containerNameValues=FXCollections.observableArrayList(
  new Container("comp20081-files-container1"),
  new Container("comp20081-files-container2"),     
  new Container("comp20081-files-container3"),
  new Container("comp20081-files-container4"));
  
  @FXML
    private Button BackBtn;
  
public void selectContainerHandler(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
            // Get the selected item
            Container selectedContainer = containerSelection.getSelectionModel().getSelectedItem();
            // Pass the selected item to another function
            if (selectedContainer != null) {
                log.RemoteTerminalLog(userName, selectedContainer.getContainerName());
                handleContainerSelection(selectedContainer);
                RemoteTerminalEmulation shell = new RemoteTerminalEmulation();
                shell.handleContainerSelection(selectedContainer.getContainerName());
            }
        }
    }

private void handleContainerSelection(Container selectedContainer) {
        statusOfSelection.setText("selected container: " + selectedContainer.getContainerName());
      
        
    }

    public void initialize() {
          containerName.setCellValueFactory(new PropertyValueFactory<Container, String>("containerName"));
          containerSelection.setItems(containerNameValues);
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
            java.util.logging.Logger.getLogger(RemoteTerminalController.class.getName()).log(Level.SEVERE, null, e);
      
        }
    }
}