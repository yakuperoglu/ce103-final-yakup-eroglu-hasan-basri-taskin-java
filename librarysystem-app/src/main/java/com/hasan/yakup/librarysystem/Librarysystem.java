package com.hasan.yakup.librarysystem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Librarysystem {
	private Scanner scanner; /**<Scanner for user input in the Library System. */
    private PrintStream out; /**<PrintStream for output in the Library System. */
    /**
     * @brief Constructor for the Librarysystem class.
     *
     * This constructor initializes a Librarysystem object with the specified input stream for user input
     * and output stream for printing messages.
     *
     * @param in The input stream for user input.
     * @param out The output stream for printing messages.
     */
    public Librarysystem(InputStream in, PrintStream out) {
        this.scanner = new Scanner(in);
        this.out = out;
    }
	public void clearScreen() throws InterruptedException, IOException {  
        String operatingSystem = System.getProperty("os.name");
        if (operatingSystem.contains("Windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            out.print("\033[H\033[2J");
            out.flush();
        }
   }
	public boolean addBook(String bookName, String pathFileBooks) throws FileNotFoundException, IOException {
        Book newBook = new Book();
        newBook.setId(getNewId(pathFileBooks));
        newBook.setName(bookName);
        newBook.setMarked(false);
        newBook.setWishlist(false);
        newBook.setLoaned(false);

        try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(pathFileBooks, true))) {
            writer.writeInt(newBook.getId());
            writer.writeUTF(newBook.getName());
            writer.writeBoolean(newBook.isMarked());
            writer.writeBoolean(newBook.isWishlist());
            writer.writeBoolean(newBook.isLoaned());
        } 

        return true;
    }

	 public int getNewId(String pathFileBooks) throws FileNotFoundException, IOException {
	        List<Book> books = loadBooks(pathFileBooks);
	        return books.size() + 1;
	    }
    
    public List<Book> loadBooks(String pathFileBooks) throws FileNotFoundException, IOException {
        List<Book> books = new ArrayList<Book>();

        // Checks if file path exists
        if (new File(pathFileBooks).exists()) {
            // Read books from file
            try (DataInputStream reader = new DataInputStream(new FileInputStream(pathFileBooks))) {
                while (reader.available() > 0) {
                    Book book = new Book();
                    book.setId(reader.readInt());
                    book.setName(reader.readUTF());
                    book.setMarked(reader.readBoolean());
                    book.setWishlist(reader.readBoolean());
                    book.setLoaned(reader.readBoolean());

                    books.add(book);
                }
            }
        }

        return books;
    }
}
