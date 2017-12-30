package OOP.Tests.PartB.InherentAmbiguity;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMethodModifier;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I20 {
    @OOPMultipleMethod
    default void foo(int x) throws OOPMultipleException {

    }
}
