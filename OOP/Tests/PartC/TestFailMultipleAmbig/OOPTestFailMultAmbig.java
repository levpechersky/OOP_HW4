package OOP.Tests.PartC.TestFailMultipleAmbig;

import OOP.Provided.OOPCoincidentalAmbiguity;
import OOP.Provided.OOPMultipleClassGenerator;
import static org.junit.Assert.*;
import org.junit.Test;
import OOP.Provided.OOPMultipleException;

public class OOPTestFailMultAmbig {
    private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();

    //@Rule
    //public Timeout timeout = new Timeout(1500);

    @Test
    public void testFailMultipleAmbig() {
        try {
            I5 obj = (I5)generator.generateMultipleClass(I5.class);
            String res = obj.foo();
            fail("Should not succeed!!!");
        } catch (OOPCoincidentalAmbiguity e) {
            //Nothing, success.
            System.out.println(e.getMessage());
        } catch (OOPMultipleException e) {
            fail("Wrong throw???");
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
