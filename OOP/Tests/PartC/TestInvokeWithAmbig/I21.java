package OOP.Tests.PartC.TestInvokeWithAmbig;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I21 {
    @OOPMultipleMethod
    public Double foo(Object x) throws OOPMultipleException;
}
