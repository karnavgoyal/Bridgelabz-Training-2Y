import java.util.Arrays;
import java.util.Stack;

/*
 * ============================================================================
 *  S3 : STOCK SPAN PROBLEM
 *  Problem : For each day i, span[i] = number of consecutive days ending at
 *            day i (including i) whose price was <= price[i].
 *  Brute   : For every day walk backwards            -> O(n^2)
 *  Optimal : Keep a stack of INDICES whose prices are in decreasing order.
 *            For day i:
 *              - pop all indices whose price <= price[i]  (they are "covered")
 *              - if stack empty -> span = i + 1   (price[i] beats everything so far)
 *                else           -> span = i - stack.peek()  (distance to the
 *                                  previous GREATER price)
 *              - push i
 *            Each index is pushed and popped at most once.
 *  Time    : O(n)          Space : O(n)
 *  Example : prices = {100, 80, 60, 70, 60, 75, 85}
 *            spans  = {  1,  1,  1,  2,  1,  4,  6}
 *  Run     : javac S3_StockSpan.java   then   java S3_StockSpan
 * ============================================================================
 */
public class S3_StockSpan {

    static int[] calculateSpan(int[] price) {
        int n = price.length;
        int[] span = new int[n];
        Stack<Integer> stack = new Stack<>();        // stores indices

        for (int i = 0; i < n; i++) {
            // remove days whose price is <= today's price
            while (!stack.isEmpty() && price[stack.peek()] <= price[i]) stack.pop();

            span[i] = stack.isEmpty() ? (i + 1) : (i - stack.peek());
            stack.push(i);
        }
        return span;
    }

    static void test(int... prices) {
        System.out.println("Prices: " + Arrays.toString(prices));
        System.out.println("Spans : " + Arrays.toString(calculateSpan(prices)) + "\n");
    }

    public static void main(String[] args) {
        test(100, 80, 60, 70, 60, 75, 85);   // sample
        test(10, 20, 30, 40, 50);            // increasing -> span grows 1..n
        test(50, 40, 30, 20, 10);            // decreasing -> all spans 1
        test(30, 30, 30);                    // equal prices count (<=)
        test(42);                            // single day
        test();                              // empty input
    }
}
