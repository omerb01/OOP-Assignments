//
// Created by Oran Nahoum on 2020-01-12.
//

#ifndef OOP_ASSIGNMENT5_LIST_H
#define OOP_ASSIGNMENT5_LIST_H

//////////////////////// List init

template<typename... TT>
struct List {
    constexpr static int size = 0;
};

template<typename T>
struct List<T> {
    typedef T head;
    constexpr static int size = 1;
};

template<typename T, typename... TT>
struct List<T, TT...> {
    typedef T head;
    typedef List<TT...> next;
    constexpr static int size = sizeof...(TT) + 1;

};

//////////////////////// PrependList

template<typename U, typename T>
struct PrependList {
};

template<typename U, typename ...T>
struct PrependList<U, List<T...>> {
    typedef List<U, T...> list;
};

//////////////////////// GetAtIndex

template<int N, typename T>
struct GetAtIndex {
    typedef T value;
};

template<int N, typename T, typename... TT>
struct GetAtIndex<N, List<T, TT...>> {
    typedef typename GetAtIndex<N - 1, List<TT...>>::value value;
};

template<typename T, typename... TT>
struct GetAtIndex<0, List<T, TT...>> {
    typedef T value;
};

//////////////////////// SetAtIndex

template<int N, typename U, typename T>
struct SetAtIndex {
    typedef List<> list;
};

template<int N, typename U, typename T, typename... TT>
struct SetAtIndex<N, U, List<T, TT...>> {
    typedef typename PrependList<T, typename SetAtIndex<N - 1, U, List<TT...>>::list>::list list;
};

template<typename U, typename T, typename... TT>
struct SetAtIndex<0, U, List<T, TT...>> {
    typedef List<U, TT...> list;
};


#endif //OOP_ASSIGNMENT5_LIST_H
