package com.company;

import java.util.ArrayList;
import java.util.List;

public class PatternCheckClass {
    ReadFileDB readFileDB = new ReadFileDB();
    TradeCheckClass tradeCheckClass = new TradeCheckClass();
    // Для удобства написания кода, выделим последний и предпоследний индексы
    private final int lastIndex = readFileDB.getCountCandles() - 1;
    private final int penultimateIndex = readFileDB.getCountCandles() - 2;

    private final ArrayList<Candle> candlesList = readFileDB.getCandlesList();
    private final int countCandles = readFileDB.getCountCandles();

    public void darkСloudСover() {
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
        if (candlesList.get(penultimateIndex).isWhiteColor() && !candlesList.get(lastIndex).isWhiteColor()) {

            /** !!! Нужно уточнить (отношение тела свечи ко всей длине свечи)*/
            // 2. свечи д.б. с длинным белым телом. Т.е. !!! Пусть Длина тела составляет как мин 2/3 от всей длины свечи
            double bodyLastLength = candlesList.get(lastIndex).getBodyLength();
            double candleLastLength = candlesList.get(lastIndex).getCandleLength();
            double bodyPenultimateLength = candlesList.get(penultimateIndex).getBodyLength();
            double candlePenultimateLength = candlesList.get(penultimateIndex).getCandleLength();
            if ((bodyLastLength >= candleLastLength / 3 * 2)
                    && (bodyPenultimateLength >= candlePenultimateLength / 3 * 2)) {

                /** !!! Нужно уточнить (обязательно ли должна превышать, чтобы называться так) */
                // 3. цена открытия второго превышает верхнюю тень первой(белой) свечи
                if (candlesList.get(lastIndex).getOpenDayPrice() >= candlesList.get(penultimateIndex).getMaxPrice()) {

                    // 4. однако завершается вторая сессия снижением в район дневного минимума,
                    // который должен находиться в границах предыдущего белого тела
                    if ((candlesList.get(lastIndex).getCloseDayPrice() > candlesList.get(penultimateIndex).getOpenDayPrice())) {

                        /** !!! Нужно уточнить (уточнить вероятность разворота и минимальное перекрытие, чтобы называться так)*/
                        // 5. Чем сильнее черная свеча перекрывает белую, тем выше вероятность разворота
                        // должно перекрываться не меньше половины белого тела, если этого не происходит, то стоит дождаться
                        // дополнительных подтверждений медвежьему сигналу
                        if (candlesList.get(lastIndex).getCloseDayPrice() <
                                candlesList.get(penultimateIndex).getCloseDayPrice() - bodyPenultimateLength / 2) {
                            System.out.println("Это Завеса из темных облаков");
                            System.out.println("Это сигнал о окнчании тренда и м.б. развороте на вершине");
                        }
                    }
                }
            }
        }
    }

