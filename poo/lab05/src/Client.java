import java.util.*;

public class Client {
   public static void main(String[] args){
      var in = new Scanner(System.in);

      var cut = new LineSegment(in.nextFloat(),in.nextFloat(),in.nextFloat(),in.nextFloat());
      var skin = new Skin(100000f,100000f);

      int nRectangles = in.nextInt();
      for(int i = 0; i < nRectangles ; i++)
         skin.addRectangle(new Rectangle(in.nextFloat(),in.nextFloat(),in.nextFloat(),in.nextFloat()));

      int nCircumferences = in.nextInt();
      for(int i =0; i < nCircumferences ; i++)
         skin.addCircumferences(new Circumference(in.nextFloat(),in.nextFloat(),in.nextFloat()));

      int nTriangles = in.nextInt();
      for(int i =0; i < nTriangles; i++)
         skin.addTriangles(new Triangle(in.nextFloat(),in.nextFloat(),in.nextFloat(),in.nextFloat(),in.nextFloat(),in.nextFloat()));

      System.out.println(skin.nIntersection(cut));
   }
}
