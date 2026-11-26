import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
 * ============================================================================
 *  H3 : LONGEST CONSECUTIVE SEQUENCE
 *  Problem : In an UNSORTED array find the length of the longest run of
 *            consecutive integers (order in the array does not matter).
 *  Sorting : sort then scan                                     -> O(n log n)
 *  Optimal : Put all numbers in a HashSet (O(1) lookup).
 *            A number x is the START of a sequence only if (x - 1) is NOT in
 *            the set. For every start, keep counting x+1, x+2, ... while they
 *            exist. Because we only count from starts, every element is
 *            visited a constant number of times.
 *  Time    : O(n)          Space : O(n)
 *  Dry run : {100, 4, 200, 1, 3, 2}
 *            start 100 (99 absent): 100 -> length 1
 *            start 200 (199 absent): length 1
 *            start 1   (0 absent) : 1,2,3,4 -> length 4   <- answer
 *            4,3,2 are skipped because 3,2,1 exist (not starts)
 *  Run     : javac H3_LongestConsecutiveSequence.java  then  java H3_LongestConsecutiveSequence
 * ============================================================================
 */
public class H3_LongestConsecutiveSequence {

    static int longestConsecutive(int[] arr) {
        Set<Integer> set = new HashSet<>();
        for (int x : arr) set.add(x);                       // also removes duplicates

        int best = 0;
        for (int x : set) {
            if (!set.contains(x - 1)) {                     // x is the start of a sequence
                int current = x, length = 1;
                while (set.contains(current + 1)) {         // extend the sequence
                    current++;
                    length++;
                }
                best = Math.max(best, length);
            }
        }
        return best;
    }

    static void test(int... arr) {
        System.out.println("Array  : " + Arrays.toString(arr));
        System.out.println("Longest: " + longestConsecutive(arr) + "\n");
    }

    public static void main(String[] args) {
        test(100, 4, 200, 1, 3, 2);            // 4  (1,2,3,4)
        test(0, 3, 7, 2, 5, 8, 4, 6, 0, 1);    // 9  (0..8), has a duplicate
        test(10, 5, 12);                       // 1
        test(-2, -1, 0, 1);                    // 4  (negatives)
        test(9, 9, 9);                         // 1  (all duplicates)
        test(42);                              // 1
        test();                                // 0
    }
}
