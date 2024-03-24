import java.util.ArrayList;
import java.util.List;

public class Patron {
    private int accountNumber;
    private List<Book> checkedOutBooks = new ArrayList<>();
    private List<Book> holdBooks = new ArrayList<>();

    // Constructor
    public Patron(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    // Getter for account number
    public int getAccountNumber() {
        return accountNumber;
    }

    // Getter for checked out books
    public List<Book> getCheckedOutBooks() {
        if (checkedOutBooks == null) {
            return new ArrayList<>();
        } else {
            return checkedOutBooks;
        }
    }

    // Getter for hold books
    public List<Book> getHoldBooks() {
        if (holdBooks == null) {
            return new ArrayList<>();
        } else {
            return holdBooks;
        }
    }

    // Method to check out a book
    public void checkOutBook(Book book) {
        if (checkedOutBooks == null) {
            checkedOutBooks = new ArrayList<>();
        }
        if (checkedOutBooks.size() < 3) {
            book.checkOut();
            checkedOutBooks.add(book);
            System.out.println("You have successfully checked out " + book.getTitle());
        } else {
            System.out.println("You have reached the maximum limit for checked out books (3).");
        }
    }

    // Method to return a book
    public void returnBook(Book book) {
        book.returnBook();
        checkedOutBooks.remove(book);
    }

    // Method to check if a hold can be placed
    public boolean canPlaceHold() {
        if (holdBooks == null) {
            holdBooks = new ArrayList<>();
        }
        return holdBooks.size() < 3;
    }

    // Method to check if a book is on hold
    public boolean hasBookOnHold(Book book) {
        if (holdBooks == null) {
            holdBooks = new ArrayList<>();
        }
        return holdBooks.contains(book);
    }

    // Method to place a hold on a book
    public void placeHold(Book book) {
        // Check if the holdBooks list is null and initialize it if necessary
        if (holdBooks == null) {
            holdBooks = new ArrayList<>();
        }

        // Check if the patron can place a hold (limited to 3 holds)
        if (canPlaceHold()) {
            // Add the current patron to the hold list of the book
            book.addHoldPatron(this);

            // Add the book to the patron's hold list
            holdBooks.add(book);
        } else {
            // If the patron has reached the maximum limit for holds, display an error
            // message
            System.out.println("You have reached the maximum limit for holds (3).");
        }
    }

    // Method to remove a hold from a book
    public void removeHold(Book book) {
        // Check if the holdBooks list is null and initialize it if necessary
        if (holdBooks == null) {
            holdBooks = new ArrayList<>();
        }

        // Check if the book is in the holdBooks list
        if (holdBooks.contains(book)) {
            // Remove the current patron from the hold list of the book
            book.removeHoldPatron(this);

            // Remove the book from the patron's hold list
            holdBooks.remove(book);
        } else {
            // If the book is not in the holdBooks list, display an error message
            System.out.println("Invalid book or you haven't placed a hold on this book.");
        }
    }
}
