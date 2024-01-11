/**
 * @file LibrarysystemApp.java
 * @brief Main class to start the Library System application.
 * 
 * This class contains the main method to initialize the Library System.
 * It creates a Scanner for user input, initializes a Librarysystem instance,
 * and invokes the mainMenu method to start the application.
 * 
 * @author Hasan Yakup
 * @version 1.0
 * @since 2024-01-11
 */
package com.hasan.yakup.librarysystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class LibrarysystemApp {
     /**
     * The main entry point for the Library System application.
     * 
     * @param args Command-line arguments (not used).
     * @throws FileNotFoundException If a required file is not found.
     * @throws IOException           If an I/O error occurs.
     * @throws InterruptedException  If a thread is interrupted during execution.
     */
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        Scanner inputScanner = new Scanner(System.in);
        Librarysystem librarysystem = new Librarysystem(inputScanner, System.out);
        String pathFileBooks = "books.bin";
        String pathFileUsers = "users.bin";
        librarysystem.mainMenu(pathFileUsers, pathFileBooks);
    }
}
