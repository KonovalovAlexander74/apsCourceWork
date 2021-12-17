package com.company.managers;

import com.company.Buffer;
import com.company.Main;
import com.company.Statistic;
import com.company.models.Device;
import com.company.models.Order;
import com.company.models.Stat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DeviceManager {
    private final Buffer buffer;
    private final ArrayList<Device> devices;
    private int packageNumber;

    public DeviceManager(Buffer buffer, int devicesCount) {
        this.buffer = buffer;
        this.packageNumber = -1;

        devices = new ArrayList<>();
        for (int i = 0; i < devicesCount; i++) {
            var device = new Device(i + 1);
            devices.add(device);
            Statistic.addDevice(device);
        }
    }

    public void check() {
        for (Device device : devices) {
            device.isDone();
        }
    }

    public boolean sendOrderToDevice() {
        // Find free device
        // Pick order from Buffer ()
        // Send Order to chosen Device

        for (Device device : devices) {
            device.isDone();
        }

        Device device = pickDevice();
        if (device == null) { return false; }

        Order order = pickOrder();
        if (order == null) { return false; }

        device.add(order);
        return true;
    }

    private Device pickDevice() { // Pick device - по приоритету
        for (Device device : devices) {
            if (device.isAvailable()) {
                return device;
            }
        }

        return null;
    }

    private Order pickOrder() {
        Order order = buffer.getOrder(packageNumber);

        if (order != null) { // Фиксируем номер пакета
            packageNumber = order.getSourceNumber();
        }

        return order;
    }

    public void printDevicesState() {
        System.out.println("PACKAGE NUMBER = " + packageNumber);
        for (Device device : devices) {
            System.out.print("П" + device.getNumber() + " - ");
            if (device.isAvailable()) {
                System.out.println("empty");
            } else {
                System.out.println(device.getOrder().getSourceNumber() + "." + device.getOrder().getNumber());
            }
        }
    }
}
