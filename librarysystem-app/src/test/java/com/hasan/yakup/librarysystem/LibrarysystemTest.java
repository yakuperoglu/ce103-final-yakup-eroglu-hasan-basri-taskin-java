package com.hasan.yakup.librarysystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

public class LibrarysystemTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private String testFilePathBooks = "test_books.bin";
    private String testFilePathUsers = "test_users.bin";

    @After
    public void tearDown() throws IOException {
        cleanupTestDataBook();
        cleanupTestDataUser();
        System.setOut(null);
        System.setIn(null);
    }

    @Test
    public void testAddBookMenu_SuccessfulAddition() throws InterruptedException, IOException {
        String input = "NewBook\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);

        Librarysystem library = new Librarysystem(testScanner, new PrintStream(outContent));

        boolean result = library.addBookMenu(testFilePathBooks);

        System.setIn(System.in);

        assertTrue(result);
    }

    @Test
    public void testGetNewId_ShouldReturnCorrectId() throws FileNotFoundException, IOException {

        Librarysystem library = new Librarysystem(new Scanner(""), new PrintStream(outContent));
        int newId = library.getNewId(testFilePathBooks);

        assertEquals(1, newId);
    }
    
}