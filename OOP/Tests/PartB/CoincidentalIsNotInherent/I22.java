package OOP.Tests.PartB.CoincidentalIsNotInherent;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPInnerMethodCall;
import OOP.Solution.OOPMethodModifier;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I22 {
    @OOPMultipleMethod(modifier = OOPMethodModifier.PROTECTED)
    void foo() throws OOPMultipleException;
}
