/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author ntu-user
 */

import com.jcraft.jsch.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

public class DisrtibutionAroundContainers {
    private FileChunking chunkingMechanism;
    static String[] namesOfHosts = {"comp20081-files-container1", "comp20081-files-container2", "comp20081-files-container3", "comp20081-files-container4"};
    
   // private static final String REMOTE_HOST = "comp20081-files-container2";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "ntu-user";
    private static final int REMOTE_PORT = 22;
    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;
    private static int numberOfHosts=4;
    
    public static void distributeChunksAroundContainers(List<File> ChunkFiles) throws IOException {
      
        //String basket="/home/ntu-user/basket/";
        Session jschSession = null;
        
        try {
            JSch jsch = new JSch();
            jsch.setKnownHosts("/home/mkyong/.ssh/known_hosts");
            for(int i = 0; i < numberOfHosts; i++) { 
            // Set the StrictHostKeyChecking option to "no" to automatically answer "yes" to the prompt
            jschSession = jsch.getSession(USERNAME, namesOfHosts[i], REMOTE_PORT);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            jschSession.setConfig(config);

            // authenticate using password
            jschSession.setPassword(PASSWORD);

            // 10 seconds session timeout
            jschSession.connect(SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");

            // 5 seconds timeout
            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;
            File chunkFile=ChunkFiles.get(i);
            // transfer file from local to remote server
            String RemoteFilePathOfChunk = "/root/" + chunkFile.getName();
            InputStream inputStream = new FileInputStream(chunkFile);
            channelSftp.put(chunkFile.getCanonicalPath(), RemoteFilePathOfChunk);
            chunkFile.delete();
            channelSftp.exit();
            }


        } catch (JSchException | SftpException e ) {
                java.util.logging.Logger.getLogger(DisrtibutionAroundContainers.class.getName()).log(Level.SEVERE, null, e);
       
        }
        finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }

        //System.out.println("Done");
    }
    public static List<File> gettingChunksFromContainers(String fileName) throws IOException {
        
        //String basket="/home/ntu-user/basket/";
        Session jschSession = null;
        File oldFile=new File(fileName);
        List<File> chunkFiles=new ArrayList<>();
        try {
            JSch jsch = new JSch();
            jsch.setKnownHosts("/home/mkyong/.ssh/known_hosts");
            for(int i = 0; i < numberOfHosts; i++) { 
            // Set the StrictHostKeyChecking option to "no" to automatically answer "yes" to the prompt
            jschSession = jsch.getSession(USERNAME, namesOfHosts[i], REMOTE_PORT);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            jschSession.setConfig(config);

            // authenticate using password
            jschSession.setPassword(PASSWORD);

            // 10 seconds session timeout
            jschSession.connect(SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");

            // 5 seconds timeout
            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;
            
            File chunkFile=new File(oldFile.getName()+".part"+(i+1)+".zip");
            chunkFiles.add(chunkFile);
            // transfer file from local to remote server
            String RemoteFilePathOfChunk = "/root/" + oldFile.getName()+".part"+(i+1)+".zip";
          
            String localFilePathOfChunk=oldFile.getName()+".part"+(i+1)+".zip";
          
            channelSftp.get(RemoteFilePathOfChunk, localFilePathOfChunk);
            channelSftp.exit();
            }


        } catch (JSchException | SftpException e ) {
           java.util.logging.Logger.getLogger(DisrtibutionAroundContainers.class.getName()).log(Level.SEVERE, null, e);
       
        }
        finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }

      
        return chunkFiles;
    }
     public static void deleteChunks(String fileName) throws IOException {
        
         Session jschSession = null;
        File oldFile=new File(fileName);
        List<File> chunkFiles=new ArrayList<>();
        try {
            JSch jsch = new JSch();
            jsch.setKnownHosts("/home/mkyong/.ssh/known_hosts");
            for(int i = 0; i < numberOfHosts; i++) { 
            // Set the StrictHostKeyChecking option to "no" to automatically answer "yes" to the prompt
            jschSession = jsch.getSession(USERNAME, namesOfHosts[i], REMOTE_PORT);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            jschSession.setConfig(config);

            // authenticate using password
            jschSession.setPassword(PASSWORD);

            // 10 seconds session timeout
            jschSession.connect(SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");

            // 5 seconds timeout
            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;
            
         
            // transfer file from local to remote server
            String RemoteFilePathOfChunk = "/root/" + oldFile.getName()+".part"+(i+1)+".zip";
          
            channelSftp.rm(RemoteFilePathOfChunk);
            channelSftp.exit();
            }
            

        } catch (JSchException | SftpException e ) {
           java.util.logging.Logger.getLogger(DisrtibutionAroundContainers.class.getName()).log(Level.SEVERE, null, e);
       
        }
        finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }

      
     
    }
 
     
      public void sendFileToContainers(File myObj) throws IOException, ClassNotFoundException{
      try{zipping zipMechanism=new zipping();
      FileChunking chunking = new FileChunking();
      List<File> chunks=chunking.splitFile(myObj, numberOfHosts);
    
      List<File> encryptedChunks=zipMechanism.zip(chunks);
     
      distributeChunksAroundContainers(encryptedChunks);}
      catch (IOException | ClassNotFoundException e ){
       java.util.logging.Logger.getLogger(DisrtibutionAroundContainers.class.getName()).log(Level.SEVERE, null, e);
      }
      
 }
       
      public File retrieveFileFromContainers(String fileName) throws IOException, ClassNotFoundException{
      zipping zipMechanism=new zipping();
      FileChunking chunking = new FileChunking();
      List<File> encryptedChunks=gettingChunksFromContainers(fileName);
    
     List<File> decryptedChunks=zipping.unzip(encryptedChunks);
    
     File myObj = chunking.uniteChunksIntoAFile(decryptedChunks);
     return myObj;
 }
     
}
