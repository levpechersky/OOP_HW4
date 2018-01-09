package OOP.Tests.PartC.TestAmbigSuccThenFail;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I42 extends I41 {
    @OOPMultipleMethod
    public String bar4()  throws OOPMultipleException;
}
