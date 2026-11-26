import java.util.ArrayDeque;
import java.util.Queue;

/*
 * ============================================================================
 *  S5 : CIRCULAR TOUR (GAS STATION) PROBLEM
 *  Problem : N petrol pumps on a circle. petrol[i] = petrol available at pump
 *            i, distance[i] = distance from pump i to pump i+1 (1 unit of
 *            petrol = 1 unit of distance). Find the FIRST pump from which a
 *            truck (empty tank, unlimited capacity) can complete the circle.
 *  Idea    : QUEUE simulation (as per the hint).
 *            The queue holds the pumps of the tour we are CURRENTLY trying.
 *            surplus = total (petrol - distance) of the pumps in the queue.
 *              - add the next pump at the rear, add its (petrol - distance)
 *              - while surplus < 0 -> this start cannot work: remove pumps
 *                from the FRONT (subtracting their contribution)
 *              - when the queue holds all N pumps (and surplus >= 0) the
 *                front pump is the answer
 *            We go around at most 2n steps.
 *  Time    : O(n)          Space : O(n)
 *  Example : petrol   = {4, 6, 7, 4},  distance = {6, 5, 3, 5}  -> start = 1
 *  Run     : javac S5_CircularTour.java   then   java S5_CircularTour
 * ============================================================================
 */
public class S5_CircularTour {

    // returns the index of the starting pump, or -1 if no tour is possible
    static int findStart(int[] petrol, int[] distance) {
        int n = petrol.length;
        if (n == 0 || distance.length != n) return -1;          // validation

        Queue<Integer> tour = new ArrayDeque<>();               // pumps in current attempt
        int surplus = 0;

        for (int step = 0; step < 2 * n; step++) {
            int pump = step % n;                                // circular index
            tour.offer(pump);                                   // add pump at the rear
            surplus += petrol[pump] - distance[pump];

            // current start is hopeless -> drop pumps from the front
            while (surplus < 0 && !tour.isEmpty()) {
                int removed = tour.poll();
                surplus -= petrol[removed] - distance[removed];
            }

            if (tour.size() == n) return tour.peek();           // full circle with surplus >= 0
        }
        return -1;
    }

    static void test(int[] petrol, int[] distance) {
        System.out.println("Petrol   : " + java.util.Arrays.toString(petrol));
        System.out.println("Distance : " + java.util.Arrays.toString(distance));
        int start = findStart(petrol, distance);
        System.out.println(start == -1 ? "Result   : No tour possible\n"
                                      : "Result   : Start at pump " + start + "\n");
    }

    public static void main(String[] args) {
        test(new int[]{4, 6, 7, 4}, new int[]{6, 5, 3, 5});     // start = 1
        test(new int[]{6, 3, 7}, new int[]{4, 6, 3});           // start = 2
        test(new int[]{1, 2, 3}, new int[]{4, 5, 6});           // impossible
        test(new int[]{5}, new int[]{5});                       // single pump, exact
        test(new int[]{2}, new int[]{3});                       // single pump, short
        test(new int[]{3, 3, 3}, new int[]{3, 3, 3});           // exactly enough from pump 0
    }
}
