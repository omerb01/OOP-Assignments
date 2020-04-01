//
// Created by Oran Nahoum on 2020-01-13.
//

#ifndef OOP_ASSIGNMENT5_MOVEVEHICLE_H
#define OOP_ASSIGNMENT5_MOVEVEHICLE_H

#include "GameBoard.h"
#include "Utilities.h"
#include "TransposeList.h"

/**
 * check coordinates if wrong ASSERT
 * check if coordinates are EMPTY
 *
 * Find type position
 * check if car can ride in the requested direction
 * for amount steps do :
 *  Move that type 1 step left or right (by moving just the "corner piece" to the other side (+3 for big +2 for small))
 * if at any point, there is a car in the way ASSERT
 */

//////////////////////// Move
template<CellType t, Direction d, int a>
struct Move {
    static_assert(t != EMPTY, "Trying to move EMPTY cell!");
    constexpr static int type = t;
    constexpr static int direction = d;
    constexpr static int amount = a;
};

//////////////////////// GetCellData
/// gets data from a given Board Cell
template<CellType TypeToCompare, typename>
struct GetCellData {
};

template<CellType TypeToCompare, CellType Type, Direction Dir, int Length>
struct GetCellData<TypeToCompare, BoardCell<Type, Dir, Length>> {
    constexpr static Direction type_dir = Dir;
    constexpr static int type_length = Length;
    constexpr static CellType type = Type;
};


//////////////////////// CompareCellType
/// Checks if a given cell is of a given type
template<CellType TypeToCompare, typename>
struct CompareCellType {
};

template<CellType TypeToCompare, CellType Type, Direction Dir, int Length>
struct CompareCellType<TypeToCompare, BoardCell<Type, Dir, Length>> {
    constexpr static bool res = (Type == TypeToCompare);
};

////////////////////////// GetPosFromTypeList
/// Helper function to return position of certain type in a List (Helper for Mat)
template<CellType type, typename T>
struct GetPosFromTypeList {
    constexpr static bool curr_found = 0;
    constexpr static bool found = false;
    constexpr static int pos = 0;
    constexpr static int type_length = 0;
    constexpr static Direction type_dir = INVALID;

};


template<CellType type, typename T, typename ...TT>
struct GetPosFromTypeList<type, List<T, TT...>> {
    constexpr static bool curr_found = CompareCellType<type, T>::res;
    constexpr static bool found = ConditionalBool<
            curr_found,
            true,
            GetPosFromTypeList<type, List<TT...>>::found
    >::value;
    constexpr static int pos = ConditionalInteger<
            curr_found,
            0,
            1 + GetPosFromTypeList<type, List<TT...>>::pos
    >::value;

    constexpr static int type_length = ConditionalInteger<
            curr_found,
            GetCellData<type, T>::type_length,
            GetPosFromTypeList<type, List<TT...>>::type_length
    >::value;

    constexpr static Direction type_dir = ConditionalDirection<
            curr_found,
            GetCellData<type, T>::type_dir,
            GetPosFromTypeList<type, List<TT...>>::type_dir
    >::value;


};

////////////////////////// GetPosFromTypeMat (Returns top/left corner)
/// Given the position of a given type in the Mat (pos_row, pos_col)
template<CellType type, typename T, typename... TT>
struct GetPosFromTypeMat {
    constexpr static bool curr_found = 0;
    constexpr static bool found = false;
    constexpr static int pos_row = 0;
    constexpr static int pos_col = 0;
    constexpr static int type_length = 0;
    constexpr static Direction type_dir = INVALID;

};

template<CellType type, typename T, typename... TT>
struct GetPosFromTypeMat<type, GameBoard<List<T, TT...>>> {
    constexpr static bool curr_found = GetPosFromTypeList<type, T>::found;
    constexpr static bool found = ConditionalBool<
            curr_found,
            true,
            GetPosFromTypeMat<type, GameBoard<List<TT...>>>::found
    >::value;

    constexpr static int pos_col = ConditionalInteger<
            curr_found,
            GetPosFromTypeList<type, T>::pos,
            GetPosFromTypeMat<type, GameBoard<List<TT...>>>::pos_col
    >::value;

    constexpr static int pos_row = ConditionalInteger<
            curr_found,
            0,
            1 + GetPosFromTypeMat<type, GameBoard<List<TT...>>>::pos_row
    >::value;

    constexpr static int type_length = ConditionalInteger<
            curr_found,
            GetPosFromTypeList<type, T>::type_length,
            GetPosFromTypeMat<type, GameBoard<List<TT...>>>::type_length
    >::value;

