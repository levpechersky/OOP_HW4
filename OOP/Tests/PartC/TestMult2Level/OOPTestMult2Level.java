package OOP.Tests.PartC.TestMult2Level;

import OOP.Provided.OOPMultipleClassGenerator;
import OOP.Tests.PartC.TestInvokeRegularAdvanced.I5;
import static org.junit.Assert.*;
import org.junit.Test;

import OOP.Provided.OOPMultipleClassGenerator;
import OOP.Provided.OOPMultipleException;

public class OOPTestMult2Level {
    private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();

    //@Rule
    //public Timeout timeout = new Timeout(1500);

    @Test
    public void inherit2LevelTest() {
        try {
            I3 obj = (I3) generator.generateMultipleClass(I3.class);
            Integer ex1 = 6, in1 = 3;
            Character ex2 = 'A', in2 = 'a';
            Double ex3 = 6.28, in3 = 2.0;
            assertEquals("C11.foo1", obj.foo1());
            assertEquals(ex1, obj.foo2(in1));
            assertEquals(ex2, obj.bar1(in2));
            assertEquals(ex3, obj.MultPI(in3));
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
