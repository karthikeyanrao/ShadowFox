package com.library;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();

        // Register a new user
        library.registerUser("john_doe", "password123", "john@example.com");

        // Login
        User user = library.loginUser("john_doe", "password123");
        if (user != null) {
            System.out.println("Logged in as: " + user.getUsername());

            // Add some books
            library.addBook("The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", 3);
            library.addBook("1984", "George Orwell", "978-0451524935", 2);

            // Search for books
            System.out.println("\nSearching for 'Gatsby':");
            List<Book> searchResults = library.searchBooks("Gatsby");
            for (Book book : searchResults) {
                System.out.println(book);
            }

            // Borrow a book
            Book bookToBorrow = searchResults.get(0);
            if (library.borrowBook(user.getId(), bookToBorrow.getId())) {
                System.out.println("\nSuccessfully borrowed: " + bookToBorrow.getTitle());
            }

            // Return the book
            if (library.returnBook(user.getId(), bookToBorrow.getId())) {
                System.out.println("Successfully returned: " + bookToBorrow.getTitle());
            }
        }

        // Close the library connection
        library.close();
    }
} 