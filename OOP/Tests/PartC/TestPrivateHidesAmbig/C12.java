package OOP.Tests.PartC.TestPrivateHidesAmbig;

import OOP.Provided.OOPMultipleException;

public class C12 extends C11 implements I12 {
    public String getIntString(Integer x) throws OOPMultipleException {
        return "-1";
    }

    public String foo(Integer x) throws OOPMultipleException {
        return "Number";
    }
}
