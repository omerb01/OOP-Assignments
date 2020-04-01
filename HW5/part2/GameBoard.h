//
// Created by Oran Nahoum on 2020-01-12.
//

#ifndef OOP_ASSIGNMENT5_GAMEBOARD_H
#define OOP_ASSIGNMENT5_GAMEBOARD_H

#include "BoardCell.h"
#include "List.h"

template<typename T, typename... TT>
struct GameBoard {
};

template<typename T, typename... TT>
struct GameBoard<List<T, TT...>> {
    typedef List<T, TT...> board;
    constexpr static int width = T::size;
    constexpr static int length = List<T,TT...>::size;
};


#endif //OOP_ASSIGNMENT5_GAMEBOARD_H