    public void piercingCloud() {
        // метод определяет, является ли модель разворота "Просвет в облаках"
        // появляется на падающем рынке

        // 1. если предпоследняя(черная) и последняя свеча(белая) разного цвета
        // 2. обе свечи д.б. с длинным телом
        // 3. цена открытия второго сильно ниже нижней тени первой свечи
        // 4. однако завершается вторая сессия повышением в район дневного максимума,
        // который должен находиться в границах тела предыдущей свечи
        // 5. Чем сильнее последняя свеча перекрывает предыдущую, тем выше вероятность разворота в основании
        // должно перекрываться не меньше половины белого тела, если этого не происходит, то стоит дождаться
        // дополнительных подтверждений медвежьему сигналу

        /** !!! Но если на графике затем появляется длинная черная свеча, у которой цена закрытия ниже минимумов,
         * достигнутых при возникновении "просвета в облаках" или "бычьей модели поглощения",
         * то это будет означать, что сигнал разворота не сработал и нисходящий тренд, скорее всего, продолжится.*/

        // 1. Если предпоследняя свеча черная, а последняя белая
        if (!candlesList.get(penultimateIndex).isWhiteColor() && candlesList.get(lastIndex).isWhiteColor()) {

            /** !!! Нужно уточнить (отношение тела свечи ко всей длине свечи)*/
            // 2. свечи д.б. с длинным телом. Т.е. !!! Пусть Длина тела составляет как мин 2/3 от всей длины свечи
            double bodyLastLength = candlesList.get(lastIndex).getBodyLength();
            double candleLastLength = candlesList.get(lastIndex).getCandleLength();
            double bodyPenultimateLength = candlesList.get(penultimateIndex).getBodyLength();
            double candlePenultimateLength = candlesList.get(penultimateIndex).getCandleLength();
            if ((bodyLastLength >= candleLastLength / 3 * 2)
                    && (bodyPenultimateLength >= candlePenultimateLength / 3 * 2)) {

                /** !!! Нужно уточнить (обязательно ли должна превышать, чтобы называться так)*/
                // 3. цена открытия последней значительно ниже нижней тени предпоследней(черной) свечи
                if (candlesList.get(lastIndex).getOpenDayPrice() <= candlesList.get(penultimateIndex).getMinPrice()) {

                    // 4. однако завершается вторая сессия повышением в район дневного максимума,
                    // который должен находиться в границах предыдущего тела
                    if ((candlesList.get(lastIndex).getCloseDayPrice() < candlesList.get(penultimateIndex).getOpenDayPrice())) {

                        /** !!! Нужно уточнить (уточнить вероятность разворота и минимальное перекрытие, чтобы называться так)*/
                        // 5. Чем сильнее последняя свеча перекрывает предыдущую, тем выше вероятность разворота
                        // должно перекрываться не меньше половины белого тела, если этого не происходит, то стоит дождаться
                        // дополнительных подтверждений медвежьему сигналу
                        if (candlesList.get(lastIndex).getCloseDayPrice() >
                                candlesList.get(penultimateIndex).getOpenDayPrice() + bodyPenultimateLength / 2) {
                            System.out.println("Это Завеса из темных облаков");
                            System.out.println("Это сигнал о окнчании тренда и м.б. развороте на вершине");
                        }
                    }
                }
            }
        }
    }

    /*public void trandType(int levelTradeCheck) {
        // Метод будет определять тренд (медвежий или бычий) по предыдущим свечам
        // Это очень важный метод, от правильности распознавания зависит качество дальнейшей аналитика

        // Нужно проработать исключения
        // 4. когда на рассматриваемом участке исключения составляют до 80% - высоты, и 65% - цвета
        // Причем если встречается исключение, то оно д.б. не слишком низким(не ниже 2 до нее свече), а следующая свеча, д. удовлетворять всем условиям и отдельно д.проходить проверка следующая самая высокая среди всех рассматриваемых на данном участке.
        // Если свеча не удовлетворяет требованию - верхняя линия тела свечи д.б. выше предыдущей верхней линии тела свечи, то следующая за ним должна быть нужного цвета или выше двух предыдущих и последующая тоже должна быть выше всех предыдущих рассматриваемых


        // т.к. условия для 3х участков идентичны, то код один

//         Условия:
//        1. Через каждые промежутки верхний уровень тела свечи должен быть больше/меньше и больше/меньше.
//        (т.е. каждый 5, 10, 15, 20 свечи д.б. больше 1, 6, 11, 16 свечи соответственно).
//        2. Тело 10, 15 и 20 свеч точно должно находиться выше/ниже чем тело 1-ой свечи.
//        3. суммарная длина тел одного цвета, чья тенденция идет д.б. больше. 65%/35% хотя бы.

        // из всех свеч возьмем последние 16
        // начинаем проверять 16 с конца до последней
        int sumLengthBlackCandles = 0;
        int sumLengthWhiteCandles = 0;
        int myNulIndex = countCandles - 1 - levelTradeCheck;
        // посчитаем сумму длин белой и черной свеч
        for (int i = myNulIndex; i < candlesList.size(); i++) {
//            3. суммарная длина тел одного цвета, чья тенденция идет д.б. больше. 65%/35% хотя бы.
            if (candlesList.get(i).isWhiteColor()) {
                sumLengthWhiteCandles += candlesList.get(i).getBodyLength();
            } else sumLengthBlackCandles += candlesList.get(i).getBodyLength();
        }
        // мы предполагаем, что у нас бычий тренд (sumLengthWhiteCandles > sumLengthBlackCandles
        if (sumLengthWhiteCandles >= sumLengthBlackCandles * 65 / 35) {
//            2. Тело 10, 15 и 20 свеч точно должно находиться выше чем тело 1-ой свечи.
            if (candlesList.get(myNulIndex + 10).)
        }
        // если не бычий тренд не пройдет, то возможно у нас медвежий тренд (sumLengthBlackCandles > sumLengthWhiteCandles
        else if (sumLengthBlackCandles >= sumLengthWhiteCandles * 65 / 35) {

        } else {  // если условие не сработало, то проверим по мини-участкам

        }


    }*/