    constexpr static Direction type_dir = ConditionalDirection<
            curr_found,
            GetPosFromTypeList<type, T>::type_dir,
            GetPosFromTypeMat<type, GameBoard<List<TT...>>>::type_dir
    >::value;

};

////////////////////////// GetTypeFromPos
/// Gets the type of a certain position in the Matrix.

template<int N, typename T>
struct GetTypeValAtIndex {
    constexpr static CellType value = EMPTY;
};

template<int N, typename T, typename... TT>
struct GetTypeValAtIndex<N, List<T, TT...>> {
    constexpr static CellType value = GetTypeValAtIndex<N - 1, List<TT...>>::value;
};

template<typename T, typename... TT>
struct GetTypeValAtIndex<0, List<T, TT...>> {
    constexpr static CellType value = GetCellData<EMPTY, T>::type;
};

template<int I, int J, typename T, typename... TT>
struct GetTypeFromPosMat {
};


template<int I, int J, typename T, typename... TT>
struct GetTypeFromPosMat<I, J, GameBoard<List<T, TT...>>> {
    typedef typename GetAtIndex<I, List<T, TT...>>::value row_list;
    constexpr static CellType type = GetTypeValAtIndex<J, row_list>::value;
};

////////////////////////// SetTypeAtPos
/// Puts type in I1,J1 and puts Empty in I1,J2
template<CellType t, Direction dir, int len, int I1, int J1, int J2, typename T, typename... TT>
struct SetTypeAtPos {
};


template<CellType t, Direction dir, int len, int I1, int J1, int J2, typename T, typename... TT>
struct SetTypeAtPos<t, dir, len, I1, J1, J2, GameBoard<List<T, TT...>>> {
    typedef typename GetAtIndex<I1, List<T, TT...>>::value row_list;
    typedef typename SetAtIndex<J2, BoardCell<EMPTY, UP, 0>, row_list>::list temp_row_list;
    typedef typename SetAtIndex<J1, BoardCell<t, dir, len>, temp_row_list>::list new_row_list;
    typedef typename SetAtIndex<I1, new_row_list, List<T, TT...>>::list new_mat;
};


////////////////////////// GetPosToChange
template<CellType type, Direction d, typename T, typename... TT>
struct GetPosToChange {
};

template<CellType type, Direction d, typename T, typename... TT>
struct GetPosToChange<type, d, GameBoard<List<T, TT...>>> {
    constexpr static int top_left_row = GetPosFromTypeMat<type, GameBoard<List<T, TT...>>>::pos_row;
    constexpr static int top_left_col = GetPosFromTypeMat<type, GameBoard<List<T, TT...>>>::pos_col;
    constexpr static int length = GetPosFromTypeMat<type, GameBoard<List<T, TT...>>>::type_length;
    constexpr static int row_to_change =
            ConditionalInteger<
                    d == LEFT,
                    top_left_row,
                    ConditionalInteger<
                            d == RIGHT,
                            top_left_row,
                            ConditionalInteger<
                                    d == UP,
                                    top_left_row - 1,
                                    top_left_row + length
                            >::value
                    >::value
            >::value;

    constexpr static int col_to_change =
            ConditionalInteger<
                    d == LEFT,
                    top_left_col - 1,
                    ConditionalInteger<
                            d == RIGHT,
                            top_left_col + length,
                            ConditionalInteger<
                                    d == UP,
                                    top_left_col,
                                    top_left_col
                            >::value
                    >::value
            >::value;
};


////////////////////////// OneStepLeft (or up for Transpose)
template<CellType type, typename T, typename... TT>
struct OneStepLeft {
};

template<CellType type, typename T, typename... TT>
struct OneStepLeft<type, GameBoard<List<T, TT...>>> {
    constexpr static int top_left_row = GetPosFromTypeMat<type, GameBoard<List<T, TT...>>>::pos_row;
    constexpr static int top_left_col = GetPosFromTypeMat<type, GameBoard<List<T, TT...>>>::pos_col;
    constexpr static int length = GetPosFromTypeMat<type, GameBoard<List<T, TT...>>>::type_length;
    constexpr static Direction type_direction = GetPosFromTypeMat<type, GameBoard<List<T, TT...>>>::type_dir;
    typedef typename SetTypeAtPos<type, type_direction, length, top_left_row,
            top_left_col - 1, top_left_col - 1 + length, GameBoard<List<T, TT...>>>::new_mat new_mat;
};

////////////////////////// OneStepRight (or down for Transpose)
template<CellType type, typename T, typename... TT>
struct OneStepRight {
};

