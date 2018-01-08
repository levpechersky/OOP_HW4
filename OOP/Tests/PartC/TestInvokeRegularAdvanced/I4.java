package OOP.Tests.PartC.TestInvokeRegularAdvanced;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I4 extends I3 {
    @OOPMultipleMethod
    public String bar() throws OOPMultipleException;
}
