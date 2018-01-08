package OOP.Tests.PartC.TestInvokeRegular;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleMethod;

public class C2 extends C1 implements I2 {
    @OOPMultipleMethod
    public String bar() throws OOPMultipleException { return "B.bar"; }

    @OOPMultipleMethod
    public String baz(Integer a) throws OOPMultipleException {
        return "a = " + Integer.toString(a);
    }
}
