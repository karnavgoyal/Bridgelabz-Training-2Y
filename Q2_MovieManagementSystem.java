/*
 * ============================================================================
 *  PROBLEM 2 : DOUBLY LINKED LIST - MOVIE MANAGEMENT SYSTEM
 *  Node data  : Movie Title, Director, Year of Release, Rating
 *  Operations : 1) Add at beginning / end / specific position
 *               2) Remove by Movie Title
 *               3) Search by Director or Rating
 *               4) Display forward and reverse
 *               5) Update Rating by Movie Title
 *  Run        : javac Q2_MovieManagementSystem.java
 *               java Q2_MovieManagementSystem
 * ============================================================================
 */
public class Q2_MovieManagementSystem {

    // ---------------------------- NODE CLASS --------------------------------
    static class Movie {
        String title;
        String director;
        int year;
        double rating;
        Movie next;                      // forward pointer
        Movie prev;                      // backward pointer

        Movie(String title, String director, int year, double rating) {
            this.title = title;
            this.director = director;
            this.year = year;
            this.rating = rating;
        }
    }

    // ------------------------- LINKED LIST CLASS ----------------------------
    static class MovieList {
        private Movie head;              // first movie
        private Movie tail;              // last movie (makes end operations O(1))
        private int size;

        boolean isEmpty() { return head == null; }

        // ---- 1a) ADD AT BEGINNING ------------------------------ O(1) ------
        void addAtBeginning(String title, String director, int year, double rating) {
            Movie node = new Movie(title, director, year, rating);
            if (isEmpty()) {
                head = tail = node;
            } else {
                node.next = head;
                head.prev = node;
                head = node;
            }
            size++;
            System.out.println("Added at beginning: " + title);
        }

        // ---- 1b) ADD AT END ------------------------------------ O(1) ------
        void addAtEnd(String title, String director, int year, double rating) {
            Movie node = new Movie(title, director, year, rating);
            if (isEmpty()) {
                head = tail = node;
            } else {
                node.prev = tail;
                tail.next = node;
                tail = node;
            }
            size++;
            System.out.println("Added at end: " + title);
        }

        // ---- 1c) ADD AT POSITION (1-based) --------------------- O(n) ------
        void addAtPosition(int pos, String title, String director, int year, double rating) {
            if (pos < 1 || pos > size + 1) {
                System.out.println("Invalid position " + pos + " (valid: 1 to " + (size + 1) + ")");
                return;
            }
            if (pos == 1) { addAtBeginning(title, director, year, rating); return; }
            if (pos == size + 1) { addAtEnd(title, director, year, rating); return; }

            Movie node = new Movie(title, director, year, rating);
            Movie cur = head;
            for (int i = 1; i < pos; i++) cur = cur.next;   // cur = node currently at 'pos'
            // insert the new node BEFORE cur
            node.next = cur;
            node.prev = cur.prev;
            cur.prev.next = node;
            cur.prev = node;
            size++;
            System.out.println("Added at position " + pos + ": " + title);
        }

        // ---- 2) REMOVE BY TITLE -------------------------------- O(n) ------
        void removeByTitle(String title) {
            if (isEmpty()) { System.out.println("List is empty. Nothing to remove."); return; }
            Movie cur = head;
            while (cur != null && !cur.title.equalsIgnoreCase(title)) cur = cur.next;
            if (cur == null) { System.out.println("Movie '" + title + "' not found."); return; }

            // Fix BOTH pointers (best practice #5)
            if (cur.prev != null) cur.prev.next = cur.next; else head = cur.next;  // removing head?
            if (cur.next != null) cur.next.prev = cur.prev; else tail = cur.prev;  // removing tail?
            cur.next = cur.prev = null;  // nullify pointers (best practice #4)
            size--;
            System.out.println("Removed: " + title);
        }

        // ---- 3a) SEARCH BY DIRECTOR ---------------------------- O(n) ------
        void searchByDirector(String director) {
            System.out.println("Search Director = " + director);
            boolean found = false;
            for (Movie m = head; m != null; m = m.next) {
                if (m.director.equalsIgnoreCase(director)) { System.out.println("  FOUND -> " + format(m)); found = true; }
            }
            if (!found) System.out.println("  No movies found.");
        }

        // ---- 3b) SEARCH BY RATING ------------------------------ O(n) ------
        void searchByRating(double rating) {
            System.out.println("Search Rating = " + rating);
            boolean found = false;
            for (Movie m = head; m != null; m = m.next) {
                if (m.rating == rating) { System.out.println("  FOUND -> " + format(m)); found = true; }
            }
            if (!found) System.out.println("  No movies found.");
        }

        // ---- 4a) DISPLAY FORWARD (head -> tail) ---------------- O(n) ------
        void displayForward() {
            if (isEmpty()) { System.out.println("No movies."); return; }
            System.out.println("---- Movies (FORWARD) ----");
            for (Movie m = head; m != null; m = m.next) System.out.println("  " + format(m));
        }

        // ---- 4b) DISPLAY REVERSE (tail -> head using prev) ----- O(n) ------
        void displayReverse() {
            if (isEmpty()) { System.out.println("No movies."); return; }
            System.out.println("---- Movies (REVERSE) ----");
            for (Movie m = tail; m != null; m = m.prev) System.out.println("  " + format(m));
        }

        // ---- 5) UPDATE RATING BY TITLE ------------------------- O(n) ------
        void updateRating(String title, double newRating) {
            for (Movie m = head; m != null; m = m.next) {
                if (m.title.equalsIgnoreCase(title)) {
                    System.out.println("Rating of '" + title + "' updated: " + m.rating + " -> " + newRating);
                    m.rating = newRating;
                    return;
                }
            }
            System.out.println("Movie '" + title + "' not found. Rating not updated.");
        }

        private String format(Movie m) {
            return m.title + " | Director: " + m.director + " | Year: " + m.year + " | Rating: " + m.rating;
        }
    }

    // ------------------------------ MAIN (DEMO) -----------------------------
    public static void main(String[] args) {
        MovieList list = new MovieList();

        System.out.println(">> Empty list checks");
        list.removeByTitle("Any");
        list.displayForward();

        System.out.println("\n>> Adding movies");
        list.addAtEnd("Inception", "Christopher Nolan", 2010, 8.8);
        list.addAtEnd("Interstellar", "Christopher Nolan", 2014, 8.7);
        list.addAtBeginning("3 Idiots", "Rajkumar Hirani", 2009, 8.4);
        list.addAtPosition(2, "Dangal", "Nitesh Tiwari", 2016, 8.4);   // middle
        list.addAtPosition(10, "Invalid", "None", 2000, 1.0);          // invalid position

        System.out.println();
        list.displayForward();
        list.displayReverse();

        System.out.println("\n>> Search");
        list.searchByDirector("Christopher Nolan");
        list.searchByRating(8.4);
        list.searchByDirector("Unknown");

        System.out.println("\n>> Update rating");
        list.updateRating("Dangal", 8.5);
        list.updateRating("Nothing", 5.0);

        System.out.println("\n>> Remove head, middle, tail");
        list.removeByTitle("3 Idiots");        // head
        list.removeByTitle("Inception");       // middle
        list.removeByTitle("Interstellar");    // tail
        list.removeByTitle("Missing");
        list.displayForward();
        list.displayReverse();
    }
}
