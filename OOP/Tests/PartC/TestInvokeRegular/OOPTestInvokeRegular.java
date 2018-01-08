package OOP.Tests.PartC.TestInvokeRegular;

import OOP.Tests.PartC.TestInvokeRegular.I3;
import org.junit.Test;
import static org.junit.Assert.*;

import OOP.Provided.OOPMultipleClassGenerator;
import OOP.Provided.OOPMultipleException;

public class OOPTestInvokeRegular {
	private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();

	//@Rule
	//public Timeout timeout = new Timeout(1500);

	@Test
	public void regularTest() {
		try {
			I3 obj = (I3) generator.generateMultipleClass(I3.class);
			assertEquals("A.foo", obj.foo());
			assertEquals("B.bar", obj.bar());
			assertEquals("a = 6", obj.baz(6));
		}
		catch(OOPMultipleException e) {
			e.printStackTrace();
			fail();
		}
		finally {
			generator.removeSourceFile();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		generator.removeSourceFile();
	}
}