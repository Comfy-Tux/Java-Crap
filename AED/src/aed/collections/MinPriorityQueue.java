package aed.collections;

import java.util.*;

public class MinPriorityQueue<T extends Comparable<T>> {
    UnrolledLinkedList<T> list;

    public static void main(String[] args) {
        final int LENGTH = 50000;
        final int nTRIALS = 50;
        int M = 41;

        var array = new Integer[M];

        for(int i = 0; i < M; i++)
            array[i] = i;

        shuffle(array);
        System.out.println("unordered");
        System.out.println(Arrays.toString(array));

        var pq = new MinPriorityQueue<Integer>();

        for(Integer i : array)
            pq.insert(i);

        System.out.println("ordered");
        System.out.println(Arrays.toString(pq.getElements()));
        var pqCopy = pq.shallowCopy();

        for(int i = 0; i < M; i++) {
            pq.removeMin();
            pq.removeMin();
        }

        System.out.println("original");
        System.out.println(Arrays.toString(pq.getElements()));
        System.out.println("copy");
        System.out.println(Arrays.toString(pqCopy.getElements()));

        for(Integer i : array)
            pq.insert(i);
        System.out.println("original after inserting again");
        System.out.println(Arrays.toString(pq.getElements()));

        /////

        //0.44
        //0.022

        //0.4
        //0.004

        //O(N)
        var insertTime = DoublingRatio.doublingRatioInsert(nTRIALS, LENGTH, 2048);
        System.out.println("An InsertTime is O(" + bigO(insertTime.ratio) + ") worst case , real ratio = " + insertTime.ratio +
                " and it took " + insertTime.time + " seconds");

        //O(1)
        var removeMinTime = DoublingRatio.doublingRatioRemoveMin(nTRIALS, LENGTH, 2048);
        System.out.println("An removeMinTime is O(" + bigO(removeMinTime.ratio) + ") worst case , real ratio = " + removeMinTime.ratio +
                " and it took " + removeMinTime.time + " seconds");


        //optimal blocksize for 10000 around 2928 , between 2048 and 2928 there is not much difference
        //for 50000 , 4096 is advised , as there seems to be a better balance between remove and insert .
        // 8192 is faster but the wasted space , makes it not worth it for an input of 50k elements.
        // For mooshak I will use 2048 as it wastes less space and the speed difference is negligible .

        //tests to seek an optimal blockSize
/*
        double min = 100.0;
        TimeAndRatio minInsertTime = null;
        TimeAndRatio minRemoveMinTime = null;
        int indexMin = 0;
        for(int i = 2048; i < 4096; i += 10) {
            System.out.println();
            System.out.println("BLOCKSIZE= " + i);

            //O(N)
            var insertTime = DoublingRatio.doublingRatioInsert(nTRIALS, LENGTH,i);
            System.out.println("An InsertTime is O(" + bigO(insertTime.ratio) + ") worst case , real ratio = " + insertTime.ratio +
                    " and it took " + insertTime.time + " seconds");

            //O(1)
            var removeMinTime = DoublingRatio.doublingRatioRemoveMin(nTRIALS, LENGTH,i);
            System.out.println("An removeMinTime is O(" + bigO(removeMinTime.ratio) + ") worst case , real ratio = " + removeMinTime.ratio +
                    " and it took " + removeMinTime.time + " seconds");

            double totalTime = insertTime.time + removeMinTime.time;
            if(less(totalTime,min)) {
                min = totalTime;
                minInsertTime = insertTime;
                minRemoveMinTime = removeMinTime;
                indexMin = i;
            }
        }
        System.out.println();
        System.out.println("MIN INDEX is " + indexMin);
        System.out.println();

        System.out.println("An InsertTime is O(" + bigO(minInsertTime.ratio) + ") worst case , real ratio = " + minInsertTime.ratio +
                " and it took " + minInsertTime.time + " seconds");

        System.out.println("An removeMinTime is O(" + bigO(minRemoveMinTime.ratio) + ") worst case , real ratio = " + minRemoveMinTime.ratio +
                " and it took " + minRemoveMinTime.time + " seconds");


        System.out.println("");
*/

/*

        for(int i = 2048; i < 50000; i += i) {
            System.out.println();
            System.out.println("BLOCKSIZE= " + i);

            //O(N)
            var insertTime2 = DoublingRatio.doublingRatioInsert(nTRIALS, LENGTH,i);
            System.out.println("An InsertTime is O(" + bigO(insertTime2.ratio) + ") worst case , real ratio = " + insertTime2.ratio +
                    " and it took " + insertTime2.time + " seconds");

            //O(1)
            var removeMinTime2 = DoublingRatio.doublingRatioRemoveMin(nTRIALS, LENGTH,i);
            System.out.println("An removeMinTime is O(" + bigO(removeMinTime2.ratio) + ") worst case , real ratio = " + removeMinTime2.ratio +
                    " and it took " + removeMinTime2.time + " seconds");
        }

*/


    }

    public MinPriorityQueue() {
        list = new UnrolledLinkedList<>(2928);
    }

    public MinPriorityQueue(int blockSize) {
        list = new UnrolledLinkedList<>(blockSize);
    }


    public void insert(T element) {
        list.addAt( reverseBinarySearch(element), element);
    }

    public T peekMin() {
        if(list.isEmpty())
            return null;
        return list.get(size() - 1);
    }

    public T removeMin() {
        return list.remove();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }

    public MinPriorityQueue<T> shallowCopy() {
        var copy = new MinPriorityQueue<T>();
        copy.list = (UnrolledLinkedList<T>) list.shallowCopy();
        return copy;
    }

