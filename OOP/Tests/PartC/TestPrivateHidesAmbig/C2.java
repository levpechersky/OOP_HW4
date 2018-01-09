package OOP.Tests.PartC.TestPrivateHidesAmbig;

import OOP.Provided.OOPMultipleException;

public class C2 implements I2 {
    public String getIntString(Integer x) throws OOPMultipleException {
        return Integer.toString(x);
    }
}
