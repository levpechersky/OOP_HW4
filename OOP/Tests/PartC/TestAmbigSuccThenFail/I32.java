package OOP.Tests.PartC.TestAmbigSuccThenFail;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I32 extends I31 {
    @OOPMultipleMethod
    public String bar3() throws OOPMultipleException;
}
