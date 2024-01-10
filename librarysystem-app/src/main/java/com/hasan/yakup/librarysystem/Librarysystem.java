package com.hasan.yakup.librarysystem;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class Librarysystem {
	private Scanner scanner; /**<Scanner for user input in the Library System. */
    private PrintStream out; /**<PrintStream for output in the Library System. */
    
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
	
	public boolean enterToContinue() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        return true;
    }
	
	public List<Book> loadBooks(String pathFileBooks) throws ClassNotFoundException {
        List<Book> books = new ArrayList<>();

        File file = new File(pathFileBooks);
        if (file.exists() && file.isFile()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(pathFileBooks))) {
                while (true) {
                    Book book = (Book) objectInputStream.readObject();
                    books.add(book);
                }
            } catch (IOException e) {
                return null;
            }
        }

        return books;
    }
	
	public int getNewId(String pathFileBooks) throws ClassNotFoundException {
        List<Book> books = loadBooks(pathFileBooks);
        return (books != null ? books.size() : 0) + 1;
    }
	
	public boolean addBook(String bookName, String pathFileBooks) throws IOException, ClassNotFoundException {
		Book newBook = new Book(getNewId(pathFileBooks), "Kitap Adı", false, false, false);
		
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(pathFileBooks, true))) {
            objectOutputStream.writeObject(newBook);
        }

        return true;
    }
	public boolean addBookMenu(String pathFileBooks) throws InterruptedException, IOException, ClassNotFoundException {
        clearScreen();
        System.out.print("Enter a book name: ");
        String bookName = scanner.nextLine();
        addBook(bookName, pathFileBooks);
        return true;
    }
	
}
