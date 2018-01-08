package OOP.Tests.PartC.TestMult2Almost;

import OOP.Provided.OOPMultipleClassGenerator;
import OOP.Tests.PartC.TestInvokeRegularAdvanced.I5;
import static org.junit.Assert.*;
import org.junit.Test;

import OOP.Provided.OOPMultipleClassGenerator;
import OOP.Provided.OOPMultipleException;

public class OOPTestMult2Almost {
    private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();

    //@Rule
    //public Timeout timeout = new Timeout(1500);

    @Test
    public void mult2LevelAlmostMatch() {
        try {
            I3 obj = (I3) generator.generateMultipleClass(I3.class);
            Shape s1 = new Shape();
            Shape s2 = new Circle(7);
            Shape s3 = new Square(9);
            Integer sq_ex = 9, ci_ex = 14;
            assertEquals("Shape", obj.foo1(s1));
            assertEquals("Shape", obj.bar1(s1));
            assertEquals(sq_ex, obj.foo2((Square) s3));
            assertEquals(ci_ex, obj.bar2(2, (Circle)s2));
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
