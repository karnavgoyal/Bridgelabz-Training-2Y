/*
 * ============================================================================
 *  PROBLEM 3 : CIRCULAR LINKED LIST - TASK SCHEDULER
 *  Node data  : Task ID, Task Name, Priority, Due Date
 *  Operations : 1) Add at beginning / end / specific position
 *               2) Remove by Task ID
 *               3) View current task and move to next
 *               4) Display all tasks from head
 *               5) Search by Priority
 *  Run        : javac Q3_TaskScheduler.java
 *               java Q3_TaskScheduler
 * ============================================================================
 */
public class Q3_TaskScheduler {

    // ---------------------------- NODE CLASS --------------------------------
    static class Task {
        int id;
        String name;
        int priority;                    // 1 = highest priority
        String dueDate;
        Task next;

        Task(int id, String name, int priority, String dueDate) {
            this.id = id;
            this.name = name;
            this.priority = priority;
            this.dueDate = dueDate;
        }
    }

    // ------------------------- CIRCULAR LIST CLASS --------------------------
    static class TaskScheduler {
        private Task head;               // first task
        private Task tail;               // last task; tail.next is ALWAYS head
        private Task current;            // the task currently being viewed
        private int size;

        boolean isEmpty() { return head == null; }

        // Helper: first node inserted -> it points to itself
        private void insertFirstNode(Task node) {
            head = tail = current = node;
            node.next = node;            // circular: points to itself
        }

        // ---- 1a) ADD AT BEGINNING ------------------------------ O(1) ------
        void addAtBeginning(int id, String name, int priority, String due) {
            Task node = new Task(id, name, priority, due);
            if (isEmpty()) insertFirstNode(node);
            else {
                node.next = head;
                head = node;
                tail.next = head;        // keep the list circular
            }
            size++;
            System.out.println("Added at beginning: " + name);
        }

        // ---- 1b) ADD AT END ------------------------------------ O(1) ------
        void addAtEnd(int id, String name, int priority, String due) {
            Task node = new Task(id, name, priority, due);
            if (isEmpty()) insertFirstNode(node);
            else {
                tail.next = node;
                node.next = head;        // new last node points back to head
                tail = node;
            }
            size++;
            System.out.println("Added at end: " + name);
        }

        // ---- 1c) ADD AT POSITION (1-based) --------------------- O(n) ------
        void addAtPosition(int pos, int id, String name, int priority, String due) {
            if (pos < 1 || pos > size + 1) {
                System.out.println("Invalid position " + pos + " (valid: 1 to " + (size + 1) + ")");
                return;
            }
            if (pos == 1) { addAtBeginning(id, name, priority, due); return; }
            if (pos == size + 1) { addAtEnd(id, name, priority, due); return; }

            Task node = new Task(id, name, priority, due);
            Task temp = head;
            for (int i = 1; i < pos - 1; i++) temp = temp.next;  // node BEFORE pos
            node.next = temp.next;
            temp.next = node;
            size++;
            System.out.println("Added at position " + pos + ": " + name);
        }

        // ---- 2) REMOVE BY TASK ID ------------------------------ O(n) ------
        void removeById(int id) {
            if (isEmpty()) { System.out.println("Scheduler is empty. Nothing to remove."); return; }

            Task prev = tail;            // prev always trails cur by one node
            Task cur = head;
            do {                         // do-while: stops after ONE full cycle
                if (cur.id == id) {
                    if (size == 1) {                      // only node in the list
                        head = tail = current = null;
                    } else {
                        prev.next = cur.next;             // bypass cur
                        if (cur == head) head = cur.next; // removed the head
                        if (cur == tail) tail = prev;     // removed the tail
                        if (cur == current) current = cur.next; // move "current" forward
                    }
                    cur.next = null;
                    size--;
                    System.out.println("Removed task ID " + id);
                    return;
                }
                prev = cur;
                cur = cur.next;
            } while (cur != head);
            System.out.println("Task ID " + id + " not found.");
        }

        // ---- 3) VIEW CURRENT TASK AND MOVE TO NEXT ------------- O(1) ------
        void viewCurrentAndMoveNext() {
            if (isEmpty()) { System.out.println("No tasks to view."); return; }
            System.out.println("CURRENT TASK -> " + format(current));
            current = current.next;      // wraps to head automatically after the last task
            System.out.println("   (moved to next: " + current.name + ")");
        }

        // ---- 4) DISPLAY ALL TASKS FROM HEAD -------------------- O(n) ------
        void display() {
            if (isEmpty()) { System.out.println("No tasks."); return; }
            System.out.println("---- Tasks (" + size + ") ----");
            Task temp = head;
            do {                         // do-while avoids an infinite loop (best practice #6)
                System.out.println("  " + format(temp));
                temp = temp.next;
            } while (temp != head);
            System.out.println("  (last task points back to head: " + head.name + ")");
        }

        // ---- 5) SEARCH BY PRIORITY ----------------------------- O(n) ------
        void searchByPriority(int priority) {
            if (isEmpty()) { System.out.println("No tasks."); return; }
            System.out.println("Search Priority = " + priority);
            boolean found = false;
            Task temp = head;
            do {
                if (temp.priority == priority) { System.out.println("  FOUND -> " + format(temp)); found = true; }
                temp = temp.next;
            } while (temp != head);
            if (!found) System.out.println("  No task with this priority.");
        }

        private String format(Task t) {
            return "ID=" + t.id + " | " + t.name + " | Priority=" + t.priority + " | Due=" + t.dueDate;
        }
    }

    // ------------------------------ MAIN (DEMO) -----------------------------
    public static void main(String[] args) {
        TaskScheduler s = new TaskScheduler();

        System.out.println(">> Empty checks");
        s.display();
        s.removeById(1);
        s.viewCurrentAndMoveNext();

        System.out.println("\n>> Adding tasks");
        s.addAtEnd(2, "Write report", 2, "2026-12-01");
        s.addAtEnd(3, "Submit assignment", 1, "2026-11-30");
        s.addAtBeginning(1, "Study DSA", 1, "2026-11-28");
        s.addAtPosition(3, 4, "Gym", 3, "2026-12-05");
        s.display();

        System.out.println("\n>> View current & move (circular: wraps after last)");
        for (int i = 0; i < 5; i++) s.viewCurrentAndMoveNext();

        System.out.println("\n>> Search by priority");
        s.searchByPriority(1);
        s.searchByPriority(9);

        System.out.println("\n>> Remove head, tail, middle, missing");
        s.removeById(1);
        s.removeById(3);
        s.removeById(4);
        s.removeById(99);
        s.display();

        System.out.println("\n>> Remove the last remaining task");
        s.removeById(2);
        s.display();
    }
}
