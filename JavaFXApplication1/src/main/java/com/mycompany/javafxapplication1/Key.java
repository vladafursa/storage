/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ntu-user
 */
public class Key {
     private SimpleStringProperty FileName;
    private SimpleStringProperty secretKey;
    Key(String FileName, String secretKey) {
        
        this.FileName = new SimpleStringProperty(FileName);
        this.secretKey = new SimpleStringProperty(secretKey);
       
    }

    public String getFileName() {
        return FileName.get();
    }

    public void setFileName(String FileName) {
        this.FileName.set(FileName);
    }

    public String getSecretKey() {
        return secretKey.get();
    }

    public void setSecretKey(String secretKey) {
        this.secretKey.set(secretKey);
    }

     
   
}
