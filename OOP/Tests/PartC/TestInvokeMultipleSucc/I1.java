package OOP.Tests.PartC.TestInvokeMultipleSucc;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I1 {
    @OOPMultipleMethod
    public Integer foo(Integer a) throws OOPMultipleException;
}
