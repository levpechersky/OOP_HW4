package OOP.Tests.PartC.TestPrivateHidesAmbig;

import OOP.Provided.OOPMultipleException;

public class C11 implements I11 {
    public String getIntString(Integer x) throws OOPMultipleException {
        return Integer.toString(x + 1);
    }
}
