package OOP.Tests.PartB.NoMethodAnnotationsInBase;

import OOP.Provided.OOPBadClass;
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

    @Test
    public void missingMethodAnnotationTest() {
        /* Interface class (root) also need to annotate all methods (but not interface itself - WTF is wrong with you, Ofir???) */
        try {
            OOP.Tests.PartB.NoMethodAnnotationsInBase.I3 obj
                    = (I3) generator.generateMultipleClass(I3.class);
            fail("No exception was thrown");
        } catch (OOPMultipleException e) {
            assertEquals(OOPBadClass.class, e.getClass());
        } finally {
            generator.removeSourceFile();
        }
    }
}
