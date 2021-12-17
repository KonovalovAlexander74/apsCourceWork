package com.company.managers;

import com.company.Buffer;
import com.company.Main;
import com.company.Statistic;
import com.company.models.Order;

public class BufferManager {
    private final Buffer buffer;

    public BufferManager(Buffer buffer) {
        this.buffer = buffer;
    }

    public boolean acceptOrder(Order order) {
        if (buffer.isFree()) {
            Statistic.acceptOrder();
            buffer.addOrder(order);
            return true;
        } else {
            order.stat.rejectT = Main.systemT;
            Statistic.rejectOrder();
            if (Main.isStepMode()) {
                System.out.println("Отказ " + order.getSourceNumber() + "." + order.getNumber());
            }
            return false;
        }
    }
}
