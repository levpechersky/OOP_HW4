package OOP.Tests.PartC.TestMult2Level;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I22 extends I21 {
    @OOPMultipleMethod
    public Double MultPI(Double d) throws OOPMultipleException;
}
