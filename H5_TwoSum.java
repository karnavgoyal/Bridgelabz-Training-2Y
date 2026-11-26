import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * ============================================================================
 *  H5 : TWO SUM PROBLEM
 *  Problem : Return the INDICES of two numbers that add up to the target.
 *  Brute   : two nested loops                                   -> O(n^2)
 *  Optimal : HashMap  value -> index.
 *            For each element arr[i], compute  need = target - arr[i].
 *            If 'need' is already in the map, its stored index and i are the
 *            answer. Otherwise store arr[i] -> i and continue.
 *            (Lookup happens BEFORE insertion so an element is never used twice.)
 *  Time    : O(n)          Space : O(n)
 *  Dry run : arr = {2, 7, 11, 15}, target = 9
 *            i=0: need 7, map empty        -> store {2:0}
 *            i=1: need 2, found at index 0 -> answer [0, 1]
 *  Run     : javac H5_TwoSum.java    then    java H5_TwoSum
 * ============================================================================
 */
public class H5_TwoSum {

    static int[] twoSum(int[] arr, int target) {
        Map<Integer, Integer> indexOf = new HashMap<>();    // value -> its index
        for (int i = 0; i < arr.length; i++) {
            int need = target - arr[i];
            if (indexOf.containsKey(need)) return new int[]{indexOf.get(need), i};
            indexOf.put(arr[i], i);
        }
        return new int[0];                                  // no valid pair
    }

    static void test(int[] arr, int target) {
        System.out.println("Array: " + Arrays.toString(arr) + "   target = " + target);
        int[] r = twoSum(arr, target);
        if (r.length == 0) System.out.println("  No two numbers add up to the target\n");
        else System.out.println("  Indices " + Arrays.toString(r) + "  ->  " + arr[r[0]] + " + " + arr[r[1]] + " = " + target + "\n");
    }

    public static void main(String[] args) {
        test(new int[]{2, 7, 11, 15}, 9);          // [0, 1]
        test(new int[]{3, 2, 4}, 6);               // [1, 2]  (3 must not be used twice)
        test(new int[]{3, 3}, 6);                  // [0, 1]  (duplicate values)
        test(new int[]{-1, -2, -3, -4, -5}, -8);   // negatives -> [2, 4]
        test(new int[]{1, 2, 3}, 10);              // no answer
        test(new int[]{5}, 10);                    // single element
        test(new int[]{}, 0);                      // empty
    }
}
