package OOP.Tests

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import OOP.Solution.OOPMultipleControl;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

@OOPMultipleInterface
static interface A1 {
	@OOPMultipleMethod
	public String foo1(Integer a);
}

public class A1Impl implements A {
	@OOPMultipleMethod
	public String foo1(Integer a) {
		return "a = " + Integer.toString(a);
	}
}

@OOPMultipleInterface
static interface A2 {
	@OOPMultipleMethod
	public String foo1(Integer b);
}

public class A2Impl implements A2 {
	@OOPMultipleMethod
	public String foo1(Integer b) {
		return "b = " + Integer.toString(a);
	}
}

static interface B1 extends A1 { }
public class B1Impl implements B1 { }

static interface B2 extends A2 {
	@OOPMultipleMethod(modifier=OOPMethodModifier.PRIVATE)
	private String foo1(Integer a);
}
public class B2Impl implements B2 {
	@OOPMultipleMethod(modifier=OOPMethodModifier.PRIVATE)
	private String foo1(Integer a) {
		return "private b = " + Integer.toString(a);
	}
}

static interface CC extends B1, B2 { }

public class OOPTestPrivateHidesAmbig {
	private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();
	@Override
    protected void finalize() throws Throwable {
        super.finalize();
        generator.removeSourceFile();
    }

    @Test
    public void TestAmbiguityHidden() {
    	try {
    		C obj = (C) generator.generateMultipleClass(C.class);
    		Integer a = 7;
    		assertEquals(obj.foo1(a), "a = 7");
    	} catch(OOPMultipleException e) {
    		e.printStackTrace();
    		Assert.fail();
    	} finally {
    		generator.removeSourceFile();
    	}
    }
}