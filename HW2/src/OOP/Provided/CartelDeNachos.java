package OOP.Provided;

import java.util.Collection;
import OOP.Provided.Profesor.*;
import OOP.Provided.CasaDeBurrito.*;

import java.util.List;
import java.util.Set;

/**
 * Each instance of the CartelDeNachos class has holds a collection of registered profesores,
 * a collection of registered casas de burrito, and manages different relations between
 * the two.
 * */
public interface CartelDeNachos {

    class ImpossibleConnectionException     extends Exception {}

    /**
     * add a profesor to the cartel.
     *
     * @param id - the id of the profesor
     * @param name - the name of the profesor
     * @return the Profesor added
     * */
    Profesor joinCartel(int id, String name)
            throws ProfesorAlreadyInSystemException;

    /**
     * add a casa de burrito to the cartel
     * @param id - the id of the casa de burrito
     * @param name - the name of the casa de burrito
     * @param dist - the distance of the casa de burrito from the Technion
     * @param menu - the set of menu items of the casa de burrito
     * @return the CasaDeBurrito added
     * */
    CasaDeBurrito addCasaDeBurrito(int id, String name, int dist, Set<String> menu)
            throws CasaDeBurritoAlreadyInSystemException;

    /**
     * @return a collection of all profesores in the cartel
     * */
    Collection<Profesor> registeredProfesores();

    /**
     * @return a collection of all casas de burrito in the cartel
     * */
    Collection<CasaDeBurrito> registeredCasasDeBurrito();

    /**
     * @return the profesor in the cartel by the id given
     * @param id - the id of the profesor to look for in the cartel
     * */
    Profesor getProfesor(int id)
            throws ProfesorNotInSystemException;

    /**
     * @return the casa de burrito in the cartel by the id given
     * @param id - the id of the casa de burrito to look for in the cartel
     * */
    CasaDeBurrito getCasaDeBurrito(int id)
            throws CasaDeBurritoNotInSystemException;

    /**
     * add a connection of friendship between the two profesores received.
     * friendship is a symmetric relation!
     *
     * @return the object to allow concatenation of function calls.
     * @param p1 - the first profesor
     * @param p2 - the second profesor
     * */
    CartelDeNachos addConnection(Profesor p1, Profesor p2)
            throws ProfesorNotInSystemException, ConnectionAlreadyExistsException, SameProfesorException;

    /**
     * returns a collection of casas de burrito favored by the friends of the received profesor,
     * ordered by rating
     *
     * @param p - the profesor whom in relation to him, favored casas de burrito by his friends are considered
     * */
    Collection<CasaDeBurrito> favoritesByRating(Profesor p)
            throws ProfesorNotInSystemException;

    /**
     * returns a collection of casas de burrito favored by the friends of the received profesor,
     * ordered by distance from the Technion
     *
     * @param p - the profesor whom in relation to him, favored casas de burrito by his friends are considered
     * */
    Collection<CasaDeBurrito> favoritesByDist(Profesor p)
            throws ProfesorNotInSystemException;

    /**
     * check whether the casa de burrito received is t-recommended by the received profesor (definition in the PDF)
     *
     * @param p - the profesor (dis)recommending the casa de burrito
     * @param c - the casa de burrito being (dis)recommended
     * @param t - the limit in the t-recommendation
     *
     * @return whether s t-recommends r
     * */
    boolean getRecommendation(Profesor p, CasaDeBurrito c, int t)
            throws ProfesorNotInSystemException, CasaDeBurritoNotInSystemException, ImpossibleConnectionException;

    /**
     * @return a list of the most popular casas-de-burrito's ids in the cartel.
     * */
    List<Integer> getMostPopularRestaurantsIds();

    /**
     * @return the cartel's description as a string in the following format:
     * <format>
     * Registered profesores: <profesorId1, profesorId2, profesorId3...>.
     * Registered casas de burrito: <casaId1, casaId2, casaId3...>.
     * Profesores:
     * <profesor1Id> -> [<friend1Id, friend2Id, friend3Id...>].
     * <profesor2Id> -> [<friend1Id, friend2Id, friend3Id...>].
     * ...
     * End profesores.
     * </format>
     * Note: profesores, casas de burrito and friends' ids are ordered by natural integer order, asc.*
     * Example:
     *
     * Registered profesores: 1, 236703, 555555.
     * Registered casas de burrito: 12, 13.
     * Profesores:
     * 1 -> [236703, 555555555].
     * 236703 -> [1].
     * 555555 -> [1].
     * End profesores.
     * */
    String toString();
}
