package OOP.Provided;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Each instance of the Profesor class has an id, a name, and holds
 * a collection of favorite casas de burrito, and a collection of the profesor's friends.
 * The id is unique for every profesor.
 * */
public interface Profesor extends Comparable<Profesor> {

    class SameProfesorException                  extends Exception {}
    class ProfesorAlreadyInSystemException       extends Exception {}
    class ProfesorNotInSystemException           extends Exception {}
    class UnratedFavoriteCasaDeBurritoException  extends Exception {}
    class ConnectionAlreadyExistsException       extends Exception {}

    /**
     * @return the id of the profesor.
     * */
    int getId();

    /**
     * the profesor favorites a casa de burrito
     *
     * @return the object to allow concatenation of function calls.
     * @param c - the casa de burrito being favored by the profesor
     * */
    Profesor favorite(CasaDeBurrito c)
            throws UnratedFavoriteCasaDeBurritoException;

    /**
     * @return the profesor's favorite casas de burrito
     * */
    Collection<CasaDeBurrito> favorites();

    /**
     * adding a profesor as a friend
     * @return the object to allow concatenation of function calls.
     * @param p - the profesor being "friend-ed"
     * */
    Profesor addFriend(Profesor p)
            throws SameProfesorException, ConnectionAlreadyExistsException;

    /**
     * @return the profesor's set of friends
     * */
    Set<Profesor> getFriends();

    /**
     * @return the profesor's set of friends, filtered by a predicate
     * @param p - the predicate for filtering
     * */
    Set<Profesor> filteredFriends(Predicate<Profesor> p);

    /**
     * @return the profesor's favorite casas de burrito, ordered by a Comparator, and filtered by a predicate.
     * @param comp - a comparator for ordering
     * @param p - a predicate for filtering
     * */
    Collection<CasaDeBurrito> filterAndSortFavorites(Comparator<CasaDeBurrito> comp, Predicate<CasaDeBurrito> p);

    /**
     * @return the profesor's favorite casas de burrito, ordered by rating.
     * @param rLimit - the limit of rating under which casas de burrito will not be included.
     * */
    Collection<CasaDeBurrito> favoritesByRating(int rLimit);

    /**
     * @return the profesor's favorite casas de burrito, ordered by distance and then rating.
     * @param dLimit - the limit of distance above which casas de burrito will not be included.
     * */
    Collection<CasaDeBurrito> favoritesByDist(int dLimit);

    /**
     * @return the profesors's description as a string in the following format:
     * <format>
     * Profesor: <name>.
     * Id: <id>.
     * Favorites: <casaName1, casaName2, casaName3...>
     * </format>
     * Note: favorite casas de burrito are ordered by lexicographical order, asc.
     *
     * Example:
     *
     * Profesor: Oren.
     * Id: 236703.
     * Favorites: BBB, Burger salon.
     *
     * */

    String toString();
}
