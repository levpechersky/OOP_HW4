package OOP.Tests.PartC.TestInvokeWithAmbig;

import OOP.Provided.OOPMultipleException;

public class C11 implements I11 {
    public Double foo(Double x) throws OOPMultipleException {
        return x * 2.71;
    }
}
