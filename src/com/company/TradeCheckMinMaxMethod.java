package com.company;

import java.util.ArrayList;
import java.util.List;

public class TradeCheckMinMaxMethod {
    ReadFileDB readFileDB = new ReadFileDB();
    ArrayList<Candle> candlesList = readFileDB.getCandlesList();
//    List<Integer> minMaxList = new ArrayList<>();

    public void trendCheck(int endIndex, int rangeTrade, ArrayList<Candle> candlesList) {
        // инициализируем нулевой последний индексы
        int firstIndex = endIndex - rangeTrade;
        List<Integer> minMaxList;

        List<Integer> list1 = new ArrayList<>();
        // Нужно разделить общий участок на 3 под-участка, до экстремума, между экстремумами и после
        list1.add(firstIndex);
        list1.add(endIndex);

        // Если под-участок МЕНЕЕ 6 свеч, то просто сравниваем
        if (endIndex - firstIndex < 6) {
            // На участке от firstIndex до endIndex тренд:
            String nameTrend = candlesTradeCheckMethod(firstIndex, endIndex, candlesList);
            System.out.println("На участке от "
                    + candlesList.get(firstIndex).getDate()
                    + " до "
                    + candlesList.get(endIndex).getDate()
                    + " Тренд: "
                    + nameTrend);

            // как только мы закончили с этим участком
            // нужно перейти к другой под-участок
            // для этого нужно удалить рассмотренный под-участок
            list1.remove(0);
        } else {
            // Мы ищем экстремумы
            minMaxList = minMaxSearchMethod(firstIndex, endIndex, candlesList);
            // если бычий тренд (мин левее мах)
            if (minMaxList.get(0) < minMaxList.get(1)) {
                // добавляем мах и мин как новые под-участки в лист
                list1.add(1, minMaxList.get(1)); // сначала добавляем мах
                list1.add(1, minMaxList.get(0)); // потом мин
            } else if (minMaxList.get(1) < minMaxList.get(0)) {
                // добавляем мин и мах как новые под-участки в лист
                list1.add(1, minMaxList.get(0)); // сначала добавляем мин
                list1.add(1, minMaxList.get(1)); // потом мах
            }

            // заходим внутрь участка
            while (list1.size() > 1) {
                // далее работаем с под-участками
                firstIndex = list1.get(0);
                endIndex = list1.get(1);

                // Если под-участок нулевой, т.е. первый элемент и есть последний
                if (endIndex == firstIndex) {
                    list1.remove(0);
                }
                // Если под-участок МАЛЕНЬКИЙ ( < 6 )
                else if (endIndex - firstIndex < 6) {
                    // На участке от firstIndex до minMaxList.get(0) тренд:
                    String nameTrend = candlesTradeCheckMethod(firstIndex, endIndex, candlesList);
                    System.out.println("На участке от "
                            + candlesList.get(firstIndex).getDate()
                            + " до "
                            + candlesList.get(endIndex).getDate()
                            + " Тренд: "
                            + nameTrend);

                    // как только мы закончили с этим участком
                    // нужно перейти к другой под-участок
                    // для этого нужно удалить рассмотренный под-участок
                    list1.remove(0);
                } else {
                    // Мы ищем экстремумы
                    minMaxList = minMaxSearchMethod(firstIndex, endIndex - 1, candlesList);
                    // если бычий тренд (мин левее мах)
                    if (minMaxList.get(0) < minMaxList.get(1)) {
                        // добавляем мах и мин как новые под-участки в лист
                        list1.add(1, minMaxList.get(1)); // сначала добавляем мах
                        list1.add(1, minMaxList.get(0)); // потом мин
                    }
                    // если медвежий тренд (мах левее мин)
                    else {
                        // добавляем мин и мах как новые под-участки в лист
                        list1.add(1, minMaxList.get(0)); // сначала добавляем мин
                        list1.add(1, minMaxList.get(1)); // потом мах
                    }
                    // мы в minMaxSearchMethod сдвигаем участок влево на 1, чтобы найти новый экстремум,
                    // а здесь мы пропущенную свечу добавляем в следующий под-участок, тем самым расширяя влево
                    // тем самым, чтобы не учитываемые свечи(в minMaxSearchMethod) не потерялись

                    // хоть на первый взгляд может показаться, что команда ниже может привести к исключению переполнения
                    // потому в условии цикла указано, минимум 2 значения можно. А если будет как раз 2 значения, то
                    // list1.get(2) - это уже 3-ее значение,
                    // но если посмотреть выше, то в любом случае наш лист расширится еще как минимум на 2
                    // т.о. исключения быть не может
                    list1.set(2, list1.get(2) - 1);
                }
            }
        }
    }

    private List<Integer> minMaxSearchMethod(int firstIndex, int endIndex, ArrayList<Candle> candlesList) {
        // Инициализируем мин и макс значение тела свечи - первым телом
        List<Integer> minMaxList = new ArrayList<>();
        minMaxList.add(0, firstIndex);
        minMaxList.add(1, firstIndex);
        if (firstIndex == endIndex) {
            return null;
        }

        // Идем по всему участку и ищем экстремумы (в высших точках тел)
        for (int i = firstIndex; i <= endIndex; i++) {
            if (candlesList.get(i).getUpBodyPrice() < candlesList.get(minMaxList.get(0)).getUpBodyPrice()) {
                minMaxList.set(0, i);
            }
            if (candlesList.get(i).getUpBodyPrice() > candlesList.get(minMaxList.get(1)).getUpBodyPrice()) {
                minMaxList.set(1, i);
            }

        }
        return minMaxList;
    }

    private String candlesTradeCheckMethod(int firstIndex, int endIndex, ArrayList<Candle> candlesList) {
        int checkBullTrendCount = 0;
        int checkBearTrendCount = 0;
        boolean flagBullCheck = false;
        boolean flagBearCheck = false;

        // Инициализируем суммы тел белых и черных свеч
        double sumLengthBlackCandles = 0;
        double sumLengthWhiteCandles = 0;

        for (int i = firstIndex; i <= endIndex; i++) {
            // суммируем длины тел одного цвета
            if (candlesList.get(i).isWhiteColor()) {
                sumLengthWhiteCandles += candlesList.get(i).getBodyLength();
            } else sumLengthBlackCandles += candlesList.get(i).getBodyLength();
        }

        // Суммарная длина тел одного цвета, чья тенденция идет д.б. больше, хотя бы 60% / 40%
        // мы предполагаем, что у нас бычий тренд (60%/40%)
        if (sumLengthWhiteCandles > sumLengthBlackCandles * 60 / 40) {                                      // Наверно не нужно смотреть на вес тел
            // 1. Тело последней свечи точно должно находиться выше чем тело 1-ой свечи.
            if (candlesList.get(endIndex).getUpBodyPrice() > candlesList.get(firstIndex).getUpBodyPrice()) {
                for (int i = firstIndex; i <= endIndex - 1; i++) {
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
        else if ((sumLengthBlackCandles > sumLengthWhiteCandles * 60 / 40)) {
            // 1. Тело последней и предпоследней свечи точно должно находиться ниже чем тело 1-ой свечи.
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
                            }
                        }
                    } else flagBullCheck = true;
                }
            }
        }


        if (flagBullCheck && checkBullTrendCount == endIndex - firstIndex) {
            return "bull";
        } else if (flagBearCheck && checkBearTrendCount == endIndex - firstIndex) {
            return "bear";
        } else return "sideways";
    }
}
