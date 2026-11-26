import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/*
 * ============================================================================
 *  S4 : SLIDING WINDOW MAXIMUM
 *  Problem : Given array and window size k, print the maximum of every
 *            window of k consecutive elements.
 *  Brute   : scan every window                         -> O(n*k)
 *  Optimal : Deque of INDICES kept in DECREASING order of their values.
 *            Front of the deque is always the maximum of the current window.
 *            For each index i:
 *              1. remove front index if it is outside the window (<= i-k)
 *              2. remove from the BACK all indices whose value <= arr[i]
 *                 (they can never be a maximum while arr[i] is in the window)
 *              3. add i at the back
 *              4. once i >= k-1, answer for this window = arr[front]
 *  Time    : O(n) (each index enters and leaves the deque once)
 *  Space   : O(k)
 *  Example : {1,3,-1,-3,5,3,6,7}, k=3  ->  {3,3,5,5,6,7}
 *  Run     : javac S4_SlidingWindowMaximum.java  then  java S4_SlidingWindowMaximum
 * ============================================================================
 */
public class S4_SlidingWindowMaximum {

    static int[] slidingWindowMax(int[] arr, int k) {
        int n = arr.length;
        if (n == 0 || k <= 0 || k > n) return new int[0];      // input validation

        int[] result = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>();                // stores indices

        for (int i = 0; i < n; i++) {
            // 1) drop index that slid out of the window
            if (!dq.isEmpty() && dq.peekFirst() <= i - k) dq.pollFirst();

            // 2) drop smaller elements from the back
            while (!dq.isEmpty() && arr[dq.peekLast()] <= arr[i]) dq.pollLast();

            // 3) add current index
            dq.offerLast(i);

            // 4) window is complete -> record maximum (front of deque)
            if (i >= k - 1) result[i - k + 1] = arr[dq.peekFirst()];
        }
        return result;
    }

    static void test(int[] arr, int k) {
        System.out.println("Array: " + Arrays.toString(arr) + "   k = " + k);
        System.out.println("Max  : " + Arrays.toString(slidingWindowMax(arr, k)) + "\n");
    }

    public static void main(String[] args) {
        test(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3);   // sample
        test(new int[]{9, 8, 7, 6, 5}, 2);              // decreasing
        test(new int[]{1, 2, 3, 4, 5}, 3);              // increasing
        test(new int[]{4, 4, 4, 4}, 2);                 // duplicates
        test(new int[]{5, 1, 9}, 1);                    // k = 1 -> array itself
        test(new int[]{5, 1, 9}, 3);                    // k = n -> one window
        test(new int[]{5, 1, 9}, 5);                    // k > n -> invalid
        test(new int[]{}, 2);                           // empty array
    }
}
