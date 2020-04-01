package OOP.Tests;

import OOP.Provided.CasaDeBurrito;
import OOP.Provided.Profesor;
import OOP.Solution.CasaDeBurritoImpl;
import OOP.Solution.ProfesorImpl;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.*;

public class CasaDeBurritoTest {

    private static CasaDeBurrito createSampledCase(int id) {
        String name = "Falafel";
        int dist = 250;
        Set<String> menu = new HashSet<>();
        menu.add("pitta");
        menu.add("humus");
        return new CasaDeBurritoImpl(id, name, dist, menu);
    }

    @Test
    public void testGetId() {
        CasaDeBurrito my_casa = createSampledCase(10);
        assertEquals(10, my_casa.getId());
    }

    @Test
    public void testGetName() {
        CasaDeBurrito my_casa = createSampledCase(10);
        assertEquals("Falafel", my_casa.getName());
    }

    @Test
    public void testDistance() {
        CasaDeBurrito my_casa = createSampledCase(10);
        assertEquals(250, my_casa.distance());
    }

    @Test
    public void testIsRatedBy() {
        CasaDeBurrito my_casa = createSampledCase(10);
        Profesor p = new ProfesorImpl(999, "Alex");
        Profesor same_p = new ProfesorImpl(999, "Alex");
        Profesor different_p = new ProfesorImpl(111, "Boas");

        assertFalse(my_casa.isRatedBy(p));
        try {
            my_casa.rate(p, 2);
        } catch (CasaDeBurrito.RateRangeException e) {
            fail();
        }
        assertTrue(my_casa.isRatedBy(p));
        assertTrue(my_casa.isRatedBy(same_p));

        assertFalse(my_casa.isRatedBy(different_p));
        try {
            my_casa.rate(different_p, 2);
        } catch (CasaDeBurrito.RateRangeException e) {
            fail();
        }
        assertTrue(my_casa.isRatedBy(same_p));
        assertTrue(my_casa.isRatedBy(different_p));
    }

    @Test
    public void testRate() {
        CasaDeBurrito my_casa = createSampledCase(10);
        Profesor p1 = new ProfesorImpl(111, "Alex1");
        Profesor p2 = new ProfesorImpl(222, "Alex2");
        Profesor p3 = new ProfesorImpl(333, "Alex3");
        Profesor p4 = new ProfesorImpl(444, "Alex4");
        Profesor p5 = new ProfesorImpl(555, "Alex5");
        Profesor p6 = new ProfesorImpl(666, "Alex6");
        Profesor p7 = new ProfesorImpl(777, "Alex7");

        try {
            my_casa.rate(p1, 0);
            my_casa.rate(p2, 1);
            my_casa.rate(p3, 2);
            my_casa.rate(p4, 3);
            my_casa.rate(p5, 4);
            my_casa.rate(p6, 5);
        } catch (CasaDeBurrito.RateRangeException e) {
            fail();
        }

        try {
            my_casa.rate(p7, -1);
            fail();
        } catch (CasaDeBurrito.RateRangeException e) {
            ;
        }
        try {
            my_casa.rate(p7, 6);
            fail();
        } catch (CasaDeBurrito.RateRangeException e) {
            ;
        }
    }

    @Test
    public void testNumberOfRates() {
        CasaDeBurrito my_casa = createSampledCase(10);
        Profesor p1 = new ProfesorImpl(111, "Alex1");
        Profesor p2 = new ProfesorImpl(222, "Alex2");

        assertEquals(0, my_casa.numberOfRates());
        try {
            my_casa.rate(p1, 2);
            assertEquals(1, my_casa.numberOfRates());
            my_casa.rate(p2, 2);
            assertEquals(2, my_casa.numberOfRates());
            my_casa.rate(p1, 2);
            assertEquals(2, my_casa.numberOfRates());
        } catch (CasaDeBurrito.RateRangeException e) {
            fail();
        }
    }

    @Test
    public void testAverageRating() {
        CasaDeBurrito my_casa = createSampledCase(10);
        Profesor p1 = new ProfesorImpl(111, "Alex1");
        Profesor p2 = new ProfesorImpl(222, "Alex2");

        assertEquals(0, my_casa.averageRating(), 0);
        try {
            my_casa.rate(p1, 3);
            assertEquals(3, my_casa.averageRating(), 0);
            my_casa.rate(p2, 4);
            assertEquals(3.5, my_casa.averageRating(), 0);
        } catch (CasaDeBurrito.RateRangeException e) {
            fail();
        }
    }

    @Test
    public void testEquals() {
        CasaDeBurrito my_casa = createSampledCase(10);
        CasaDeBurrito same_casa = createSampledCase(10);
        CasaDeBurrito different_casa = createSampledCase(20);

        assertTrue(my_casa.equals(my_casa));
        assertTrue(my_casa.equals(same_casa));
        assertTrue(same_casa.equals(my_casa));
        assertFalse(my_casa.equals(different_casa));
        assertFalse(different_casa.equals(my_casa));
        assertFalse(my_casa.equals(null));
    }

    @Test
    public void testToString() {
        Set<String> menu = new HashSet<>();
        menu.add("b");
        menu.add("c");
        menu.add("a");
        CasaDeBurrito special_casa = new CasaDeBurritoImpl(90, "Special", 180, menu);
        assertEquals("" +
                "CasaDeBurrito: Special.\n" +
                "Id: 90.\n" +
                "Distance: 180.\n" +
                "Menu: a, b, c.", special_casa.toString());
    }

    @Test
    public void testCompareTo() {
        CasaDeBurrito my_casa = createSampledCase(10);
        CasaDeBurrito same_casa = createSampledCase(10);
        CasaDeBurrito greater_casa = createSampledCase(20);

        assertEquals(0, my_casa.compareTo(my_casa));
        assertEquals(0, my_casa.compareTo(same_casa));
        assertTrue(my_casa.compareTo(greater_casa) < 0);
        assertTrue(greater_casa.compareTo(my_casa) > 0);
    }

    @Test
    public void testDeepCopy() {
        Integer id = 10;
        String name = "name";
        Integer dist = 250;
        Set<String> menu = new HashSet<>();
        menu.add("a");
        menu.add("b");
        CasaDeBurrito my_casa = new CasaDeBurritoImpl(id, name, dist, menu);

        id = 99;
        name = "new_name";
        dist = 50;
        menu.add("c");
        assertEquals("" +
                "CasaDeBurrito: name.\n" +
                "Id: 10.\n" +
                "Distance: 250.\n" +
                "Menu: a, b.", my_casa.toString());
    }

}