    //needed for testing purposes only
    public T[] getElements() {
        @SuppressWarnings("unchecked")
        T[] a = (T[]) new Comparable[size()];
        int i = size() - 1;
        for(T element : list)
            a[i--] = element;
        return a;
    }

    /////////////////////////////////////////////// helper methods ///////////////////////////////////////////

    //this isn't really binarySearch as there can be no elements on the list , and it will return the placement where
    // the key should be , if it finds an elements then returns the index of that element.

    private int reverseBinarySearch(T key) {
        int lo = 0;
        int hi = size() - 1;

        while(hi - lo > 1) {
            int mid = lo + (hi - lo) / 2;
            var midKey = list.get(mid);
            if(less(key, midKey))
                lo = mid;
            else if(less(midKey, key))
                hi = mid;
            else
                return mid;
        }
        if(hi >= 0 && less(key, list.get(hi)))
            return hi + 1;
        if(hi > 0 && less(key, list.get(lo)))
            return lo + 1;
        return lo;
    }


    private static <T extends Comparable<T>> boolean less(T v, T w) {
        return v.compareTo(w) < 0;
    }


    private static <T extends Comparable<T>> void shuffle(T[] a) {
        Random r = new Random();

        for(int i = a.length - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            exchange(a, i, j);
        }
    }


    protected static <T extends Comparable<T>> void exchange(T[] a, int i, int j) {
        T t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    //useful method to test if an array is sorted
    public static <T extends Comparable<T>> boolean isSorted(T[] a) {
        for(int i = 1; i < a.length; i++) {
            if(less(a[i], a[i - 1])) return false;
        }
        return true;
    }

    /////////////////////////////////////// DOUBLING RATIO /////////////////////////////////////////////////

    private static class Stopwatch {
        private final long start;

        public Stopwatch() {
            start = System.currentTimeMillis();
        }

        public double elapsedTime() {
            long now = System.currentTimeMillis();
            return (now - start) / 1000.0;
        }
    }

    private static class DoublingRatio {
        public static double timeTrialInsert(MinPriorityQueue<Integer> a, int N, int M) {
            var r = new Random();
            var stopwatch = new Stopwatch();
            for(int i = 0; i < M; i++)
                a.insert(r.nextInt(N));
            return stopwatch.elapsedTime();
        }

        public static double timeTrialRemoveMin(MinPriorityQueue<Integer> a, int N, int M) {
            var r = new Random();
            var stopwatch = new Stopwatch();
            for(int i = 0; i < M; i++)
                a.removeMin();
            return stopwatch.elapsedTime();
        }

        // N is number of times , M is length of the array
        public static TimeAndRatio doublingRatioInsert(int N, int M, int blockSize) {
            int nTrials = 2000;
            double currentTime = 0.0;
            double halfTime = 0.0;

            for(int i = 0; i < N; i++) {
                var queue = new MinPriorityQueue<Integer>(blockSize);
                var halfQueue = new MinPriorityQueue<Integer>(blockSize);
                for(int j = M - 1; j >= 0; j--)
                    queue.insert(j);
                for(int j = M / 2 - 1; j >= 0; j--)
                    halfQueue.insert(j);

                halfTime += timeTrialInsert(halfQueue, M / 2, nTrials);
                currentTime += timeTrialInsert(queue, M, nTrials);
            }
            return new TimeAndRatio(halfTime + currentTime, currentTime / halfTime);
        }


        // N is number of times , M is length of the array
        public static TimeAndRatio doublingRatioRemoveMin(int N, int M, int blockSize) {
            int nTrials = 2000;
            double currentTime = 0.0;
            double halfTime = 0.0;
            for(int i = 0; i < N; i++) {
                var queue = new MinPriorityQueue<Integer>(blockSize);
                var halfQueue = new MinPriorityQueue<Integer>(blockSize);
                for(int j = 0; j < M; j++)
                    queue.insert(j);
                for(int j = 0; j < M / 2; j++)
                    halfQueue.insert(j);

                halfTime += timeTrialRemoveMin(halfQueue, M / 2, nTrials);
                currentTime += timeTrialRemoveMin(queue, M, nTrials);
            }
            return new TimeAndRatio(halfTime + currentTime, currentTime / halfTime);
        }
    }

    private static void shuffle(Integer[] a) {
        Random r = new Random();

        for(int i = a.length - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            exchange(a, i, j);
        }
    }

    private static String bigO(double ratio){
        int n = (int) Math.round(ratio);
        switch(n) {
            case 0 : return "1";
            case 1 : return "N";
            case 2 : return "N^2";
            case 3 : return "N^3";
            default : return "1";
        }
    }

    private static double bigOExponent(double time) {
        return Math.log(time) / Math.log(2);
    }

    private static class TimeAndRatio {
        double time;
        double ratio;

        TimeAndRatio(double time, double ratio) {
            this.time = time;
            this.ratio = bigOExponent(ratio);
        }
    }


    // old implementation
    //this isn't really binarySearch as there can be no elements on the list , and it will return the placement where
    // the key should be , if it finds an elements then returns the index of that element.
    private int binarySearch(T key) {
        int lo = 0;
        int hi = size() - 1;

        while(hi - lo > 1) {
            int mid = lo + (hi - lo) / 2;
            var midKey = list.get(mid);
            if(less(key, midKey))
                hi = mid;
            else if(less(midKey, key))
                lo = mid;
            else
                return mid;
        }
        if(hi >= 0 && less(list.get(hi), key))
            return hi + 1;
        if(hi > 0 && less(list.get(lo), key))
            return lo + 1;
        return lo;
    }


}