    public String trendBefore(int toIndex, int rangeTrade, ArrayList<Candle> candlesList) {
        // Метод проверяет тренд(n свеч) до рассматриваемой свечи
                /* Условия:
        1. Через каждые промежутки верхний уровень тела свечи должен быть больше/меньше и больше/меньше.
        (т.е. каждый 5, 10, 15, 20 свечи д.б. больше 1, 6, 11, 16 свечи соответственно).
        2. Тело 5 свечи точно должно находиться выше/ниже чем тело 1-ой свечи.
        3. суммарная длина тел одного цвета, чья тенденция идет д.б. больше. 65%/35% хотя бы.
        4. желательно, чтобы тело каждой следующей свечи находилось выше предыдущей */

        int checkBullTrendCount = 0;
        int checkBearTrendCount = 0;

        // Проверяем есть ли у нас достаточное количество свеч
        if (toIndex < rangeTrade + 1) {
            return "";
        }

        // Инициализируем сумму тел белых и черных свеч
        double sumLengthBlackCandles = 0;
        double sumLengthWhiteCandles = 0;
        // инициализируем нулевой индекс как n(levelCheckTrend) до текущего(toIndex) индекса свечи
        int nullIndex = toIndex - rangeTrade;
        // создадим лист каждого пятого индекса, чтобы можно было сравнивать(для удобства)
        // мы не знаем сколько у нас должно получится таких индексов, поэтому Лист.
        ArrayList<Integer> ThreeIndexList = new ArrayList<>();
        // Аналогично, нам нужен список из индексов 1, и следующих за каждым пятым, для удобства сравнения
        ArrayList<Integer> nextThreeIndexList = new ArrayList<>();
        // посчитаем сумму длин белой и черной свеч
        nextThreeIndexList.add(nullIndex);
        // выберем жесткость проверки исходя из радиуса проверки
        int inflexible;
        if (rangeTrade < 12) inflexible = 2;
        else inflexible = rangeTrade / 3;
//        else if (rangeTrade > 10 && rangeTrade < 21) inflexible = rangeTrade / 3;
//        else inflexible = rangeTrade / 4;


        for (int i = nullIndex; i < toIndex; i++) {
            // добавим каждую пятую свечку в список-лист
            if (((rangeTrade - (toIndex - i)) % inflexible == 0) && ((rangeTrade - (toIndex - i)) != 0)) {
                ThreeIndexList.add(i);
                if (i != toIndex - 1) { // если индекс не последний, от исключений
                    nextThreeIndexList.add(i + 1);
                }
            }


//            3. суммарная длина тел одного цвета, чья тенденция идет д.б. больше. 65%/35% хотя бы.
            if (candlesList.get(i).isWhiteColor()) {
                sumLengthWhiteCandles += candlesList.get(i).getBodyLength();
            } else sumLengthBlackCandles += candlesList.get(i).getBodyLength();
        }
        // мы предполагаем, что у нас бычий тренд (sumLengthWhiteCandles > sumLengthBlackCandles(65%/35%)
        if (sumLengthWhiteCandles > sumLengthBlackCandles * 65 / 35) {
            // 1. Тело последней свечи точно должно находиться выше чем тело 1-ой свечи.
            if (candlesList.get(toIndex - 1).getUpBodyPrice() > candlesList.get(nullIndex).getUpBodyPrice()) {
                // 2. Тело каждой n-ой свечи точно должно находиться выше чем тело 1-ой свечи.
                for (int i = 0; i < ThreeIndexList.size(); i++) {
                    if (candlesList.get(ThreeIndexList.get(i)).getUpBodyPrice() > candlesList.get(nullIndex).getUpBodyPrice()) {
                        // 3. Тело каждой n-ой свечи точно должно находиться выше чем тело каждой 1-ой свечи.
                        if (candlesList.get(ThreeIndexList.get(i)).getUpBodyPrice() > candlesList.get(nextThreeIndexList.get(i)).getUpBodyPrice()) {
                            checkBullTrendCount++;
                        }
                    }
                }
            }
        }
        // если не бычий тренд не пройдет, то возможно у нас медвежий тренд (sumLengthBlackCandles > sumLengthWhiteCandles(65%/35%)
        else if (sumLengthBlackCandles >= sumLengthWhiteCandles * 65 / 35) {
            // 1. Тело последней свечи точно должно находиться ниже чем тело 1-ой свечи.
            if (candlesList.get(toIndex - 1).getDownBodyPrice() < candlesList.get(nullIndex).getDownBodyPrice()) {
                // 2. Тело каждой n-ой свечи точно должно находиться ниже чем тело 1-ой свечи.
                for (int i = 0; i < ThreeIndexList.size(); i++) {
                    if (candlesList.get(ThreeIndexList.get(i)).getDownBodyPrice() < candlesList.get(nullIndex).getDownBodyPrice()) {
                        // 3. Тело каждой n-ой свечи точно должно находиться выше чем тело каждой 1-ой свечи.
                        if (candlesList.get(ThreeIndexList.get(i)).getDownBodyPrice() < candlesList.get(nextThreeIndexList.get(i)).getDownBodyPrice()) {
                            checkBearTrendCount++;
                        }
                    }
                }
            }

        } else {  // если условие не сработало, то проверим по мини-участкам

        }
        if (checkBullTrendCount == ThreeIndexList.size()) {
            return candlesList.get(toIndex).getDate()
                    + ": в диапазоне "
                    + rangeTrade
                    + " свеч ДО у нас явный бычий тренд(↑) (индекс:" + toIndex + ")";
        } else if (checkBearTrendCount == ThreeIndexList.size()) {
            return "\t" + candlesList.get(toIndex).getDate()
                    + ": в диапазоне "
                    + rangeTrade
                    + " свеч ДО у нас явный медвежий тренд(↓) (индекс:" + toIndex + ")";
        } else return "";
    }

