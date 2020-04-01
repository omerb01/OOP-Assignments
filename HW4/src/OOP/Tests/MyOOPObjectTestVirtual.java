package OOP.Tests;

import OOP.Provided.OOP4ObjectInstantiationFailedException;
import OOP.Solution.OOPObject;
import OOP.Solution.OOPParent;
import org.junit.Test;

public class MyOOPObjectTestVirtual {
    public static class A {
    }

    @OOPParent(parent = A.class, isVirtual = true)
    public static class B extends OOPObject {
        public B() throws OOP4ObjectInstantiationFailedException {
        }
    }

    @OOPParent(parent = A.class, isVirtual = true)
    public static class C extends OOPObject {
        public C() throws OOP4ObjectInstantiationFailedException {
        }
    }

    @OOPParent(parent = B.class)
    @OOPParent(parent = C.class)
    public static class D extends OOPObject {
        public D() throws OOP4ObjectInstantiationFailedException {
        }
    }

    @Test
    public void testConstructor() throws OOP4ObjectInstantiationFailedException {
        D d = new D();
    }

    @Test
    public void testAmbiguous() throws OOP4ObjectInstantiationFailedException {
        D d = new D();
    }

}



