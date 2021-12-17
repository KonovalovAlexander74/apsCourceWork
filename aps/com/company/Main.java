package com.company;

import com.company.managers.BufferManager;
import com.company.managers.DeviceManager;
import com.company.models.Order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    private static boolean isStepMode = false;
    private static int step = 0;

    public static final double alpha = 1.6; // 1.0 / 7.0
    public static final double beta = 1.2; // 1.3 / 7.0
    public static final double lambda = 0.45; // 0.4,

    public static double systemT = 0.0; // Model Time

    public static int sourcesCount ;
    public static int ordersCount;
    public static int devicesCount;
    public static int bufferSize;

    private static final ArrayList<Order> orders = new ArrayList<>();
    private static final ArrayList<Source> sources = new ArrayList<>();

    public static boolean isStepMode() { return isStepMode; }

    static void printCurrentState(Buffer buffer, DeviceManager deviceManager) {
        if (!isStepMode) { return; }

        System.out.println("-------------- Step " + step + " --------------");
        System.out.println("Системное время - " + systemT);

        System.out.println("    Сгенерированные заказы:");
        for (Source source : sources) {
            for (Order order : orders) {
                if (order.getSourceNumber() == source.getNumber()) {
                    System.out.println("И" + source.getNumber() + " - " + order.getNumber());
                }
            }
        }

        System.out.println("\n    Состояние буффера:");
        buffer.print();

        System.out.println("\n    Состояние приборов:");
        deviceManager.printDevicesState();

        System.out.println("-------------------------------------");
    }

    private static void checkMode() {
        System.out.print("Выбор режима: a - автоматический, s - пошаговый, f - завершить: ");
        Scanner scanner = new Scanner(System.in);
        String ch = scanner.nextLine();
        switch (ch) {
            case "s" -> isStepMode = true;
            case "a" -> isStepMode = false;
            case "f" -> System.exit(2);
            default -> System.out.println("Неизвестный режим! (автомат.)");

        }
    }

    public static void main(String[] args) {
        // TODO: Блок инициализации

        checkMode();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите количество источников: ");
        sourcesCount = scanner.nextInt();
        System.out.print("Введите количество моделируемых заявок: ");
        ordersCount = scanner.nextInt();
        System.out.print("Введите размер буфера: ");
        bufferSize = scanner.nextInt();
        System.out.print("Введите количество приборов: ");
        devicesCount = scanner.nextInt();

//        sourcesCount = 10;
//        ordersCount = 2000;
//        bufferSize = 10;
//        devicesCount = 9;

        // Источники
        for (int i = 0; i < sourcesCount; i++) {
            var source = new Source(i + 1);
            sources.add(source);
            Statistic.addSource(source);
        }

        // Заказы + их генерация
        for (Source source : sources) {
            if (Statistic.getGeneratedOrders() < ordersCount) {
                orders.add(source.generate());
            }
        }

        // Буффер
        final Buffer buffer = new Buffer(bufferSize);

        // Менеджеры
        BufferManager bufferManager = new BufferManager(buffer);
        DeviceManager deviceManager = new DeviceManager(buffer, devicesCount);


        printCurrentState(buffer, deviceManager);

        while (Statistic.getSystemProcessedOrders() < ordersCount) {
            if (isStepMode) {
                checkMode();
            }
            step++;

            if (orders.isEmpty()) { // Все заказы зашли в систему
                systemT += 0.0005;
                if (deviceManager.sendOrderToDevice()) {
                    printCurrentState(buffer, deviceManager);
                }
                if (Statistic.getSystemProcessedOrders() == ordersCount) {
                    step = -1;
                    printCurrentState(buffer, deviceManager);
                    System.out.println("! C_O_M_P_L_E_T_E_D !");
                    break;
                }
//                break;
            } else { // Еще не все заказы зашли в систему

                // Поиск первого по времени заказа
                Comparator<Order> orderComp = Comparator.comparingDouble(order -> order.stat.generationT);
                Order newOrder = orders.stream().min(orderComp).orElseThrow();

                systemT = newOrder.stat.generationT; // Time - генерация заказа

                deviceManager.check();

                // Удаление его из списка заказов
                int sourceNumber = newOrder.getSourceNumber() - 1;
                orders.remove(newOrder);
                Statistic.addOrder(newOrder);
                if (isStepMode) {
                    System.out.println("Выбран заказ    " + newOrder.getSourceNumber() + "." + newOrder.getNumber());
                }

                // Создание нового заказа этим источником
                if (Statistic.getGeneratedOrders() < ordersCount) {
                    orders.add(sources.get(sourceNumber).generate());
                }

                if (bufferManager.acceptOrder(newOrder)) {
                    systemT = newOrder.stat.bufferAddT; // Time - Добавление заказа в буффер
                } else { // Отказ
                    systemT = newOrder.stat.rejectT; // Time - Отказ заказа
                    sources.get(newOrder.getSourceNumber() - 1).reject();
                }

                // Отправка заказа на обслуживание
                deviceManager.sendOrderToDevice();

                printCurrentState(buffer, deviceManager); // Состояние после отправки на обслуживание
            }
        }

        Statistic.printResultTable();

        System.out.println("\n\nEnd SMO");
    }
}
