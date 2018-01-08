package OOP.Tests.PartB.AmbiguityInTheMiddle;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

/* I2 has an ambiguity - foo() declared in I20, we resolve it by overriding  */
@OOPMultipleInterface
public interface I3 extends I2 {
    @OOPMultipleMethod
    default void foo(int x) throws OOPMultipleException {
        System.out.println("I3 overriding method");
    }
}

