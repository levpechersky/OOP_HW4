package OOP.Tests.PartC.TestSuccFailMultAdvanced;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I22 extends I21 {
    @OOPMultipleMethod
    public Integer fun1() throws OOPMultipleException;
}
