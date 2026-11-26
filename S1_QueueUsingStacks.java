import java.util.ArrayDeque;
import java.util.Deque;

/*
 * ============================================================================
 *  S1 : IMPLEMENT A QUEUE USING TWO STACKS
 *  Problem : Build a FIFO queue using only stack (LIFO) operations.
 *  Idea    : inStack  -> receives every ENQUEUE (push)
 *            outStack -> serves every DEQUEUE (pop)
 *            Only when outStack is empty, move ALL elements from inStack to
 *            outStack. This move REVERSES the order, so the oldest element
 *            ends up on top -> FIFO behaviour.
 *  Time    : enqueue O(1);  dequeue amortized O(1)  (each element is moved at
 *            most once from inStack to outStack)
 *  Space   : O(n)
 *  Run     : javac S1_QueueUsingStacks.java   then   java S1_QueueUsingStacks
 * ============================================================================
 */
public class S1_QueueUsingStacks {

    static class MyQueue {
        // ArrayDeque used ONLY through push/pop/peek = a proper stack
        // (best practice: prefer Deque over the legacy Stack class)
        private final Deque<Integer> inStack = new ArrayDeque<>();
        private final Deque<Integer> outStack = new ArrayDeque<>();

        // ---- ENQUEUE: always push on inStack ---------------------- O(1) ----
        void enqueue(int x) {
            inStack.push(x);
            System.out.println("enqueue(" + x + ")");
        }

        // Move elements only when outStack is empty
        private void transferIfNeeded() {
            if (outStack.isEmpty()) {
                while (!inStack.isEmpty()) outStack.push(inStack.pop());
            }
        }

        // ---- DEQUEUE: pop from outStack ----------------- amortized O(1) ----
        int dequeue() {
            transferIfNeeded();
            if (outStack.isEmpty()) throw new IllegalStateException("Queue Underflow: queue is empty");
            return outStack.pop();
        }

        // ---- PEEK: front element without removing ------ amortized O(1) ----
        int peek() {
            transferIfNeeded();
            if (outStack.isEmpty()) throw new IllegalStateException("Queue is empty");
            return outStack.peek();
        }

        boolean isEmpty() { return inStack.isEmpty() && outStack.isEmpty(); }
        int size() { return inStack.size() + outStack.size(); }
    }

    public static void main(String[] args) {
        MyQueue q = new MyQueue();

        System.out.println(">> Dequeue from empty queue");
        try { q.dequeue(); } catch (IllegalStateException e) { System.out.println("  Caught: " + e.getMessage()); }

        System.out.println("\n>> Enqueue 10, 20, 30");
        q.enqueue(10);
        q.enqueue(20);
        q.enqueue(30);
        System.out.println("size = " + q.size() + ", front = " + q.peek());   // 10

        System.out.println("\n>> Dequeue twice (FIFO -> 10 then 20)");
        System.out.println("dequeue() = " + q.dequeue());
        System.out.println("dequeue() = " + q.dequeue());

        System.out.println("\n>> Interleave: enqueue 40, 50 while 30 is still waiting");
        q.enqueue(40);
        q.enqueue(50);
        System.out.println("dequeue() = " + q.dequeue());   // 30
        System.out.println("dequeue() = " + q.dequeue());   // 40
        System.out.println("dequeue() = " + q.dequeue());   // 50
        System.out.println("isEmpty = " + q.isEmpty());
    }
}
