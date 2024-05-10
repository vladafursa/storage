/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileWriter; 
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ntu-user
 */
public class AuditTrail {
    
    public static void RegisterLog(String userName){
    writeToLog(userName + " registered at " + new Date().toString());
    }
    
    public static void LoginLog(String userName){
    writeToLog(userName + " logged in at " + new Date().toString());
    }
    
    public static void LogoffLog(String userName){
    writeToLog(userName + " logged off at " + new Date().toString());
    }
    
    public static void TerminalLog(String userName){
    writeToLog(userName + " accessed terminal at " + new Date().toString());
    }
    
    public static void RemoteTerminalLog(String userName, String container){
    writeToLog(userName + " accessed remote terminal of "+container+ " at " + new Date().toString());
    }
    public static void FileLog(String userName, String action, String fileName){
    writeToLog(userName + " " + action +" "+ fileName + " at " + new Date().toString());
    }
    public static void ActionLog(String message){
        writeToLog(message + " at "+ new Date().toString());
    }
    
    
    public static void writeToLog(String message){
      try {
            File logFile = new File("logs.txt");
            logFile.createNewFile();
            FileWriter myWriter = new FileWriter("logs.txt", true);
            myWriter.write(message+"\n");
            myWriter.close();
        } catch (IOException e) {
             Logger.getLogger(AuditTrail.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    }
