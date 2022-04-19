import java.util.Arrays;
import java.util.Scanner;

public final class Date implements Comparable<Date> {
   private final static int[] monthLength = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
   final private int year;
   final private int month;
   final private int day;
   final private int length;

   public Date(String date) {
      this.year = Integer.parseInt(date, 0, 4, 10);
      this.month = Integer.parseInt(date, 5, 7, 10);
      this.day = Integer.parseInt(date, 8, 10, 10);

      //checks if the date is valid
      if(year < 1875 || month < 1 || month > 12 || day < 1) error();
      else if(month == 2){
         if(isLeapYear(year)) { if(day > 29) error(); }
         else if(day > 28) error();
      }
      else if(day > monthLength[month-1])  error();

      //specific case start of the gregorian calendar
      if(year == 1875)
         if(month < 5) error();
         else if(month == 5 && day < 20) error();

      //given the date, returns the number of days since the epoch(1601-01-01).
      length = sumOfYears(year) + sumMonths(month,year) + day;

   }

   //adds dd days to a date and returns that date.
   public Date ad(int dd){
      return inverseLength(length + dd);
   }

   //subtracts dd days from the date and returns that date.
   public Date sub(int dd){
      return inverseLength(length - dd);
   }

   //returns the difference between two dates.
   public int diff(Date other){
      int cmp = compareTo(other);
      if(cmp == 0) return 0;

      Date bigDate = cmp > 0 ? this : other;
      Date smallDate = cmp > 0 ? other : this;

      return bigDate.length - smallDate.length;
   }

   //sums the months on that year given a variable month. Note that this is exclusive, so if the month is 12 we sum all
   //11 months before.
   private int sumMonths(int month,int year){
      int result = 0;
      updateFebruary(year);
      int N = month -1;
      for(int i =0; i < N ;i++)
         result += monthLength[i];
      return result;
   }

   //prints that calendar
   public void cal(){
      int weekStart = 0;
      int monthStart = 1;
      int yearStart = 1601;

      int n = year - yearStart;
      int m = n/4 - n/100 + n/400;
      int u = n - m;

      //this two for calculate in which day of the week the month starts, using 01-01-1601 as a base to start.
      weekStart = (weekStart + u + m*2)%7; // if it is leap year the week jumps 2 days otherwise it jumps one day.

      updateFebruary(year);

      for(int i = monthStart; i < month;i++)
         weekStart = (weekStart + monthLength[i-1] -28)%7; // if the month has 28 days then it does jump 0 days
                                                           // if it has 29,30,31 then jumps 1,2,3 days in that order.
      printCalendar(day,weekStart,monthLength[month-1]);
   }


   private void printCalendar(int day,int startOfWeek,int monthLength){
      System.out.println("Se Te Qu Qu Se Sa Do"); //portuguese days of the week starting with monday.
      int currentDay = 1;

      int i = 0;
      while(i < startOfWeek) {
         System.out.print("   ");
         i++;
      }

      int j = 1;
      while(j <= monthLength) {
         if(j == day) System.out.print("XX");
         else System.out.printf("%2d",j);

         if(i != 6 && j != monthLength) System.out.print(" "); //puts a with space following the number if it is
         else System.out.println();                            //not the end of the week and must not be the end of the month.
         j++;
         i = (++i%7);
      }
   }

   //if it is leap year it updates february to 29, otherwise it updates to 28
   private void updateFebruary(int year){ monthLength[1] = isLeapYear(year)? 29 : 28;}

   public int getYear() {return year;}
   public int getMonth() {return month;}
   public int getDay() {return day;}

   private void error(){
      System.out.println("Erro");
      System.exit(0);
   }

   //sums all year between 1601 and x , x is excluded.
   private int sumOfYears(int year){
      int n = year - 1601;
      int m = n/4 - n/100 + n/400;
      int u = n - m;
      return u*365 + m*366;
   }

   //returns the date, given the number of days since the epoch.
   public Date inverseLength(int length){
      int year = 1601;
      int month = 1;
      int day = 0;
      while(length >= 366)
         length -= yearLength(year++);

      if(length == 365) return new Date("01-01-" + ++year); //start of the year

      updateFebruary(year);
      while(length > monthLength[month -1]) {
         length -= monthLength[month - 1];
         month++;
      }

      day += length;

      return new Date(String.format("%04d-%02d-%02d",year,month,day));
   }

   private int yearLength(int year){
      return isLeapYear(year) ? 366 : 365;
   }

   private boolean isLeapYear(int year){
      return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
   }

   @Override
   public String toString() {
      return String.format("%04d-%02d-%02d",year,month,day);
   }

   @Override
   public int compareTo(Date date) {
      int result = year - date.getYear();
      if(result == 0) result = month - date.getMonth();
      if(result == 0) result = day - date.getDay();

      return result;
   }
}
