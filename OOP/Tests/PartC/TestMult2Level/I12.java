package OOP.Tests.PartC.TestMult2Level;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I12 extends I11 {
    @OOPMultipleMethod
    public Integer foo2(Integer x) throws OOPMultipleException;
}
