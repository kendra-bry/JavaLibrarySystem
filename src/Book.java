import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Book {
    private String title;
    private String author;
    private boolean checkedOut;
    private List<Integer> holdPatronAccountNumbers; // List of account numbers of patrons who have placed a hold on this
                                                    // book
    private LocalDate dueDate; // Due date for the book if checked out

    // Constructor to initialize a Book object with title and author
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.checkedOut = false;
        this.holdPatronAccountNumbers = new ArrayList<>(); // Initialize the list of hold patrons
        this.dueDate = null; // Initialize the due date to null
    }

    // Getter method for book title
    public String getTitle() {
        return title;
    }

    // Getter method for book author
    public String getAuthor() {
        return author;
    }

    // Method to check if the book is checked out
    public boolean isCheckedOut() {
        return checkedOut;
    }

    // Method to check if the book has holds placed on it
    public boolean hasHold() {
        if (holdPatronAccountNumbers == null) {
            holdPatronAccountNumbers = new ArrayList<>();
        }
        return !holdPatronAccountNumbers.isEmpty();
    }

    // Getter method for the list of hold patrons
    public List<Integer> getHoldPatrons() {
        if (holdPatronAccountNumbers == null) {
            holdPatronAccountNumbers = new ArrayList<>();
        }
        return holdPatronAccountNumbers;
    }

    // Getter method for the due date of the book
    public LocalDate getDueDate() {
        return dueDate;
    }

    // Method to check out the book
    public void checkOut() {
        checkedOut = true;
        // Set due date to two weeks from today
        dueDate = LocalDate.now().plusWeeks(2);
    }

    // Method to return the book
    public void returnBook() {
        checkedOut = false;
        dueDate = null; // Reset the due date when the book is returned
    }

    // Method to add a patron to the list of hold patrons
    public void addHoldPatron(Patron patron) {
        // Check if the holdPatronAccountNumbers list is null and initialize it if
        // necessary
        if (holdPatronAccountNumbers == null) {
            holdPatronAccountNumbers = new ArrayList<>();
        }

        // Check if the maximum limit for holds (3) has been reached
        if (holdPatronAccountNumbers.size() >= 3) {
            // Display an error message if the maximum limit has been reached
            System.out.println("Patron has reached the maximum limit for holds (3).");
            return;
        }

        // Check if the given patron's account number is not already in the
        // holdPatronAccountNumbers list
        if (!holdPatronAccountNumbers.contains(patron.getAccountNumber())) {
            // Add the given patron's account number to the list
            holdPatronAccountNumbers.add(patron.getAccountNumber());
            // Display a success message indicating the patron has placed a hold on the book
            System.out.println("Patron has successfully placed a hold on " + title);
        } else {
            // If the given patron's account number is already in the list, display an error
            // message
            System.out.println("Patron already has a hold on this book.");
        }
    }

    // Method to remove a patron from the list of hold patrons
    public void removeHoldPatron(Patron patron) {
        // Check if the holdPatronAccountNumbers list is null and initialize it if
        // necessary
        if (holdPatronAccountNumbers == null) {
            holdPatronAccountNumbers = new ArrayList<>();
        }

        // Check if the given patron's account number is in the holdPatronAccountNumbers
        // list
        if (holdPatronAccountNumbers.contains(patron.getAccountNumber())) {
            // Remove the given patron's account number from the list
            holdPatronAccountNumbers.remove((Integer) patron.getAccountNumber());
        } else {
            // If the given patron's account number is not found in the list, display an
            // error message
            System.out.println("Patron does not have a hold on this book.");
        }
    }
}
