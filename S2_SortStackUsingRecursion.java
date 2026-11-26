import java.util.Stack;

/*
 * ============================================================================
 *  S2 : SORT A STACK USING RECURSION
 *  Problem : Sort a stack in ascending order (smallest at the BOTTOM,
 *            largest on the TOP) using only recursion and stack operations.
 *  Idea    : sort(stack):
 *              1. if stack is empty -> return            (BASE CASE)
 *              2. pop the top element  -> 'temp'
 *              3. sort(stack)          -> recursively sort the rest
 *              4. insertSorted(stack, temp) -> put temp at the correct place
 *            insertSorted(stack, x):
 *              1. if stack is empty OR x >= top -> push x   (BASE CASE)
 *              2. else pop the top -> 't', insertSorted(stack, x), push t back
 *  Dry run : [3, 1, 2] (bottom->top)
 *            pop 2 -> sort [3,1] -> pop 1 -> sort [3] -> pop 3 -> sort [] = []
 *            insert 3 -> [3];  insert 1 -> [1,3];  insert 2 -> [1,2,3]
 *  Time    : O(n^2)       Space: O(n) recursion stack
 *  Run     : javac S2_SortStackUsingRecursion.java  then  java S2_SortStackUsingRecursion
 * ============================================================================
 */
public class S2_SortStackUsingRecursion {

    // Sorts the whole stack
    static void sortStack(Stack<Integer> stack) {
        if (stack.isEmpty()) return;                 // base case (prevents infinite recursion)
        int temp = stack.pop();                      // remove top element
        sortStack(stack);                            // sort the remaining stack
        insertSorted(stack, temp);                   // insert removed element at correct position
    }

    // Inserts x into an already-sorted stack so that it stays sorted
    static void insertSorted(Stack<Integer> stack, int x) {
        if (stack.isEmpty() || x >= stack.peek()) {  // base case: x belongs on top
            stack.push(x);
            return;
        }
        int top = stack.pop();                       // larger element blocks x -> take it out
        insertSorted(stack, x);                      // insert x deeper
        stack.push(top);                             // put the larger element back
    }

    static void test(String label, int... values) {
        Stack<Integer> s = new Stack<>();
        for (int v : values) s.push(v);
        System.out.println(label);
        System.out.println("  Before (bottom -> top): " + s);
        sortStack(s);
        System.out.println("  After  (bottom -> top): " + s);
    }

    public static void main(String[] args) {
        test("Test 1: mixed values", 34, 3, 31, 98, 92, 23);
        test("Test 2: already sorted", 1, 2, 3, 4);
        test("Test 3: reverse sorted", 5, 4, 3, 2, 1);
        test("Test 4: duplicates", 3, 1, 3, 2, 1);
        test("Test 5: single element", 7);
        test("Test 6: empty stack");
    }
}
