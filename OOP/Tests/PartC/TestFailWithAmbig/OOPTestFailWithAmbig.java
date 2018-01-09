package OOP.Tests.PartC.TestFailWithAmbig;

import OOP.Provided.OOPCoincidentalAmbiguity;
import OOP.Provided.OOPMultipleClassGenerator;
import static org.junit.Assert.*;
import org.junit.Test;
import OOP.Provided.OOPMultipleException;

public class OOPTestFailWithAmbig {
    private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();

    //@Rule
    //public Timeout timeout = new Timeout(1500);

    @Test
    public void testAmbigFail() {
        try {
            I3 obj = (I3) generator.generateMultipleClass(I3.class);
            Integer x = 5;
            Double res = obj.foo(x);
            System.out.println("Should not have worked!!!");
            fail();
        } catch(OOPCoincidentalAmbiguity e) {
            System.out.println(e.getMessage());
        } catch (OOPMultipleException e) {
            fail();
        } finally {
            generator.removeSourceFile();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        generator.removeSourceFile();
    }
}
