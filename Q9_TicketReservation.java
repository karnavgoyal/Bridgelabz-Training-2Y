/*
 * ============================================================================
 *  PROBLEM 9 : CIRCULAR LINKED LIST - ONLINE TICKET RESERVATION SYSTEM
 *  Node data  : Ticket ID, Customer Name, Movie Name, Seat Number, Booking Time
 *  Operations : 1) Add ticket at the end        2) Remove ticket by Ticket ID
 *               3) Display current tickets      4) Search by Customer / Movie
 *               5) Total number of booked tickets
 *  Run        : javac Q9_TicketReservation.java
 *               java Q9_TicketReservation
 * ============================================================================
 */
public class Q9_TicketReservation {

    // ---------------------------- NODE CLASS --------------------------------
    static class Ticket {
        int id;
        String customer;
        String movie;
        String seat;
        String bookingTime;
        Ticket next;

        Ticket(int id, String customer, String movie, String seat, String bookingTime) {
            this.id = id;
            this.customer = customer;
            this.movie = movie;
            this.seat = seat;
            this.bookingTime = bookingTime;
        }
    }

    // ------------------------- CIRCULAR LIST CLASS --------------------------
    static class ReservationSystem {
        private Ticket tail;             // tail.next is the first ticket (head)
        private int count;

        boolean isEmpty() { return tail == null; }

        // ---- 1) ADD TICKET AT END ------------------------------ O(1) ------
        void addTicket(int id, String customer, String movie, String seat, String time) {
            if (isSeatTaken(movie, seat)) {
                System.out.println("Seat " + seat + " for '" + movie + "' is already booked.");
                return;
            }
            Ticket node = new Ticket(id, customer, movie, seat, time);
            if (isEmpty()) {
                tail = node;
                node.next = node;        // first node points to itself
            } else {
                node.next = tail.next;   // new node -> head
                tail.next = node;        // old last -> new node
                tail = node;
            }
            count++;
            System.out.println("Booked ticket #" + id + " for " + customer);
        }

        // Extra safety: one seat cannot be sold twice for the same movie
        private boolean isSeatTaken(String movie, String seat) {
            if (isEmpty()) return false;
            Ticket t = tail.next;
            do {
                if (t.movie.equalsIgnoreCase(movie) && t.seat.equalsIgnoreCase(seat)) return true;
                t = t.next;
            } while (t != tail.next);
            return false;
        }

        // ---- 2) REMOVE TICKET BY ID ---------------------------- O(n) ------
        void removeTicket(int id) {
            if (isEmpty()) { System.out.println("No tickets to remove."); return; }

            Ticket prev = tail, cur = tail.next;
            for (int i = 0; i < count; i++) {                   // exactly one full cycle
                if (cur.id == id) {
                    if (count == 1) {
                        tail = null;                            // list becomes empty
                    } else {
                        prev.next = cur.next;                   // bypass cur
                        if (cur == tail) tail = prev;           // removed last ticket
                    }
                    cur.next = null;
                    count--;
                    System.out.println("Cancelled ticket #" + id);
                    return;
                }
                prev = cur;
                cur = cur.next;
            }
            System.out.println("Ticket #" + id + " not found.");
        }

        // ---- 3) DISPLAY ALL TICKETS ---------------------------- O(n) ------
        void display() {
            if (isEmpty()) { System.out.println("No tickets booked."); return; }
            System.out.println("---- Booked Tickets (" + count + ") ----");
            Ticket t = tail.next;                               // start from head
            do {
                System.out.println("  " + format(t));
                t = t.next;
            } while (t != tail.next);                           // stop after one full loop
        }

        // ---- 4a) SEARCH BY CUSTOMER NAME ----------------------- O(n) ------
        void searchByCustomer(String name) {
            System.out.println("Search Customer = " + name);
            search(name, true);
        }

        // ---- 4b) SEARCH BY MOVIE NAME -------------------------- O(n) ------
        void searchByMovie(String movie) {
            System.out.println("Search Movie = " + movie);
            search(movie, false);
        }

        private void search(String key, boolean byCustomer) {
            if (isEmpty()) { System.out.println("  No tickets booked."); return; }
            boolean found = false;
            Ticket t = tail.next;
            do {
                String field = byCustomer ? t.customer : t.movie;
                if (field.equalsIgnoreCase(key)) { System.out.println("  FOUND -> " + format(t)); found = true; }
                t = t.next;
            } while (t != tail.next);
            if (!found) System.out.println("  No match found.");
        }

        // ---- 5) TOTAL NUMBER OF TICKETS ------------------------ O(1) ------
        int totalTickets() { return count; }

        private String format(Ticket t) {
            return "Ticket#" + t.id + " | " + t.customer + " | " + t.movie
                    + " | Seat " + t.seat + " | " + t.bookingTime;
        }
    }

    // ------------------------------ MAIN (DEMO) -----------------------------
    public static void main(String[] args) {
        ReservationSystem rs = new ReservationSystem();

        System.out.println(">> Empty checks");
        rs.display();
        rs.removeTicket(1);
        System.out.println("Total tickets: " + rs.totalTickets());

        System.out.println("\n>> Booking tickets");
        rs.addTicket(1, "Karan", "Inception", "A1", "2026-11-26 10:00");
        rs.addTicket(2, "Riya", "Inception", "A2", "2026-11-26 10:05");
        rs.addTicket(3, "Aman", "Interstellar", "B5", "2026-11-26 10:10");
        rs.addTicket(4, "Neha", "Inception", "A1", "2026-11-26 10:15");   // seat clash
        rs.addTicket(5, "Dev", "Dangal", "C3", "2026-11-26 10:20");
        rs.display();

        System.out.println("\n>> Search");
        rs.searchByCustomer("riya");
        rs.searchByMovie("Inception");
        rs.searchByMovie("Titanic");

        System.out.println("\nTotal tickets: " + rs.totalTickets());

        System.out.println("\n>> Cancel first, last, middle, missing");
        rs.removeTicket(1);
        rs.removeTicket(5);
        rs.removeTicket(3);
        rs.removeTicket(99);
        rs.display();

        System.out.println("\n>> Cancel the last remaining ticket");
        rs.removeTicket(2);
        rs.display();
        System.out.println("Total tickets: " + rs.totalTickets());
    }
}
