package OOP.Tests.PartC.TestInvokeRegular;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I1 {
    @OOPMultipleMethod
    public String foo() throws OOPMultipleException;
}