    public void trendAfter(int toIndex) {
        // Метод проверяет тренд после рассматриваемой свечи
    }

    public void miniTrendType() {
        /* Если не удовлетвроряет хоть одному условию, то нужно:
        1. длинный(20свеч) тренд не явный
        2. разделить на 4 мини-участка (по 5 свеч).
        3. Рассматривать каждый мини-участок.
        4. и по этим мини-участкам анализировать общий тренд.*/


    }


    public void engulfing() {
        // "Моделью поглощения"

        /* Модель поглощения критерии должна отвечать трем критериям: *
            1. На рынке должен наблюдаться ярко выраженный восходящий тренд(для медведя) или ярко выраженный нисходящий тренд(для быка), хотя бы краткосрочный.
            2. Тело второй свечи должно перекрывать собой тело первой (при этом поглощения тени может и не происходить).
            3. Цвет второго тела должен отличаться от цвета первого.
            Исключение возможно лишь в том случае, если первая свеча имеет настолько мизерное тело, что напоминает дожи или является дожи.*/
        // Т.о, если после продложительного нисходящего тренда крохотное белое тело поглощается другим белым телом, но очень большим,
        // можно говорить о сигнале разворота в основании (обратно аналогично о сигнале разворота на вершине).

//        2. Тeло второй свечи должно перекрывать собой тело первой (при этом поглощения тени может и не происходить).
        if (candlesList.get(lastIndex).isWhiteColor()) {
            if ((candlesList.get(lastIndex).getCloseDayPrice() > candlesList.get(penultimateIndex).getOpenDayPrice())
                    && candlesList.get(lastIndex).getOpenDayPrice() < candlesList.get(penultimateIndex).getCloseDayPrice()) {

//                3. Цвет второго тела должен отличаться от цвета первого.
//                Исключение, но размер тела предпоследнего д.б 1/10 частью размера тела последего
                if ((!candlesList.get(penultimateIndex).isWhiteColor())
                        || ((candlesList.get(penultimateIndex).isWhiteColor())
                        && (candlesList.get(penultimateIndex).getBodyLength() <= candlesList.get(lastIndex).getBodyLength() / 10))) {

//                    1. На рынке должен наблюдаться ярко выраженный восходящий тренд(для медведя)
//                    или ярко выраженный нисходящий тренд(для быка), хотя бы краткосрочный.


                }
            }
        } else {
            if ((candlesList.get(lastIndex).getCloseDayPrice() < candlesList.get(penultimateIndex).getOpenDayPrice())
                    && candlesList.get(lastIndex).getOpenDayPrice() > candlesList.get(penultimateIndex).getCloseDayPrice()) {

//                3. Цвет второго тела должен отличаться от цвета первого.
//                Исключение, но размер тела предпоследнего д.б 1/10 частью размера тела последего
                if ((candlesList.get(penultimateIndex).isWhiteColor())
                        || ((!candlesList.get(penultimateIndex).isWhiteColor())
                        && (candlesList.get(penultimateIndex).getBodyLength() <= candlesList.get(lastIndex).getBodyLength() / 10))) {

                }
            }
        }

    }

