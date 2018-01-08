package OOP.Tests.PartC.TestMult2Almost;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I22 extends I21 {
    @OOPMultipleMethod
    public Integer bar2(Integer x, Circle c) throws OOPMultipleException;
}
