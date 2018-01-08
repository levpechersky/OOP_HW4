package OOP.Tests.PartB.NoMethodAnnotationsInBase;

import OOP.Provided.OOPMultipleException;

public interface I3 {
	default void methodNeedsAnnotation() throws OOPMultipleException {}
}
