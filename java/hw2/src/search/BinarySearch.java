package search;

public class BinarySearch {

    // Pre: args.length > 0 && forall i = 0..args.length - 1: args[i] == (int value) &&
    // forall i = 2..args.length - 1: args[i] <= args[i - 1]

    // Post: R = i : i + 1 == args.length || args[i] <= args[0]
    public static void main(String... args) {
        int x = Integer.parseInt(args[0]);
        int[] array = new int[args.length - 1];

        for (int i = 1; i < args.length; i++) {
            array[i - 1] = Integer.parseInt(args[i]);
        }

        System.out.println(binarySearchRecursive(array, x));
    }


    // Pre: forall i, j = 0..array.length - 1 : i < j --> array[i] >= array[j]
    // Post: R = r: (0 <= r < array.length && array[r] <= x && (r == 0 || array[r - 1] > x)) ||
    // (r == array.length && array[array.length - 1] > r)
    public static int binarySearchIterative(int[] array, int x) {
        int l = -1;
        int r = array.length;
        // Inv: (l == -1 || array[l] > x) && (r == array.length || array[r] <= x)
        // && ((r' - l' + 1) * 2 <= (r - l + 1)) && l + 1 < r
        while (l + 1 != r) {
            int m = (l + r) / 2;
            // l < m < r && (r - m + 1) * 2 <= (r - l + 1) && (m - l + 1) * 2 <= (r - l + 1)
            if (array[m] > x) {
                // array[m] > x
                l = m;
                // l' = m, r' = r
                // array[l] > x && ((m - l' + 1) * 2 <= (r - l + 1) --> (r' - l' + 1) * 2 <= (r - l + 1))
            } else {
                // array[m] <= x
                r = m;
                // l' = l, r' = m
                // array[r] <= x && ((r' - m + 1) * 2 <= (r - l + 1) --> (r' - l' + 1) * 2 <= (r - l + 1))
            }
        }
        // l + 1 == r && (0 <= r < array.length && array[r] <= x && (r == 0 || array[r - 1] > x)) ||
        // (r == array.length && array[array.length - 1] > r)
        return r;
    }

    // Pre: forall i, j = 0..array.length - 1 : i < j --> array[i] >= array[j]
    // Post: R = r: (0 <= r < array.length && array[r] <= x && (r == 0 || array[r - 1] > x)) ||
    // (r == array.length && array[array.length - 1] > r)
    // Inv: (l == -1 || array[l] > x) && (r == array.length || array[r] <= x)
    // && ((r' - l' + 1) * 2 <= (r - l + 1)) && l + 1 < r
    public static int binarySearchRecursive(int[] array, int x, int l, int r) {
        if (l + 1 == r) {
            // l + 1 == r && (0 <= r < array.length && array[r] <= x && (r == 0 || array[r - 1] > x)) ||
            // (r == array.length && array[array.length - 1] > r)
            return r;
        }

        int m = (l + r) / 2;
        // l < m < r && (r - m + 1) * 2 <= (r - l + 1) && (m - l + 1) * 2 <= (r - l + 1)

        if (array[m] > x) {
            // l' = m, r' = r
            // array[m] > x && l' = m && array[l'] > x && (m - l' + 1) * 2 <= (r - l + 1)
            return binarySearchRecursive(array, x, m, r);
        }
        // l' = l, r' = r
        // array[m] <= x && r' = m && array[r] <= x && (r' - m + 1) * 2 <= (r - l + 1)
        return binarySearchRecursive(array, x, l, m);
    }

    // Pre: forall i, j = 0..array.length - 1 : i < j --> array[i] >= array[j]
    // Post: R = r: (0 <= r < array.length && array[r] <= x && (r == 0 || array[r - 1] > x)) ||
    // (r == array.length && array[array.length - 1] > r)
    public static int binarySearchRecursive(int[] array, int x) {
        return binarySearchRecursive(array, x, -1, array.length);
    }
}
