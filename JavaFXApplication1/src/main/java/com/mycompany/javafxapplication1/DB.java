/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.javafxapplication1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.SQLiteConfig;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import static java.sql.DriverManager.println;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import static javafx.scene.Cursor.cursor;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.FileWriter;
import java.io.PrintWriter;
/**
 *
 * @author ntu-user
 */

public class DB {
    private String fileName = "jdbc:sqlite:comp20081.db";
    private int timeout = 30;
    private String dataBaseName = "COMP20081";
    private String dataBaseTableName = "Users";
    Connection connection = null;
    private Random random = new SecureRandom();
    private String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private int iterations = 10000;
    private int keylength = 256;
    private String saltValue;
    private String FileName2="Accounts";
    /**
     * @brief constructor - generates the salt if it doesn't exists or load it from the file .salt
     */
    DB() {
        try {
            File fp = new File(".salt");
            if (!fp.exists()) {
                saltValue = this.getSaltvalue(30);
                FileWriter myWriter = new FileWriter(fp);
                myWriter.write(saltValue);
                myWriter.close();
            } else {
                Scanner myReader = new Scanner(fp);
                while (myReader.hasNextLine()) {
                    saltValue = myReader.nextLine();
                }
            }
        } catch (IOException e) {
             Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
        
    /**
     * @brief create a new table
     * @param tableName name of type String
     */
    public void createTable(String tableName) throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();  
           config.enforceForeignKeys(true);   
           connection = DriverManager.getConnection(fileName, config.toProperties());

            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("create table if not exists " + tableName + "(name string  primary key, password string)");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                 Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * @brief delete table
     * @param tableName of type String
     */
    public void delTable(String tableName) throws ClassNotFoundException {
        try {
            // create a database connection
             Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();  
           config.enforceForeignKeys(true);   
           connection = DriverManager.getConnection(fileName, config.toProperties());
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("drop table if exists " + tableName);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }


        public void addDataToDB(String user, String password) throws InvalidKeySpecException, ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
//            System.out.println("Adding User: " + user + ", Password: " + password);
            statement.executeUpdate("insert into " + dataBaseTableName + " (name, password) values('" + user + "','" + generateSecurePassword(password) + "')");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }


    public ObservableList<User> getDataFromTable() throws ClassNotFoundException {
        ObservableList<User> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + this.dataBaseTableName);
            while (rs.next()) {
            String name = rs.getString("name");
            String password = rs.getString("password");
            result.add(new User(name, password));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                 Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return result;
    }

    
 
    public boolean validateUser(String user, String pass) throws InvalidKeySpecException, ClassNotFoundException {
    boolean isValidUser = false;
    try {
         Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();  
           config.enforceForeignKeys(true);   
           connection = DriverManager.getConnection(fileName, config.toProperties());

        // Use a parameterized query to check for the existence of the user with the given credentials
        String sql = "SELECT COUNT(*) AS count FROM " + this.dataBaseTableName + " WHERE name = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user);
            statement.setString(2, generateSecurePassword(pass));

            try (ResultSet rs = statement.executeQuery()) {
                // If a row is returned, it means the user with the given credentials exists
                if (rs.next() && rs.getInt("count") > 0) {
                    isValidUser = true;
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    return isValidUser;
}

    private String getSaltvalue(int length) {
        StringBuilder finalval = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            finalval.append(characters.charAt(random.nextInt(characters.length())));
        }

        return new String(finalval);
    }

    /* Method to generate the hash value */
    private byte[] hash(char[] password, byte[] salt) throws InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keylength);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public String generateSecurePassword(String password) throws InvalidKeySpecException {
        String finalval = null;

        byte[] securePassword = hash(password.toCharArray(), saltValue.getBytes());

        finalval = Base64.getEncoder().encodeToString(securePassword);

        return finalval;
    }


    public String getTableName() {
        return this.dataBaseTableName;
    }
    

    public void log(String message) {
        System.out.println(message);

    }

    
    public void deleteRow(String user) throws ClassNotFoundException, SQLException {
     Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();  
           config.enforceForeignKeys(true);   
           Connection conn = DriverManager.getConnection(fileName, config.toProperties());//connects to database

    try {
        String sql = "DELETE FROM Users WHERE name=?";//deletes selected row on scenebuilder.
        PreparedStatement ps = conn.prepareStatement(sql);//prepares sqllite code for execution
        ps.setString(1, user);//specifies position of parameter
        ps.execute();
       
    } catch (SQLException e) {
        // Handle the exception (e.g., log or rethrow)
         Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        try {
            if (conn != null && !conn.isClosed()) {//close connections
                conn.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    
    
}
    public boolean isUserExists(String user) throws SQLException, ClassNotFoundException {
            boolean flag=false;
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(fileName);
      
            try (var statement = conn.prepareStatement("SELECT COUNT(*) AS count FROM " + dataBaseTableName + " WHERE name = ?")) {
        statement.setString(1, user);
        try (var rs = statement.executeQuery()) {
            if(rs.next() && rs.getInt("count") > 0){
                flag=true;
            }
            
        }     
        
    }catch (SQLException e) {
        // Handle the exception (e.g., log or rethrow)
        Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        try {
            if (conn != null && !conn.isClosed()) {//close connections
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
      return flag;
}
    public void UpdateTable(String user, String newPassword) throws ClassNotFoundException, SQLException, InvalidKeySpecException{
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection(fileName);
        
        try{
            String sql= " update  "+this.dataBaseTableName+" set password = ? where name = ?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, generateSecurePassword(newPassword));
            ps.setString(2,user);
            ps.execute();
        }  catch (SQLException e) {
        // Handle the exception (e.g., log or rethrow)
         Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        try {
            if (conn != null && !conn.isClosed()) {//close connections
                conn.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    }     
    }
   
   
       
            
  //File table
 public void createTableFiles(String tableName) {
        try {
            // create a database connection
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            Statement pragmaStatement = connection.createStatement();
            pragmaStatement.execute("PRAGMA foreign_keys = ON;");
            pragmaStatement.close();
            statement.executeUpdate("create table if not exists " + tableName + "(FileName string primary key, size float, creationDate date, modificationDate date, owner string references Users(name) on delete cascade)");

        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

      public void addDataToFileDB(String FileName, float size,  String creationDate, String modificationDate, String owner) throws InvalidKeySpecException {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            //System.out.println("Adding file: " + FileName + ", size: " + size + ",creation date: " + creationDate + ",modification date: " + modificationDate + "owner: " + owner);
            statement.executeUpdate("insert into " + "FileTable"+ " (FileName, size,  creationDate, modificationDate, owner) values('" + FileName + "','" + size + "','" + creationDate + "','" + modificationDate +"','" +owner + "')");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                     Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }
      
public void updateFileDB(String FileName, String date, float size) throws InvalidKeySpecException {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
          
          statement.executeUpdate("UPDATE FileTable SET modificationDate = '" + date + "' WHERE FileName = '" + FileName + "'");
          statement.executeUpdate("UPDATE FileTable SET size = '" + size + "' WHERE FileName = '" + FileName + "'");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                     Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }     
    public void deleteRowFileDB(String FileName) throws InvalidKeySpecException {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
          
          statement.executeUpdate("DELETE FROM FileTable WHERE FileName = '" + FileName + "'");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }
       ObservableList<metadata> getDataFromFileTable() throws ClassNotFoundException {
      ObservableList<metadata> result = FXCollections.observableArrayList(); {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + "FileTable");
            while (rs.next()) {
                
                String FileName= rs.getString("FileName");
                float size = rs.getFloat("size");
                String creationDate=  rs.getString("creationDate");
                String modificationDate= rs.getString("modificationDate");
                String owner=  rs.getString("owner");
                
                result.add(new metadata(FileName, size, creationDate, modificationDate, owner));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
               Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
return result;
    }
   }     
         
      public boolean isFileExists(String FileName) throws SQLException, ClassNotFoundException {
            boolean flag=false;
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(fileName);
      
            try (var statement = conn.prepareStatement("SELECT COUNT(*) AS count FROM FileTable WHERE FileName  = '" + FileName + "'")) {
        try (var rs = statement.executeQuery()) {
            if(rs.next() && rs.getInt("count") > 0){
                flag=true;
            }
            
        }     
    }catch (SQLException e) {
        // Handle the exception (e.g., log or rethrow)
        Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        try {
            if (conn != null && !conn.isClosed()) {//close connections
                conn.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
      return flag;
}
      ObservableList<String> RetrieveNamesOfFilesOfUserThatOwnsThem(String userName) throws ClassNotFoundException {
      ObservableList<String> namesOfOldFiles = FXCollections.observableArrayList(); {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("SELECT FileName FROM FileTable WHERE owner='" + userName + "'");
            while (rs.next()) {
                String filename = rs.getString("filename");
                namesOfOldFiles.add(new String(filename));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
return namesOfOldFiles;
    }
   }
      
      
  public void createTableBin(String tableName) {
        try {
            // create a database connection
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            Statement pragmaStatement = connection.createStatement();
            pragmaStatement.execute("PRAGMA foreign_keys = ON;");
            pragmaStatement.close();
            statement.executeUpdate("create table if not exists " + tableName + "(FileName string primary key, size float, creationDate date, modificationDate date, owner string references Users(name) on delete cascade, dateDeleted date)");

        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                 Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

      public void addDataToBin(String FileName) throws InvalidKeySpecException {
        
try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate(" INSERT INTO Bin (FileName, size, creationDate, modificationDate, owner, dateDeleted)\n" +
"SELECT FileName, size, creationDate, modificationDate, owner, date('now') AS dateDeleted\n" +
"FROM FileTable WHERE FileName = '" + FileName + "'");//datetime('now', 'localtime')
           
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }
           public void addDataToBinTest(String FileName, long size,  String creationDate, String modificationDate, String owner, String dateDeleted) throws InvalidKeySpecException {
        
try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate(" INSERT INTO Bin (FileName, size, creationDate, modificationDate, owner, dateDeleted)  values('" + FileName + "','" + size + "','" + creationDate + "','" + modificationDate +"','" +owner +"','"+ dateDeleted+"')");
           
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                     Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }
       ObservableList<bin> getDataFromBinTable() throws ClassNotFoundException {
      ObservableList<bin> result = FXCollections.observableArrayList(); {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from " + "Bin");
            while (rs.next()) {
                String FileName= rs.getString("FileName");
                float size = rs.getFloat("size");
                String creationDate=  rs.getString("creationDate");
                String modificationDate= rs.getString("modificationDate");
                String owner=  rs.getString("owner");
                String dateDeleted=  rs.getString("dateDeleted");
                result.add(new bin(FileName, size, creationDate, modificationDate, owner, dateDeleted));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
return result;
    }
   }
       public ArrayList<String> RetrieveNamesOfOldFiles(){
           ArrayList<String> namesOfOldFiles = new ArrayList<>();
           try {
            // create a database connection
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet resultSet=statement.executeQuery("SELECT FileName FROM Bin WHERE dateDeleted <DATE('now', '-31 days');");
             while (resultSet.next()) {// < datetime('now', '-30 seconds');
                String filename = resultSet.getString("filename");
                namesOfOldFiles.add(filename);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
           return namesOfOldFiles;
       }
  public void RecoverMetadata(String FileName) throws InvalidKeySpecException {
        
try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
           statement.executeUpdate(" INSERT INTO FileTable (FileName, size, creationDate, modificationDate, owner)\n" +
"SELECT FileName, size, creationDate, modificationDate, owner\n" +
"FROM Bin WHERE FileName = '" + FileName + "'");
           
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }     
     
    public void deleteRowBin(String FileName) throws InvalidKeySpecException {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
          
          statement.executeUpdate("DELETE FROM Bin WHERE FileName = '" + FileName + "'");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                   Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }
 
    
    
    
    
     public void createACLTable() throws ClassNotFoundException {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            Statement pragmaStatement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            pragmaStatement.execute("PRAGMA foreign_keys = ON;");
            pragmaStatement.close();
           statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + "ACL" + " ("
        + "FileName STRING,"
        + "SharedWith STRING REFERENCES Users(name) ON DELETE CASCADE,"
        + "ReadPerms INTEGER,"
        + "WritePerms INTEGER"
        + ")");
            //statement.executeUpdate("alter table "+tableName+ "autoincrement=100");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    
        public void addDataToACL(String Filename, String shared_with, int Read, int Write) throws InvalidKeySpecException, ClassNotFoundException, IOException, SQLException {
    try {
        Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();  
           config.enforceForeignKeys(true);   
           connection = DriverManager.getConnection(fileName, config.toProperties());
        try (var statement = connection.prepareStatement("INSERT INTO " + "ACL" + " (FileName, SharedWith, ReadPerms, WritePerms) VALUES (?,?,?,?)")) {
      
            statement.setString(1, Filename);
            statement.setString(2, shared_with);
            statement.setInt(3, Read);
            statement.setInt(4, Write);
           statement.executeUpdate();
             connection.commit();
        } catch (SQLException e) {
           Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
           Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}  
    
    public void deleteRowACL(String FileName) throws InvalidKeySpecException {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("DELETE FROM ACL WHERE FileName = '" + FileName + "'");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }        
public ObservableList<ACL> getDataFromACL() throws ClassNotFoundException {
        ObservableList<ACL> results = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("SELECT * FROM ACL");
            while (rs.next()) {
            String filename = rs.getString("FileName");
            String shared = rs.getString("SharedWith");
            int read=rs.getInt("ReadPerms");
            int write=rs.getInt("WritePerms");
            results.add(new ACL(filename, shared,read,write));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                 Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return results;

    }   
        
        
 public boolean doesAnyPermissionExist(String FileName, String sharedWith) throws SQLException, ClassNotFoundException {
            boolean flag=false;
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(fileName);
      
            try (var statement = conn.prepareStatement("SELECT COUNT(*) AS count FROM ACL WHERE FileName  = '" + FileName + "'" +"AND SharedWith = '" + sharedWith + "'")) {
        try (var rs = statement.executeQuery()) {
            if(rs.next() && rs.getInt("count") == 1){
                flag=true;
            }
            
        }     
    }catch (SQLException e) {
        // Handle the exception (e.g., log or rethrow)
        Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        try {
            if (conn != null && !conn.isClosed()) {//close connections
                conn.close();
            }
        } catch (SQLException e) {
           Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
      return flag;
}        
        
 public void grantReadPermission(String Filename, String who)throws InvalidKeySpecException {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
         
          statement.executeUpdate("UPDATE ACL SET ReadPerms=1 WHERE FileName = '" + Filename + "' AND SharedWith = '" + who + "'");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }       
        
 public void grantWritePermission(String Filename, String who)throws InvalidKeySpecException {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
         
          statement.executeUpdate("UPDATE ACL SET ReadPerms=1 WHERE FileName = '" + Filename + "' AND SharedWith = '" + who + "'");
          statement.executeUpdate("UPDATE ACL SET WritePerms=1 WHERE FileName = '" + Filename + "' AND SharedWith = '" + who + "'");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }        
        
  public void revokeReadPermission(String Filename, String who)throws InvalidKeySpecException {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
         
          statement.executeUpdate("UPDATE ACL SET ReadPerms=0 WHERE FileName = '" + Filename + "' AND SharedWith = '" + who + "'");
          statement.executeUpdate("UPDATE ACL SET WritePerms=0 WHERE FileName = '" + Filename + "' AND SharedWith = '" + who + "'");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }        
        
  public void revokeWritePermission(String Filename, String who)throws InvalidKeySpecException {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
         
          statement.executeUpdate("UPDATE ACL SET WritePerms=0 WHERE FileName = '" + Filename + "' AND SharedWith = '" + who + "'");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                     Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }
  
  
   public boolean canUserRead(String FileName, String sharedWith) throws SQLException, ClassNotFoundException {
            boolean flag=false;
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(fileName);
      
            try (var statement = conn.prepareStatement("SELECT COUNT(*) AS count FROM ACL WHERE FileName  = '" + FileName + "'" +"AND SharedWith = '" + sharedWith + "'" + "AND ReadPerms=1")) {
        try (var rs = statement.executeQuery()) {
            if(rs.next() && rs.getInt("count") == 1){
                flag=true;
            }
            
        }     
    }catch (SQLException e) {
        // Handle the exception (e.g., log or rethrow)
        Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        try {
            if (conn != null && !conn.isClosed()) {//close connections
                conn.close();
            }
        } catch (SQLException e) {
           Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
      return flag;
} 
   
     public boolean canUserWrite(String FileName, String sharedWith) throws SQLException, ClassNotFoundException {
            boolean flag=false;
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(fileName);
      
            try (var statement = conn.prepareStatement("SELECT COUNT(*) AS count FROM ACL WHERE FileName  = '" + FileName + "'" +"AND SharedWith = '" + sharedWith + "'" + "AND WritePerms=1")) {
        try (var rs = statement.executeQuery()) {
            if(rs.next() && rs.getInt("count") == 1){
                flag=true;
            }
            
        }     
    }catch (SQLException e) {
        // Handle the exception (e.g., log or rethrow)
        Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        try {
            if (conn != null && !conn.isClosed()) {//close connections
                conn.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
      return flag;
}
  public boolean isFileOwnedByThisUser(String FileName, String userName) throws SQLException, ClassNotFoundException {
            boolean flag=false;
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(fileName);
      
            try (var statement = conn.prepareStatement("SELECT COUNT(*) AS count FROM FileTable WHERE FileName  = '" + FileName + "'"+" AND owner = '"+userName+"'")) {
        try (var rs = statement.executeQuery()) {
            if(rs.next() && rs.getInt("count") == 1){
                flag=true;
            }
            
        }     
    }catch (SQLException e) {
        // Handle the exception (e.g., log or rethrow)
        Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        try {
            if (conn != null && !conn.isClosed()) {//close connections
                conn.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
      return flag;
}
  
  
  
        ObservableList<String> RetrieveNamesOfFilesThatUserCanDownload(String userName) throws ClassNotFoundException {
      ObservableList<String> namesOfFilesForDownloading = FXCollections.observableArrayList(); 
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("SELECT FileName FROM FileTable WHERE owner='" + userName + "'");
            while (rs.next()) {
                String filename = rs.getString("filename");
                namesOfFilesForDownloading.add(new String(filename));
            }
            rs = statement.executeQuery("SELECT FileName FROM ACL WHERE SharedWith = '" + userName + "'" + "AND WritePerms=1 AND ReadPerms=1");
            while (rs.next()) {
                String filename = rs.getString("filename");
                namesOfFilesForDownloading.add(new String(filename));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
return namesOfFilesForDownloading;
    
   }
        
        
          public boolean isDeletedFileOwnedByThisUser(String FileName, String userName) throws SQLException, ClassNotFoundException {
            boolean flag=false;
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(fileName);
      
            try (var statement = conn.prepareStatement("SELECT COUNT(*) AS count FROM Bin WHERE FileName  = '" + FileName + "'"+" AND owner = '"+userName+"'")) {
        try (var rs = statement.executeQuery()) {
            if(rs.next() && rs.getInt("count") == 1){
                flag=true;
            }
            
        }     
    }catch (SQLException e) {
        // Handle the exception (e.g., log or rethrow)
        Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        try {
            if (conn != null && !conn.isClosed()) {//close connections
                conn.close();
            }
        } catch (SQLException e) {
           Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
      return flag;
}
  
          
        public void createTableKeys() throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();  
           config.enforceForeignKeys(true);   
           connection = DriverManager.getConnection(fileName, config.toProperties());

            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("create table if not exists " +"Keys" + "(FileName string  primary key, secretKey string)");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
               Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

  


        public void addDataToKeys(String FileName, String secretKey) throws InvalidKeySpecException, ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
    statement.executeUpdate("insert into " + "Keys" + " (FileName, secretKey) values('" + FileName + "','" + generateSecurePassword(secretKey) + "')");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                   Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    /**
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @brief get data from the Database method
     * @retunr results as ResultSet
     */
  
     public void deleteRowKey(String FileName) throws InvalidKeySpecException {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("DELETE FROM Keys WHERE FileName = '" + FileName + "'");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                  Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }
     
     
      public String RetrieveKey(String FileName) throws ClassNotFoundException {
      String secretKey="";
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("SELECT secretKey FROM Keys WHERE FileName = '" + FileName + "'");
            if (rs.next()) {
           secretKey = rs.getString("secretKey");
}
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
               Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    return  secretKey;
    
   }
      
      
      public ObservableList<Key> getDataFromKeys() throws ClassNotFoundException {
        ObservableList<Key> result = FXCollections.observableArrayList();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from Keys");
            while (rs.next()) {
            String FileName = rs.getString("FileName");
            String secretKey = rs.getString("secretKey");
            result.add(new Key(FileName, secretKey));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
              Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return result;
    }
        
}



