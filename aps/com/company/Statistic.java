package com.company;

import com.company.models.Device;
import com.company.models.Order;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Statistic {
    private static final ArrayList<Order> orders = new ArrayList<>();
    private static final ArrayList<Device> devices = new ArrayList<>();
    private static final ArrayList<Source> sources = new ArrayList<>();

    private static int completedOrders = 0;
    private static int rejectedOrders = 0;
    private static int generatedOrders = 0;
    private static int bufferedOrders = 0;

    private static String formatPrint(String st, int length) {
        if (st.length() > length) return st;

        int sub = length - st.length() - 2;
        int left = sub / 2;
        int right = sub - left;

        return "|" + " ".repeat(left) + st + " ".repeat(right);
    }

    public static void printResultTable() {
        System.out.println("_____________________________________________________________________________________________");
        System.out.println("|                                      РЕЗУЛЬТАТЫ                                           |");
        System.out.println("|___________________________________________________________________________________________|");
        System.out.println("|Источник |    Заявок    |    % отказа    |  Т в системе  |   Т ожидания   | Т обслуживания |");
        System.out.println("|_________|______________|________________|_______________|________________|________________|");

        DecimalFormat decForm = new DecimalFormat("#.###");

        for (Source source : sources) {
            int generatedC = source.generatedCount();
            double completionT = 0;
            double delayT = 0;
            for (Order order : orders) {
                if (order.getSourceNumber() == source.getNumber()) {
                    completionT += order.stat.completionT;
                    delayT += order.stat.delayT;
                }
            }
            double inSystemT = completionT + delayT;
            double pReject = (double)source.rejectedCount() / generatedC * 100;

            System.out.println(
                    formatPrint(decForm.format(source.getNumber()), 11) // Источник
                    + formatPrint(decForm.format(generatedC), 16) // Заявки
                    + formatPrint(decForm.format(pReject), 18) // % Отказа
                    + formatPrint(decForm.format(inSystemT / generatedC), 17) // Т в системе
                    + formatPrint(decForm.format(delayT / generatedC), 18) // Т ожидания
                    + formatPrint(decForm.format(completionT / generatedC), 18) // Т обслуживания
                    + "|"
            );
        }
        System.out.println("|_________|______________|________________|_______________|________________|________________|");

        System.out.println("__________________________");
        System.out.println("| Прибор  |   Кф. исп.   |");
        System.out.println("|_________|______________|");

        double allDevTime = 0;
        for (Device device : devices) {
            double t = device.getCompletionT() / Main.systemT;
            allDevTime += t;
            System.out.println(
                    formatPrint(decForm.format(device.getNumber()), 11)
                    + formatPrint(decForm.format(t), 16)
                    + "|  "
//                    + device.getCompletionT() + "    sysT: "
//                    + Main.systemT
            );
        }
        System.out.println("|_________|______________|");

        System.out.println(allDevTime / devices.size());


        System.out.println("\n\nИтого: ");
        System.out.println("Сгенерированно заказов: " + getGeneratedOrders());
        System.out.println("Принято в буффер заказов: " + getBufferedOrders());
        System.out.println("Обработано заказов: " + getCompletedOrders());
        System.out.println("Отказано: " + getRejectedOrders());
        System.out.println("Процент отказа: " +  decForm.format((double)getRejectedOrders() / getSystemProcessedOrders() * 100));
    }

    public static void addOrder(Order order) {
        orders.add(order);
    }

    public static void addDevice(Device device) {
        devices.add(device);
    }

    public static void addSource(Source source) {
        sources.add(source);
    }

    public static void completeOrder() {
        completedOrders++;
    }

    public static void acceptOrder() {
        bufferedOrders++;
    }

    public static void generateOrder() {
        generatedOrders++;
    }

    public static void rejectOrder() { rejectedOrders++; }

    public static int getBufferedOrders() { return bufferedOrders++; }

    public static int getSystemProcessedOrders() {
        return completedOrders + rejectedOrders;
    }

    public static int getCompletedOrders() {
        return completedOrders;
    }

    public static int getRejectedOrders() {
        return rejectedOrders;
    }

    public static int getGeneratedOrders() {
        return generatedOrders;
    }
}
