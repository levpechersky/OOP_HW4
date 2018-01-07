package OOP.Tests.PartC;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import OOP.Solution.*;
import OOP.Provided.*;

@OOPMultipleInterface
interface IA {
	@OOPMultipleMethod
	public String foo();
}
class AImpl implements IA {
		public String foo() { return "A.foo"; }
}

@OOPMultipleInterface
interface IB extends IA {
	@OOPMultipleMethod
	public String bar();

	@OOPMultipleMethod
	public String baz(Integer a);
}
class BImpl extends AImpl implements IB {
	public String bar() { return "B.bar"; }
	public String baz(Integer a) {return "a = " + Integer.toString(a);
	}
}

interface C extends IB {}

public class OOPTestInvokeRegular {
	private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();
	@Override
    protected void finalize() throws Throwable {
        super.finalize();
        generator.removeSourceFile();
    }

	@Test
	public void RegularTest() {
		try {
			C obj = (C) generator.generateMultipleClass(C.class);
			assertEquals(obj.foo(), "A.foo");
			assertEquals(obj.bar(), "B.bar");
			assertEquals(obj.baz(6), "a = 6");
		}
		catch(OOPMultipleException e) {
			e.printStackTrace();
			fail();
		}
		finally {
			generator.removeSourceFile();
		}
	}
}