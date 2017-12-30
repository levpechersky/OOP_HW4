package OOP.Solution;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OOPInnerMethodCall {
    Class<?> caller();
    Class<?> callee();
    String methodName();
    Class<?>[] argTypes() default {};
}
