import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.  junit.jupiter.api.Assertions.*;

class EncrypterSmithTests {
   @Test
   public void testEncrypt(){
      assertEquals("ABCDEFGHJLBUG", EncrypterSmith.encrypt(-3,0,"BUGABCDEFGHJL"));
      assertEquals("JLBUGABCDEFGH", EncrypterSmith.encrypt(2,0,"BUGABCDEFGHJL"));
      assertEquals("t{dwiKpecejgOgoq", EncrypterSmith.encrypt(2,2,"bugIncacheMemory"));
   }

   @Test
   public void testDecrypt(){
      assertEquals("ABCDEFGHJL", EncrypterSmith.decrypt(-3,0,"DEFGHJLABC"));
      assertEquals("ABCDEFGHJL", EncrypterSmith.decrypt(2,0,"JLABCDEFGH"));
      assertEquals("bugIncacheMemory",EncrypterSmith.decrypt(2,2,"t{dwiKpecejgOgoq"));
   }

   @Test
   public void testFindKey(){
      assertEquals("[2, 2]", Arrays.toString(EncrypterSmith.findKey("t{dwiKpecejgOgoq","bug")));
      assertEquals("[2, 3]", Arrays.toString(EncrypterSmith.findKey("V5exjIRRWLPH","bug")));
      assertEquals("[-9, -4]", Arrays.toString(EncrypterSmith.findKey("L]je_^qcGanjah","bug")));
   }


}