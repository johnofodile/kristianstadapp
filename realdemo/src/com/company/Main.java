package com.company;

public class Main {

    public static void main(String[] args) {
        String booktitle="john";
        String authorname="john1";
        String genre="john2";
        int quantity=4;
        int price=500;



        String query = "INSERT INTO `books` (`title`, `author`, `genre`, `quantity`, `price` ) VALUES " +
                "('" + booktitle+ "', '" + authorname + "', '" + genre + "', '" + quantity + "', '" +  price  ;");
	// write your code here
    }
}
