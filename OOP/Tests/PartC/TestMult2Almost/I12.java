package OOP.Tests.PartC.TestMult2Almost;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I12 extends I11 {
    @OOPMultipleMethod
    public Integer foo2(Square sq) throws OOPMultipleException;
}
