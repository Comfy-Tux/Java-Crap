import java.sql.*;

public class BDapp {
   public static void main(String args[]) {
      String dbname = "baseDeDados";
      String dbuser = "comfypad";
      String password = "";
      String url = "jdbc:postgresql://localhost:5432/" + dbname;

      try {
         Connection c = DriverManager.getConnection(url);
         Statement stmt = c.createStatement();

         //testing the queries
         // query p1
         System.out.println("query p1");
         String query = "Select nome\n" +
                 " From Cliente\n" +
                 " WHERE email IN(\n" +
                 "   Select email\n" +
                 "   FROM Pedido\n" +
                 "   GROUP BY email\n" +
                 "   HAVING COUNT(*) > 1\n" +
                 " );";
         ResultSet rs = stmt.executeQuery(query);
         System.out.println("nome");
         System.out.println("-----");
         while(rs.next()) {
            String nome = rs.getString("nome");
            System.out.println(nome);
         }

         //query p2
         System.out.println("\nquery p2");
         query = "(\n" +
                 "Select codigoDaBD , numeroDoProgramador\n" +
                 "From Equipa join  Pertencem on numero = numeroDaEquipa\n" +
                 ");";

         rs = stmt.executeQuery(query);
         System.out.println("codigoDaBD  |  numeroDoProgramador");
         while(rs.next()) {
            int codigoDaBD = rs.getInt("codigoDaBD");
            int numeroDoProgramador = rs.getInt("numeroDoProgramador");
            System.out.println("\t" + codigoDaBD + "\t\t|\t\t\th" + numeroDoProgramador);
         }

         //query p3
         System.out.println("\nquery p3");
         query = "Select sum(orcamento)\n" +
                 "From Pedido\n" +
                 "Where ' 2014-01-01' <= prazo and prazo <= '2016-01-01';\n";
         rs = stmt.executeQuery(query);
         System.out.println("sum\n----");
         rs.next();
         int sum = rs.getInt("sum");
         System.out.println(sum);

         //query p4
         System.out.println("\nquery p4");
         query = "Select codigo\n" +
                 "From BaseDeDados\n" +
                 "Where email IS NULL;\n";
         rs = stmt.executeQuery(query);
         System.out.println("codigo\n-------");
         while(rs.next()) {
            int codigo = rs.getInt("codigo");
            System.out.println(codigo);
         }

         //query p5
         System.out.println("\nquery p5");
         query = "(\n" +
                 "Select numeroUnico\n" +
                 "  from Empregados\n" +
                 "EXCEPT\n" +
                 "Select distinct numeroUnico\n" +
                 "  from desenvolvem\n" +
                 ");\n";
         rs = stmt.executeQuery(query);
         System.out.println("numeroUnico\n------------");
         while(rs.next()){
            int numeroUnico = rs.getInt("numeroUnico");
            System.out.println(numeroUnico);
         }

         //testing insert , update and delete

         //testing insert
         stmt.executeUpdate("INSERT INTO BaseDeDados Values(13,'clone','clone' , 1000 , 1 , NULL);");

         //execute query 4 to confirm this i.e. now there should be another database without an owner
         System.out.println("\ntesting insert : output expected 12 13");
         //query p4
         System.out.println("query p4");
         query = "Select codigo\n" +
                 "From BaseDeDados\n" +
                 "Where email IS NULL;\n";
         rs = stmt.executeQuery(query);
         System.out.println("codigo\n-------");
         while(rs.next()) {
            int codigo = rs.getInt("codigo");
            System.out.println(codigo);
         }

         //testing delete
         System.out.println("\ntesting delete : output expected 12");
         stmt.executeUpdate("DELETE FROM BaseDeDados WHERE codigo=13;");

         //query p4
         System.out.println("query p4");
         query = "Select codigo\n" +
                 "From BaseDeDados\n" +
                 "Where email IS NULL;\n";
         rs = stmt.executeQuery(query);
         System.out.println("codigo\n-------");
         while(rs.next()) {
            int codigo = rs.getInt("codigo");
            System.out.println(codigo);
         }

         //testing update
         System.out.println("\ntesting update : output expect : 12 11");
         stmt.executeUpdate("Update BaseDeDados SET email = NULL WHERE codigo = 11");

         //query p4
         System.out.println("query p4");
         query = "Select codigo\n" +
                 "From BaseDeDados\n" +
                 "Where email IS NULL;\n";
         rs = stmt.executeQuery(query);
         System.out.println("codigo\n-------");
         while(rs.next()) {
            int codigo = rs.getInt("codigo");
            System.out.println(codigo);
         }

         //reverts changes
         stmt.executeUpdate("Update BaseDeDados SET email = 'joana@email.com' WHERE codigo = 11");

         rs.close();
         stmt.close();
         c.close();
      }
      catch (Exception e) {
         System.err.println("Error: connection failed.");
         System.err.println( e.getMessage() );
      }

   }
}
