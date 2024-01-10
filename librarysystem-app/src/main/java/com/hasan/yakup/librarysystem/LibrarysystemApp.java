package com.hasan.yakup.librarysystem;

import java.io.IOException;
import java.util.List;

public class LibrarysystemApp {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		String pathFileBooks = "books.bin";
		String pathFileUsers = "users.bin";
		Librarysystem librarysystem = new Librarysystem(System.in, System.out);
		librarysystem.addBook("Deneme", pathFileBooks);
		List<Book> books = librarysystem.loadBooks(pathFileBooks);
		if(books != null) {
			for (Book book : books) {
				System.out.println(book.getId() + ": " + book.getName());
			}
		}else {
			System.out.println("Yarrrak");
		}
	  }

}
