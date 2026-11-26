/*
 * ============================================================================
 *  PROBLEM 8 : DOUBLY LINKED LIST - UNDO / REDO FOR A TEXT EDITOR
 *  Node data  : one STATE (snapshot) of the text
 *  Pointers   : prev -> older state (UNDO),  next -> newer state (REDO)
 *  Operations : 1) Add new text state at the end on every action
 *               2) Undo      3) Redo
 *               4) Display current state
 *               5) Limit history to a fixed size (MAX_HISTORY = 10 states)
 *  Run        : javac Q8_UndoRedoTextEditor.java
 *               java Q8_UndoRedoTextEditor
 * ============================================================================
 */
public class Q8_UndoRedoTextEditor {

    // ---------------------------- NODE CLASS --------------------------------
    static class State {
        String text;
        State prev;
        State next;
        State(String text) { this.text = text; }
    }

    // ------------------------- EDITOR CLASS ---------------------------------
    static class TextEditor {
        private final int MAX_HISTORY;   // maximum number of states kept
        private State head;              // oldest state
        private State tail;              // newest state
        private State current;           // the state shown on screen
        private int size;

        TextEditor(int maxHistory) {
            this.MAX_HISTORY = maxHistory;
            // Start with one empty state so the user can undo back to a blank page
            head = tail = current = new State("");
            size = 1;
        }

        // ---- 1) ADD A NEW STATE (user types something) --------- O(n) worst -
        void type(String extra) {
            String newText = current.text + extra;

            // If the user typed after an UNDO, the old "redo" states are no longer valid.
            // Cut them off, otherwise history would branch.
            if (current != tail) {
                current.next.prev = null;
                current.next = null;
                tail = current;
                size = countStates();
            }

            State node = new State(newText);
            node.prev = tail;            // link new node at the end
            tail.next = node;
            tail = node;
            current = node;
            size++;

            // ---- 5) LIMIT HISTORY SIZE ---------------------------------------
            if (size > MAX_HISTORY) {
                State oldHead = head;
                head = head.next;        // drop the oldest state
                head.prev = null;
                oldHead.next = null;     // nullify pointers
                size--;
                System.out.println("   (history limit reached: oldest state dropped)");
            }
            System.out.println("Typed \"" + extra + "\"  -> text is now: \"" + current.text + "\"");
        }

        // ---- 2) UNDO ------------------------------------------- O(1) ------
        void undo() {
            if (current.prev == null) { System.out.println("Nothing to undo."); return; }
            current = current.prev;      // step back in history
            System.out.println("UNDO -> \"" + current.text + "\"");
        }

        // ---- 3) REDO ------------------------------------------- O(1) ------
        void redo() {
            if (current.next == null) { System.out.println("Nothing to redo."); return; }
            current = current.next;      // step forward in history
            System.out.println("REDO -> \"" + current.text + "\"");
        }

        // ---- 4) DISPLAY CURRENT STATE -------------------------- O(1) ------
        void displayCurrent() {
            System.out.println("CURRENT TEXT: \"" + current.text + "\"");
        }

        // Extra: show whole history, marking the current state
        void displayHistory() {
            System.out.println("History (" + size + "/" + MAX_HISTORY + " states):");
            int i = 1;
            for (State s = head; s != null; s = s.next, i++)
                System.out.println("  " + i + ". \"" + s.text + "\"" + (s == current ? "   <-- current" : ""));
        }

        private int countStates() {
            int c = 0;
            for (State s = head; s != null; s = s.next) c++;
            return c;
        }
    }

    // ------------------------------ MAIN (DEMO) -----------------------------
    public static void main(String[] args) {
        TextEditor ed = new TextEditor(10);

        System.out.println(">> Nothing to undo/redo at the start");
        ed.undo();
        ed.redo();

        System.out.println("\n>> Typing");
        ed.type("Hello");
        ed.type(" World");
        ed.type("!");
        ed.displayHistory();

        System.out.println("\n>> Undo twice, then redo once");
        ed.undo();
        ed.undo();
        ed.redo();
        ed.displayCurrent();

        System.out.println("\n>> Typing after undo clears the redo history");
        ed.type(" Java");
        ed.redo();                // nothing to redo now
        ed.displayHistory();

        System.out.println("\n>> History limit demo (max 10 states, now 4 used)");
        for (int i = 1; i <= 7; i++) ed.type(" " + i);
        ed.displayHistory();

        System.out.println("\n>> Undo all the way back");
        for (int i = 0; i < 10; i++) ed.undo();   // 9 real undos + 1 "nothing to undo"
        ed.displayCurrent();
    }
}
