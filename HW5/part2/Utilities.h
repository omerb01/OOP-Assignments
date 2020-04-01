//
// Created by Oran Nahoum on 2020-01-12.
//

#ifndef OOP_ASSIGNMENT5_UTILITIES_H
#define OOP_ASSIGNMENT5_UTILITIES_H

#include "Direction.h"

//////////////////////// Conditional

template<bool b, typename T, typename U>
struct Conditional {};

template<typename T, typename U>
struct Conditional<true,T,U> {
    typedef T value;
};

template<typename T, typename U>
struct Conditional<false,T,U> {
    typedef U value;
};

//////////////////////// ConditionalInteger

template<bool b, int I1, int I2>
struct ConditionalInteger {};

template<int I1, int I2>
struct ConditionalInteger<true,I1,I2> {
    constexpr static int value = I1 ;
};

template<int I1, int I2>
struct ConditionalInteger<false,I1,I2> {
    constexpr static int value = I2 ;
};

//////////////////////// ConditionalBool

template<bool b, bool b1, bool b2>
struct ConditionalBool {};

template<bool b1, bool b2>
struct ConditionalBool<true,b1,b2> {
    constexpr static bool value = b1 ;
};

template<bool b1, bool b2>
struct ConditionalBool<false,b1,b2> {
    constexpr static bool value = b2 ;
};

//////////////////////// ConditionalDirection

template<bool b, Direction d1, Direction d2>
struct ConditionalDirection {};

template<Direction d1, Direction d2>
struct ConditionalDirection<true,d1,d2> {
    constexpr static Direction value = d1 ;
};

template<Direction d1, Direction d2>
struct ConditionalDirection<false,d1,d2> {
    constexpr static Direction value = d2 ;
};




#endif //OOP_ASSIGNMENT5_UTILITIES_H
