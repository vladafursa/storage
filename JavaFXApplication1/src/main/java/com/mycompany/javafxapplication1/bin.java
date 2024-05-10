/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

/**
 *
 * @author ntu-user
 */
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author ntu-user
 */
public class bin {
    private SimpleStringProperty FileName;
    private SimpleFloatProperty size;
    private SimpleStringProperty creationDate;
    private SimpleStringProperty modificationDate;
    private SimpleStringProperty owner;
    private SimpleStringProperty dateDeleted;
//  creationDate, modificationDate, owner, dateDeleted
    bin(String FileName, float size, String creationDate,String modificationDate,String owner, String dateDeleted) {
        this.FileName= new SimpleStringProperty(FileName);
        this.size = new SimpleFloatProperty(size);
        this.creationDate= new SimpleStringProperty(creationDate);
        this.modificationDate= new SimpleStringProperty(modificationDate);
        this.owner= new SimpleStringProperty(owner);
        this.dateDeleted= new SimpleStringProperty(dateDeleted);
       
    }

    public String getFileName() {
        return FileName.get();
    }

    public void setFileName(String FileName) {
        this.FileName.set(FileName);
    }

    public float getSize() {
        return size.get();
    }

    public void setSize(float size) {
        this.size.set(size);
    }
    
    //String modificationDate,String owner, String dateDeleted
    public String getCreationDate() {
        return creationDate.get();
    }

    public void setCreationDate(String creationDate) {
        this.creationDate.set(creationDate);
    }
public String getModificationDate() {
        return modificationDate.get();
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate.set(modificationDate);
    }
    public String getOwner() {
        return owner.get();
    }

    public void setOwner(String owner) {
        this.owner.set(owner);
    }
    
    public String getDateDeleted() {
        return dateDeleted.get();
    }

    public void setDateDeleted(String dateDeleted) {
        this.dateDeleted.set(dateDeleted);
    }
     
    
}
