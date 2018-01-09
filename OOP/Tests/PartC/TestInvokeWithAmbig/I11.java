package OOP.Tests.PartC.TestInvokeWithAmbig;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I11 {
    @OOPMultipleMethod
    public Double foo(Double x) throws OOPMultipleException;
}
