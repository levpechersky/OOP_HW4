package OOP.Tests.PartC.TestPrivateHidesAmbig;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I11 {
    @OOPMultipleMethod
    public String getIntString(Integer x) throws OOPMultipleException;
}