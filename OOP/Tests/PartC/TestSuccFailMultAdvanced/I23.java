package OOP.Tests.PartC.TestSuccFailMultAdvanced;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I23 extends I22 {
    @OOPMultipleMethod
    public String fun2(Integer x) throws OOPMultipleException;
}
