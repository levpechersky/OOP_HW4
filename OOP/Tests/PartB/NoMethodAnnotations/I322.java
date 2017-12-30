package OOP.Tests.PartB.NoMethodAnnotations;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I322 {
    @OOPMultipleMethod
    void bar() throws OOPMultipleException;
}
