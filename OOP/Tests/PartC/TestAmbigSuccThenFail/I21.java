package OOP.Tests.PartC.TestAmbigSuccThenFail;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I21 {
    @OOPMultipleMethod
    public String foo() throws OOPMultipleException;
}
