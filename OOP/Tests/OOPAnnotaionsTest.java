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

public class TestOOPAnnotations {
	
	private class AnnotatedClass {

		@OOPMultipleInterface
		public class Class1 { 
			public double baz(Double x) {
				return x + 1.5;
			}
		}

		@OOPMultipleInterface
		public class Class2 { 
			public String foo(String x) {
				return new StringBuilder(x).reverse.toString();
			}
		}

		@OOPMultipleInterface
		public class Class3 {
			public Integer bar(Integer x) {
				return x + 4;
			}
		}

		@OOPMultipleMethod(modifier=OOPMultipleControl.PUBLIC)
		public int getNum() { return 0; }

		@OOPMultipleMethod(modifier=OOPMultipleControl.PUBLIC)
		public String getString() { return "something"; }

		@OOPMultipleMethod(modifier=OOPMultipleControl.PROTECTED)
		protected char getChar() { return 'p'; }

		@OOPMultipleMethod(modifier=OOPMultipleControl.PROTECTED)
		protected long getLong() { return 100; }

		@OOPMultipleMethod(modifier=OOPMultipleControl.PRIVATE)
		private double getDouble() { return 3.14159; }

		@OOPMultipleMethod(modifier=OOPMultipleControl.PRIVATE)
		private boolean getBool() { return true; }

		@OOPMultipleMethod(modifier=OOPMultipleControl.PUBLIC)
		@OOPInnerMethodCall(caller=Class1, callee=Class2, methodName="foo", argTypes={String})
		public String F1() { return "F1"; }

		@OOPMultipleMethod(modifier=OOPMultipleControl.PUBLIC)
		@OOPInnerMethodCall(caller=Class1, callee=Class3, methodName="bar", argTypes={Integer})
		public String F2() { return "F2"; }

		@OOPMultipleMethod(modifier=OOPMultipleControl.PUBLIC)
		@OOPInnerMethodCall(caller=Class2, callee=Class1, methodName="baz", argTypes={Double})
		public String F3() { return "F3"; }

		@OOPMultipleMethod(modifier=OOPMultipleControl.PUBLIC)
		@OOPInnerMethodCall(caller=Class2, callee=Class3, methodName="bar", argTypes={Integer})
		public String F4() { return "F4"; }

		@OOPMultipleMethod(modifier=OOPMultipleControl.PUBLIC)
		@OOPInnerMethodCall(caller=Class3, callee=Class1, methodName="baz", argTypes={Double})
		public String F5() { return "F5"; }

		@OOPMultipleMethod(modifier=OOPMultipleControl.PUBLIC)
		@OOPInnerMethodCall(caller=Class3, callee=Class2, methodName="foo", argTypes={String})
		public String F6() { return "F6"; }
	}

	@Test
	public void TestMultipleInterfaceAnnot() {
		AnnotatedClass ac = new AnnotatedClass();
		Class<?>[] inner_classes = ac.getClass().getDeclaredClasses();
		for(Class<?> in_class : inner_classes) {
			assertTrue(in_class.isAnnotationPresent(OOPMultipleInterface.class), true);
		}
	}

	@Test
	public void TestMultipleMethodAnnot() {
		String mm_str = "javax.annotation.OOPMultipleMethod"
		AnnotatedClass ac = new AnnotatedClass();
		Method[] methods = ac.getClass().getDeclaredMethods();
		for(Method method : methods) {
			switch(method.getName()) {
				case "getNum":
					assertEqual(method.getAnnotations().length, 1);
					assertEqual(method.getAnnotations()[0].annotationType().getName(), mm_str);
					Class<? extends Annotation> annot_type = method.getAnnotations()[0].annotationType();
					assertEqual(annot_type.getDeclaredMethods()[0].invoke(annot_type, (Object[])null), OOPMultipleControl.PUBLIC);
					break;
				case "getString":
					assertEqual(method.getAnnotations().length, 1);
					assertEqual(method.getAnnotations()[0].annotationType().getName(), mm_str);
					Class<? extends Annotation> annot_type = method.getAnnotations()[0].annotationType();
					assertEqual(annot_type.getDeclaredMethods()[0].invoke(annot_type, (Object[])null), OOPMultipleControl.PUBLIC);
					break;
				case "getChar":
					assertEqual(method.getAnnotations().length, 1);
					assertEqual(method.getAnnotations()[0].annotationType().getName(), mm_str);
					Class<? extends Annotation> annot_type = method.getAnnotations()[0].annotationType();
					assertEqual(annot_type.getDeclaredMethods()[0].invoke(annot_type, (Object[])null), OOPMultipleControl.PROTECTED);
					break;
				case "getLong":
					assertEqual(method.getAnnotations().length, 1);
					assertEqual(method.getAnnotations()[0].annotationType().getName(), mm_str);
					Class<? extends Annotation> annot_type = method.getAnnotations()[0].annotationType();
					assertEqual(annot_type.getDeclaredMethods()[0].invoke(annot_type, (Object[])null), OOPMultipleControl.PROTECTED);
					break;
				case "getDouble":
					assertEqual(method.getAnnotations().length, 1);
					assertEqual(method.getAnnotations()[0].annotationType().getName(), mm_str);
					Class<? extends Annotation> annot_type = method.getAnnotations()[0].annotationType();
					assertEqual(annot_type.getDeclaredMethods()[0].invoke(annot_type, (Object[])null), OOPMultipleControl.PRIVATE);
					break;
				case "getBool":
					assertEqual(method.getAnnotations().length, 1);
					assertEqual(method.getAnnotations()[0].annotationType().getName(), mm_str);
					Class<? extends Annotation> annot_type = method.getAnnotations()[0].annotationType();
					assertEqual(annot_type.getDeclaredMethods()[0].invoke(annot_type, (Object[])null), OOPMultipleControl.PRIVATE);
					break;
				case "F1":
				case "F2":
				case "F3":
				case "F4":
				case "F5":
				case "F6":
			}
		}
	}
}