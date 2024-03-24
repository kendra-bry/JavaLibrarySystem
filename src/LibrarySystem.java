import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LibrarySystem {
    private static Map<Integer, Patron> patrons = new HashMap<>();
    private static Map<String, Book> books = new HashMap<>();

    public static void main(String[] args) {
        readBooksFromJSON("books.json");
        readPatronsFromJSON("patrons.json");
        greetUser();
        simulateLibrary();
    }

    private static void greetUser() {
        System.out.println("\n----------------------------------------");
        System.out.println("Welcome to the Library Checkout System!");
        System.out.println("Please login with your account number.");
        System.out.println("----------------------------------------\n");
    }

    // Method to read books data from a JSON file and populate the 'books' map
    private static void readBooksFromJSON(String filename) {
        // Create a Gson object for JSON deserialization
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            // Define the type of data to be read from JSON using TypeToken
            Type type = new TypeToken<List<Book>>() {
            }.getType();
            // Deserialize JSON data into a list of Book objects
            List<Book> bookList = gson.fromJson(reader, type);
            // Iterate through the list of books and add them to the 'books' map
            for (Book book : bookList) {
                books.put(book.getTitle(), book);
            }
            System.out.println("Books loaded successfully."); // Print success message
        } catch (IOException e) {
            // Handle IO exception if file reading fails
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
    }

    // Method to read patrons data from a JSON file and populate the 'patrons' map
    private static void readPatronsFromJSON(String filename) {
        // Create a Gson object for JSON deserialization
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            // Define the type of data to be read from JSON using TypeToken
            Type type = new TypeToken<List<Patron>>() {
            }.getType();
            // Deserialize JSON data into a list of Patron objects
            List<Patron> patronList = gson.fromJson(reader, type);
            // Iterate through the list of patrons and add them to the 'patrons' map
            for (Patron patron : patronList) {
                patrons.put(patron.getAccountNumber(), patron);
            }
            System.out.println("Patrons loaded successfully."); // Print success message
        } catch (IOException e) {
            // Handle IO exception if file reading fails
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
    }

    // Simulate the library system where patrons can perform various actions
    private static void simulateLibrary() {
        Scanner scanner = new Scanner(System.in);
        int accountNumber;
        Patron patron = null;
        boolean loggedIn = false;

        // Continue prompting for user input until the program exits
        while (true) {
            if (!loggedIn) {
                System.out.print("Enter your account number to login (or 0 to exit): ");
                accountNumber = scanner.nextInt();
                if (accountNumber == 0) {
                    break; // Exit the loop if the user chooses to exit
                }
                patron = patrons.get(accountNumber);
                if (patron != null) {
                    System.out.println("Welcome, " + patron.getAccountNumber() + "!");
                    loggedIn = true;
                } else {
                    System.out.println("Invalid account number. Please try again.");
                }
            } else {
                // Once logged in, handle the patron's actions
                handlePatron(patron);
                loggedIn = false; // Log out after handling the patron's actions
            }
        }

        System.out.println("\nGoodbye!"); // Print farewell message upon program exit
    }

    // Handle the actions of a logged-in patron
    private static void handlePatron(Patron patron) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            // Display the menu options for the patron
            System.out.println("\n----------------------------------------");
            System.out.println("Menu");
            System.out.println("1. Check out a book");
            System.out.println("2. Return a book");
            System.out.println("3. Place a hold on a book");
            System.out.println("4. Remove a hold on a book");
            System.out.println("5. Check account status");
            System.out.println("6. Logout");
            System.out.println("\n----------------------------------------");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            // Perform actions based on the patron's choice
            switch (choice) {
                case 1:
                    checkoutBook(patron); // Call method to check out a book
                    break;
                case 2:
                    returnBook(patron); // Call method to return a book
                    break;
                case 3:
                    placeHold(patron); // Call method to place a hold on a book
                    break;
                case 4:
                    removeHold(patron); // Call the method to remove a hold
                    break;
                case 5:
                    checkAccountStatus(patron); // Call method to check account status
                    break;
                case 6:
                    System.out.println("Exiting patron menu...\n"); // Inform user of logout
                    break;
                default:
                    System.out.println("Invalid choice. Please try again."); // Handle invalid input
            }
        } while (choice != 6); // Continue until the patron chooses to logout
    }

    // Allow a patron to check out a book from the library
    private static void checkoutBook(Patron patron) {
        Scanner scanner = new Scanner(System.in);

        // Display available books for checkout
        System.out.println("\n----------------------------------------");
        System.out.println("Available Books:\n");
        int count = 1; // Counter for book numbering
        for (Book book : books.values()) {
            if (!book.isCheckedOut()) {
                System.out.println(count + ". " + book.getTitle() + " by " + book.getAuthor());
                count++;
            }
        }

        // Check if patron has reached the maximum limit for checked out books
        if (patron.getCheckedOutBooks().size() >= 3) {
            System.out.println("You have reached the maximum limit for checked out books (3).");
            return;
        }

        // Prompt user to choose a book to check out
        System.out.print("\nEnter the number of the book you want to check out (or 0 to cancel): ");
        int choice = scanner.nextInt();
        if (choice == 0) {
            return;
        }
        choice -= 1;
        int index = 0;
        Book selectedBook = null;
        for (Book book : books.values()) {
            if (!book.isCheckedOut()) {
                if (index == choice) {
                    selectedBook = book;
                    break;
                }
                index++;
            }
        }

        // Check if the selected book is available and check it out
        if (selectedBook != null) {
            patron.checkOutBook(selectedBook);
            writePatronsToJSON("patrons.json");
            System.out.println("You have successfully checked out " + selectedBook.getTitle());
        } else {
            System.out.println("Invalid book selection or book not available for checkout.");
        }
    }

    // Allow a patron to return a book to the library
    private static void returnBook(Patron patron) {
        Scanner scanner = new Scanner(System.in);

        // Display patron's checked out books
        System.out.println("\n----------------------------------------");
        List<Book> checkedOutBooks = patron.getCheckedOutBooks();
        if (checkedOutBooks.isEmpty()) {
            System.out.println("You haven't checked out any books.");
            return;
        }
        System.out.println("Your Checked Out Books:");
        int count = 1;
        for (Book book : checkedOutBooks) {
            System.out.println(count + ". " + book.getTitle() + " by " + book.getAuthor());
            count++;
        }

        // Prompt user to choose a book to return
        System.out.print("\nEnter the number of the book you want to return (or 0 to cancel): ");
        int choice = scanner.nextInt();
        if (choice == 0) {
            return;
        }

        // Adjust choice to match the list index
        choice -= 1;
        if (choice >= 0 && choice < checkedOutBooks.size()) {
            Book selectedBook = checkedOutBooks.get(choice);
            patron.returnBook(selectedBook);
            writePatronsToJSON("patrons.json");
            System.out.println("You have successfully returned " + selectedBook.getTitle());
        } else {
            System.out.println("Invalid book selection or you haven't checked out this book.");
        }
    }

    // Allow a patron to place a hold on an available book
    private static void placeHold(Patron patron) {
        Scanner scanner = new Scanner(System.in);

        // Display available books for placing a hold
        System.out.println("\n----------------------------------------");
        System.out.println("Available Books:\n");
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (!book.isCheckedOut() && !book.hasHold() && patron.canPlaceHold()) {
                availableBooks.add(book);
            }
        }

        // Check if there are any available books to place a hold on
        if (availableBooks.isEmpty()) {
            System.out.println("There are no available books to place a hold on.");
            return;
        }

        // Display available books with numbering for user selection
        int count = 1;
        for (Book book : availableBooks) {
            System.out.println(count + ". " + book.getTitle() + " by " + book.getAuthor());
            count++;
        }

        // Check if the patron has reached the maximum limit for holds
        if (!patron.canPlaceHold()) {
            System.out.println("You have reached the maximum limit for holds (3).");
            return;
        }

        // Prompt user to choose a book to place a hold on
        System.out.print("\nEnter the number of the book you want to place a hold on (or 0 to cancel): ");
        int choice = scanner.nextInt();
        if (choice == 0) {
            return;
        }

        // Adjust choice to match the list index
        choice -= 1;
        if (choice >= 0 && choice < availableBooks.size()) {
            Book selectedBook = availableBooks.get(choice);
            patron.placeHold(selectedBook);
            writePatronsToJSON("patrons.json");
            System.out.println("You have successfully placed a hold on " + selectedBook.getTitle());
        } else {
            System.out.println("Invalid book selection or the book is not available for hold.");
        }
    }

    // Remove a hold on a book for the given patron
    private static void removeHold(Patron patron) {
        Scanner scanner = new Scanner(System.in);

        // Display books on hold for the patron
        System.out.println("\n----------------------------------------");
        System.out.println("Books on Hold:\n");
        List<Book> holdBooks = patron.getHoldBooks();

        // Check if the patron has any books on hold
        if (holdBooks.isEmpty()) {
            System.out.println("You don't have any books on hold.");
            return;
        }

        // Display books on hold with numbering for user selection
        for (int i = 0; i < holdBooks.size(); i++) {
            Book book = holdBooks.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + " by " + book.getAuthor());
        }

        // Prompt user to choose a book to remove the hold on
        System.out.print("\nEnter the number of the book you want to remove the hold on (or 0 to cancel): ");
        int choice = scanner.nextInt();
        if (choice == 0) {
            return;
        }

        // Validate the user's choice
        if (choice < 1 || choice > holdBooks.size()) {
            System.out.println("Invalid choice. Please try again.");
            return;
        }

        // Adjust choice to match the list index
        Book selectedBook = holdBooks.get(choice - 1);

        // Remove the hold on the selected book
        patron.removeHold(selectedBook);
        writePatronsToJSON("patrons.json");
        System.out.println("Hold removed successfully for " + selectedBook.getTitle());
    }

    // Display the account status for the given patron
    private static void checkAccountStatus(Patron patron) {
        System.out.println("\n----------------------------------------");
        System.out.println("Account Status:");

        // Display the account number of the patron
        System.out.println("Account Number: " + patron.getAccountNumber());

        // Display the list of checked out books for the patron
        System.out.println("Checked Out Books:");
        for (Book book : patron.getCheckedOutBooks()) {
            System.out.println(book.getTitle() + " by " + book.getAuthor());
        }

        // Display the list of books on hold for the patron
        System.out.println("Books on Hold:");
        for (Book book : patron.getHoldBooks()) {
            System.out.println(book.getTitle() + " by " + book.getAuthor());
        }

        System.out.println("----------------------------------------\n");
    }

    // Write the updated patron information to a JSON file
    private static void writePatronsToJSON(String filename) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filename)) {
            // Serialize the values of the patron map to JSON and write to the file
            gson.toJson(patrons.values(), writer);
            System.out.println("Patron information updated and saved successfully.");
        } catch (IOException e) {
            // Handle any exceptions that occur during file writing
            System.err.println("Error writing to JSON file: " + e.getMessage());
        }
    }
}