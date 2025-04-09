package com.bookstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        // Load MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        // Establish connection to the database
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/bookstore", "root", "Saurab@123"
        );
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        // Open connection to the database
        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            while (rs.next()) {
                Book b = new Book();
                b.setId(rs.getInt("id"));
                b.setTitle(rs.getString("title"));
                b.setAuthor(rs.getString("author"));
                b.setPrice(rs.getDouble("price"));
                books.add(b);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    public void addBook(Book book) {
        // Open connection to the database
        try (Connection conn = getConnection()) {
            // Prepare SQL statement for insertion
            String sql = "INSERT INTO books(title, author, price) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                // Set parameters for the prepared statement
                ps.setString(1, book.getTitle());
                ps.setString(2, book.getAuthor());
                ps.setDouble(3, book.getPrice());

                // Execute the query
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Book added successfully!");
                } else {
                    System.out.println("Failed to add book.");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean updateBook(int id, Book updatedBook) {
        // Open connection to the database
        try (Connection conn = getConnection()) {
            // Prepare SQL statement for update
            String sql = "UPDATE books SET title = ?, author = ?, price = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                // Set parameters for the prepared statement
                ps.setString(1, updatedBook.getTitle());
                ps.setString(2, updatedBook.getAuthor());
                ps.setDouble(3, updatedBook.getPrice());
                ps.setInt(4, id);

                // Execute the update
                int rowsUpdated = ps.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private void resequenceIds(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TEMPORARY TABLE temp_books AS SELECT title, author, price FROM books");
        stmt.executeUpdate("TRUNCATE TABLE books");
        stmt.executeUpdate("INSERT INTO books (title, author, price) SELECT title, author, price FROM temp_books");
        stmt.close();
    }

    public boolean deleteBook(int id) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM books WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    // Re-sequence IDs after deletion
                    resequenceIds(conn);
                    System.out.println("Book deleted successfully!");
                    return true;
                } else {
                    System.out.println("No book found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
