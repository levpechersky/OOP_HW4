package OOP.Tests.PartB.NoMethodAnnotations;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMethodModifier;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I31 extends I311 {
    @OOPMultipleMethod
    void doNothing() throws OOPMultipleException;

    @OOPMultipleMethod(modifier = OOPMethodModifier.PROTECTED)
    Integer baz() throws OOPMultipleException;
}
