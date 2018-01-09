package OOP.Tests.PartC.TestInvokeMultipleSucc;
import OOP.Provided.OOPMultipleClassGenerator;
import static org.junit.Assert.*;
import org.junit.Test;
import OOP.Provided.OOPMultipleException;


public class TestInvokeMultipleSucc {
    private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();

    //@Rule
    //public Timeout timeout = new Timeout(1500);

    @Test
    public void invokeMultiplePass() {
        try {
            I3 obj = (I3)generator.generateMultipleClass(I3.class);
            Integer a = 4;
            Integer b = obj.foo(4);
            assertEquals(a, b);
            assertEquals("123 good", obj.bar("good"));

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
