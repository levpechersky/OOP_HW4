package OOP.Tests.PartC.TestInvokeMultipleSucc;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I2 {
    @OOPMultipleMethod
    public String bar(String str) throws OOPMultipleException;
}
