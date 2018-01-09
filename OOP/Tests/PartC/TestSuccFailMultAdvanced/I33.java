package OOP.Tests.PartC.TestSuccFailMultAdvanced;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I33 extends I32 {
    @OOPMultipleMethod
    public Integer fun1() throws OOPMultipleException;
}