    public boolean hammerOrHangedCheckMethod() {
        // Этот метод будет определять модель разворота "Молот" и "Повешенный"
        //                  !!! Определяется по 1 последней свече

        // если перед последней свечой-молотом, тренд нисходящий, то следует ожидать его завершения
        // если перед последней свечой-повешенный, тренд восходящий, то аналогично

        // 3 признака молота и повешенного
        // 1. Тело свечи находится в верхней части ценового диапазона, цвет тела не важен
        // 2. Нижняя тень д.б. в 2 раза длиннее тела
        // 3. Верхняя тень д. отсутствовать или быть очень короткой

        /** !!! Чем длиннее нижняя тень и чем короче верхняя тень и чем меньше тело, тем сильнее сигнал*/
        // Идеально, когда верхняя тень вообще отсутствует, тело = 1/4 от всей длины или меньше - это 100% вероятность сигнала

        // 2. Нижняя тень д.б. в 2 раза длиннее тела
        if (candlesList.get(lastIndex).getDownShadowLength() > candlesList.get(lastIndex).getBodyLength() * 2) {
            // 3. Верхняя тень д. отсутствовать или быть очень короткой
            /** !!! У меня будет 1/5 от всей длины свечи - но нужно уточнить*/
            if (candlesList.get(lastIndex).getUpShadowLength() < candlesList.get(lastIndex).getCandleLength() / 5) {
                // 1. Тело свечи находится в верхней части ценового диапазона, цвет тела не важен
                // т.е. нижняя граница тела свечи д.б. выше средней линии свечи
                if (candlesList.get(lastIndex).isWhiteColor()) {
                    if (candlesList.get(lastIndex).getOpenDayPrice() > candlesList.get(lastIndex).getCandleLength() / 2)
                        System.out.println("Последняя свеча является молотом или повешенным с белым телом");
                    return true;
                } else {
                    if (candlesList.get(lastIndex).getCloseDayPrice() > candlesList.get(lastIndex).getCandleLength() / 2)
                        System.out.println("Последняя свеча является молотом или повешенным с красным телом");
                    return true;
                }
            }
        }
        return false;
    }

