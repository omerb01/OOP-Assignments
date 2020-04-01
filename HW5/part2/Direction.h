//
// Created by Oran Nahoum on 2020-01-12.
//

#ifndef OOP_ASSIGNMENT5_DIRECTION_H
#define OOP_ASSIGNMENT5_DIRECTION_H

enum Direction {
    UP = 0,
    DOWN = 1,
    LEFT = 2,
    RIGHT = 3,

    INVALID = 420 // for testing and invalid value
};

/// Helper struct to compare Left/Right and Up/Down, will give each couple same value.
template <Direction d>
struct DirectionVal {
    constexpr static int val = -1;
};

template <>
struct DirectionVal<UP> {
    constexpr static int val = 1;
};

template <>
struct DirectionVal<DOWN> {
    constexpr static int val = 1;
};

template <>
struct DirectionVal<LEFT> {
    constexpr static int val = 2;
};

template <>
struct DirectionVal<RIGHT> {
    constexpr static int val = 2;
};

#endif //OOP_ASSIGNMENT5_DIRECTION_H
