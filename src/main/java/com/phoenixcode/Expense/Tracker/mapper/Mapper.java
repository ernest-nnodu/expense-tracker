package com.phoenixcode.Expense.Tracker.mapper;

public interface Mapper<A,B> {

    A mapTo(B b);

    B mapFrom(A a);
}
