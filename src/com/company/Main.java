package com.company;
// Программа принимает данные свечи, а мах, мин, цена открытия и цена закрытия
// Программа будет определять вид свечи и заданный тренд

// В программе будут заданы правила, которые будут определять вероятность дальнейшего тренда
// Программа будет с определенной вероятностью определять дальнейший тренд
// и давать рекомендации по покупке и продаже фьючерсов

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static int countCandles = 5;
    private static String[] pricesCandle = new String[4];
    private static ArrayList<Candle> candlesList;

    static {
        Scanner input = new Scanner(System.in);
        System.out.println("Введите " + countCandles + " последних свеч в формате:");
        System.out.println("мин, мах, цена открытия, цена закрытия:");

        for (int i = 0; i < countCandles; i++) {
            String inputLine = input.next();
            pricesCandle = inputLine.split(" ");
            candlesList.add(new Candle(
                    Double.parseDouble(pricesCandle[0]),
                    Double.parseDouble(pricesCandle[1]),
                    Double.parseDouble(pricesCandle[2]),
                    Double.parseDouble(pricesCandle[3])));
        }


    }

    public static void main(String[] args) {
        // write your code here


    }

    private static void darkСloudСover(){
        // метод определяет, является ли модель разворота "Завесой из темных облаков"

        // 1. если предпоследняя и последняя свеча разного цвета
        // 2. первая свеча д.б. с длинным белым телом
        // 3. цена открытия второго превышает вернюю тень первой свечи
        // 4. однако завершается вторая сессия снижением в район дневного минимума,
        // который должен находиться в границах предыдущего белого тела
        // 5. Чем сильнее черная свеча перекрывает белую, тем выше вероятность разворота
        // должно перекрываться не меньше половины белого тела, если этого не происходит, то стоит дождаться
        // дополнительных подтверждений медвежьему сигналу

        // 1. Если предпоследняя свеча белая, а последняя красная
        if (candlesList.get(candlesList.size()-2).isWhiteOrBlack() && !candlesList.get(candlesList.size()-1).isWhiteOrBlack()) {

            // !!! Нужно уточнить (отношение тела свечи ко всей длине свечи)
            // 2. свечи д.б. с длинным белым телом. Т.е. !!! Пусть Длина тела составляет как мин 2/3 от всей длины свечи
            double lengthCandleBodyLast = Math.abs(candlesList.get(candlesList.size()-1).getOpenDayPrice() - candlesList.get(candlesList.size()-1).getCloseDayPrice());
            double lengthCandleLast = Math.abs(candlesList.get(candlesList.size()-1).getMaxPrice() - candlesList.get(candlesList.size()-1).getMinPrice());
            double lengthCandleBodyPenultimate = Math.abs(candlesList.get(candlesList.size()-2).getOpenDayPrice() - candlesList.get(candlesList.size()-2).getCloseDayPrice());
            double lengthCandlePenultimate = Math.abs(candlesList.get(candlesList.size()-2).getMaxPrice() - candlesList.get(candlesList.size()-2).getMinPrice());
            if ((lengthCandleBodyLast >= lengthCandleLast / 3 * 2)
                    && (lengthCandleBodyPenultimate >= lengthCandlePenultimate / 3 * 2) ) {

                // !!! Нужно уточнить (обязательно ли должна превышать, чтобы называться так)
                // 3. цена открытия второго превышает верхнюю тень первой(белой) свечи
                if (candlesList.get(candlesList.size()-1).getOpenDayPrice() > candlesList.get(candlesList.size()-2).getMinPrice()) {

                    // 4. однако завершается вторая сессия снижением в район дневного минимума,
                    // который должен находиться в границах предыдущего белого тела
                    if (candlesList.get(candlesList.size()-1).getCloseDayPrice() < candlesList.get(candlesList.size()-2).getCloseDayPrice()) {

                        // !!! Нужно уточнить (уточнить вероятность разворота и минимальное перекрытие, чтобы называться так)
                        // 5. Чем сильнее черная свеча перекрывает белую, тем выше вероятность разворота
                        // должно перекрываться не меньше половины белого тела, если этого не происходит, то стоит дождаться
                        // дополнительных подтверждений медвежьему сигналу
                        if (candlesList.get(candlesList.size()-1).getCloseDayPrice() >
                                candlesList.get(candlesList.size()-2).getCloseDayPrice() - lengthCandleBodyPenultimate / 2) {
                            System.out.println("Это Завеса из темных облаков");
                        }
                    }
                }
            }
        }
    }

    private static void trandType() {
        // здесь будет определяться тренд (медвежий или бычий) по предыдущим свечам
    }
}
