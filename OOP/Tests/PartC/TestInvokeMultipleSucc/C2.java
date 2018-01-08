package OOP.Tests.PartC.TestInvokeMultipleSucc;

import OOP.Provided.OOPMultipleException;

public class C2 implements I2 {
    public String bar(String str) throws OOPMultipleException {
        return "123 " + str;
    }
}