template<CellType type, typename T, typename... TT>
struct OneStepRight<type, GameBoard<List<T, TT...>>> {
    constexpr static int top_left_row = GetPosFromTypeMat<type, GameBoard<List<T, TT...>>>::pos_row;
    constexpr static int top_left_col = GetPosFromTypeMat<type, GameBoard<List<T, TT...>>>::pos_col;
    constexpr static int length = GetPosFromTypeMat<type, GameBoard<List<T, TT...>>>::type_length;
    constexpr static Direction type_direction = GetPosFromTypeMat<type, GameBoard<List<T, TT...>>>::type_dir;
    typedef typename SetTypeAtPos<type, type_direction, length, top_left_row,
            top_left_col + length, top_left_col, GameBoard<List<T, TT...>>>::new_mat new_mat;
};

////////////////////////// OneStepUp
template<CellType type, typename T, typename... TT>
struct OneStepUp {
};

template<CellType type, typename T, typename... TT>
struct OneStepUp<type, GameBoard<List<T, TT...>>> {
    typedef typename Transpose<List<T, TT...>>::matrix t_mat;
    typedef typename OneStepLeft<type, GameBoard<t_mat>>::new_mat t_new_mat;
    typedef typename Transpose<t_new_mat>::matrix new_mat;
};

////////////////////////// OneStepDown
template<CellType type, typename T, typename... TT>
struct OneStepDown {
};

template<CellType type, typename T, typename... TT>
struct OneStepDown<type, GameBoard<List<T, TT...>>> {
    typedef typename Transpose<List<T, TT...>>::matrix t_mat;
    typedef typename OneStepRight<type, GameBoard<t_mat>>::new_mat t_new_mat;
    typedef typename Transpose<t_new_mat>::matrix new_mat;
};

////////////////////////// MoveVehicleOnce
template<CellType type, Direction d, typename T, typename... TT>
struct MoveVehicleOnce {
};

template<CellType type, Direction d, typename T, typename... TT>
struct MoveVehicleOnce<type, d, GameBoard<List<T, TT...>>> {

    constexpr static Direction type_direction = GetPosFromTypeMat<type, GameBoard<List<T, TT...>>>::type_dir;
    static_assert(DirectionVal<d>::val == DirectionVal<type_direction>::val,
                  "Trying to move a vehicle in a wrong direction");
    constexpr static int row_to_change = GetPosToChange<type, d, GameBoard<List<T, TT...>>>::row_to_change;
    constexpr static int col_to_change = GetPosToChange<type, d, GameBoard<List<T, TT...>>>::col_to_change;
    constexpr static CellType type_of_dst_cell = GetTypeFromPosMat<row_to_change,
            col_to_change, GameBoard<List<T, TT...>>>::type;
    static_assert(type_of_dst_cell == EMPTY, "Another vehicle is blocking the road !");

    typedef typename Conditional<
            d == LEFT,
            typename OneStepLeft<type, GameBoard<List<T, TT...>>>::new_mat,
            typename Conditional<
                    d == RIGHT,
                    typename OneStepRight<type, GameBoard<List<T, TT...>>>::new_mat,
                    typename Conditional<
                            d == UP,
                            typename OneStepUp<type, GameBoard<List<T, TT...>>>::new_mat,
                            typename OneStepDown<type, GameBoard<List<T, TT...>>>::new_mat
                    >::value
            >::value
    >::value new_mat;

};

//////////////////////////// MoveVehicleAux
template<CellType type, Direction d, int A, typename... TT>
struct MoveVehicleAux {
    typedef GameBoard<List<>> board;
};

template<CellType type, Direction d, int A, typename... TT>
struct MoveVehicleAux<type, d, A, GameBoard<List<TT...>>> {
    typedef typename MoveVehicleOnce<type, d, GameBoard<List<TT...>>>::new_mat temp_mat;
    typedef typename MoveVehicleAux<type, d, A - 1, GameBoard<temp_mat> >::board board;
};

template<CellType type, Direction d, typename... TT>
struct MoveVehicleAux<type, d, 0, GameBoard<List<TT...>>> {
    typedef GameBoard<List<TT...>> board;
};

////////////////////////////// MoveVehicle

template<typename T, int R, int C, Direction d, int A>
struct MoveVehicle {
};

template<typename... TT, int R, int C, Direction d, int A>
struct MoveVehicle<GameBoard<List<TT...>>, R, C, d, A> {
    static_assert(R >= 0 && R < GameBoard<List<TT...>>::length && C >= 0 && C < GameBoard<List<TT...>>::width,
                  "Bad coordinates");
    constexpr static CellType type = GetTypeFromPosMat<R, C, GameBoard<List<TT...>>>::type;
    static_assert(type != EMPTY, "Trying to move an empty cell");
    typedef typename MoveVehicleAux<type, d, A, GameBoard<List<TT...>>>::board board;

};


#endif //OOP_ASSIGNMENT5_MOVEVEHICLE_H
