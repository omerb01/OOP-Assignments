//
// Created by Oran Nahoum on 2020-01-13.
//

#ifndef OOP_ASSIGNMENT5_RUSHHOUR_H
#define OOP_ASSIGNMENT5_RUSHHOUR_H

#include "GameBoard.h"
#include "MoveVehicle.h"

////////////////////////////// CheckWinAux
template<int i, int j, int width, typename T>
struct CheckWinAux {
};

template<int i, int j, int width, typename ...TT>
struct CheckWinAux<i, j, width, GameBoard<List<TT...>>> {
    constexpr static bool result =
            ConditionalBool<
                    GetTypeFromPosMat<i, j, GameBoard<List<TT...>>>::type != EMPTY,
                    false,
                    CheckWinAux<i, j + 1, width, GameBoard<List<TT...>>>::result
            >::value;
};

template<int i, int width, typename ...TT>
struct CheckWinAux<i, width, width, GameBoard<List<TT...>>> {
    constexpr static bool result = true;
};


////////////////////////////// CheckWin
template<typename T>
struct CheckWin {
};

template<typename... TT>
struct CheckWin<GameBoard<List<TT...>>> {
    constexpr static int x_row = GetPosFromTypeMat<X, GameBoard<List<TT...>>>::pos_row;
    constexpr static int x_col = GetPosFromTypeMat<X, GameBoard<List<TT...>>>::pos_col;
    constexpr static int length = GetPosFromTypeMat<X, GameBoard<List<TT...>>>::type_length;
    constexpr static int width = GameBoard<List<TT...>>::width;
    constexpr static bool result = CheckWinAux<x_row, x_col + length, width, GameBoard<List<TT...>>>::result;
};


////////////////////////////// ExeMove
template<typename m, typename T>
struct ExeMove {
};

template<CellType t, Direction d, int a, typename... TT>
struct ExeMove<Move<t, d, a>, GameBoard<List<TT...>>> {
    constexpr static int row = GetPosFromTypeMat<t, GameBoard<List<TT...>>>::pos_row;
    constexpr static int col = GetPosFromTypeMat<t, GameBoard<List<TT...>>>::pos_col;
    typedef typename MoveVehicle<GameBoard<List<TT...>>,row,col,d,a>::board board;
};

////////////////////////////// ExeMoves
template<typename T, typename Moves>
struct ExeMoves {
};

template<typename... TT, typename Move, typename... Moves>
struct ExeMoves<GameBoard<List<TT...>>, List<Move, Moves...>> {
    typedef typename ExeMove<Move, GameBoard<List<TT...>>>::board temp_board;
    typedef typename ExeMoves<temp_board, List<Moves...>>::board board;

};

template<typename... TT>
struct ExeMoves<GameBoard<List<TT...>>, List<>> {
    typedef GameBoard<List<TT...>> board;
};


////////////////////////////// CheckSolution
template<typename T, typename Moves>
struct CheckSolution {
};

template<typename... TT, typename... Moves>
struct CheckSolution<GameBoard<List<TT...>>, List<Moves...>> {
    typedef typename ExeMoves<GameBoard<List<TT...>>, List<Moves...>>::board final_board;
    constexpr static bool result = CheckWin<final_board>::result;
};

#endif //OOP_ASSIGNMENT5_RUSHHOUR_H
