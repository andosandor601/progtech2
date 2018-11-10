/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2;

import hu.elte.progtech2.frontend.GuiManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andó Sándor Zsolt
 */
public class Application {
    
    private static Connection con;
    
    
    /** Az adatbáziskapcsolódáshoz szükséges adatok */
    private static final String URL = "jdbc:derby:progtech2;create=true";
    private static final String USER = "username";
    private static final String PASSWORD = "password";
    
    private static final String SCRIPT_PATH = "init.sql";
    private static final String DELIMITER = "(;(\r)?\n)|(--\n)";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        /* Csak inicializáláshoz, előtte törölni az adatbázist tartalmazó mappát (progtech2)            
        */
        //initDB();
        
        
        GuiManager.start();
    }

    private static void initDB() throws Exception {
        openConnection();
        executeScript();
        closeConnection();
    }

    private static void openConnection() {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void closeConnection() {
        try {
            if ((con != null) && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void executeScript() throws SQLException, FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(new File(SCRIPT_PATH)));
        scanner.useDelimiter(DELIMITER);
        try(Statement st = con.createStatement()){
            while (scanner.hasNext()) {
                String line = scanner.next();
                if (line.contains(";")) {
                    line = line.replace(";", "");
                }
                st.execute(line);
            }
        }
    }

}
