/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author ntu-user
 */
public class ACL {

    private SimpleStringProperty FileName;
    private SimpleStringProperty SharedWith;
    private SimpleIntegerProperty ReadPerms;
    private SimpleIntegerProperty WritePerms;

    ACL(String FileName, String SharedWith,int ReadPerms,int WritePerms) {
        
       
        this.FileName = new SimpleStringProperty(FileName);
        this.SharedWith= new SimpleStringProperty(SharedWith);
        this.ReadPerms=new SimpleIntegerProperty(ReadPerms);
        this.WritePerms=new SimpleIntegerProperty(WritePerms);
    }


    public String getFileName() {
        return FileName.get();
    }

    public void setFileName(String FileName) {
        this.FileName.set(FileName);
    }

    public String getSharedWith() {
        return SharedWith.get();
    }

    public void setSharedWith(String SharedWith) {
        this.SharedWith.set(SharedWith);
    }
      public int getReadPerms() {
        return ReadPerms.get();
    }

    public void setReadPerms(int ReadPerms) {
        this.ReadPerms.set(ReadPerms);
    }

    public int getWritePerms() {
        return WritePerms.get();
    }

    public void setWritePerms(int WritePerms) {
    this.WritePerms.set(WritePerms);
}

     
      
}
