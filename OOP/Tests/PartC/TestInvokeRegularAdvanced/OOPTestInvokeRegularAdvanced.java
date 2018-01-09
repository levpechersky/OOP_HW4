package OOP.Tests.PartC.TestInvokeRegularAdvanced;

import OOP.Provided.OOPMultipleClassGenerator;
import static org.junit.Assert.*;
import org.junit.Test;
import OOP.Provided.OOPMultipleException;

public class OOPTestInvokeRegularAdvanced {
    private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();

    //@Rule
    //public Timeout timeout = new Timeout(1500);

    @Test
    public void advancedRegularTest() {
        try {
            I5 obj = (I5) generator.generateMultipleClass(I5.class);
            assertEquals("C3.foo", obj.foo());
            assertEquals("C4.bar", obj.bar());
        } catch(OOPMultipleException e) {
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