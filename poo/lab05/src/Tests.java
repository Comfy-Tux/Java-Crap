import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Tests {


   //Polygon
   @Test
   void PolygonIntersectTests(){ //{0, 0, 0, 6, 3, 3}
      assertTrue(new Polygon(new float[]{0f,0f,0f,6f,3f,3f}).intersect(new LineSegment(1,1,5,1)));
      assertTrue(new Polygon(new float[]{0f,0f,0f,6f,6f,0f,6f,6f}).intersect(new LineSegment(-6f,6f,10f,6f)));
      assertFalse(new Polygon(new float[]{0f,0f,0f,6f,3f,3f,6f,6f,3f,8f}).intersect(new LineSegment(10,10,20,10)));
   }



   //CIRCUMFERENCE
   @Test
   void CircumferenceIntersectTests(){
      assertTrue(new Circumference(5,5,5).intersect(new LineSegment(5,5,9,9))); //intersects circle
      assertTrue(new Circumference(5,5,1).intersect(new LineSegment(5,6,9,9))); //touch circle
      assertFalse(new Circumference(51,5,1).intersect(new LineSegment(6,6,9,9)));//does not intersect
   }


   ///// LINE SEGMENT
   @Test
   void LineSegmentIntersectTests(){
      assertTrue(new LineSegment(1,1,5,5).intersect(new LineSegment(5,1,1,5)));
      assertFalse(new LineSegment(1,1,5,5).intersect(new LineSegment(6,7,6,1)));
      assertTrue(new LineSegment(1,1,5,5).intersect(new LineSegment(2,2,7,7))); //collinear
   }

   /////////////////////////////////
}