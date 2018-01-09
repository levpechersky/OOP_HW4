package OOP.Tests.Example;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;

/**
 * Created by danie_000 on 6/7/2017.
 */
@OOPMultipleInterface
public interface I5B {
    @OOPMultipleMethod
    default void f(B p1, A p2, A p3,A p4) throws OOPMultipleException{}
    @OOPMultipleMethod
    default String g(B p1, A p2, A p3) throws OOPMultipleException{return "";}
}
