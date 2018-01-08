package OOP.Tests.PartC.TestMult2Almost;

import OOP.Provided.OOPMultipleException;

public class C21 implements I21 {
    @Override
    public String bar1(Shape s) throws OOPMultipleException {
        return s.getDescr();
    }
}
