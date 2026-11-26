import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
 * ============================================================================
 *  H2 : CHECK FOR A PAIR WITH GIVEN SUM
 *  Problem : Does the array contain two (different-position) elements whose
 *            sum equals the target?
 *  Brute   : check every pair                                   -> O(n^2)
 *  Optimal : Walk through the array once. For each number x, the partner we
 *            need is  (target - x).  If that partner was ALREADY visited
 *            (stored in the hash set) -> pair found. Otherwise store x.
 *            (Checking BEFORE inserting stops an element pairing with itself,
 *             e.g. array {5}, target 10 must be false.)
 *  Time    : O(n) average       Space : O(n)
 *  Run     : javac H2_PairWithGivenSum.java  then  java H2_PairWithGivenSum
 * ============================================================================
 */
public class H2_PairWithGivenSum {

    static boolean hasPair(int[] arr, int target) {
        Set<Integer> seen = new HashSet<>();                // visited numbers
        for (int x : arr) {
            int need = target - x;                          // required partner
            if (seen.contains(need)) {
                System.out.println("  Pair found: " + need + " + " + x + " = " + target);
                return true;
            }
            seen.add(x);
        }
        return false;
    }

    static void test(int[] arr, int target) {
        System.out.println("Array: " + Arrays.toString(arr) + "   target = " + target);
        if (!hasPair(arr, target)) System.out.println("  No pair found");
        System.out.println();
    }

    public static void main(String[] args) {
        test(new int[]{8, 7, 2, 5, 3, 1}, 10);      // 8+2 (or 7+3)
        test(new int[]{1, 2, 3, 9}, 8);             // none
        test(new int[]{5}, 10);                     // element must not pair with itself
        test(new int[]{5, 5}, 10);                  // duplicates form a valid pair
        test(new int[]{-3, 4, 1, 7}, 1);            // negatives: -3 + 4
        test(new int[]{0, 0}, 0);
        test(new int[]{}, 5);                       // empty
    }
}
