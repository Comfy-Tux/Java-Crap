package aed.sorting;

import java.util.*;
import java.util.stream.*;

public class QuickSort3Way extends Sort {
    final static int LENGTH = 100000;
    final static int nTRIALS = 50;

    public static void main(String[] args)
    {
        var a = new Integer[31];
        for(int i = 0; i < 31 ; i++){
            a[i] = i;
        }

        shuffle(a);
        System.out.println("Unordered array");
        System.out.println(Arrays.toString(a));
        secureSort(a);
        System.out.println("Ordered array");
        System.out.println(Arrays.toString(a));
        System.out.println();

        var b = invertedArray(31);
        System.out.println("Inverted array");
        System.out.println(Arrays.toString(b));
        secureSort(b);
        System.out.println("Ordered array");
        System.out.println(Arrays.toString(b));
        System.out.println();

        //attention these might be O(log(N)N) , but doubling ratio is not really appropriate for such order of growth ,
        //and the difference between O(N) and O(log(N)N) is not that great so, we will be using O(N) to represent both.

        //around O(N)
        var orderedTime = DoublingRatio.doublingRatio(nTRIALS,LENGTH,QuickSort3Way::orderedArray);
        System.out.println("An ordered array is O(" + bigO(orderedTime.ratio)  +") worst case , real ratio = " + orderedTime.ratio +
                " and it took " + orderedTime.time + " seconds");

        //around 0(N)
        var invertedTime = DoublingRatio.doublingRatio(nTRIALS,LENGTH,QuickSort3Way::invertedArray);
        System.out.println("An inverted array is O(" + bigO(invertedTime.ratio)  +") worst case , real ratio = " + invertedTime.ratio +
                " and it took " + invertedTime.time + " seconds");

        //around O(N)
        var partialTime = DoublingRatio.doublingRatio(nTRIALS,LENGTH,QuickSort3Way::partialArray);
        System.out.println("An partially ordered array is O(" + bigO(partialTime.ratio)  +") worst case , real ratio = " + partialTime.ratio +
                " and it took " + partialTime.time + " seconds");

        //around O(N)
        var unorderedTime = DoublingRatio.doublingRatio(nTRIALS,LENGTH,QuickSort3Way::unorderedArray);
        System.out.println("An unordered array is O(" + bigO(unorderedTime.ratio)  +") worst case , real ratio = " + unorderedTime.ratio +
                " and it took " + unorderedTime.time + " seconds");

        //around O(N)
        var lowRepeatedTime = DoublingRatio.doublingRatio(nTRIALS,LENGTH,QuickSort3Way::lowRepeatedArray);
        System.out.println("An unordered array with low repeated elements is O(" + bigO(lowRepeatedTime.ratio)  +") worst case , real ratio = " + lowRepeatedTime.ratio +
                " and it took " + lowRepeatedTime.time + " seconds");

        //around O(N)
        var highRepeatedTime = DoublingRatio.doublingRatio(nTRIALS,LENGTH,QuickSort3Way::highRepeatedArray);
        System.out.println("An unordered array with lots of repeated elements  is O(" + bigO(highRepeatedTime.ratio)  +") worst case , real ratio = " + highRepeatedTime.ratio +
                " and it took " + highRepeatedTime.time + " seconds");


        // the order of growth is almost equal to all the array , but the coefficients vary on some
        //the ordered array is the worst case , taking the longest to order even though I shuffle the input
        // which is kinda odd . The other four types , inverted , partially
        //ordered , unordered and unordered with low repeated elements have little difference on time and order of growth ,
        //the same can't be said to the array with lots of repeated elements , the ratio is almost perfectly linear ,
        //and the time is far lower than his cousins , being for this kind of array that this algorithm was devised.
    }

    public static <T extends Comparable<T>> void sort(T[] a)
    {
        shuffle(a);
        sort(a,0,a.length -1);
    }

    private static <T extends Comparable<T>> void sort(T[]a ,int lo,int hi){
        if(hi - lo < 15) {
            InsertionSort.sort(a,lo,hi);
            return;
        }

        int mid = (hi -lo)/2 + lo;

      //order lo , mid , hi on the array
      if(less(a[hi],a[lo]))
         exchange(a,hi,lo);
      if(less(a[mid],a[lo]))
         exchange(a,mid,lo);
      if(less(a[hi],a[mid]))
         exchange(a,mid,hi);

      exchange(a,mid,lo);

        int lt = lo , i = lo+1 , gt = hi;
        T key = a[lo];
        while(i <= gt) {
            int cmp = a[i].compareTo(key);
            if(cmp < 0) exchange(a, lt++, i++);
            else if(cmp > 0) exchange(a, i, gt--);
            else i++;
        }
        sort(a,lo,lt-1);
        sort(a,gt+1,hi);
    }

