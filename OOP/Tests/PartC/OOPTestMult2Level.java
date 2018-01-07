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
static interface C extends A {
	@OOPMultipleMethod
	public String printString(String s);
}

public class CImpl implements C {
	@OOPMultipleMethod
	public String printString(String s) {
		return "s = " + s;
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

@OOPMultipleMethod
static interface D {
	public double printDouble(Double d);
}

public class DImpl implements D {
	@OOPMultipleMethod
	public double printDouble(Double d) {
		return "d = " + Double.toString(d);
	}
}

static interface E extends C,D { }

public class OOPTestMult2Level {
	private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();
	@Override
    protected void finalize() throws Throwable {
        super.finalize();
        generator.removeSourceFile();
    }

    @Test
    public void TestMult2Level() {
    	try {
    		E obj = (E) generator.generateMultipleClass(E.class);
    		assertEqual(obj.printInt(7), "a = 7");
    		assertEqual(obj.printString("test"), "s = test");
    		assertEqual(obj.printChar('h'), "b = 'h");
    		assertEqual(obj.printDouble(3.14), "d = 3.14");
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