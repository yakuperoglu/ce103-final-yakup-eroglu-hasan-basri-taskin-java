package com.hasan.yakup.librarysystem;
import java.io.Serializable;

public class Book implements Serializable {
	private int id ;
    private String name;
    private boolean isMarked;
    private boolean isWishlist;
    private boolean isLoaned;
    
    public Book(int id, String name, boolean isMarked, boolean isWishlist, boolean isLoaned) {
        this.id = id;
        this.name = name;
        this.isMarked = isMarked;
        this.isWishlist = isWishlist;
        this.isLoaned = isLoaned;
    }
    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public boolean isMarked() {
        return isMarked;
    }


    public boolean isWishlist() {
        return isWishlist;
    }


    public boolean isLoaned() {
        return isLoaned;
    }

}
