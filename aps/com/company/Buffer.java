package com.company;

import com.company.models.Order;

import java.util.*;

public class Buffer {
    private final ArrayList<Order> orders;
    private final int size;

    public Buffer(int size) {
        this.size = size;
        orders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        orders.add(order);
        if (Main.isStepMode()) {
            System.out.println("Добавление в Буфер " + order.getSourceNumber() + "." + order.getNumber());
        }
        order.stat.bufferAddT = Main.systemT;
    }

    public Order getOrder(int packageNumber) {
        if (orders.isEmpty()) { return null; }

        if (orders.size() == 1) { return orders.remove(0); }

        Order order = null;

        for (Order ord : orders) { // Находим заказ из пакета
            if (ord.packageNumber == packageNumber) {
                order = ord;
                break;
            }
        }

        // Нет заказов из пакета, находим самый приоритетный заказ по номеру источника
        if (order == null) {
//            for (int i = 1; i <= Main.sourcesCount; i++) {
//                boolean t = false;
//                for (Order ord : orders) {
//                    if (ord.getSourceNumber() == i) {
//                        order = ord;
//                        t = true;
//                        break;
//                    }
//                }
//                if (t) {
//                    break;
//                }
//            }
//
//            if (order == null) {
//                System.out.println("sad");
//            }

            Comparator<Order> comparator = Comparator.comparingInt(Order::getSourceNumber);
            order = orders.stream().min(comparator).orElseThrow();

            // Фиксируем новый пакет для следующего обращения прибора
            for (Order ord : orders) {
                if (ord.getSourceNumber() == order.getSourceNumber()) {
                    ord.packageNumber = order.getSourceNumber();
                }
            }
        }

        // Удаление найденного заказа из буфера
        orders.remove(order);

        order.stat.bufferOutT = Main.systemT;
        order.stat.delayT = order.stat.bufferOutT - order.stat.bufferAddT;

        if (Main.isStepMode()) {
            System.out.println("Удаление из Буфера " + order.getSourceNumber() + "." + order.getNumber());
        }
        return order;
    }

    public void print() {
        for (int i = 0; i < orders.size(); i++) {
            System.out.println("[" + i + "] - " + orders.get(i).getSourceNumber() + "." + orders.get(i).getNumber() + "    p: " + orders.get(i).packageNumber);
        }
        for (int i = 0; i < size - orders.size(); i++) {
            System.out.println("[" + i + "] - empty");
        }
    }

    public boolean isFree() {
        return (orders.size() < this.size);
    }
}
