package OOP.Tests.PartC.TestMult2Almost;

import OOP.Provided.OOPMultipleException;

public class C22 extends C21 implements I22 {
    public Integer bar2(Integer x, Circle c) throws OOPMultipleException {
        Integer prev = c.getRadius();
        return prev * x;
    }
}
