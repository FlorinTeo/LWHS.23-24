package SimpleList.main;

import java.util.ArrayList;
import java.util.List;

import graph.Graph;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Program {
    
    // Create two static lists: a standard ArrayList<Integer> and a custom SimpleArrayList.
    private static List<Integer> aList = new ArrayList<Integer>();
    private static SimpleList sList = new SimpleList();
    
    /**
     * Prepends the first {count} numbers starting from 1, into the given {list}.
     * In the end, the resulting list will contain the numbers in decreasing order. 
     * @param list - list to be used for prepending.
     * @param count - count of numbers to be prepended.
     * @return the time it took for the entire operation, in nano seconds.
     */
    public static double prependToList(List list, int count) {
        long start = System.nanoTime();
        for (int n = 1; n <= count; n++) {
            list.add(0, n);
        }
        return System.nanoTime() - start;
    }
    
    /**
     * Appends the first {count} numbers starting from 1, into the given {list}.
     * In the end, the resulting list will contain the numbers in ascending order. 
     * @param list - list to be used for appending.
     * @param count - count of numbers to be appended.
     * @return the time it took for the entire operation, in nano seconds.
     */
    public static double appendToList(List list, int count) {
        long start = System.nanoTime();
        for (int n = 1; n <= count; n++) {
            list.add(n);
        }
        return System.nanoTime() - start;
    }

    /***
     * Run several performance experiments:
     * (1) populate each list with an increasing count of numbers, by prepending only.
     * (2) populate each list with an increasing count of numbers, by appending only.
     * Plot the execution times on two graphs:
     * [Prepend Experiments] & [Append Experiments]
     *     X-axis: the count of numbers
     *     Y-axis: time it took to add the numbers in the sList and aList respectively.
     */
    public static void main(String[] args) {
        System.out.println("Hello to SimpleList Performance Bench!");
        
        // Create arrays to hold the data for the experiments
        // X-axis: the count of numbers
        // Y-axis for the sList: time it took for the count numbers to be added
        // Y-axis for the aList: time it took for the count numbers to be added
        double[] countX = new double[100];
        double[] sListY = new double[100];
        double[] aListY = new double[100];
        
        // Run the prepending experiment
        for (int i = 0; i < countX.length; i++) {
            // prepend an increasing count of numbers: 0, 200, 400, 600, ...
            countX[i] = i * 200;
            // record the time it took to add these numbers into the sList and aList
            sListY[i] = prependToList(sList, (int)countX[i]);
            aListY[i] = prependToList(aList, (int)countX[i]);
            // this round is complete, clear the lists.
            sList.clear();
            aList.clear();
        }
        
        // Plot the result of the prepend experiment
        Graph graphPrepend = new Graph("Prepend Experiment");
        graphPrepend.add("sList", countX, sListY);
        graphPrepend.add("aList", countX, aListY);
        graphPrepend.plot();
        System.out.printf("sList max prepend = %f, aList max prepend = %f\n", sListY[99], aListY[99]);
        
        // Run the appending experiment
        for (int i = 0; i < countX.length; i++) {
            // prepend an increasing count of numbers: 0, 200, 400, 600, ... 
            countX[i] = i * 200;
            // record time it took to add these numbers into the sList and aList
            sListY[i] = appendToList(sList, (int)countX[i]);
            aListY[i] = appendToList(aList, (int)countX[i]);
            // this round is complete, clear the lists.
            sList.clear();
            aList.clear();
        }
        
        // Plot the result of the append experiment
        Graph graphAppend = new Graph("Append Experiment");
        graphAppend.add("sList", countX, sListY);
        graphAppend.add("aList", countX, aListY);
        graphAppend.plot();
        System.out.printf("sList max append = %f, aList max append = %f\n", sListY[99], aListY[99]);
        
        System.out.println("Goodbye!");
    }
}
