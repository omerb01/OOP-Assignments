package OOP.Solution;

import OOP.Provided.CasaDeBurrito;
import OOP.Provided.Profesor;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CasaDeBurritoImpl implements CasaDeBurrito {
    private int id;
    private String name;
    private int dist;
    private Set<String> menu;
    private Hashtable<Integer, Integer> rates = new Hashtable<Integer, Integer>();

    public CasaDeBurritoImpl(int id, String name, int dist, Set<String> menu) {
        this.id = id;
        this.name = name;
        this.dist = dist;
        this.menu = new HashSet<>(menu);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int distance() {
        return dist;
    }

    @Override
    public boolean isRatedBy(Profesor p) {
        return rates.containsKey(p.getId());
    }

    @Override
    public CasaDeBurrito rate(Profesor p, int r) throws RateRangeException {
        if (r < 0 || r > 5) throw new RateRangeException();
        rates.put(p.getId(), r);
        return this;
    }

    @Override
    public int numberOfRates() {
        return rates.size();
    }

    @Override
    public double averageRating() {
        if (rates.size() == 0) return 0;
        int sum = rates.values().stream().reduce(0, Integer::sum);
        return (double) sum / (double) rates.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CasaDeBurrito)) return false;
        return this.id == ((CasaDeBurrito) obj).getId();
    }

    @Override
    public int hashCode() {
        return this.getId();
    }

    @Override
    public String toString() {
        return "CasaDeBurrito: " + this.name + ".\n" +
                "Id: " + this.id + ".\n" +
                "Distance: " + this.dist + ".\n" +
                "Menu: " + this.menu.stream().sorted().collect(Collectors.joining(", ")) + ".";
    }

    @Override
    public int compareTo(CasaDeBurrito casaDeBurrito) {
        return this.id - casaDeBurrito.getId();
    }
}
