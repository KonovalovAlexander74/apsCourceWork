#Вариант 9

##Источники:
- [x] ИБ – бесконечный;
- [x] ИЗ1 – пуассоновский закон распределения для источника;

##Приборы:
- [x] ПЗ2 – равномерный закон распределения для прибора;

##Дисциплины постановки, выбора, отказа:

- [x] Д1032 - постановка в буфер в порядке поступления
  Если в момент поступления заявок в систему все приборы оказываются занятыми, заявка последовательно занимает места в буфере памяти, начиная с первого. В случае освобождения какого-либо места в БП с номером N (заявка уходит на обслуживание или получает отказ), все заявки, стоящие на местах, начиная с (N+1), сдвигаются на одно место. Следующая заявка, вынужденная встать в очередь, всегда будет ставиться в ее конец, пока есть свободные места.

- [x] Д10О5 – отказ вновь прибывшей заявки;
  Заявка, сгенерированная источником и не нашедшая свободного места в буфере, уходит из системы, не изменяя состояния буфера. При этом она обязательно учитывается при подсчете общего количества сгенерированных источником заявок.

- [x] Д2П1 - приоритет по номеру прибытия
  Приоритеты приборов, также как и приоритеты источников определяются номерами приборов. Поэтому поиск свободного прибора ведется последовательным перебором, каждый раз начиная с самого приоритетного.

- [x] Д2Б5 – приоритет по номеру источника, заявки в пакете;
  Назовем  «пакетом»  совокупность  заявок  одного  источника, находящихся в буфере на момент освобождения одного из приборов. Количество  пакетов  в  БП  может  меняться  от  0  до  n,
  где  n  - количество источников.
  Когда при освобождении прибора происходит выбор первой заявки из буфера, вначале определяется самый приоритетный на данный момент пакет и происходит обслуживание заявок только этого пакета до тех пор, пока к моменту очередного освобождения прибора в БП не останется ни одной заявки этого пакета. Затем снова определяется самый приоритетный на данный момент пакет и далее повторяется весь процесс обслуживания этого пакета. Таким образом, происходит динамическая смена приоритетов обслуживания заявок, причем приоритетность пакетов можно регулировать, изменяя интенсивность генерации заявок источниками.


###Виды отображения результатов работы программной модели:
- [x] ОР1 – сводная таблица результатов;

- [x] ОД3 - временные диаграммы, текущее состояние
