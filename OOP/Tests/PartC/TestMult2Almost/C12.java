package OOP.Tests.PartC.TestMult2Almost;

import OOP.Provided.OOPMultipleException;

public class C12 extends C11 implements I12 {
    public Integer foo2(Square sq) throws OOPMultipleException {
        return sq.getSide();
    }
}
