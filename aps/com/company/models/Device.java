package com.company.models;

import com.company.Main;
import com.company.Statistic;

public class Device {
    private final int number;
    private Order order;
    private double deviceCompletionT;

    public Device(int number) {
        this.number = number;
        this.order = null;
    }

    public void add(Order order) {
        this.order = order;
        this.order.stat.deviceAddT = Main.systemT;
        this.order.stat.deviceDeleteT = 1+ this.order.stat.deviceAddT + ((Main.beta - Main.alpha) * Math.random() + Main.alpha);
        if (Main.isStepMode()) {
            System.out.print("П" + number + " постановка  " + this.order.getSourceNumber() + "." + this.order.getNumber());
            System.out.println("    T - " + this.order.stat.deviceAddT);
        }
    }

    public void delete() {
        order.stat.completionT = order.stat.deviceDeleteT - order.stat.deviceAddT;
        deviceCompletionT += order.stat.completionT;
        if (Main.isStepMode()) {
            System.out.print("П" + number + " выполнен  " + order.getSourceNumber() + "." + order.getNumber());
            System.out.println("    T - " + order.stat.deviceDeleteT);
        }
        Statistic.completeOrder();

        order = null;
    }

    public void isDone() { // Закончил ли прибор обрабатывать текущий заказ
        if (order != null && Main.systemT >= order.stat.deviceDeleteT) {
            delete();
        }
    }

    public boolean isAvailable() {
        isDone();
        return (order == null);
    }

    public int getNumber() {
        return number;
    }

    public Order getOrder() {
        return order;
    }

    public double getCompletionT() { return deviceCompletionT; }
}
