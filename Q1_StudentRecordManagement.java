/*
 * ============================================================================
 *  PROBLEM 1 : SINGLY LINKED LIST - STUDENT RECORD MANAGEMENT
 *  Node data  : Roll Number, Name, Age, Grade
 *  Operations : 1) Add at beginning / end / specific position
 *               2) Delete by Roll Number
 *               3) Search by Roll Number
 *               4) Display all records
 *               5) Update grade by Roll Number
 *  Run        : javac Q1_StudentRecordManagement.java
 *               java Q1_StudentRecordManagement
 * ============================================================================
 */
public class Q1_StudentRecordManagement {

    // ---------------------------- NODE CLASS --------------------------------
    static class Student {
        int roll;
        String name;
        int age;
        String grade;
        Student next;                    // pointer to the next student (null at the end)

        Student(int roll, String name, int age, String grade) {
            this.roll = roll;
            this.name = name;
            this.age = age;
            this.grade = grade;
            this.next = null;
        }
    }

    // ------------------------- LINKED LIST CLASS ----------------------------
    static class StudentList {
        private Student head;            // first student
        private int size;                // number of students

        // Helper: is the list empty? (null check - best practice #2)
        boolean isEmpty() { return head == null; }

        // Helper: roll numbers must be unique
        private boolean exists(int roll) { return search(roll) != null; }

        // ---- 1a) ADD AT BEGINNING ------------------------------ O(1) ------
        void addAtBeginning(int roll, String name, int age, String grade) {
            if (exists(roll)) { System.out.println("Roll " + roll + " already exists. Not added."); return; }
            Student node = new Student(roll, name, age, grade);
            node.next = head;            // new node points to the old head
            head = node;                 // head now points to the new node
            size++;
            System.out.println("Added at beginning: " + name);
        }

        // ---- 1b) ADD AT END ------------------------------------ O(n) ------
        void addAtEnd(int roll, String name, int age, String grade) {
            if (exists(roll)) { System.out.println("Roll " + roll + " already exists. Not added."); return; }
            Student node = new Student(roll, name, age, grade);
            if (isEmpty()) {
                head = node;
            } else {
                Student temp = head;
                while (temp.next != null) temp = temp.next;   // go to last node
                temp.next = node;
            }
            size++;
            System.out.println("Added at end: " + name);
        }

        // ---- 1c) ADD AT SPECIFIC POSITION (1-based) ------------ O(n) ------
        void addAtPosition(int pos, int roll, String name, int age, String grade) {
            if (pos < 1 || pos > size + 1) {
                System.out.println("Invalid position " + pos + " (valid: 1 to " + (size + 1) + ")");
                return;
            }
            if (pos == 1) { addAtBeginning(roll, name, age, grade); return; }
            if (pos == size + 1) { addAtEnd(roll, name, age, grade); return; }
            if (exists(roll)) { System.out.println("Roll " + roll + " already exists. Not added."); return; }

            Student node = new Student(roll, name, age, grade);
            Student temp = head;
            for (int i = 1; i < pos - 1; i++) temp = temp.next;   // stop at node BEFORE pos
            node.next = temp.next;       // step 1: link new node to the next one
            temp.next = node;            // step 2: link previous node to new node
            size++;
            System.out.println("Added at position " + pos + ": " + name);
        }

        // ---- 2) DELETE BY ROLL NUMBER -------------------------- O(n) ------
        void deleteByRoll(int roll) {
            if (isEmpty()) { System.out.println("List is empty. Nothing to delete."); return; }

            if (head.roll == roll) {     // boundary case: deleting the head
                Student old = head;
                head = head.next;
                old.next = null;         // nullify pointer (best practice #4)
                size--;
                System.out.println("Deleted roll " + roll);
                return;
            }
            Student temp = head;
            while (temp.next != null && temp.next.roll != roll) temp = temp.next;
            if (temp.next == null) {
                System.out.println("Roll " + roll + " not found.");
            } else {
                Student old = temp.next;
                temp.next = old.next;    // bypass the deleted node
                old.next = null;
                size--;
                System.out.println("Deleted roll " + roll);
            }
        }

        // ---- 3) SEARCH BY ROLL NUMBER -------------------------- O(n) ------
        Student search(int roll) {
            Student temp = head;
            while (temp != null) {
                if (temp.roll == roll) return temp;
                temp = temp.next;
            }
            return null;
        }

        void searchAndPrint(int roll) {
            Student s = search(roll);
            if (s == null) System.out.println("Roll " + roll + " not found.");
            else System.out.println("FOUND -> " + format(s));
        }

        // ---- 4) DISPLAY ALL RECORDS ---------------------------- O(n) ------
        void display() {
            if (isEmpty()) { System.out.println("No student records."); return; }
            System.out.println("---- Student Records (" + size + ") ----");
            System.out.printf("%-6s %-12s %-5s %-6s%n", "Roll", "Name", "Age", "Grade");
            Student temp = head;
            while (temp != null) {
                System.out.printf("%-6d %-12s %-5d %-6s%n", temp.roll, temp.name, temp.age, temp.grade);
                temp = temp.next;
            }
            System.out.println("-----------------------------");
        }

        // ---- 5) UPDATE GRADE BY ROLL NUMBER -------------------- O(n) ------
        void updateGrade(int roll, String newGrade) {
            Student s = search(roll);
            if (s == null) { System.out.println("Roll " + roll + " not found. Grade not updated."); return; }
            String old = s.grade;
            s.grade = newGrade;
            System.out.println("Grade of roll " + roll + " updated: " + old + " -> " + newGrade);
        }

        private String format(Student s) {
            return "Roll=" + s.roll + ", Name=" + s.name + ", Age=" + s.age + ", Grade=" + s.grade;
        }
    }

    // ------------------------------ MAIN (DEMO) -----------------------------
    public static void main(String[] args) {
        StudentList list = new StudentList();

        System.out.println(">> Delete / display on EMPTY list");
        list.deleteByRoll(1);
        list.display();

        System.out.println("\n>> Adding students");
        list.addAtEnd(102, "Riya", 19, "A");
        list.addAtEnd(103, "Aman", 20, "B");
        list.addAtBeginning(101, "Karan", 19, "A+");
        list.addAtPosition(3, 105, "Neha", 18, "B+");   // middle
        list.addAtPosition(9, 106, "Test", 20, "C");     // invalid position
        list.addAtEnd(102, "Duplicate", 20, "C");        // duplicate roll
        list.display();

        System.out.println("\n>> Search");
        list.searchAndPrint(103);
        list.searchAndPrint(999);

        System.out.println("\n>> Update grade");
        list.updateGrade(103, "A");
        list.updateGrade(555, "A");

        System.out.println("\n>> Delete head, middle, tail, missing");
        list.deleteByRoll(101);   // head
        list.deleteByRoll(105);   // middle
        list.deleteByRoll(103);   // tail
        list.deleteByRoll(777);   // not found
        list.display();
    }
}
