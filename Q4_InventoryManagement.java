import java.util.Comparator;

/*
 * ============================================================================
 *  PROBLEM 4 : SINGLY LINKED LIST - INVENTORY MANAGEMENT SYSTEM
 *  Node data  : Item Name, Item ID, Quantity, Price
 *  Operations : 1) Add at beginning / end / specific position
 *               2) Remove by Item ID
 *               3) Update quantity by Item ID
 *               4) Search by Item ID or Item Name
 *               5) Total inventory value  (sum of Price * Quantity)
 *               6) Sort by Item Name or Price (ascending / descending)
 *                  using MERGE SORT on the linked list
 *  Run        : javac Q4_InventoryManagement.java
 *               java Q4_InventoryManagement
 * ============================================================================
 */
public class Q4_InventoryManagement {

    // ---------------------------- NODE CLASS --------------------------------
    static class Item {
        String name;
        int id;
        int quantity;
        double price;
        Item next;

        Item(String name, int id, int quantity, double price) {
            this.name = name;
            this.id = id;
            this.quantity = quantity;
            this.price = price;
        }
    }

    // ------------------------- LINKED LIST CLASS ----------------------------
    static class Inventory {
        private Item head;
        private int size;

        boolean isEmpty() { return head == null; }

        // ---- 1a) ADD AT BEGINNING ------------------------------ O(1) ------
        void addAtBeginning(String name, int id, int qty, double price) {
            Item node = new Item(name, id, qty, price);
            node.next = head;
            head = node;
            size++;
            System.out.println("Added at beginning: " + name);
        }

        // ---- 1b) ADD AT END ------------------------------------ O(n) ------
        void addAtEnd(String name, int id, int qty, double price) {
            Item node = new Item(name, id, qty, price);
            if (isEmpty()) head = node;
            else {
                Item temp = head;
                while (temp.next != null) temp = temp.next;
                temp.next = node;
            }
            size++;
            System.out.println("Added at end: " + name);
        }

        // ---- 1c) ADD AT POSITION (1-based) --------------------- O(n) ------
        void addAtPosition(int pos, String name, int id, int qty, double price) {
            if (pos < 1 || pos > size + 1) {
                System.out.println("Invalid position " + pos + " (valid: 1 to " + (size + 1) + ")");
                return;
            }
            if (pos == 1) { addAtBeginning(name, id, qty, price); return; }
            Item node = new Item(name, id, qty, price);
            Item temp = head;
            for (int i = 1; i < pos - 1; i++) temp = temp.next;   // node BEFORE pos
            node.next = temp.next;
            temp.next = node;
            size++;
            System.out.println("Added at position " + pos + ": " + name);
        }

        // ---- 2) REMOVE BY ITEM ID ------------------------------ O(n) ------
        void removeById(int id) {
            if (isEmpty()) { System.out.println("Inventory is empty."); return; }
            if (head.id == id) {
                Item old = head;
                head = head.next;
                old.next = null;
                size--;
                System.out.println("Removed item ID " + id);
                return;
            }
            Item temp = head;
            while (temp.next != null && temp.next.id != id) temp = temp.next;
            if (temp.next == null) { System.out.println("Item ID " + id + " not found."); return; }
            Item old = temp.next;
            temp.next = old.next;
            old.next = null;
            size--;
            System.out.println("Removed item ID " + id);
        }

        // ---- 3) UPDATE QUANTITY BY ITEM ID --------------------- O(n) ------
        void updateQuantity(int id, int newQty) {
            for (Item t = head; t != null; t = t.next) {
                if (t.id == id) {
                    System.out.println("Quantity of '" + t.name + "' updated: " + t.quantity + " -> " + newQty);
                    t.quantity = newQty;
                    return;
                }
            }
            System.out.println("Item ID " + id + " not found. Quantity not updated.");
        }

        // ---- 4a) SEARCH BY ITEM ID ----------------------------- O(n) ------
        void searchById(int id) {
            for (Item t = head; t != null; t = t.next) {
                if (t.id == id) { System.out.println("FOUND -> " + format(t)); return; }
            }
            System.out.println("Item ID " + id + " not found.");
        }

