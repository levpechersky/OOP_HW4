package OOP.Tests.PartB.InherentAmbiguity;

import OOP.Provided.OOPInherentAmbiguity;
import OOP.Provided.OOPMultipleClassGenerator;
import OOP.Provided.OOPMultipleException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PartBTest {
    private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();

    @Rule
    public Timeout timeout = new Timeout(1500);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        generator.removeSourceFile();
    }

    /*
    *   Inheritance goes in radix-tree order. For example:
    *   I21, I22, I23... are childen of I2
    *   I211, I212, I213... are childen of I21
    *   and so on.
    */

    @Test
    public void diamondWithDefaultMethodsTest() {
        try {
            OOP.Tests.PartB.InherentAmbiguity.I2 obj = (I2) generator.generateMultipleClass(I2.class);
            fail("No exception was thrown");
        } catch (OOPMultipleException e) {
            assertEquals(OOPInherentAmbiguity.class, e.getClass());
        } finally {
            generator.removeSourceFile();
        }
    }
}
