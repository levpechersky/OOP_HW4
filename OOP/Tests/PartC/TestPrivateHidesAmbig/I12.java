package OOP.Tests.PartC.TestPrivateHidesAmbig;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMethodModifier;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I12 extends I11 {
    @OOPMultipleMethod(modifier = OOPMethodModifier.PRIVATE)
    public String getIntString(Integer x) throws OOPMultipleException;

    @OOPMultipleMethod
    public String foo(Integer x) throws OOPMultipleException;
}
