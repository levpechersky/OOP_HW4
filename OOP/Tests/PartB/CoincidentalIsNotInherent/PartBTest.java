package OOP.Tests.PartB.CoincidentalIsNotInherent;

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
    public void CoincidentalIsNotInherentTest() {
        try {
            OOP.Tests.PartB.CoincidentalIsNotInherent.I2 obj = (I2) generator.generateMultipleClass(I2.class);
        } catch (OOPMultipleException e) {
            e.printStackTrace();
            fail();
        } finally {
            generator.removeSourceFile();
        }
    }
}
