package OOP.Tests.PartC.TestSuccFailMultAdvanced;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I24 extends I23 {
    @OOPMultipleMethod
    public Double fun3(Double x) throws OOPMultipleException;
}
