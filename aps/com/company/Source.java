package com.company;

import com.company.models.Order;

public class Source {
    private int generatedOrdersCount = 0;
    private int rejectedOrdersCount = 0;

    private final int number;
    private double prevTime;

    public Source(int number) {
        this.number = number;
        prevTime = 0;
    }

    public void reject() { rejectedOrdersCount++; }

    public Order generate() {
        double time =1+ prevTime + ( ((-1) / Main.lambda) * Math.log(Math.random())); // Пуассоновский закон
        prevTime = time;

        Statistic.generateOrder();
        generatedOrdersCount++;

        Order order = new Order(generatedOrdersCount, number, time);

        if (Main.isStepMode()) {
            System.out.println("Создание заказа " + order.getSourceNumber() + "." + order.getNumber()+ "   T - " + order.stat.generationT);
        }

        return order;
    }

    public int getNumber() {
        return number;
    }

    public int generatedCount() {
        return generatedOrdersCount;
    }

    public int rejectedCount() {
        return rejectedOrdersCount;
    }
}
