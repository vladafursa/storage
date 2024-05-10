package com.mycompany.javafxapplication1;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;
import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;
import java.util.logging.ConsoleHandler;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException, InvalidKeySpecException, SQLException {
        
   try {
            // Create a FileHandler
            FileHandler fileHandler = new FileHandler("errors.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            Handler[] handlers = Logger.getLogger("").getHandlers();
        for (Handler handler : handlers) {
            if (handler instanceof ConsoleHandler) {
                Logger.getLogger("").removeHandler(handler);
            }
        }
            // Get the root logger and add the FileHandler to it
            Logger.getLogger("").addHandler(fileHandler);

            // Set the logging level
            Logger.getLogger("").setLevel(Level.SEVERE);
        } catch (IOException e) {
            e.printStackTrace(); 
        }

        
        
        
        Stage secondaryStage = new Stage();
        DB myObj = new DB();
       
        try {
            myObj.createTable(myObj.getTableName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            myObj.createTableFiles("FileTable");
            myObj.createTableBin("Bin"); 
            try {
          myObj.createTableKeys(); 
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
          
        try {
           myObj.createACLTable();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("primary.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 640, 480);
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Primary View");
            secondaryStage.show();
        } catch (Exception e) {
          Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
        }
        
        String AppfolderPath = "/home/ntu-user/App";

        Path folder = Paths.get(AppfolderPath);

        try {
            if (!Files.exists(folder)) {
                Files.createDirectory(folder);
            }
        } catch (IOException e) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
        }

         String DownloadsfolderPath = "/home/ntu-user/App/UserDownloads";

        Path folderDownloads = Paths.get(DownloadsfolderPath);

        try {
            if (!Files.exists(folderDownloads)) {
                Files.createDirectory(folderDownloads);
            }
        } catch (IOException e) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
        }
        
        
        
    TimerTask repeatedTask = new TimerTask(){
        public void run()  {
         try {
         String[] namesOfOldFiles=(myObj.RetrieveNamesOfOldFiles()).toArray(new String[0]);
             for(int i=0;i<namesOfOldFiles.length; i++){
             DisrtibutionAroundContainers containersManager=new DisrtibutionAroundContainers();
             containersManager.deleteChunks(namesOfOldFiles[i]);
             myObj.deleteRowBin(namesOfOldFiles[i]);}
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
          catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    };
    Timer timer = new Timer();
    
    long delay = 0;
    long period = 24*60*60*1000;//60*1000 for showing
    timer.scheduleAtFixedRate(repeatedTask, delay, period);
}
    
    public static void main(String[] args) {
        launch();
    }

}