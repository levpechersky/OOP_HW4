package OOP.Tests.PartB.NoMethodAnnotations;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMethodModifier;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I321 {
    @OOPMultipleMethod(modifier = OOPMethodModifier.PRIVATE)
    void foo() throws OOPMultipleException;
}