        // ---- 4b) SEARCH BY ITEM NAME --------------------------- O(n) ------
        void searchByName(String name) {
            boolean found = false;
            for (Item t = head; t != null; t = t.next) {
                if (t.name.equalsIgnoreCase(name)) { System.out.println("FOUND -> " + format(t)); found = true; }
            }
            if (!found) System.out.println("Item '" + name + "' not found.");
        }

        // ---- 5) TOTAL INVENTORY VALUE -------------------------- O(n) ------
        double totalValue() {
            double total = 0;
            for (Item t = head; t != null; t = t.next) total += t.price * t.quantity;
            return total;
        }

        // ---- 6) SORT USING MERGE SORT -------------------------- O(n log n) -
        // field: "name" or "price";  ascending: true / false
        void sort(String field, boolean ascending) {
            Comparator<Item> cmp;
            if (field.equalsIgnoreCase("name")) cmp = (a, b) -> a.name.compareToIgnoreCase(b.name);
            else                                cmp = (a, b) -> Double.compare(a.price, b.price);
            if (!ascending) cmp = cmp.reversed();

            head = mergeSort(head, cmp);
            System.out.println("Sorted by " + field + (ascending ? " (ascending)" : " (descending)"));
        }

        // Step 1: split the list in two halves, Step 2: sort each, Step 3: merge
        private Item mergeSort(Item h, Comparator<Item> cmp) {
            if (h == null || h.next == null) return h;       // base case: 0 or 1 node
            Item mid = getMiddle(h);
            Item right = mid.next;
            mid.next = null;                                  // cut the list into two
            return merge(mergeSort(h, cmp), mergeSort(right, cmp), cmp);
        }

        // Slow/fast pointer technique: when fast reaches the end, slow is at the middle
        private Item getMiddle(Item h) {
            Item slow = h, fast = h.next;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            return slow;
        }

        // Merge two sorted lists into one sorted list
        private Item merge(Item a, Item b, Comparator<Item> cmp) {
            Item dummy = new Item("", 0, 0, 0);               // dummy head simplifies code
            Item tail = dummy;
            while (a != null && b != null) {
                if (cmp.compare(a, b) <= 0) { tail.next = a; a = a.next; }
                else                        { tail.next = b; b = b.next; }
                tail = tail.next;
            }
            tail.next = (a != null) ? a : b;                  // attach the remaining nodes
            return dummy.next;
        }

        void display() {
            if (isEmpty()) { System.out.println("Inventory is empty."); return; }
            System.out.println("---- Inventory (" + size + " items) ----");
            System.out.printf("%-12s %-6s %-6s %-10s%n", "Name", "ID", "Qty", "Price");
            for (Item t = head; t != null; t = t.next)
                System.out.printf("%-12s %-6d %-6d %-10.2f%n", t.name, t.id, t.quantity, t.price);
        }

        private String format(Item t) {
            return "Name=" + t.name + ", ID=" + t.id + ", Qty=" + t.quantity + ", Price=" + t.price;
        }
    }

    // ------------------------------ MAIN (DEMO) -----------------------------
    public static void main(String[] args) {
        Inventory inv = new Inventory();

        System.out.println(">> Empty checks");
        inv.removeById(1);
        inv.display();

        System.out.println("\n>> Adding items");
        inv.addAtEnd("Keyboard", 102, 10, 750.00);
        inv.addAtEnd("Mouse", 103, 25, 350.50);
        inv.addAtBeginning("Monitor", 101, 5, 8999.99);
        inv.addAtPosition(3, "Cable", 104, 50, 120.00);
        inv.addAtPosition(20, "Bad", 0, 0, 0);
        inv.display();

        System.out.println("\n>> Search");
        inv.searchById(103);
        inv.searchByName("keyboard");
        inv.searchByName("Printer");

        System.out.println("\n>> Update quantity");
        inv.updateQuantity(104, 60);
        inv.updateQuantity(999, 1);

        System.out.printf("%n>> Total inventory value = %.2f%n", inv.totalValue());

        System.out.println("\n>> Sorting");
        inv.sort("name", true);   inv.display();
        inv.sort("name", false);  inv.display();
        inv.sort("price", true);  inv.display();
        inv.sort("price", false); inv.display();

        System.out.println("\n>> Remove items");
        inv.removeById(101);   // head after sorting by price desc
        inv.removeById(104);
        inv.removeById(555);
        inv.display();
        System.out.printf("Total inventory value = %.2f%n", inv.totalValue());
    }
}