    public String hammerOrHangedCheck(int lastIndex, ArrayList<Candle> candlesList) {
        /** !!! Чем длиннее нижняя тень и чем короче верхняя тень и чем меньше тело, тем сильнее сигнал*/
        // Идеально, когда верхняя тень вообще отсутствует, тело = 1/4 от всей длины или меньше - это 100% вероятность сигнала

        // 2. Нижняя тень д.б. в 2 раза длиннее тела
        if (candlesList.get(lastIndex).getDownShadowLength() >= candlesList.get(lastIndex).getBodyLength()) {
            // 3. Верхняя тень д. отсутствовать или быть очень короткой
            /** !!! У меня будет 1/5 от всей длины свечи - но нужно уточнить*/
            if (candlesList.get(lastIndex).getUpShadowLength() < candlesList.get(lastIndex).getCandleLength() / 5) {
                // 1. Тело свечи находится в верхней части ценового диапазона, цвет тела не важен
                // т.е. нижняя граница тела свечи д.б. выше средней линии свечи
                if (candlesList.get(lastIndex).isWhiteColor()) {
                    if (candlesList.get(lastIndex).getOpenDayPrice() > candlesList.get(lastIndex).getCandleLength() / 2)
//                        System.out.println("Последняя свеча является молотом или повешенным с белым телом");
                        return candlesList.get(lastIndex).getDate();
                } else {
                    if (candlesList.get(lastIndex).getCloseDayPrice() > candlesList.get(lastIndex).getCandleLength() / 2)
//                        System.out.println("Последняя свеча является молотом или повешенным с красным телом");
                        return candlesList.get(lastIndex).getDate();
                }
            }
        }
        return "";
    }

    public int hammerOrHangedRightCheck(ArrayList<Candle> candlesList) {
        int countRightPrognosisHammer = 0;
        int countRightPrognosisHanged = 0;

        // Программа должна на всем диапазоне искать молоты и повешенные, учитывая тренд до них
        // вывести прогнозируемый тренд после молота/повешенного
        // вывести фактический тренд
        // Считать все прогнозы
        // считать все правльные прогнозы
        // вывести отношение правильных прогнозов ко всем прогнозам = вероятность успеха
        List<Integer> extremumList = tradeCheckClass.localExtremumSearchMethod(0, candlesList.size() - 1, candlesList);
        // Отладка
        /*System.out.println("extremumList: ");
        for (int extremum : extremumList) {
            System.out.print(candlesList.get(extremum).getDate() + "\t");
        }
        System.out.println();*/


//        System.out.println();
        for (int i = 0; i < extremumList.size() - 1; i++) {
            String tradeLastName =
                    tradeCheckClass.trendCheck(extremumList.get(i + 1),
                    (extremumList.get(i + 1) - extremumList.get(i)),
                    candlesList);
            // Отладка
            /*if (i == (extremumList.size() - 3)) {
                System.out.println();
            }
//            System.out.println("candlesList.get(extremumList.get(i+1)).getDate(): " + candlesList.get(extremumList.get(extremumList.get(i+1))).getDate());
//            System.out.println();*/

            // 2. Нижняя тень д.б. в 2 раза длиннее тела
            if (candlesList.get(extremumList.get(i + 1)).getDownShadowLength()
                    > candlesList.get(extremumList.get(i + 1)).getBodyLength() * 2) {
                // 3. Верхняя тень д. отсутствовать или быть очень короткой
                /** !!! У меня будет 1/5 от всей длины свечи - но нужно уточнить*/
                if (candlesList.get(extremumList.get(i + 1)).getUpShadowLength()
                        < candlesList.get(extremumList.get(i + 1)).getCandleLength() / 5) {
                    // 1. Тело свечи находится в верхней части ценового диапазона, цвет тела не важен
                    // т.е. нижняя граница тела свечи д.б. выше средней линии свечи
                    if (tradeLastName.equals("Медвежий")) {
                        if (candlesList.get(extremumList.get(i + 1)).getOpenDayPrice()
                                > candlesList.get(extremumList.get(i + 1)).getCandleLength() / 2) {
                            System.out.println("Свеча " + candlesList.get(extremumList.get(i + 1)).getDate() + " является молотом");
                            countRightPrognosisHammer ++;
                        }
                    } else if (tradeLastName.equals("Бычий")) {
                        if (candlesList.get(extremumList.get(i + 1)).getCloseDayPrice()
                                > candlesList.get(extremumList.get(i + 1)).getCandleLength() / 2) {
                            System.out.println("Свеча " + candlesList.get(extremumList.get(i + 1)).getDate() + " является повешенным");
                            countRightPrognosisHanged ++;
                        }
                    }
                }
            }
        }
        return countRightPrognosisHammer + countRightPrognosisHanged;
    }

