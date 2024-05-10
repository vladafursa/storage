package com.mycompany.javafxapplication1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class FileChunking {

 public List<File> splitFile(File inputFile, int numberOfChunks) throws IOException {
    List<File> files = new ArrayList<>();

    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile))) {
        String name = inputFile.getName();
        long totalFileSize = inputFile.length();
        int chunkSize = totalFileSize == 0 ? 0 : (int) Math.ceil((double) totalFileSize / (double) numberOfChunks);

        byte[] buffer = new byte[chunkSize];
        int bytesRead;

        for (int i = 0; i < numberOfChunks; i++) {
            bytesRead = bis.read(buffer, 0, chunkSize);
            if (bytesRead == -1) { // If the end of file is reached
                break;
            }

            File newFile = new File(inputFile.getParent(), name + ".part" + (i + 1));
            try (FileOutputStream out = new FileOutputStream(newFile)) {
                out.write(buffer, 0, bytesRead);
            }
            files.add(newFile);
        }

        // Create additional empty files if the input file is smaller than expected
        for (int i = files.size(); i < numberOfChunks; i++) {
            File newFile = new File(inputFile.getParent(), name + ".part" + (i + 1));
            newFile.createNewFile();
            files.add(newFile);
        }
    }
    catch (IOException e) {
          java.util.logging.Logger.getLogger(FileChunking.class.getName()).log(Level.SEVERE, null, e);  
    }
    return files;
}


    public static File uniteChunksIntoAFile(List<File> chunkFiles) throws IOException{
    String nameOfChunk=chunkFiles.get(0).getName();
    int indexOfUnwantedPartOfName=nameOfChunk.indexOf(".part");
    String previousFileName=nameOfChunk.substring(0,indexOfUnwantedPartOfName);
    File originalFile = new File(previousFileName);
     try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(originalFile))) {
            for (int i = 0; i < chunkFiles.size(); i++) {
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(chunkFiles.get(i).getName()))) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                    }
                }
                chunkFiles.get(i).delete();
            }
        }
     catch (IOException e) {
          java.util.logging.Logger.getLogger(FileChunking.class.getName()).log(Level.SEVERE, null, e);
       
    }
    return originalFile;}
}
