package search;

public class BinarySearchSpan {

    // Pre: args.length > 0 && forall i = 0..args.length - 1: args[i] == (int value) &&
    // forall i = 2..args.length - 1: args[i] <= args[i - 1]

    // Post: R = (i, len) : (i + 2 == args.length || args[i + 1] <= args[0]) && (forall j : (i + 1)..(i + len) : args[j] == x)
    public static void main(String... args) {
        int x = Integer.parseInt(args[0]);
        int[] array = new int[args.length - 1];

        for (int i = 1; i < args.length; i++) {
            array[i - 1] = Integer.parseInt(args[i]);
        }

        int leftEntry = binarySearchRecursive(array, x, false);
        int length = binarySearchIterative(array, x, true) - leftEntry;

        System.out.println(leftEntry + " " + length);
    }


    // Pre: forall i, j = 0..array.length - 1 : i < j --> array[i] >= array[j]
    // Post: R = r: (rightEntry == false && 0 <= r < array.length && array[r] <= x && (r == 0 || array[r - 1] > x)) ||
    // (rightEntry == True && 0 <= r < array.length && array[r] < x && (r == 0 || array[r - 1] >= x)) ||
    // (r == array.length && array[array.length - 1] > r)
    public static int binarySearchIterative(int[] array, int x, boolean rightEntry) {
        int l = -1;
        int r = array.length;
        // Inv: (l == -1 || (rightEntry == false && array[l] > x) || (rightEntry == true && array[l] >= x)) &&
        // (r == array.length || (rightEntry == false && array[r] <= x) || (rightEntry == true && array[r] < x)) &&
        // ((r' - l' + 1) * 2 <= (r - l + 1)) && l + 1 < r
        while (l + 1 != r) {
            int m = (l + r) / 2;
            // l < m < r && (r - m + 1) * 2 <= (r - l + 1) && (m - l + 1) * 2 <= (r - l + 1)
            if (array[m] > x || (rightEntry && array[m] == x)) {
                // array[m] > x || (rightEntry && array[m] == x)
                l = m;
                // l' = m, r' = r
                // array[l'] > x || (rightEntry && array[l'] == x) ||
                // ((m - l' + 1) * 2 <= (r - l + 1) --> (r' - l' + 1) * 2 <= (r - l + 1))
            } else {
                // array[m] <= x || (rightEntry && array[m] < x)
                r = m;
                // l' = l, r' = m
                // array[r'] <= x || (rightEntry && array[r'] < x) || ((r' - m + 1) * 2 <= (r - l + 1) --> (r' - l' + 1) * 2 <= (r - l + 1))
            }
        }
        // l + 1 == r && (rightEntry == false && 0 <= r < array.length && array[r] <= x && (r == 0 || array[r - 1] > x)) ||
        // (rightEntry == True && 0 <= r < array.length && array[r] < x && (r == 0 || array[r - 1] >= x)) ||
        // (r == array.length && array[array.length - 1] > r)
        return r;
    }

    // Pre: forall i, j = 0..array.length - 1 : i < j --> array[i] >= array[j]
    // Post: R = r: (rightEntry == false && 0 <= r < array.length && array[r] <= x && (r == 0 || array[r - 1] > x)) ||
    // (rightEntry == True && 0 <= r < array.length && array[r] < x && (r == 0 || array[r - 1] >= x)) ||
    // (r == array.length && array[array.length - 1] > r)
    // Inv: (l == -1 || (rightEntry == false && array[l] > x) || (rightEntry == true && array[l] >= x)) &&
    // (r == array.length || (rightEntry == false && array[r] <= x) || (rightEntry == true && array[r] < x)) &&
    // ((r' - l' + 1) * 2 <= (r - l + 1)) && l + 1 < r
    public static int binarySearchRecursive(int[] array, int x, int l, int r, boolean rightEntry) {
        if (l + 1 == r) {
            // l + 1 == r && rightEntry == false && 0 <= r < array.length && array[r] <= x && (r == 0 || array[r - 1] > x)) ||
            // (rightEntry == True && 0 <= r < array.length && array[r] < x && (r == 0 || array[r - 1] >= x)) ||
            // (r == array.length && array[array.length - 1] > r)
            return r;
        }

        int m = (l + r) / 2;
        // l < m < r && (r - m + 1) * 2 <= (r - l + 1) && (m - l + 1) * 2 <= (r - l + 1)

        if (array[m] > x || (rightEntry && array[m] == x)) {
            // l' = m, r' = r
            // array[l'] > x || (rightEntry && array[l'] == x) ||
            // ((m - l' + 1) * 2 <= (r - l + 1) --> (r' - l' + 1) * 2 <= (r - l + 1))
            return binarySearchRecursive(array, x, m, r, rightEntry);
        }
        // l' = l, r' = r
        // array[r'] <= x || (rightEntry && array[r'] < x) || ((r' - m + 1) * 2 <= (r - l + 1) --> (r' - l' + 1) * 2 <= (r - l + 1))
        return binarySearchRecursive(array, x, l, m, rightEntry);
    }

    // Pre: forall i, j = 0..array.length - 1 : i < j --> array[i] >= array[j]
    // Post: R = r: (rightEntry == false && 0 <= r < array.length && array[r] <= x && (r == 0 || array[r - 1] > x)) ||
    // (rightEntry == True && 0 <= r < array.length && array[r] < x && (r == 0 || array[r - 1] >= x)) ||
    // (r == array.length && array[array.length - 1] > r)
    public static int binarySearchRecursive(int[] array, int x, boolean rightEntry) {
        return binarySearchRecursive(array, x, -1, array.length, rightEntry);
    }
}
