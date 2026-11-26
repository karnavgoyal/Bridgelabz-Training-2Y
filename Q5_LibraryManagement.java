/*
 * ============================================================================
 *  PROBLEM 5 : DOUBLY LINKED LIST - LIBRARY MANAGEMENT SYSTEM
 *  Node data  : Book Title, Author, Genre, Book ID, Availability Status
 *  Operations : 1) Add at beginning / end / specific position
 *               2) Remove by Book ID
 *               3) Search by Book Title or Author
 *               4) Update Availability Status
 *               5) Display forward and reverse
 *               6) Count total books
 *  Run        : javac Q5_LibraryManagement.java
 *               java Q5_LibraryManagement
 * ============================================================================
 */
public class Q5_LibraryManagement {

    // ---------------------------- NODE CLASS --------------------------------
    static class Book {
        String title;
        String author;
        String genre;
        int id;
        boolean available;               // true = Available, false = Issued
        Book next;
        Book prev;

        Book(String title, String author, String genre, int id, boolean available) {
            this.title = title;
            this.author = author;
            this.genre = genre;
            this.id = id;
            this.available = available;
        }
    }

    // ------------------------- LINKED LIST CLASS ----------------------------
    static class Library {
        private Book head;
        private Book tail;
        private int size;

        boolean isEmpty() { return head == null; }

        // ---- 1a) ADD AT BEGINNING ------------------------------ O(1) ------
        void addAtBeginning(String title, String author, String genre, int id, boolean avail) {
            Book node = new Book(title, author, genre, id, avail);
            if (isEmpty()) head = tail = node;
            else {
                node.next = head;
                head.prev = node;
                head = node;
            }
            size++;
            System.out.println("Added at beginning: " + title);
        }

        // ---- 1b) ADD AT END ------------------------------------ O(1) ------
        void addAtEnd(String title, String author, String genre, int id, boolean avail) {
            Book node = new Book(title, author, genre, id, avail);
            if (isEmpty()) head = tail = node;
            else {
                node.prev = tail;
                tail.next = node;
                tail = node;
            }
            size++;
            System.out.println("Added at end: " + title);
        }

        // ---- 1c) ADD AT POSITION (1-based) --------------------- O(n) ------
        void addAtPosition(int pos, String title, String author, String genre, int id, boolean avail) {
            if (pos < 1 || pos > size + 1) {
                System.out.println("Invalid position " + pos + " (valid: 1 to " + (size + 1) + ")");
                return;
            }
            if (pos == 1) { addAtBeginning(title, author, genre, id, avail); return; }
            if (pos == size + 1) { addAtEnd(title, author, genre, id, avail); return; }

            Book node = new Book(title, author, genre, id, avail);
            Book cur = head;
            for (int i = 1; i < pos; i++) cur = cur.next;         // cur = book currently at 'pos'
            node.next = cur;                                      // insert BEFORE cur
            node.prev = cur.prev;
            cur.prev.next = node;
            cur.prev = node;
            size++;
            System.out.println("Added at position " + pos + ": " + title);
        }

        // ---- 2) REMOVE BY BOOK ID ------------------------------ O(n) ------
        void removeById(int id) {
            if (isEmpty()) { System.out.println("Library is empty."); return; }
            Book cur = head;
            while (cur != null && cur.id != id) cur = cur.next;
            if (cur == null) { System.out.println("Book ID " + id + " not found."); return; }

            if (cur.prev != null) cur.prev.next = cur.next; else head = cur.next;  // fix prev side
            if (cur.next != null) cur.next.prev = cur.prev; else tail = cur.prev;  // fix next side
            cur.next = cur.prev = null;
            size--;
            System.out.println("Removed book ID " + id);
        }

        // ---- 3a) SEARCH BY TITLE ------------------------------- O(n) ------
        void searchByTitle(String title) {
            System.out.println("Search Title = " + title);
            boolean found = false;
            for (Book b = head; b != null; b = b.next)
                if (b.title.equalsIgnoreCase(title)) { System.out.println("  FOUND -> " + format(b)); found = true; }
            if (!found) System.out.println("  No book found.");
        }

        // ---- 3b) SEARCH BY AUTHOR ------------------------------ O(n) ------
        void searchByAuthor(String author) {
            System.out.println("Search Author = " + author);
            boolean found = false;
            for (Book b = head; b != null; b = b.next)
                if (b.author.equalsIgnoreCase(author)) { System.out.println("  FOUND -> " + format(b)); found = true; }
            if (!found) System.out.println("  No book found.");
        }

        // ---- 4) UPDATE AVAILABILITY STATUS --------------------- O(n) ------
        void updateAvailability(int id, boolean available) {
            for (Book b = head; b != null; b = b.next) {
                if (b.id == id) {
                    b.available = available;
                    System.out.println("Book ID " + id + " is now " + (available ? "Available" : "Issued"));
                    return;
                }
            }
            System.out.println("Book ID " + id + " not found.");
        }

        // ---- 5a) DISPLAY FORWARD ------------------------------- O(n) ------
        void displayForward() {
            if (isEmpty()) { System.out.println("No books."); return; }
            System.out.println("---- Books (FORWARD) ----");
            for (Book b = head; b != null; b = b.next) System.out.println("  " + format(b));
        }

        // ---- 5b) DISPLAY REVERSE (uses prev pointers) ---------- O(n) ------
        void displayReverse() {
            if (isEmpty()) { System.out.println("No books."); return; }
            System.out.println("---- Books (REVERSE) ----");
            for (Book b = tail; b != null; b = b.prev) System.out.println("  " + format(b));
        }

        // ---- 6) COUNT BOOKS ------------------------------------ O(1) ------
        // We keep a 'size' counter, so no traversal is needed.
        int count() { return size; }

        private String format(Book b) {
            return "ID=" + b.id + " | " + b.title + " | " + b.author + " | " + b.genre
                    + " | " + (b.available ? "Available" : "Issued");
        }
    }

    // ------------------------------ MAIN (DEMO) -----------------------------
    public static void main(String[] args) {
        Library lib = new Library();

        System.out.println(">> Empty checks");
        lib.removeById(1);
        lib.displayForward();
        System.out.println("Total books: " + lib.count());

        System.out.println("\n>> Adding books");
        lib.addAtEnd("Clean Code", "Robert Martin", "Programming", 201, true);
        lib.addAtEnd("The Pragmatic Programmer", "Andrew Hunt", "Programming", 202, true);
        lib.addAtBeginning("Wings of Fire", "A.P.J. Abdul Kalam", "Autobiography", 200, true);
        lib.addAtPosition(3, "Refactoring", "Martin Fowler", "Programming", 203, false);
        lib.addAtPosition(15, "Bad", "None", "None", 0, true);

        System.out.println();
        lib.displayForward();
        lib.displayReverse();

        System.out.println("\n>> Search");
        lib.searchByTitle("clean code");
        lib.searchByAuthor("Martin Fowler");
        lib.searchByAuthor("Nobody");

        System.out.println("\n>> Update availability");
        lib.updateAvailability(201, false);
        lib.updateAvailability(203, true);
        lib.updateAvailability(999, true);

        System.out.println("\nTotal books: " + lib.count());

        System.out.println("\n>> Remove head, middle, tail, missing");
        lib.removeById(200);
        lib.removeById(203);
        lib.removeById(202);
        lib.removeById(555);
        lib.displayForward();
        lib.displayReverse();
        System.out.println("Total books: " + lib.count());
    }
}
