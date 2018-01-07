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
	public String foo1(Double b);
}

public class A2Impl implements A2 {
	@OOPMultipleMethod
	public String foo1(Double b) {
		return "b = " + Double.toString(b);
	}
}

static interface B1 extends A1 { }
public class B1Impl implements B1 { }

static interface B2 extends A2 { }
public class B2Impl implements B2 { }

static interface CC extends B1, B2 { }

public class OOPTestInvokeWithAmbig {
	private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();
	@Override
    protected void finalize() throws Throwable {
        super.finalize();
        generator.removeSourceFile();
    }

    @Test
    public void TestInvokePossibleAmbig() {
    	try {
    		C obj = (C) generator.generateMultipleClass(C.class);
            Integer a = 6;
            Double  b = 0.4;
    		assertEqual(obj.foo1(a), "a = 6");
            assertEqual(obj.foo1(b), "b = 0.4");
    	}
    	catch(OOPMultipleException e) {
    		e.printStackTrace();
    		Assert.fail();
    	}
    	finally {
    		generator.removeSourceFile();
    	}
    }
}