package OOP.Tests.PartB.NoMethodAnnotations;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;

import java.lang.reflect.Method;

@OOPMultipleInterface
public interface I311 {
    Method withoutAnnotation() throws OOPMultipleException; // <-------- here it is
}
