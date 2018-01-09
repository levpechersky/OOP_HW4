package OOP.Tests.PartC.TestSuccFailMultAdvanced;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;
import javafx.scene.Group;

@OOPMultipleInterface
public interface I32 extends I31 {
    @OOPMultipleMethod
    public Group foo3(Boolean b) throws OOPMultipleException;
}
