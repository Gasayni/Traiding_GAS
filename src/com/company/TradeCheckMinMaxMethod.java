package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TradeCheckMinMaxMethod {
    ReadFileDB readFileDB = new ReadFileDB();
    ArrayList<Candle> candlesList = readFileDB.getCandlesList();
//    List<Integer> minMaxList = new ArrayList<>();

    public void trendCheck(int endIndex, int rangeTrade, ArrayList<Candle> candlesList) {
        // инициализируем нулевой последний индексы
        int firstIndex = endIndex - rangeTrade;
        List<String[]> outputList = new ArrayList<>();


        List<Integer> rangeList = new ArrayList<>();
        // Нужно разделить общий участок на 3 под-участка, до экстремума, между экстремумами и после
        rangeList.add(firstIndex);
        rangeList.add(endIndex);
        // Проверка
        System.out.println();
        System.out.println("\t\t\t\t\t\t\tРассматриваем общий участок от " + candlesList.get(firstIndex).getDate() + " и до " + candlesList.get(endIndex).getDate());


        // На рассматриваемом общем участке найдем сразу все локальные экстремумы
        rangeList = localExtremumSearchMethod(firstIndex, endIndex, candlesList);
        rangeList.add(0, firstIndex);
        rangeList.add(endIndex);

        // Теперь рассмотрим каждый под-участок
        while (rangeList.size() > 1) {
            // далее работаем с под-участками
            firstIndex = rangeList.get(0);
            endIndex = rangeList.get(1);

            // Проверка
            // выводим список для наглядности
            for (Integer i : rangeList) {
                System.out.print(i + ":" + candlesList.get(i).getDate() + "\t");
            }

            /*System.out.println();
            System.out.println("Рассматриваем под-участок от "
                    + candlesList.get(firstIndex).getDate()
                    + " и до "
                    + candlesList.get(endIndex).getDate());*/


            // Если под-участок нулевой, т.е. первый элемент и есть последний
            if (endIndex == firstIndex) {
                rangeList.remove(0);
                // Проверка
//                System.out.println("Под-участок нулевой, он нам не нужен");
            } else {
                // На участке от firstIndex до endIndex тренд:
                String[] outputMas = new String[3];
                outputMas[0] = candlesList.get(firstIndex).getDate();
                outputMas[1] = candlesList.get(endIndex).getDate();
                outputMas[2] = candlesTradeCheckMethod(firstIndex, endIndex, candlesList);
                outputList.add(outputMas);

                // как только мы закончили с этим участком
                // нужно перейти к другой под-участок
                // для этого нужно удалить рассмотренный под-участок
                rangeList.remove(0);
            }
        }


        // Проверка
        System.out.println("До:");
        for (String[] sMas : outputList) {
            System.out.println("На участке от "
                    + sMas[0]
                    + " до "
                    + sMas[1]
                    + " Тренд: "
                    + sMas[2]);
        }

        for (int i = 1; i < outputList.size(); /*i++*/) {
            // Если тренд на предыдущем участке совпадает с нынешним участком,
            // тогда правльнее было бы объединить эти участки
            if (outputList.get(i)[2].equals(outputList.get(i - 1)[2])) {
                // объеденяю участки, путем изменения начальной даты нынешнего участка на начальную дату предыдущего
                outputList.set(i, new String[]{outputList.get(i - 1)[0], outputList.get(i)[1], outputList.get(i)[2]});
                // и, конечно удаляю предыдущий участок
                outputList.remove(i - 1);
            } else i++;
        }

        System.out.println();
        for (String[] sMas : outputList) {
            System.out.println("На участке от "
                    + sMas[0]
                    + " до "
                    + sMas[1]
                    + " Тренд: "
                    + sMas[2]);
        }

    }

    private String candlesTradeCheckMethod(int firstIndex, int endIndex, ArrayList<Candle> candlesList) {
        int checkBullTrendCount = 0;
        int checkBearTrendCount = 0;
        boolean flagBullCheck = false;
        boolean flagBearCheck = false;

        // Инициализируем суммы тел белых и черных свеч
        double sumLengthBlackCandles = 0;
        double sumLengthWhiteCandles = 0;

        /*// Проверка
        if (firstIndex == 185) {
            System.out.println();
        }*/

        for (int i = firstIndex; i < endIndex; i++) {
            // суммируем длины тел одного цвета
            if (candlesList.get(i).isWhiteColor()) {
                sumLengthWhiteCandles += candlesList.get(i).getBodyLength();
            } else sumLengthBlackCandles += candlesList.get(i).getBodyLength();

        }


        // если все свечи белые или черные, то легко
        if (sumLengthWhiteCandles - sumLengthBlackCandles == 0) {
            return "Бычий";
        } else if (sumLengthWhiteCandles > sumLengthBlackCandles * 70 / 100) {
            return "Бычий";
        } else if (sumLengthBlackCandles - sumLengthWhiteCandles == 0) {
            return "Медвежий";
        } else if ((sumLengthBlackCandles > sumLengthWhiteCandles * 70 / 100)) {
            return "Медвежий";
        }

        // Суммарная длина тел одного цвета, чья тенденция идет д.б. больше, хотя бы 60% / 40%
        // мы предполагаем, что у нас бычий тренд (60%/40%)
        else if (sumLengthWhiteCandles > sumLengthBlackCandles * 55 / 45) {                                      // Наверно не нужно смотреть на вес тел
            // 1. Тело последней свечи точно должно находиться выше чем тело 1-ой свечи.
            if (candlesList.get(endIndex).getUpBodyPrice() > candlesList.get(firstIndex).getUpBodyPrice()) {
                for (int i = firstIndex; i < endIndex; i++) {
                    // Все тела рассматриваемых свеч (можно кроме одной) д.б. выше предыдущих свеч
                    if (candlesList.get(i + 1).getUpBodyPrice() > candlesList.get(i).getUpBodyPrice()) {
                        checkBullTrendCount++; // Нам нужно накопить число = (endIndex - firstIndex)
                    }

                    // если случится так, что под ряд будут свечи неожиданного цвета(черного - для бычьего тренда)...
                    if ((!candlesList.get(i).isWhiteColor()) && (!candlesList.get(i + 1).isWhiteColor())) {
                        // то пусть верхняя точка тела следующей будет ВЫШЕ верхней точки тела предыдущей...
                        if (candlesList.get(i + 1).getUpBodyPrice() > candlesList.get(i).getUpBodyPrice()) {
                            // нахождение разности высших точек соседних тел (для условия ниже)
                            double difUpBodyPrice = Math.abs(candlesList.get(i).getUpBodyPrice() - candlesList.get(i + 1).getUpBodyPrice());
                            double difDownBodyPrice = Math.abs(candlesList.get(i).getDownBodyPrice() - candlesList.get(i + 1).getDownBodyPrice());
                            // и нижняя точка тела следующей будет (равна или выше) нижней точки тела предыдущей или...
                            if ((candlesList.get(i + 1).getDownBodyPrice() >= candlesList.get(i).getDownBodyPrice())
                                    // или чтобы разность высших точек соседних тел была больше чем разность низших точек тел
                                    || (difUpBodyPrice > difDownBodyPrice)) {
                                flagBullCheck = true;
                            }
                        }
                    } else flagBullCheck = true;
                }
            }
        }

        // мы предполагаем, что у нас медвежий тренд (60% / 40%)
        else if ((sumLengthBlackCandles > sumLengthWhiteCandles * 55 / 45)) {
            // 1. Тело последней свечи точно должно находиться ниже чем тело 1-ой свечи.
            if (candlesList.get(endIndex).getDownBodyPrice() < candlesList.get(firstIndex).getDownBodyPrice()) {
                for (int i = firstIndex; i <= endIndex - 1; i++) {
                    // Все тела рассматриваемых свеч (можно кроме одной) д.б. ниже предыдущих свеч
                    if (candlesList.get(i + 1).getDownBodyPrice() < candlesList.get(i).getDownBodyPrice()) {
                        checkBearTrendCount++; // Нам нужно накопить число = (endIndex - firstIndex)
                    }

                    // если случится так, что под ряд будут свечи неожиданного цвета(белого - для медвежьего тренда)...
                    if ((candlesList.get(i).isWhiteColor()) && (candlesList.get(i + 1).isWhiteColor())) {
                        // то пусть нижняя точка тела следующей будет НИЖЕ нижней точки тела предыдущей...
                        if (candlesList.get(i + 1).getDownBodyPrice() < candlesList.get(i).getDownBodyPrice()) {
                            // нахождение разности высших точек соседних тел (для условия ниже)
                            double difUpBodyPrice = Math.abs(candlesList.get(i).getUpBodyPrice() - candlesList.get(i + 1).getUpBodyPrice());
                            double difDownBodyPrice = Math.abs(candlesList.get(i).getDownBodyPrice() - candlesList.get(i + 1).getDownBodyPrice());
                            // и нижняя точка тела следующей будет (равна или ниже) нижней точки тела предыдущей или...
                            if ((candlesList.get(i + 1).getDownBodyPrice() <= candlesList.get(i).getDownBodyPrice())
                                    // или чтобы разность низших точек была больше чем разность высших точек тел
                                    || (difDownBodyPrice > difUpBodyPrice)) {
                                flagBearCheck = true;
                            } else {
                                flagBearCheck = false;
                                break;
                            }
                        } else {
                            flagBearCheck = false;
                            break;
                        }
                    } else flagBearCheck = true;
                }
            }
        }

        if (flagBullCheck && checkBullTrendCount > (endIndex - firstIndex) * 0.7) {
            return "Бычий";
        } else if (flagBearCheck && checkBearTrendCount > (endIndex - firstIndex) * 0.7) {
            return "Медвежий";
        } else return "Боковой";
    }

    private List<Integer> localExtremumSearchMethod(int firstIndex, int endIndex, ArrayList<Candle> candlesList) {
        List<Integer> extremumList = new ArrayList<>();
        boolean checkFlag;

        // находим все идеальные экстремумы, у него 2 соседние д.б. больше/меньше одновременно
        for (int i = firstIndex + 2; i <= endIndex - 2; i++) {
        // Если это 1 или последний элемент, то нужно сравнивать по другому
            if (i == firstIndex + 1 || i == endIndex - 1) {
                if (candlesList.get(i).getUpBodyPrice() > candlesList.get(i + 1).getUpBodyPrice()
                        && candlesList.get(i).getUpBodyPrice() > candlesList.get(i - 1).getUpBodyPrice()) {

                    // это идеальный вариант, если он проходит, то просто говорим, что это локальный экстремум
                    extremumList.add(i);
                } else if (candlesList.get(i).getDownBodyPrice() <= candlesList.get(i + 1).getDownBodyPrice()
                        && candlesList.get(i).getDownBodyPrice() <= candlesList.get(i - 1).getDownBodyPrice()) {
                    // это идеальный вариант, если он проходит, то просто говорим, что это локальный экстремум
                    extremumList.add(i);
                }

            } else {
                if ((candlesList.get(i).getUpBodyPrice() >= candlesList.get(i + 1).getUpBodyPrice()
                        && candlesList.get(i + 1).getUpBodyPrice() >= candlesList.get(i + 2).getUpBodyPrice()
                        && candlesList.get(i).getUpBodyPrice() >= candlesList.get(i - 1).getUpBodyPrice()
                        && candlesList.get(i - 1).getUpBodyPrice() >= candlesList.get(i - 2).getUpBodyPrice())) {
                    // это идеальный вариант, если он проходит, то просто говорим, что это локальный экстремум
                    extremumList.add(i);

                } else if (candlesList.get(i).getDownBodyPrice() <= candlesList.get(i + 1).getDownBodyPrice()
                        && candlesList.get(i + 1).getDownBodyPrice() <= candlesList.get(i + 2).getDownBodyPrice()
                        && candlesList.get(i).getDownBodyPrice() <= candlesList.get(i - 1).getDownBodyPrice()
                        && candlesList.get(i - 1).getDownBodyPrice() <= candlesList.get(i - 2).getDownBodyPrice()) {
                    // это идеальный вариант, если он проходит, то просто говорим, что это локальный экстремум
                    extremumList.add(i);
                }
            }
        }

        for (int i = firstIndex + 2; i <= endIndex - 2; i++) {

            /*// Проверка
            if (i == 186) {
                System.out.println(i);
            }*/

            if ((candlesList.get(i).getUpBodyPrice() > candlesList.get(i + 1).getUpBodyPrice()
                    || candlesList.get(i).getDownBodyPrice() > candlesList.get(i + 1).getDownBodyPrice())
                    && candlesList.get(i).getUpBodyPrice() > candlesList.get(i + 2).getUpBodyPrice()
                    && (candlesList.get(i).getUpBodyPrice() > candlesList.get(i - 1).getUpBodyPrice()
                    || candlesList.get(i).getDownBodyPrice() > candlesList.get(i - 1).getDownBodyPrice())
                    && candlesList.get(i).getUpBodyPrice() > candlesList.get(i - 2).getUpBodyPrice()
                    && !candlesList.get(i).isWhiteColor()) {

                checkFlag = true;
                if ((candlesList.get(i + 1).getUpBodyPrice() > candlesList.get(i + 2).getUpBodyPrice()
//                    || candlesList.get(index + 1).getUpBodyPrice() > candlesList.get(index + 3).getUpBodyPrice()
                        || candlesList.get(i + 1).getDownBodyPrice() > candlesList.get(i + 2).getDownBodyPrice())
                        && (candlesList.get(i - 1).getUpBodyPrice() > candlesList.get(i - 2).getUpBodyPrice())
                        || candlesList.get(i - 1).getDownBodyPrice() > candlesList.get(i - 2).getDownBodyPrice()) {

                    for (int j : extremumList) {
                        if (i == j
                                || i + 1 == j
                                || i - 1 == j) {
                            checkFlag = false;
                            break;
                        }
                    }
                    if (checkFlag) extremumList.add(i);
                }
            } else if ((candlesList.get(i).getDownBodyPrice() < candlesList.get(i + 1).getDownBodyPrice()
                    || candlesList.get(i).getUpBodyPrice() < candlesList.get(i + 1).getUpBodyPrice())
                    && candlesList.get(i).getDownBodyPrice() < candlesList.get(i + 2).getDownBodyPrice()
                    && (candlesList.get(i).getDownBodyPrice() < candlesList.get(i - 1).getDownBodyPrice()
                    || candlesList.get(i).getUpBodyPrice() < candlesList.get(i - 1).getUpBodyPrice())
                    && candlesList.get(i).getDownBodyPrice() < candlesList.get(i - 2).getDownBodyPrice()
                    && candlesList.get(i).isWhiteColor()) {

                checkFlag = true;
                if ((candlesList.get(i + 1).getDownBodyPrice() < candlesList.get(i + 2).getDownBodyPrice()
                        || candlesList.get(i + 1).getUpBodyPrice() < candlesList.get(i + 2).getUpBodyPrice())
                        && (candlesList.get(i - 1).getDownBodyPrice() < candlesList.get(i - 2).getDownBodyPrice()
                        || candlesList.get(i - 1).getUpBodyPrice() < candlesList.get(i - 2).getUpBodyPrice())) {

                    for (int j : extremumList) {
                        if (i == j
                                || i + 1 == j
                                || i - 1 == j) {
                            checkFlag = false;
                            break;
                        }
                    }
                    if (checkFlag) extremumList.add(i);
                }

            }
        }
        return extremumList.stream().sorted((x1, x2) -> x1 - x2).collect(Collectors.toList());
    }
}
