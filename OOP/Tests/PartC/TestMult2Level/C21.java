package OOP.Tests.PartC.TestMult2Level;

import OOP.Provided.OOPMultipleException;

public class C21 implements I21 {
    @Override
    public Character bar1(Character c) throws OOPMultipleException {
        return Character.toUpperCase(c);
    }
}
