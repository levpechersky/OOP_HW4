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
static interface A {
	@OOPMultipleMethod
	public String printInt(Integer a);
}

public class AImpl implements A {
	@OOPMultipleMethod
	public String printInt(Integer a) {
		return "a = " + Integer.toString(a);
	}
}

@OOPMultipleInterface
static interface B {
	@OOPMultipleMethod
	public String printChar(Character b);
}

public class BImpl implements B {
	@OOPMultipleMethod
	public String printChar(Character b) {
		return "b = '" + Character.toString(b);
	}
}

static interface C extends A,B { }

public class OOPTestInvokeMultiple {
	private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();
	@Override
    protected void finalize() throws Throwable {
        super.finalize();
        generator.removeSourceFile();
    }

    @Test
    public void TestMultipleRegInheritance() {
    	try {
    		C obj = (C) generator.generateMultipleClass(C.class);
    		assertEqual(obj.printInt(5), "a = 5");
    		assertEqual(obj.printChar('g'), "b = 'g");
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