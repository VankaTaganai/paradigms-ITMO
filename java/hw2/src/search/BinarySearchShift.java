package search;

public class BinarySearchShift {

    // Pre: exist j: (forall 0 < i <= j: args[i - 1] < args[i]) &&
    // (forall j + 1 < i < args.length: args[i - 1] < args[i]) &&
    // (j + 1 == args.length || args[j] > args[j + 1])

    // Post: R = j mod args.length : (forall 0 < i <= j: args[i - 1] <= args[i]) &&
    // (forall j + 1 < i < args.length: args[i - 1] <= args[i]) &&
    // (j + 1 == args.length || args[j] > args[j + 1])
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println(0);
            return;
        }

        int[] array = new int[args.length];

        for (int i = 0; i < args.length; i++) {
            array[i] = Integer.parseInt(args[i]);
        }

        System.out.println(binarySearchRecursive(array, array[array.length - 1]));
    }


    // Pre: exist j: (forall 0 < i <= j: array[i - 1] < array[i]) &&
    // (forall j + 1 < i < array.length: array[i - 1] < array[i]) &&
    // (j + 1 == array.length || array[j] > array[j + 1]) --> (forall 0 <= i <= j: array[i] > x) &&
    // (forall j < i < array.length: array[i] <= x)

    // Post: R = r: (forall 0 <= i <= r: array[i] > x) &&
    // (forall r < i < array.length: array[i] <= x)
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
        // l + 1 == r && (forall 0 <= i <= r: array[i] > x) &&
        // (forall r < i < array.length: array[i] <= x)
        return r;
    }



    // Pre: exist j: (forall 0 < i <= j: array[i - 1] < array[i]) &&
    // (forall j + 1 < i < array.length: array[i - 1] < array[i]) &&
    // (j + 1 == array.length || array[j] > array[j + 1]) --> (forall 0 <= i <= j: array[i] > x) &&
    // (forall j < i < array.length: array[i] <= x)

    // Post: R = r: (forall 0 <= i <= j: array[i] > x) &&
    // (forall j < i < array.length: array[i] <= x)

    // Inv: (l == -1 || array[l] > x) && (r == array.length || array[r] <= x)
    // && ((r' - l' + 1) * 2 <= (r - l + 1)) && l + 1 < r
    public static int binarySearchRecursive(int[] array, int x, int l, int r) {
        if (l + 1 == r) {
            // l + 1 == r && (forall 0 <= i <= r: array[i] > x) &&
            // (forall r < i < array.length: array[i] <= x)
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

    // Pre: exist j: (forall 0 < i <= j: array[i - 1] < array[i]) &&
    // (forall j + 1 < i < array.length: array[i - 1] < array[i]) &&
    // (j + 1 == array.length || array[j] > array[j + 1]) --> (forall 0 <= i <= j: array[i] > x) &&
    // (forall j < i < array.length: array[i] <= x)

    // Post: R = r: (forall 0 <= i <= j: array[i] > x) &&
    // (forall j < i < array.length: array[i] <= x)
    public static int binarySearchRecursive(int[] array, int x) {
        return binarySearchRecursive(array, x, -1, array.length);
    }
}
