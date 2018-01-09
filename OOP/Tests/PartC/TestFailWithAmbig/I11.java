package OOP.Tests.PartC.TestFailWithAmbig;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I11 {
    @OOPMultipleMethod
    public Double foo(Integer x) throws OOPMultipleException;
}
