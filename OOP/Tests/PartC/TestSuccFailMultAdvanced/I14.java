package OOP.Tests.PartC.TestSuccFailMultAdvanced;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;
import javafx.scene.Group;

@OOPMultipleInterface
public interface I14 extends I13 {
    @OOPMultipleMethod
    public Group foo1(Boolean b) throws OOPMultipleException;
}
