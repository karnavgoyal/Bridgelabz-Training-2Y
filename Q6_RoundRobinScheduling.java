/*
 * ============================================================================
 *  PROBLEM 6 : CIRCULAR LINKED LIST - ROUND ROBIN CPU SCHEDULING
 *  Node data  : Process ID, Burst Time, Priority
 *  Operations : 1) Add a process at the end of the circular list
 *               2) Remove a process by Process ID (also done automatically
 *                  when a process finishes execution)
 *               3) Simulate round robin with a fixed time quantum
 *               4) Display the circular queue after each round
 *               5) Average waiting time and turn-around time
 *  Assumption : All processes arrive at time 0.
 *               Turn-Around Time (TAT) = Completion Time - Arrival Time
 *               Waiting Time    (WT)  = TAT - Burst Time
 *               (Priority is stored as required but plain Round Robin does not use it.)
 *  Run        : javac Q6_RoundRobinScheduling.java
 *               java Q6_RoundRobinScheduling
 * ============================================================================
 */
public class Q6_RoundRobinScheduling {

    // ---------------------------- NODE CLASS --------------------------------
    static class Process {
        int id;
        int burstTime;                   // total CPU time needed
        int priority;
        int remaining;                   // CPU time still needed
        Process next;

        Process(int id, int burstTime, int priority) {
            this.id = id;
            this.burstTime = burstTime;
            this.priority = priority;
            this.remaining = burstTime;
        }
    }

    // ------------------------- CIRCULAR QUEUE CLASS -------------------------
    static class RoundRobinQueue {
        private Process tail;            // we keep only 'tail'; head is tail.next
        private int size;

        boolean isEmpty() { return tail == null; }

        // ---- 1) ADD PROCESS AT END ----------------------------- O(1) ------
        void addProcess(int id, int burst, int priority) {
            Process node = new Process(id, burst, priority);
            if (isEmpty()) {
                tail = node;
                node.next = node;        // single node points to itself
            } else {
                node.next = tail.next;   // new node -> head
                tail.next = node;        // old tail -> new node
                tail = node;             // new node is the tail
            }
            size++;
            System.out.println("Added process P" + id + " (burst=" + burst + ")");
        }

        // ---- 2) REMOVE PROCESS BY ID (manual) ------------------ O(n) ------
        void removeById(int id) {
            if (isEmpty()) { System.out.println("Queue is empty."); return; }
            Process prev = tail, cur = tail.next;
            for (int i = 0; i < size; i++) {                // exactly one full cycle
                if (cur.id == id) {
                    unlink(prev, cur);
                    System.out.println("Removed process P" + id);
                    return;
                }
                prev = cur;
                cur = cur.next;
            }
            System.out.println("Process P" + id + " not found.");
        }

        // Helper used by removeById and by the simulation
        private void unlink(Process prev, Process cur) {
            if (size == 1) {
                tail = null;                                // queue becomes empty
            } else {
                prev.next = cur.next;                       // bypass cur
                if (cur == tail) tail = prev;               // removed the tail
            }
            cur.next = null;
            size--;
        }

        // ---- 4) DISPLAY QUEUE ---------------------------------- O(n) ------
        void display() {
            if (isEmpty()) { System.out.println("  Queue: (empty)"); return; }
            StringBuilder sb = new StringBuilder("  Queue: ");
            Process temp = tail.next;
            do {
                sb.append("P").append(temp.id).append("(rem=").append(temp.remaining).append(") -> ");
                temp = temp.next;
            } while (temp != tail.next);
            sb.append("(back to P").append(tail.next.id).append(")");
            System.out.println(sb);
        }

        // ---- 3) + 5) SIMULATE ROUND ROBIN & AVERAGES ----------- O(total/q) -
        void simulate(int quantum) {
            if (isEmpty()) { System.out.println("No processes to schedule."); return; }
            int n = size;
            int time = 0, round = 1;
            double totalWT = 0, totalTAT = 0;

            System.out.println("Time Quantum = " + quantum);
            System.out.println("Initial state:");
            display();

            while (!isEmpty()) {
                System.out.println("\n--- Round " + round + " ---");
                int inThisRound = size;                      // each process gets one turn per round
                Process prev = tail, cur = tail.next;

                for (int k = 0; k < inThisRound; k++) {
                    int run = Math.min(quantum, cur.remaining);
                    int start = time;
                    time += run;
                    cur.remaining -= run;
                    System.out.println("  P" + cur.id + " runs " + start + " -> " + time);

                    Process nextNode = cur.next;
                    if (cur.remaining == 0) {                // process finished
                        int tat = time;                      // arrival time = 0
                        int wt = tat - cur.burstTime;
                        totalTAT += tat;
                        totalWT += wt;
                        System.out.println("     P" + cur.id + " FINISHED  (TAT=" + tat + ", WT=" + wt + ")");
                        unlink(prev, cur);                   // remove after execution
                        // 'prev' stays the same because cur was removed
                    } else {
                        prev = cur;                          // move prev forward
                    }
                    cur = nextNode;
                }
                System.out.println("  After round " + round + ":");
                display();
                round++;
            }

            System.out.println("\n===== RESULT =====");
            System.out.printf("Average Waiting Time     = %.2f%n", totalWT / n);
            System.out.printf("Average Turn-Around Time = %.2f%n", totalTAT / n);
        }
    }

    // ------------------------------ MAIN (DEMO) -----------------------------
    public static void main(String[] args) {
        RoundRobinQueue q = new RoundRobinQueue();

        System.out.println(">> Empty checks");
        q.removeById(1);
        q.simulate(2);

        System.out.println("\n>> Adding processes");
        q.addProcess(1, 10, 2);
        q.addProcess(2, 5, 1);
        q.addProcess(3, 8, 3);
        q.addProcess(4, 3, 1);        // will be removed manually below
        q.removeById(4);
        q.removeById(99);

        System.out.println("\n>> Simulation");
        q.simulate(3);
    }
}
