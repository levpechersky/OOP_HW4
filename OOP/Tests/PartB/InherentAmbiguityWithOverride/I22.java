package OOP.Tests.PartB.InherentAmbiguityWithOverride;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPInnerMethodCall;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I22 extends I20 {
    @OOPMultipleMethod
    default void foo(int x) throws OOPMultipleException {
        System.out.println("I22 overrides foo");
    }
}
