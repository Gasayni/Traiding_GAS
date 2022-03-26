package com.company;
// Программа принимает данные свечи, а мах, мин, цена открытия и цена закрытия
// Программа будет определять вид свечи и заданный тренд

// В программе будут заданы правила, которые будут определять вероятность дальнейшего тренда
// Программа будет с определенной вероятностью определять дальнейший тренд
// и давать рекомендации по покупке и продаже фьючерсов


import java.util.ArrayList;

public class Main {
    TradePatternCheck tradePatternCheck = new TradePatternCheck();
    TradeCheckMinMaxMethod tradeCheckMinMaxMethod = new TradeCheckMinMaxMethod();
    ReadFileDB readFileDB = new ReadFileDB();
    {
        readFileDB.init();

        ArrayList<Candle> candlesList = readFileDB.getCandlesList();
        int countHammerOrHanged=0;
        for (int i=0; i<candlesList.size(); i++) {
            String s=null;
            // определим сколько молотов и повешенных всего было
            /*if (!(s = tradePatternCheck.hammerOrHangedCheck(i, candlesList)).equals("")){
                countHammerOrHanged++;
                System.out.println("Дата формирования молота или повешенного: " + s);
            }*/
//            String ss = tradePatternCheck.trendBefore(i, 10, candlesList);

//            if (!ss.equals("")) {
//                System.out.println(ss);
//            }
        }
//        System.out.print("Из " + candlesList.size() +" свеч ");
//        System.out.println("повешенных и молотов = " + countHammerOrHanged);

        System.out.println(candlesList.size());
        tradeCheckMinMaxMethod.trendCheck(235, 25, candlesList);
    }

    public static void main(String[] args) {
        new Main();
    }


}
