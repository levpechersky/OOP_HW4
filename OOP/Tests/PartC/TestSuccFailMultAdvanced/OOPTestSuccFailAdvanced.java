package OOP.Tests.PartC.TestSuccFailMultAdvanced;

import OOP.Provided.OOPCoincidentalAmbiguity;
import OOP.Provided.OOPMultipleClassGenerator;
import static org.junit.Assert.*;
import org.junit.Test;
import OOP.Provided.OOPMultipleException;

public class OOPTestSuccFailAdvanced {
    private OOPMultipleClassGenerator generator = new OOPMultipleClassGenerator();

    //@Rule
    //public Timeout timeout = new Timeout(1500);

    private Boolean test_fun1(I55 obj) {
        try {
            Integer x = obj.fun1();
            return false;
        } catch(OOPCoincidentalAmbiguity e) {
            System.out.println("test_fun1 output:");
            System.out.println(e.getMessage());
            return true;
        } catch (OOPMultipleException e) {
            System.out.println("Wrong exception thrown!");
            return false;
        }
    }

    private Boolean test_fun2(I55 obj) {
        try {
            Integer m = 5;
            String x = obj.fun2(m);
            return false;
        } catch(OOPCoincidentalAmbiguity e) {
            System.out.println("test_fun2 output:");
            System.out.println(e.getMessage());
            return true;
        } catch (OOPMultipleException e) {
            System.out.println("Wrong exception thrown!");
            return false;
        }
    }

    private Boolean test_fun3(I55 obj) {
        try {
            Double x = obj.fun3(3.14159);
            return false;
        } catch(OOPCoincidentalAmbiguity e) {
            System.out.println("test_fun3 output:");
            System.out.println(e.getMessage());
            return true;
        } catch (OOPMultipleException e) {
            System.out.println("Wrong exception thrown!");
            return false;
        }
    }

    private Boolean test_succ(I55 obj) {
        try {
            javafx.scene.Group g1 = obj.foo1(true);
            javafx.scene.Group g2 = obj.foo2(true);
            javafx.scene.Group g3 = obj.foo3(true);
            javafx.scene.Group g4 = obj.foo4(true);
            return true;
        } catch (OOPMultipleException e) {
            System.out.println("Test should have passed.");
            return false;
        }
    }

    @Test
    public void testSuccFailAdvanced() {
        try {
            I55 obj = (I55)generator.generateMultipleClass(I55.class);
            Boolean res1 = true, res2 = true, res3 = true, res4 = true;
            res1 &= test_fun1(obj);
            res2 &= test_fun2(obj);
            res3 &= test_fun3(obj);
            res4 &= test_succ(obj);
            if(!res1)
                fail("Failed res1");
            if(!res2)
                fail("Failed res1");
            if(!res3)
                fail("Failed res1");
            if(!res4)
                fail("Failed res1");
        } catch(OOPMultipleException e) {
            fail();
        } finally {
            generator.removeSourceFile();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        generator.removeSourceFile();
    }
}
