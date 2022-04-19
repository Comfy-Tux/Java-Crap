public class Ponto {
   // File: Ponto.java
   double x, y;
   Ponto(double x,double y) {
      this.x = x; this.y = y;
   }

   double dist (Ponto p) {
      double dx = x - p.x;
      double dy = y - p.y;
      return Math.sqrt(dx*dx + dy*dy);
   }
}

