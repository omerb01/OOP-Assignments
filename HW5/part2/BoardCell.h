//
// Created by Oran Nahoum on 2020-01-12.
//

#ifndef OOP_ASSIGNMENT5_BOARDCELL_H
#define OOP_ASSIGNMENT5_BOARDCELL_H

#include "CellType.h"
#include "Direction.h"

template<CellType t, Direction d, int l>
struct BoardCell {
    constexpr static int type = t;
    constexpr static int direction = d;
    constexpr static int length = l;
};

#endif //OOP_ASSIGNMENT5_BOARDCELL_H
