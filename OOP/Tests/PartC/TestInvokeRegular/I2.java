package OOP.Tests.PartC.TestInvokeRegular;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
interface I2 extends I1 {
    @OOPMultipleMethod
    public String bar() throws OOPMultipleException;

    @OOPMultipleMethod
    public String baz(Integer a) throws OOPMultipleException;
}
