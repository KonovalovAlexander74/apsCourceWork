package com.company.models;

public class Order {
    private final int number;
    private final int sourceNumber;
    public int packageNumber = -2;

    public Stat stat;

    public Order(int number, int sourceNumber, double generationT) {
        stat = new Stat();
        this.number = number;
        this.sourceNumber = sourceNumber;
        stat.generationT = generationT;
    }

    public int getSourceNumber() {
        return sourceNumber;
    }

    public int getNumber() {
        return number;
    }
}