    public int hammerOrHangedAllCheck(ArrayList<Candle> candlesList) {
        int countRightPrognosisHammer = 0;
        int countRightPrognosisHanged = 0;

        // Программа должна на всем диапазоне искать молоты и повешенные, учитывая тренд до них
        // вывести прогнозируемый тренд после молота/повешенного
        // вывести фактический тренд
        // Считать все прогнозы
        // считать все правльные прогнозы
        // вывести отношение правильных прогнозов ко всем прогнозам = вероятность успеха
        // Отладка
        /*System.out.println("extremumList: ");
        for (int extremum : extremumList) {
            System.out.print(candlesList.get(extremum).getDate() + "\t");
        }
        System.out.println();*/



//        System.out.println();
        for (int i = 0; i < candlesList.size() - 10; i++) {
            // Отладка
            /*if (i == (extremumList.size() - 3)) {
                System.out.println();
            }
//            System.out.println("candlesList.get(extremumList.get(i+1)).getDate(): " + candlesList.get(extremumList.get(extremumList.get(i+1))).getDate());
//            System.out.println();*/

            String tradeLastName = tradeCheckClass.trendCheck(i + 10, 10, candlesList);

            // 2. Нижняя тень д.б. в 2 раза длиннее тела
            if (candlesList.get(i + 1).getDownShadowLength()
                    > candlesList.get(i + 1).getBodyLength() * 2) {
                // 3. Верхняя тень д. отсутствовать или быть очень короткой
                /** !!! У меня будет 1/5 от всей длины свечи - но нужно уточнить*/
                if (candlesList.get(i + 1).getUpShadowLength()
                        < candlesList.get(i + 1).getCandleLength() / 5) {
                    // 1. Тело свечи находится в верхней части ценового диапазона, цвет тела не важен
                    // т.е. нижняя граница тела свечи д.б. выше средней линии свечи
                    if (tradeLastName.equals("Медвежий")) {
                        if (candlesList.get(i + 1).getOpenDayPrice()
                                > candlesList.get(i + 1).getCandleLength() / 2) {
                            System.out.println("Свеча " + candlesList.get(i + 1).getDate() + " является молотом");
                            countRightPrognosisHammer ++;
                        }
                    } else if (tradeLastName.equals("Бычий")) {
                        if (candlesList.get(i + 1).getCloseDayPrice()
                                > candlesList.get(i + 1).getCandleLength() / 2) {
                            System.out.println("Свеча " + candlesList.get(i + 1).getDate()
                                    + " является повешенным");
                            countRightPrognosisHanged ++;
                        }
                    }
                }
            }
        }
        return countRightPrognosisHammer + countRightPrognosisHanged;
    }
}
