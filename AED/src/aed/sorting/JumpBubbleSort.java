package aed.sorting;

import java.util.*;
import java.util.stream.*;

public class JumpBubbleSort extends Sort {
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

        //attention these might be O(log(N)N) , but doubling ratio is not really appropriate for such order of growth ,
        //and the difference between O(N) and O(log(N)N) is not that great so, we will be using O(N) to represent both.

        //around O(N)
        var orderedTime = DoublingRatio.doublingRatio(nTRIALS,LENGTH,JumpBubbleSort::orderedArray);
        System.out.println("An ordered array is O(" + bigO(orderedTime.ratio)  +") worst case , real ratio = " + orderedTime.ratio +
                " and it took " + orderedTime.time + " seconds");

        //around 0(N)
        var invertedTime = DoublingRatio.doublingRatio(nTRIALS,LENGTH,JumpBubbleSort::invertedArray);
        System.out.println("An inverted array is O(" + bigO(invertedTime.ratio)  +") worst case , real ratio = " + invertedTime.ratio +
                " and it took " + invertedTime.time + " seconds");

        //around O(N)
        var partialTime = DoublingRatio.doublingRatio(nTRIALS,LENGTH,JumpBubbleSort::partialArray);
        System.out.println("An partially ordered array is O(" + bigO(partialTime.ratio)  +") worst case , real ratio = " + partialTime.ratio +
                " and it took " + partialTime.time + " seconds");

        //around O(N)
        var unorderedTime = DoublingRatio.doublingRatio(nTRIALS,LENGTH,JumpBubbleSort::unorderedArray);
        System.out.println("An unordered array is O(" + bigO(unorderedTime.ratio)  +") worst case , real ratio = " + unorderedTime.ratio +
                " and it took " + unorderedTime.time + " seconds");

        //While the order of growth is apparently equal to all the arrays , the coefficient not so much ,
        // the ordered array is by large the faster to sort , as there are no swaps , and it's only a matter of time until
        // d =1 is reached , the inverted array is the worst case for bubbleSort , but for jumpbubblesort it seems he is
        //able to escape quadratic running time and is somehow better than partially ordered and unordered arrays ,
        // between those two partially ordered arrays has slightly better running time.

    }


    public static <T extends Comparable<T>> void sort(T[] a) {
        boolean isSorted = false;
        int d = a.length -1;
        while(!isSorted){
            int N = a.length;
            if(d <= 1) {
                isSorted = true;
                d = 1;
            }
            for(int i = d;i < N; i++)
                if(less(a[i], a[i - d])) {
                    exchange(a, i - d, i);
                    isSorted = false;
                }
            d = (int)(d * 0.77);
        }
    }

    private static<T extends Comparable<T>> void shuffle(T[] a){
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

    private static void shuffle(Integer[] a){
        Random r = new Random();

        for(int i = a.length -1 ; i > 0; i--){
            int j = r.nextInt(i+1);
            exchange(a,i,j);
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
