package OOP.Tests.PartC.TestFailMultipleAmbig;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I12 extends I11 {
    @OOPMultipleMethod
    public String foo() throws OOPMultipleException;
}
