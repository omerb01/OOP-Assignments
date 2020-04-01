//
// Created by Omer Belhasin on 12/01/2020.
//

#ifndef OOP_ASSIGNMENT5_SUBJECT_H
#define OOP_ASSIGNMENT5_SUBJECT_H

#include <vector>
#include "Observer.h"
#include "OOP5EventException.h"
#include <algorithm>

using std::vector;

template<typename ParameterType>
class Subject {
    vector<Observer<ParameterType> *> observers;

public:
    Subject() = default;

    void notify(const ParameterType &parameter) {
        for (Observer<ParameterType> *observer : observers) observer->handleEvent(parameter);
    }

    void addObserver(Observer<ParameterType> &observer) {
        auto it = std::find(observers.begin(), observers.end(), &observer);
        if (it != observers.end()) throw ObserverAlreadyKnownToSubject();
        observers.push_back(&observer);
    }

    void removeObserver(Observer<ParameterType> &observer) {
        auto it = std::find(observers.begin(), observers.end(), &observer);
        if (it == observers.end()) throw ObserverUnknownToSubject();
        observers.erase(it);
    }

    Subject<ParameterType> &operator+=(Observer<ParameterType> &observer) {
        addObserver(observer);
        return *this;
    }

    Subject<ParameterType> &operator-=(Observer<ParameterType> &observer) {
        removeObserver(observer);
        return *this;
    }

    Subject<ParameterType> &operator()(const ParameterType &parameter) {
        notify(parameter);
        return *this;
    }

};

#endif //OOP_ASSIGNMENT5_SUBJECT_H
