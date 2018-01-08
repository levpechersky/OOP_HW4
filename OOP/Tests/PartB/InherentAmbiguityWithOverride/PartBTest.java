package OOP.Tests.PartB.InherentAmbiguityWithOverride;

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

    @Test
    public void InherentAmbiguityWithOverrideTest() {
        try {
            OOP.Tests.PartB.InherentAmbiguityWithOverride.I2 obj = (I2) generator.generateMultipleClass(I2.class);
            fail("No exception was thrown");
        } catch (OOPMultipleException e) {
            System.out.println(e.getMessage());
            assertEquals(OOPInherentAmbiguity.class, e.getClass());
        } finally {
            generator.removeSourceFile();
        }
    }
}
