package OOP.Tests.PartC.TestPrivateHidesAmbig;

import OOP.Provided.OOPMultipleClassGenerator;
import static org.junit.Assert.*;
import org.junit.Test;
import OOP.Provided.OOPMultipleException;

public class OOPTestPrivateHidesAmbig {
    private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();

    //@Rule
    //public Timeout timeout = new Timeout(1500);

    @Test
    public void privateHidesAmbigTest() {
        try{
            I3 obj = (I3)generator.generateMultipleClass(I3.class);
            Integer x1 = 5, x2 = 8;
            assertEquals("Number", obj.foo(x1) );
            assertEquals("8", obj.getIntString(x2));
        } catch (OOPMultipleException e) {
            fail();
        } finally {
            generator.removeSourceFile();
        }
    }
}
