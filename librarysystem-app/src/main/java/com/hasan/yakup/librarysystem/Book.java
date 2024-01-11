package com.hasan.yakup.librarysystem;

public class Book {
    private int id;
    private String name;
    private boolean isMarked;
    private boolean isWishlist;
    private boolean isLoaned;

    public Book() {
    }

    public Book(int bookId, String bookname, boolean isMarked, boolean isWishlist, boolean isLoaned) {
        this.id = bookId;
        this.name = bookname;
        this.isMarked = isMarked;
        this.isWishlist = isWishlist;
        this.isLoaned = isLoaned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public boolean isWishlist() {
        return isWishlist;
    }

    public void setWishlist(boolean wishlist) {
        isWishlist = wishlist;
    }

    public boolean isLoaned() {
        return isLoaned;
    }

    public void setLoaned(boolean loaned) {
        isLoaned = loaned;
    }
}
