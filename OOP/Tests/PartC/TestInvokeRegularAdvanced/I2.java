package OOP.Tests.PartC.TestInvokeRegularAdvanced;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I2 extends I1 {
    @OOPMultipleMethod
    public String bar() throws OOPMultipleException;
}
