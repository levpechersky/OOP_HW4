package OOP.Tests.PartC.TestSuccFailMultAdvanced;

import OOP.Provided.OOPMultipleException;
import OOP.Solution.OOPMultipleInterface;
import OOP.Solution.OOPMultipleMethod;
import javafx.scene.Group;

@OOPMultipleInterface
public interface I43 extends I42 {
    @OOPMultipleMethod
    public Group foo4(Boolean b) throws OOPMultipleException;
}