    public static<T extends Comparable<T>> void shuffle(T[] a){
        Random r = new Random();

        for(int i = a.length -1 ; i > 0; i--){
            int j = r.nextInt(i+1);
            exchange(a,i,j);
        }
    }

    private static <T extends Comparable<T>> void secureSort(T[] a){
        sort(a);
        if(!isSorted(a))
            throw new IllegalStateException();
    }

    private static class InsertionSort extends Sort{
        public static <T extends Comparable<T>>void sort(T[] a , int start , int end) {
            int N = end;
            for(int i = start + 1; i <= N; i++) {
                for(int j = i; j > start && less(a[j], a[j - 1]); j--)
                    exchange(a, j, j - 1);
            }
        }
    }



    /////////////////////////////////////// DOUBLING RATIO /////////////////////////////////////////////////

    private static class Stopwatch{
        private final long start;

        public Stopwatch(){ start = System.currentTimeMillis();}

        public double elapsedTime(){
            long now = System.currentTimeMillis();
            return (now - start ) / 1000.0;
        }
    }

    private static class DoublingRatio{
        public static double timeTrial(int N,ArrayGenerator generator){
            Integer[] a = generator.generate(N);
            var stopwatch = new Stopwatch();
            sort(a);
            return stopwatch.elapsedTime();
        }

        // N is number of times , M is length of the array
        public static TimeAndRatio doublingRatio(int N,int M,ArrayGenerator generator){
            double currentTime = 0.0;
            double halfTime = 0.0;
            for(int i = 0; i < N ; i++){
                halfTime += timeTrial((M/2) , generator);
                currentTime += timeTrial(M , generator);
            }
            return new TimeAndRatio(halfTime + currentTime,currentTime/halfTime);
        }
    }

    //the array should be empty , is just use it to discover the type I want to use , I might use doubles or Integer and so forth
    private static Integer[] orderedArray(int N){
        var a = new Integer[N];
        for(int i = 0; i < N; i++)
            a[i] = i;
        return a;
    }

    private static Integer[]  invertedArray(int N){
        var a = new Integer[N];
        for(int i = 0; i < N; i++)
            a[i] = N - i -1;
        return a;
    }

    private static Integer[] partialArray(int N){
        var a = orderedArray(N);
        int length = N/8;
        Random r = new Random();

        for(int i = 0; i < length;i++){
            exchange(a,r.nextInt(N), r.nextInt(N));
        }
        return a;
    }

    private static Integer[] unorderedArray(int N){
        var a = orderedArray(N);
        shuffle(a);
        return a;
    }

    private static Integer[] lowRepeatedArray(int N){
        var a = new Integer[N];
        var r = new Random();
        int allowedElements = 10000;
        for(int i = 0; i < N; i++){
            a[i] = r.nextInt(allowedElements);
        }
        return a;
    }

    private static Integer[] highRepeatedArray(int N){
        var a = new Integer[N];
        var r = new Random();
        int allowedElements = 10;
        for(int i = 0; i < N; i++){
            a[i] = r.nextInt(allowedElements);
        }
        return a;
    }

    private static void shuffle(Integer[] a){
        Random r = new Random();

        for(int i = a.length -1 ; i > 0; i--){
            int j = r.nextInt(i+1);
            exchange(a,i,j);
        }
    }

    private static String bigO(double ratio){
        int n = (int) Math.round(ratio);
        return switch(n) {
            case 0 -> "1";
            case 1 -> "N";
            case 2 -> "N^2";
            case 3 -> "N^3";
            default -> "N^infinite";
        };
    }

    private static double bigOExponent(double time){
        return  Math.log(time)/Math.log(2);
    }


    public interface ArrayGenerator{
        public  Integer[] generate(int N);
    }

    private static class TimeAndRatio{
        double time;
        double ratio;
        TimeAndRatio(double time,double ratio){this.time = time;this.ratio = bigOExponent(ratio);}
    }

}
