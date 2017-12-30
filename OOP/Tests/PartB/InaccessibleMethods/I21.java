package OOP.Tests.PartB.InaccessibleMethods;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPInnerMethodCall;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I21 extends I211 {
    @OOPMultipleMethod
    @OOPInnerMethodCall(caller = I21.class, callee = I211.class, methodName = "publicMethod", argTypes = {})
    default void callPublic(I211 arg) throws OOPMultipleException {
        arg.publicMethod();
    }
}
