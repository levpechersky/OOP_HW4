package OOP.Tests.PartB.InaccessibleMethods;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPInnerMethodCall;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

@OOPMultipleInterface
public interface I22 extends I221, I222 {
    @OOPMultipleMethod
    @OOPInnerMethodCall(caller = I22.class, callee = I221.class, methodName = "protectedMethod", argTypes = {})
    default void callProtected(I221 arg) throws OOPMultipleException {
        arg.protectedMethod();
    }

    @OOPMultipleMethod
    @OOPInnerMethodCall(caller = I22.class, callee = I222.class, methodName = "privateMethod", argTypes = {})
    default void callPrivate(I222 arg) throws OOPMultipleException {
        arg.privateMethod();
    }
}
