import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * ============================================================================
 *  H1 : FIND ALL SUBARRAYS WITH ZERO SUM
 *  Problem : Print every subarray whose elements add up to 0.
 *  Idea    : Let prefix[i] = sum of arr[0..i].
 *            If prefix[j] == prefix[i] for some j < i, then the elements
 *            from j+1 to i sum to 0.
 *            The HashMap stores:  prefix sum -> list of indices where it occurred
 *            (a list is needed because the same sum can repeat many times and
 *            EVERY earlier occurrence gives a different zero-sum subarray).
 *            We seed the map with  0 -> [-1]  so subarrays starting at index 0
 *            are also detected.
 *  Time    : O(n + k) where k = number of zero-sum subarrays found
 *            (k can be up to O(n^2), e.g. an array of all zeros)
 *  Space   : O(n)
 *  Dry run : arr = {4, 2, -3, 1, 6}
 *            i=0 sum=4  new          i=1 sum=6  new          i=2 sum=3  new
 *            i=3 sum=4  seen at 0 -> subarray (1..3) = {2,-3,1}  sum 0
 *            i=4 sum=10 new
 *  Run     : javac H1_ZeroSumSubarrays.java  then  java H1_ZeroSumSubarrays
 * ============================================================================
 */
public class H1_ZeroSumSubarrays {

    static int findZeroSumSubarrays(int[] arr) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        map.put(0, new ArrayList<>(List.of(-1)));      // empty prefix before index 0
        int sum = 0, count = 0;

        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];                             // running (cumulative) sum

            if (map.containsKey(sum)) {                // this sum was seen before
                for (int start : map.get(sum)) {       // each earlier index gives a subarray
                    System.out.println("  Subarray from index " + (start + 1) + " to " + i + " : "
                            + Arrays.toString(Arrays.copyOfRange(arr, start + 1, i + 1)));
                    count++;
                }
            }
            map.computeIfAbsent(sum, k -> new ArrayList<>()).add(i);   // remember this index
        }
        return count;
    }

    static void test(int... arr) {
        System.out.println("Array: " + Arrays.toString(arr));
        int c = findZeroSumSubarrays(arr);
        System.out.println(c == 0 ? "  No zero-sum subarray" : "  Total = " + c);
        System.out.println();
    }

    public static void main(String[] args) {
        test(4, 2, -3, 1, 6);
        test(6, 3, -1, -3, 4, -2, 2, 4, 6, -1);
        test(0, 0, 0);                  // every subarray of zeros counts
        test(1, 2, 3);                  // none
        test(5, -5);
        test(0);                        // single zero
        test();                         // empty
    }
}
