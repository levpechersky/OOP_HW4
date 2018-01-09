package OOP.Tests.PartC.TestAmbigSuccThenFail;

import OOP.Provided.OOPCoincidentalAmbiguity;
import OOP.Provided.OOPMultipleClassGenerator;
import static org.junit.Assert.*;
import org.junit.Test;
import OOP.Provided.OOPMultipleException;

public class OOPTestAmbigSuccFail {
    private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();

    //@Rule
    //public Timeout timeout = new Timeout(1500);

    @Test
    public void testMultSuccThenFail() {
        try {
            I5 obj = (I5)generator.generateMultipleClass(I5.class);
            assertEquals("C12", obj.bar1());
            assertEquals("C22", obj.bar2());
            assertEquals("C32", obj.bar3());
            assertEquals("C42", obj.bar4());
            String str = obj.foo();
            fail("Did not fail on ambiguity!!!");
        } catch (OOPCoincidentalAmbiguity e) {
            System.out.println(e.getMessage());
        } catch (OOPMultipleException e) {
            fail("Bad exception thrown!!!");
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
