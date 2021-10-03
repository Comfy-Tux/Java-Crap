import java.util.*;

class MinStack {

   public static void main(String[] argv) {
      var stock = new MinStack();

      stock.push(-2);
      stock.push(0);
      stock.push(-3);
//      stock.push(-2147483648);
      System.out.println(stock.getMin());
      stock.pop();
      System.out.println( stock.top());
      System.out.println( stock.getMin());
   }

      private Node head;

      public void push(int x) {
         if (head == null)
            head = new Node(x, x, null);
         else
            head = new Node(x, Math.min(x, head.min), head);
      }

      public void pop() {
         head = head.next;
      }

      public int top() {
         return head.val;
      }

      public int getMin() {
         return head.min;
      }

      private class Node {
         int val;
         int min;
         Node next;

         private Node(int val, int min, Node next) {
            this.val = val;
            this.min = min;
            this.next = next;
         }
      }
}

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(val);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */
