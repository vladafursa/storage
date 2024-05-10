/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javafxapplication1;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author ntu-user
 */
public class TerminalCommandsImplementation {
       public String runCommand(String command) throws IOException {
        String[] acceptedCommands = {"mv", "cp", "ls", "mkdir", "ps", "whoami", "tree", "nano"};
        String CommandForNano=command;
        boolean notAcceptCommand = true;
        String[] cmd=null;
        String res="";
                cmd = command.split(" ");
               // System.out.print(cmd);
                for (int i = 0; i < acceptedCommands.length; i++) {
                    if (cmd[0].equals(acceptedCommands[i])) {
                        notAcceptCommand = false;
                        break;
                    }
                }
                if (notAcceptCommand) {
                    res="Invalid command - not from the list of accepted commands. Please try again.";
                }
                else{
            var processBuilder = new ProcessBuilder();
if (cmd[0].equals("nano")) {
    CommandForNano="terminator -e "+CommandForNano;
    String[] newcmd = CommandForNano.split(" ");
    processBuilder.command(newcmd);
}
 else {
    processBuilder.command(cmd);
}
            try{
            var process = processBuilder.start();
          
                        try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                res+=line+"\n";
                            }
           
            }
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                res="invalid command";
            }
            }
            catch( InterruptedException e){
            java.util.logging.Logger.getLogger(TerminalCommandsImplementation.class.getName()).log(Level.SEVERE, null, e);
      
            }
             }
        return res;
    }
}