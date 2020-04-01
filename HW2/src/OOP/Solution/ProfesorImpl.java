package OOP.Solution;

import OOP.Provided.CasaDeBurrito;
import OOP.Provided.Profesor;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProfesorImpl implements Profesor {

    private int id;
    private String name;
    private Set<CasaDeBurrito> favorites;
    private Set<Profesor> friends;

    private static Comparator<CasaDeBurrito> CompareByRating = (CasaDeBurrito c1, CasaDeBurrito c2) -> {
        double diff1 = c2.averageRating() - c1.averageRating();
        if (diff1 != 0) {
            if (diff1 > 0) return 1;
            return -1;
        }
        int diff2 = c1.distance() - c2.distance();
        if (diff2 != 0) {
            return diff2;
        }
        return c1.getId() - c2.getId();
    };

    private static Comparator<CasaDeBurrito> CompareByDistance = (CasaDeBurrito c1, CasaDeBurrito c2) -> {
        int diff1 = c1.distance() - c2.distance();
        if (diff1 != 0) {
            return diff1;
        }
        double diff2 = c2.averageRating() - c1.averageRating();
        if (diff2 != 0) {
            if (diff2 > 0) return 1;
            return -1;
        }
        return c1.getId() - c2.getId();
    };


    public ProfesorImpl(int id, String name) {
        this.id = id;
        this.name = name;
        this.favorites = new HashSet<>();
        this.friends = new HashSet<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Profesor favorite(CasaDeBurrito c) throws UnratedFavoriteCasaDeBurritoException {
        if (!c.isRatedBy(this)) {
            throw new UnratedFavoriteCasaDeBurritoException();
        }
        this.favorites.add(c);
        return this;
    }

    @Override
    public Collection<CasaDeBurrito> favorites() {
        return new HashSet<>(this.favorites);
    }

    @Override
    public Profesor addFriend(Profesor p) throws SameProfesorException, ConnectionAlreadyExistsException {
        if (this.equals(p)) {
            throw new SameProfesorException();
        }

        if (friends.contains(p)) {
            throw new ConnectionAlreadyExistsException();
        }

        friends.add(p);

        return this;
    }

    @Override
    public Set<Profesor> getFriends() {
        return new HashSet<>(this.friends);
    }

    @Override
    public Set<Profesor> filteredFriends(Predicate<Profesor> p) {
        return this.friends.stream().filter(p).collect(Collectors.toSet());
    }

    @Override
    public Collection<CasaDeBurrito> filterAndSortFavorites(Comparator<CasaDeBurrito> comp, Predicate<CasaDeBurrito> p) {
        return this.favorites.stream().filter(p).sorted(comp).collect(Collectors.toList());
    }

    @Override
    public Collection<CasaDeBurrito> favoritesByRating(int rLimit) {
        Predicate<CasaDeBurrito> AboveRank = (CasaDeBurrito c1) -> c1.averageRating() >= rLimit;
        return this.filterAndSortFavorites(CompareByRating, AboveRank);
    }

    @Override
    public Collection<CasaDeBurrito> favoritesByDist(int dLimit) {
        Predicate<CasaDeBurrito> CloserThan = (CasaDeBurrito c1) -> c1.distance() <= dLimit;
        return this.filterAndSortFavorites(CompareByDistance, CloserThan);
    }

    @Override
    public int compareTo(Profesor o) {
        return this.getId() - o.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Profesor)) return false;
        return ((Profesor) obj).getId() == this.getId();
    }

    @Override
    public int hashCode() {
        return this.getId();
    }

    @Override
    public String toString() {
        return "Profesor: " + this.name + ".\n" +
                "Id: " + this.id + ".\n" +
                "Favorites: " + this.favorites().stream().map(CasaDeBurrito::getName).sorted().collect(Collectors.joining(", ")) + ".";

    }
}
