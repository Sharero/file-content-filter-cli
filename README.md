# Утилита фильтрации содержимого файлов

## Описание проекта

**Курс:** Java ШИФТ  
**Тип проекта:** Тестовое задание  
**Цель:** Разработка утилиты для фильтрации содержимого файлов по типу данных.

Утилита читает один или несколько входных файлов, содержащих в перемешку:

- целые числа
- числа с плавающей точкой
- строки

Каждая запись находится на отдельной строке.  
Утилита распределяет данные по отдельным выходным файлам:

- целые числа → `integers.txt`
- числа с плавающей точкой → `floats.txt`
- строки → `strings.txt`

По умолчанию файлы создаются в текущей директории.

## Требования проекта

- **Язык**: Java 21

- **Система сборки**: Maven 4.0.0

- **Тестирование**: JUnit 5.10.1, Mockito 5.5.0

- **Обработка командной строки**: picocli 4.7.5

### Зависимости Maven

```xml

<dependencies>
    <!-- JUnit -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>

    <!-- Mockito -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <version>5.5.0</version>
        <scope>test</scope>
    </dependency>

    <!-- Picocli -->
    <dependency>
        <groupId>info.picocli</groupId>
        <artifactId>picocli</artifactId>
        <version>4.7.5</version>
    </dependency>
</dependencies>
```

## Структура проекта

```bash
src
├── main
│   └── java
│       └── org/shift
│           ├── Main.java
│           │
│           ├── cli
│           │   └── CommandLineArguments.java
│           │
│           ├── core
│           │   ├── DataFilter.java
│           │   ├── DataType.java
│           │   ├── FilesFilter.java
│           │   └── OutputManager.java
│           │
│           └── stats
│               ├── StatisticsCollector.java
│               ├── StatisticsType.java
│               ├── IntegerStatisticCollector.java
│               ├── FloatStatisticCollector.java
│               └── StringStatisticCollector.java
│
└── test
    └── java
        └── org/shift
            ├── DataFilterTest.java
            ├── FilesFilterTest.java
            ├── OutputManagerTest.java
            ├── IntegerStatisticCollectorTest.java
            ├── FloatStatisticCollectorTest.java
            └── StringStatisticCollectorTest.java
pom.xml
README.md
```

## Инструкция по сборке

1. Склонировать репозиторий:

```bash
git clone https://github.com/Sharero/file-content-filter-cli.git
cd file-content-filter-cli
```

2. Собрать проект:

```bash
mvn clean package
```

3. Запустить утилиту:

```bash
java -jar target/file-content-filter-1.jar [опции] <файлы>
```

4. Допустимые опции запуска утилиты:

```bash
-s - вывести краткую статистику работы утилиты
-f - вывести полную статистику работы утилиты
-a - добавление новых данных в существующие файлы
-p <prefix> - задать префикс для выходных файлов
-o <path> - задать путь для выходных файлов
```

5. Запуск Unit-тестов

```bash
mvn clean test
```

## Пример использования

1. Запуск без опций

```bash
java -jar target/file-content-filter-1.jar in1.txt in2.txt
```

2. Запуск с выводом краткой статистики и выходные файлы имеют префикс

```bash
java -jar target/file-content-filter-1.jar -s -p new_ in1.txt in2.txt
```

3. Запуск с выводом полной статистики и выходные файлы имеют специальной путь
   для вывода

```bash
java -jar target/file-content-filter-1.jar -f -o /some/path in1.txt in2.txt
```

## Пример результата фильтрации файлов утилитой

**Команда запуска**:
`java -jar target/file-content-filter-1.jar in1.txt in2.txt`

### Входные данные

- **in1.txt**

```
Lorem ipsum dolor sit amet
45
Пример
3.1415
consectetur adipiscing
-0.001
тестовое задание
100500
```

- **in2.txt**

```
Нормальная форма числа с плавающей запятой
1.528535047E-25
Long
1234567890123456789
```

### Выходные данные

**integers.txt**

```
45
100500
1234567890123456789
```

**floats.txt**

```
3.1415
-0.001
1.528535047E-25
```

**strings.txt**

```
Lorem ipsum dolor sit amet
Пример
consectetur adipiscing
тестовое задание
Нормальная форма числа с плавающей запятой
Long 
```

## Пример вывода краткой статистики

Краткая статистика содержит количество элементов, обработанных во время
фильтрации, для каждого типа данных.

```bash
=== Statistics ===
INTEGER: Count: 3
FLOAT: Count: 3
STRING: Count: 6
```

## Пример вывода полной статистики

Полная статистика содержит количество элементов, обработанных во время
фильтрации,
для каждого типа данных. А также:

**Для строк:**

- длина самой короткой строки
- длина самой длинной строки

**Для чисел:**

- минимальное значение
- максимальное значение
- сумма значений
- среднее значение

```bash
=== Statistics ===
INTEGER: Count: 3, Min: 45, Max: 1234567890123456789, Sum: 1234567890123557334, Avg: 4.115226300411858E17
FLOAT: Count: 3, Min: -0.001, Max: 3.1415, Sum: 3.1405000000000003, Avg: 1.0468333333333335
STRING: Count: 6, MinLength: 4, MaxLength: 42
```

## Особенности реализации

1. Файлы создаются только если есть данные соответствующего типа;
2. Поддерживается работа с очень большими числами (BigInteger и Double);
3. Статистика рассчитывается по мере фильтрации, без повторного чтения файлов;
4. При одновременном вводе опций `-s -f` программа завершится без фильтрации с
   ошибкой `Options -s and -f cannot be used together`;
5. Если какой-то тип данных не был обработан в статистике выведется `no data`
   для этого типа;
6. Если утилите не передать файлы будет ошибка `No valid input files provided`;
7. К формату данных типа `FLOAT` относятся
   `1.0, 1e5, 1E5, 1e+5, 1E+5, 1e-5, 1E-5, 1.0e5, 1.0E5`;
8. К формату данных типа `INTEGER` относятся `1, 153, 4122222`;
9. К формату данных типа `STRING` относится все остальное;
10. Если в файл не удается записать какой-то тип данных, то этот тип заносится в
    список исключения. Тоесть все остальные типы будут записываться дальше в
    файлы, а этот нет;
11. Если не удается прочитать данные из какого-то файла - он удаляется из списка
    входных файлов с выводом в консоль ошибки
    `Input file does not exist: <name>`. Если
    в списке не остается файлов программа завершается с ошибкой
    `No valid input files provided`.