package OOP.Solution;

import OOP.Provided.*;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OOPMultipleControl {

    private Class<?> interfaceClass;
    private File sourceFile;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OOPMultipleInterface { }

    public enum OOPMethodModifier {
        PUBLIC(2),
        PROTECTED(1),
        PRIVATE(0);
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OOPMultipleMethod { 
        OOPMethodModifier modifier() default PUBLIC;
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OOPInnerMethodCall {
        Class<?> caller();
        Class<?> callee();
        String methodName();
        Class<?>[] argTypes();

    }

    public OOPMultipleControl(Class<?> interfaceClass, File sourceFile) {
        this.interfaceClass = interfaceClass;
        this.sourceFile = sourceFile;
    }

    //TODO: fill in here :
    public void validateInheritanceGraph() throws OOPMultipleException {

    }

    //TODO: fill in here :
    public Object invoke(String methodName, Object[] args)
            throws OOPMultipleException {
        return null;
    }

    public void removeSourceFile() {
        if (sourceFile.exists()) {
            sourceFile.delete();
        }
    }
}

