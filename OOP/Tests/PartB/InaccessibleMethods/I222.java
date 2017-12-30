package OOP.Tests.PartB.InaccessibleMethods;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMethodModifier;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I222 {
    @OOPMultipleMethod(modifier = OOPMethodModifier.PRIVATE)
    void privateMethod() throws OOPMultipleException;
}
