package OOP.Tests.PartC.TestInvokeWithAmbig;

import OOP.Provided.OOPMultipleClassGenerator;
import static org.junit.Assert.*;
import org.junit.Test;
import OOP.Provided.OOPMultipleException;

public class OOPTestInvokeWithAmbig {
    private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();

    //@Rule
    //public Timeout timeout = new Timeout(1500);

    @Test
    public void invokeWithAmbigTest() {
        try {
            I3 obj = (I3) generator.generateMultipleClass(I3.class);
            Double ex1 = 2.71*2;
            Double ex2 = 3.14;
            Double in1 = 2.0;
            Integer in2 = 4;
            assertEquals(ex1, obj.foo(in1));
            assertEquals(ex2, obj.foo(in2));
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
