package OOP.Tests;

import OOP.Provided.OOP4AmbiguousMethodException;
import OOP.Provided.OOP4MethodInvocationFailedException;
import OOP.Provided.OOP4NoSuchMethodException;
import OOP.Provided.OOP4ObjectInstantiationFailedException;
import OOP.Solution.OOPObject;
import OOP.Solution.OOPParent;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyOOPObjectTest {

    public static class A {
        public String f() {
            return "f";
        }

        public String g(Integer a, String s) {
            return "g:A";
        }
    }

    public static class B extends A {
        public String g(Integer a) {
            return "g:B";
        }

        public String h() {
            return "h:B";
        }
    }

    @OOPParent(parent = B.class)
    public static class B2 extends OOPObject {
        public B2() throws OOP4ObjectInstantiationFailedException {
        }

        public String h() {
            return "h:B2";
        }
    }

    @OOPParent(parent = A.class)
    public static class C extends OOPObject {
        public C() throws OOP4ObjectInstantiationFailedException {
        }

        public String h() {
            return "h:C";
        }

        public Integer e(Integer n) {
            Integer[] a = {1};
            if (n < 0) return a[2];
            return n;
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
    public void testMultInheritsFrom() throws OOP4ObjectInstantiationFailedException {
        C c = new C();
        D d = new D();

        assertTrue(c.multInheritsFrom(C.class));
        assertTrue(d.multInheritsFrom(D.class));
        assertTrue(c.multInheritsFrom(A.class));
        assertTrue(d.multInheritsFrom(A.class));

        assertFalse(c.multInheritsFrom(B.class));
        assertFalse(c.multInheritsFrom(D.class));

    }

    @Test
    public void testDefiningObject() throws OOP4ObjectInstantiationFailedException {
        B2 b2 = new B2();
        C c = new C();
        D d = new D();

        assertDoesNotThrow(() -> b2.definingObject("g", Integer.class, String.class).getClass().equals(A.class));
        assertDoesNotThrow(() -> d.definingObject("g", Integer.class).getClass().equals(B.class));

        assertDoesNotThrow(() -> c.definingObject("g", Integer.class, String.class).getClass().equals(B.class));

        assertDoesNotThrow(() -> b2.definingObject("h").getClass().equals(B2.class));
        assertDoesNotThrow(() -> c.definingObject("h").getClass().equals(C.class));
        assertThrows(OOP4AmbiguousMethodException.class, () -> d.definingObject("h"));

    }

    @Test
    public void testInvoke() throws OOP4ObjectInstantiationFailedException {
        B2 b2 = new B2();
        C c = new C();
        D d = new D();

        assertThrows(OOP4AmbiguousMethodException.class, () -> d.invoke("g", 1, "a"));
        assertThrows(OOP4NoSuchMethodException.class, () -> c.invoke("g", 1));
        assertThrows(OOP4MethodInvocationFailedException.class, () -> c.invoke("e", -1));

        assertDoesNotThrow(() -> c.invoke("e", 0).equals(0));
        assertDoesNotThrow(() -> c.invoke("e", 80).equals(80));
        assertDoesNotThrow(() -> c.invoke("f").equals("f:A"));
        assertDoesNotThrow(() -> c.invoke("g", 1, "name").equals("g:A"));
        assertDoesNotThrow(() -> b2.invoke("g", 1).equals("g:B"));
        assertDoesNotThrow(() -> b2.invoke("h").equals("h:B"));
        assertDoesNotThrow(() -> d.invoke("e", 1).equals(1));

    }


}
