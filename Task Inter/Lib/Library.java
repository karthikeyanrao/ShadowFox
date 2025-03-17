package com.library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:library.db";

    public Library() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        // Create Users table
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL
            )""";

        // Create Books table
        String createBooksTable = """
            CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                isbn TEXT UNIQUE NOT NULL,
                quantity INTEGER NOT NULL,
                available INTEGER NOT NULL
            )""";

        // Create Borrowings table
        String createBorrowingsTable = """
            CREATE TABLE IF NOT EXISTS borrowings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                book_id INTEGER,
                borrow_date TEXT NOT NULL,
                return_date TEXT,
                FOREIGN KEY (user_id) REFERENCES users(id),
                FOREIGN KEY (book_id) REFERENCES books(id)
            )""";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createBooksTable);
            stmt.execute(createBorrowingsTable);
        }
    }

    // User Management
    public boolean registerUser(String username, String password, String email) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public User loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Book Management
    public boolean addBook(String title, String author, String isbn, int quantity) {
        String sql = "INSERT INTO books (title, author, isbn, quantity, available) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, isbn);
            pstmt.setInt(4, quantity);
            pstmt.setInt(5, quantity);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Book> searchBooks(String query) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + query + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getInt("quantity"),
                    rs.getInt("available")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Borrowing Management
    public boolean borrowBook(int userId, int bookId) {
        String checkAvailability = "SELECT available FROM books WHERE id = ?";
        String updateAvailability = "UPDATE books SET available = available - 1 WHERE id = ?";
        String insertBorrowing = "INSERT INTO borrowings (user_id, book_id, borrow_date) VALUES (?, ?, date('now'))";

        try {
            connection.setAutoCommit(false);
            
            // Check availability
            try (PreparedStatement pstmt = connection.prepareStatement(checkAvailability)) {
                pstmt.setInt(1, bookId);
                ResultSet rs = pstmt.executeQuery();
                if (!rs.next() || rs.getInt("available") <= 0) {
                    return false;
                }
            }

            // Update book availability
            try (PreparedStatement pstmt = connection.prepareStatement(updateAvailability)) {
                pstmt.setInt(1, bookId);
                pstmt.executeUpdate();
            }

            // Record borrowing
            try (PreparedStatement pstmt = connection.prepareStatement(insertBorrowing)) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, bookId);
                pstmt.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean returnBook(int userId, int bookId) {
        String updateAvailability = "UPDATE books SET available = available + 1 WHERE id = ?";
        String updateBorrowing = "UPDATE borrowings SET return_date = date('now') WHERE user_id = ? AND book_id = ? AND return_date IS NULL";

        try {
            connection.setAutoCommit(false);

            // Update book availability
            try (PreparedStatement pstmt = connection.prepareStatement(updateAvailability)) {
                pstmt.setInt(1, bookId);
                pstmt.executeUpdate();
            }

            // Update borrowing record
            try (PreparedStatement pstmt = connection.prepareStatement(updateBorrowing)) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, bookId);
                int updated = pstmt.executeUpdate();
                if (updated == 0) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 