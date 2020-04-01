package OOP.Solution;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OOPParents {
    OOPParent[] value();
}