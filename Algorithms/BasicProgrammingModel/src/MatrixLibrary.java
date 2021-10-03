import java.util.*;

//btw no ragged matrices
public class MatrixLibrary {
   public static void main(String[] argv) {
      var x1 = new double[]{1, 2, 3, 4, 5, 6};
      var y1 = new double[]{6, 5, 4, 3, 2, 1};

      var z1 = dot(x1, y1);
      System.out.println(z1);

      var x2 = new double[][]{{1, 2, 3}, {2, 3, 4}};
      var y2 = new double[][]{{5, 6, 1}, {3, 4, 2}, {2, 3, 3}};

      var z2 = mult(x2, y2);
      printSigmaMatrix(z2);

      var z3 = transpose(x2);
      printSigmaMatrix(z3);

      var x3 = new double[]{10,20,30};
      var z4 = mult(y2,x3);
      System.out.println(Arrays.toString(z4));

      var z5 = mult(x3,y2);
      System.out.println(Arrays.toString(z5));
   }

   //vector must have the same size
   public static double dot(double[] x, double[] y) {
      double result = 0.0;
      assert (x.length == y.length);
      for (int i = 0; i < x.length; i++) {
         result += x[i] * y[i];
      }
      return result;
   }

   //matrix-matrix product
   public static double[][] mult(double[][] x, double[][] y) {
      assert (x[0].length == y.length);
      var result = new double[x.length][y[0].length];

      for (int i = 0; i < x.length; i++)
         for (int j = 0; j < y[0].length; j++)
            for (int k = 0; k < y.length; k++)
               result[i][j] += x[i][k] * y[k][j];

      return result;
   }

   //transposes the matrix M^t
   public static double[][] transpose(double[][] matrix){
      var result = new double[matrix[0].length][matrix.length];

      for(int i = 0 ; i < matrix.length ; i++)
         for(int j = 0 ; j < matrix[i].length ; j++)
            result[j][i] = matrix[i][j];

      return result;
   }

   //matrix-vector product
   public static double[] mult(double[][] matrix , double[] vector){
      assert(vector.length == matrix[0].length);
      var result = new double[vector.length];

      for(int i = 0; i < matrix.length; i++)
         for(int j = 0; j < matrix[i].length ; j++)
            result[i] += vector[j] * matrix[i][j];

      return result;
   }


   //vector-matrix product
   public static double[] mult(double[] vector, double[][] matrix ){
      assert(vector.length == matrix.length);
      var result = new double[vector.length];

      for(int j = 0; j < matrix[0].length; j++)
         for(int i = 0; i < matrix.length ; i++)
            result[j] += vector[i] * matrix[i][j];

      return result;
   }



   //prints matrix , my boring code
   public static void printMatrix(double[][] matrix){
      for(double[] x : matrix)
         System.out.println(Arrays.toString(x));
   }

   //prints matrix , the chad way , stolen off course from the Sigma grinders https://stackoverflow.com/questions/5061912/printing-out-a-2d-array-in-matrix-format ,
   // chad name user16119950
   public static void printSigmaMatrix(double[][] matrix) {
      Arrays.stream(matrix).map(Arrays::toString).forEach(System.out::println);
   }
}
