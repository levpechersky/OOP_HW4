package OOP.Tests.PartB.InaccessibleMethods;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMethodModifier;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I221 {
    @OOPMultipleMethod(modifier = OOPMethodModifier.PROTECTED)
    void protectedMethod() throws OOPMultipleException;
}
