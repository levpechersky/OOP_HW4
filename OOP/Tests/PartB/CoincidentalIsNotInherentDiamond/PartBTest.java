package OOP.Tests.PartB.CoincidentalIsNotInherentDiamond;

import OOP.Provided.OOPMultipleClassGenerator;
import OOP.Provided.OOPMultipleException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
    public void CoincidentalIsNotInherentDiamondTest() {
        try {
            OOP.Tests.PartB.CoincidentalIsNotInherentDiamond.I2 obj = (I2) generator.generateMultipleClass(I2.class);
        } catch (OOPMultipleException e) {
            e.printStackTrace();
            fail();
        } finally {
            generator.removeSourceFile();
        }
    }
}
