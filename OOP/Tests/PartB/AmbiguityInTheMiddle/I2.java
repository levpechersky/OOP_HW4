package OOP.Tests.PartB.AmbiguityInTheMiddle;

import OOP.Solution.OOPMultipleInterface;

/* I2 has an ambiguity - foo() declared in I20 */
@OOPMultipleInterface
public interface I2 extends I21, I22 {}

