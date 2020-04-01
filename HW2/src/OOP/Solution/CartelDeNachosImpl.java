package OOP.Solution;

import OOP.Provided.CartelDeNachos;
import OOP.Provided.CasaDeBurrito;
import OOP.Provided.Profesor;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CartelDeNachosImpl implements CartelDeNachos {
    private Set<Profesor> profesors;
    private Set<CasaDeBurrito> casas;

    public CartelDeNachosImpl() {
        profesors = new HashSet<>();
        casas = new HashSet<>();
    }

    @Override
    public Profesor joinCartel(int id, String name) throws Profesor.ProfesorAlreadyInSystemException {
        Profesor p = new ProfesorImpl(id, name);
        if (profesors.contains(p)) throw new Profesor.ProfesorAlreadyInSystemException();
        profesors.add(p);
        return p;
    }

    @Override
    public CasaDeBurrito addCasaDeBurrito(int id, String name, int dist, Set<String> menu) throws CasaDeBurrito.CasaDeBurritoAlreadyInSystemException {
        CasaDeBurrito c = new CasaDeBurritoImpl(id, name, dist, menu);
        if (casas.contains(c)) throw new CasaDeBurrito.CasaDeBurritoAlreadyInSystemException();
        casas.add(c);
        return c;
    }

    @Override
    public Collection<Profesor> registeredProfesores() {
        return new HashSet<>(profesors);
    }

    @Override
    public Collection<CasaDeBurrito> registeredCasasDeBurrito() {
        return new HashSet<>(casas);
    }

    @Override
    public Profesor getProfesor(int id) throws Profesor.ProfesorNotInSystemException {
        Profesor p = new ProfesorImpl(id, "temp");
        if (!profesors.contains(p)) throw new Profesor.ProfesorNotInSystemException();
        return profesors.stream().filter(curr -> curr.equals(p)).findAny().orElse(null);
    }

    @Override
    public CasaDeBurrito getCasaDeBurrito(int id) throws CasaDeBurrito.CasaDeBurritoNotInSystemException {
        CasaDeBurrito c = new CasaDeBurritoImpl(id, "temp", 0, new HashSet<>());
        if (!casas.contains(c)) throw new CasaDeBurrito.CasaDeBurritoNotInSystemException();
        return casas.stream().filter(curr -> curr.equals(c)).findAny().orElse(null);
    }

    @Override
    public CartelDeNachos addConnection(Profesor p1, Profesor p2) throws Profesor.ProfesorNotInSystemException, Profesor.ConnectionAlreadyExistsException, Profesor.SameProfesorException {
        Profesor inner_p1 = getProfesor(p1.getId());
        Profesor inner_p2 = getProfesor(p2.getId());
        if (p1.equals(p2)) throw new Profesor.SameProfesorException();
        inner_p1.addFriend(inner_p2);
        inner_p2.addFriend(inner_p1);
        return this;
    }

    @Override
    public Collection<CasaDeBurrito> favoritesByRating(Profesor p) throws Profesor.ProfesorNotInSystemException {
        Profesor inner_p = getProfesor(p.getId());
        return inner_p.getFriends().stream()
                .sorted()
                .map(curr_p -> curr_p.favoritesByRating(0))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CasaDeBurrito> favoritesByDist(Profesor p) throws Profesor.ProfesorNotInSystemException {
        Profesor inner_p = getProfesor(p.getId());
        return inner_p.getFriends().stream()
                .sorted()
                .map(curr_p -> curr_p.favoritesByDist(Integer.MAX_VALUE))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    private static boolean getRecommendationRec(Profesor p, CasaDeBurrito c, int t) {
        if (t == 0) return p.favorites().contains(c);
        if (p.favorites().contains(c)) return true;
        for (Profesor curr : p.getFriends()) {
            return getRecommendationRec(curr, c, t - 1);
        }
        return false;
    }

    @Override
    public boolean getRecommendation(Profesor p, CasaDeBurrito c, int t) throws Profesor.ProfesorNotInSystemException, CasaDeBurrito.CasaDeBurritoNotInSystemException, ImpossibleConnectionException {
        Profesor inner_p = getProfesor(p.getId());
        CasaDeBurrito inner_c = getCasaDeBurrito(c.getId());
        if (t < 0) throw new ImpossibleConnectionException();
        return getRecommendationRec(inner_p, inner_c, t);
    }

    @Override
    public List<Integer> getMostPopularRestaurantsIds() {
        Map<Integer, Integer> scores = new Hashtable<>();
        casas.forEach(c -> scores.put(c.getId(), 0));

        for (CasaDeBurrito c : casas) {
            for (Profesor p : profesors) {
                if (c.isRatedBy(p)) {
                    int prev = scores.get(c.getId());
                    scores.put(c.getId(), ++prev);
                }
            }
        }
        int max_score = scores.values().stream().reduce(0, Integer::max);
        return scores.entrySet().stream()
                .filter(entry -> entry.getValue() == max_score)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        String ps = profesors.stream()
                .sorted()
                .map(Profesor::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        String cs = casas.stream()
                .sorted()
                .map(CasaDeBurrito::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        String detailed_ps = profesors.stream()
                .sorted()
                .map(p -> String.valueOf(p.getId()) + " -> " + p.getFriends().stream()
                        .sorted()
                        .map(Profesor::getId)
                        .map(String::valueOf)
                        .collect(Collectors.toList())
                        .toString())
                .collect(Collectors.joining(".\n"));
        return "Registered profesores: " + ps + ".\n"
                + "Registered casas de burrito: " + cs + ".\n"
                + "Profesores:\n"
                + detailed_ps + ".\n"
                + "End profesores.";
    }
}
