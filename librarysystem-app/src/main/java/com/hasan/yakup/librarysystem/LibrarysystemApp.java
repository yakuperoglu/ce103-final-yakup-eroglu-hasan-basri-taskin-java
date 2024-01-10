package com.hasan.yakup.librarysystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class LibrarysystemApp {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Librarysystem librarysystem = new Librarysystem(System.in, System.out);
		String pathFileBooks = "books.bin";
		String pathFileUsers = "users.bin";
		
		librarysystem.addBook("Deneme",pathFileBooks);
		List<Book> books = librarysystem.loadBooks(pathFileBooks);
		for (Book book : books) {
			System.out.println(book.getId());
			System.out.println(book.getName());
			System.out.println(book.isMarked());
			System.out.println(book.isWishlist());
			System.out.println(book.isLoaned());
		}

	}

}
