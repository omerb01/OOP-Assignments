//
// Created by Omer Belhasin on 12/01/2020.
//

#ifndef OOP_ASSIGNMENT5_OBSERVER_H
#define OOP_ASSIGNMENT5_OBSERVER_H

using namespace std;

template<typename ParameterType>
class Observer {
public:
    Observer() = default;

    virtual void handleEvent(const ParameterType &parameter) = 0;
};

#endif //OOP_ASSIGNMENT5_OBSERVER_H
