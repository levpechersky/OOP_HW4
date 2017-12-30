package OOP.Tests.PartB.InaccessibleMethods;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMethodModifier;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I211 {
    @OOPMultipleMethod(modifier = OOPMethodModifier.PUBLIC)
    void publicMethod() throws OOPMultipleException;
}
