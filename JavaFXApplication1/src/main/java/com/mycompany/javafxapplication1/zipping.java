/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

/**
 *
 * @author ntu-user
 */
import java.io.File;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;


/**
 *
 * @author pedro
 */

public class zipping {
  
  
     public static List<File> zip(List<File> ChunkFiles) throws ZipException, ClassNotFoundException
    {   List<File> encryptedChunkFiles=new ArrayList<>();
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setCompressionLevel(CompressionLevel.MAXIMUM);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        String FileName=ChunkFiles.get(0).getName();
        int indexOfUnwantedPartOfName=FileName.indexOf(".part");
        FileName=FileName.substring(0,indexOfUnwantedPartOfName);
      
        DB DB=new DB();
        String password=DB.RetrieveKey(FileName);
      
        for(int i = 0; i < ChunkFiles.size(); i++){
        String PathOfFile=ChunkFiles.get(i).getAbsolutePath();
        String PathOfZippedFile=ChunkFiles.get(i).getAbsolutePath()+".zip";
        try{
            ZipFile zipFile = new ZipFile(PathOfZippedFile, password.toCharArray());
            zipFile.addFile(new File(PathOfFile), zipParameters);
         
            ChunkFiles.get(i).delete();
            File outputFile=new File(PathOfZippedFile);
            encryptedChunkFiles.add(outputFile);
        }
        catch(Exception e) 
        {
              java.util.logging.Logger.getLogger(zipping.class.getName()).log(Level.SEVERE, null, e);
        }
    
    }
        return encryptedChunkFiles;
    }

     
       public static List<File> unzip(List<File> ChunkFiles) throws ZipException, ClassNotFoundException {
      List<File> decryptedChunkFiles=new ArrayList<>();
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setCompressionLevel(CompressionLevel.MAXIMUM);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);
        String FileName=ChunkFiles.get(0).getName();
        int indexOfUnwantedPartOfName=FileName.indexOf(".part");
        FileName=FileName.substring(0,indexOfUnwantedPartOfName);
        
        DB DB=new DB();
        String password=DB.RetrieveKey(FileName);
       
        for(int i = 0; i < ChunkFiles.size(); i++){
        try{
            ZipFile zipFile = new ZipFile(ChunkFiles.get(i).getName(), password.toCharArray());
            String currentDirectory = Paths.get("").toAbsolutePath().toString();
           //zipFile.extractAll("/home/ntu-user/NetBeansProjects/System_software_individul/JavaFXApplication1/");
            zipFile.extractAll(currentDirectory);
           
          
            ChunkFiles.get(i).delete();
            File decryptedChunk=new File(zipFile.getFileHeaders().get(0).getFileName());
            decryptedChunkFiles.add(decryptedChunk);
            //zipFile.close();
        }
        catch(Exception e) 
        {
            java.util.logging.Logger.getLogger(zipping.class.getName()).log(Level.SEVERE, null, e);
        }
    
    }
        return decryptedChunkFiles;
    }
}