package OOP.Tests.PartB.CoincidentalIsNotInherentDiamond;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMethodModifier;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I22  extends I20 {
    @OOPMultipleMethod(modifier = OOPMethodModifier.PROTECTED)
    void foo() throws OOPMultipleException;
}
