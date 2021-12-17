package com.company.models;

public class Stat {
    public double generationT; // Время генерации
    public double bufferAddT; // Добавление в буфер
    public double bufferOutT; // Удаление из буфера
    public double delayT; // Время ожидания
    public double deviceAddT; // Начало обслуживания
    public double deviceDeleteT; // Конец обслуживания
    public double completionT; // Конец обслуживания
    public double rejectT; // Время отказа
}
