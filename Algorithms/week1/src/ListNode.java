import java.util.*;

public class ListNode {
   int val;
   ListNode next;

   ListNode() {
   }

   ListNode(int val) {
      this.val = val;
      this.next = null;
   }

   ListNode(int val, ListNode next) {
      this.val = val;
      this.next = next;
   }

   public static void printList(ListNode list){
      System.out.print("[");
      while(list != null) {
         System.out.print(" " + list.val + " ");
         list = list.next;
      }

      System.out.println("]");
   }

   public static void main (String[] argv){
      var x = new ListNode(1,new ListNode(2 , new ListNode(4,null)));
      var y = new ListNode(1,new ListNode(3, new ListNode(4,null)));


      var z = mergeTwoLists(x,y);
      printList(z);
   }

   public static ListNode mergeTwoLists(ListNode l1, ListNode l2){
         ListNode result = null;

      if (l1 != null || l2 != null) {
            if ( l2 == null || l1 != null && l1.val < l2.val) {
               result = l1;
               l1 = l1.next;
            } else {
               result = l2;
               l2 = l2.next;
            }
      }

         var head = result;

      while (l1 != null || l2 != null) {
         if (l2 == null || l1 != null && l1.val < l2.val) {
            result.next = l1;
            result = result.next;
            l1 = l1.next;
         } else {
            result.next = l2;
            result = result.next;
            l2 = l2.next;
         }
      }
      return head;
   }

   public boolean hasCycle(ListNode head) {
      ListNode node = head;
      int counter = 0;
      if(node == null)
         return false;
      while(true){
         counter++;
         for(int i = 0; i < counter; i++) {
            head = head.next;
            if(head == null || head.next == null)
               return false;
            if(node == head)
               return true;
         }
         node = head;
      }
   }

}
