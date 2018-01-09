package OOP.Tests.PartC.TestAmbigSuccThenFail;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I22 extends I21 {
    @OOPMultipleMethod
    public String bar2() throws OOPMultipleException;
}
