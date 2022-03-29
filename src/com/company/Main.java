package com.company;
// Программа принимает данные свечи, а мах, мин, цена открытия и цена закрытия
// Программа будет определять вид свечи и заданный тренд

// В программе будут заданы правила, которые будут определять вероятность дальнейшего тренда
// Программа будет с определенной вероятностью определять дальнейший тренд
// и давать рекомендации по покупке и продаже фьючерсов


import java.util.ArrayList;

public class Main {
    TradeCheckClass tradeCheckClass = new TradeCheckClass();
    PatternCheckClass patternCheckClass = new PatternCheckClass();
    ReadFileDB readFileDB = new ReadFileDB();


    {
        readFileDB.init();

        ArrayList<Candle> candlesList = readFileDB.getCandlesList();
//        String lastTrend = tradeCheckClass.trendCheck(200, 25, candlesList);
//        System.out.println("\n\n\n\n\n");

        System.out.println("\n");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tИтак, молоты и повешенные");
        System.out.println("\n");

        System.out.println("Правильный прогноз");
        int countRightPrognosisHammerOrHanged  = patternCheckClass.hammerOrHangedRightCheck(candlesList);
        System.out.println();
        System.out.println("Все найденные молоты и повешенные на всем участке");
        int countAllPrognosisHammerOrHanged = patternCheckClass.hammerOrHangedAllCheck(candlesList);


        System.out.println();
        if (countAllPrognosisHammerOrHanged != 0) {

            System.out.println("Количество молотов и повешенный, которые меняют тренд (рабочие) = " + countRightPrognosisHammerOrHanged);
            System.out.println("Количество всех молотов и повешенный на всем отрезке = " + countAllPrognosisHammerOrHanged);
            System.out.println();
            System.out.println("Вероятность правильной работы молота и повешенного = "
                    + String.format("%.2f", ((double) countRightPrognosisHammerOrHanged / countAllPrognosisHammerOrHanged) * 100) + " %");
        } else System.out.println("Ошибочка вышла, countAllPrognosisHammerOrHanged = " + countAllPrognosisHammerOrHanged);

    }








    public static void main(String[] args) {
        new Main();
    }


}
