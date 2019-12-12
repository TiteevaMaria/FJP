import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class FJP 
{
    public static class FJPMax extends RecursiveAction
    {
        public static final int mLength = 3; //THRESHOLD
	private int[] Array;
	private int first;
        private int last;
        public int result;
        
        public FJPMax(int[] Array, int first, int last) 
        {
            this.Array = Array;
            this.first = first;
            this.last = last;
	    }
        
        //поиск максимального значения
        public int FindMAX(int[] Array, int first, int last) 
        {
            int max = Integer.MIN_VALUE;
            for (int i = first; i < last; i++)
            {
                if (max < Array[i]) max = Array[i];
            }
            return max;
        }
        
        @Override
        protected void compute() 
        {
            int length = last - first;
            if (length < mLength) result = FindMAX(Array, first, last);
            else 
            {
                FJPMax left = new FJPMax(Array, first, first + length/2);
                FJPMax right = new FJPMax(Array, first + length/2, last);
                ForkJoinTask.invokeAll(left, right);
                result = Math.max(left.result, right.result);
            }
        }
    }
    
    public static void main(String[] args) 
    {
        int length = 10;
        
        int[] Array = new int[length];
        for (int i = 0; i < Array.length; i++)
        {
            Array[i] = (int)((Math.random() * 101) - 50);
        }
        
        System.out.println("Array length is: " + Array.length + ".");
        for (int i = 0; i < Array.length; i++) 
        {
            System.out.print(Array[i] + " ");
        }
        System.out.println("\nLet's find MAX VALUE...");

        ForkJoinPool pool = new ForkJoinPool(); 
        FJPMax myArray = new FJPMax(Array, 0, Array.length);
        
        pool.invoke(myArray);
        int max = myArray.result;
        
        System.out.println("MAX VALUE is: " + max);
    }   
}
