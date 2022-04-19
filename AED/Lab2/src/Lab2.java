import java.util.Random;

public class Lab2 {

    public static int threeSum(int[] a)
    {
        int N = a.length;
        int count = 0;
        for(int i = 0; i < N; i++)
        {
            for(int j = i+1; j < N; j++)
            {
                for(int k = j+1; k < N; k++)
                {
                    if(a[i]+a[j]+a[k] == 0)
                        count++;
                }
            }
        }
        return count;
    }

    public static int oneSum(int[] a)
    {
        int N = a.length;

        int count = 0;
        for(int i = 0; i < N; i++)
        {
            if(a[i] == 0)
                count++;
        }

        return count;
    }

    public static int twoSum(int[] a)
    {
        int N = a.length;

        int count = 0;
        for(int i = 0; i < N; i++)
        {
            for(int j = i; j < N; j++)
            {
                if(a[i] + a[j] == 0)
                    count++;
            }
        }

        return count;
    }

    public static int[] generateExample(int n)
    {
        Random r = new Random();
        int [] examples = new int[n];

        for(int i = 0;  i < n; i++)
        {
            examples[i] = r.nextInt();
        }

        return examples;
    }

    public static double calculateAverageExecutionTime(int n)
    {
    	//TODO implement
        int trials = 30;
        double totalTime = 0;
        
        return 0;  
    }

    public static void main(String[] args)
    {
        //TODO implement
    }
}
