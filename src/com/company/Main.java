package com.company;
// Программа принимает данные свечи, а мах, мин, цена открытия и цена закрытия
// Программа будет определять вид свечи и заданный тренд

// В программе будут заданы правила, которые будут определять вероятность дальнейшего тренда
// Программа будет с определенной вероятностью определять дальнейший тренд
// и давать рекомендации по покупке и продаже фьючерсов


import java.util.ArrayList;

public class Main {
    TradePatternCheck tradePatternCheck = new TradePatternCheck();
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

            System.out.println(tradePatternCheck.trandBefore(i, 5, candlesList));
        }
//        System.out.print("Из " + candlesList.size() +" свеч ");
//        System.out.println("повешенных и молотов = " + countHammerOrHanged);
    }

    public static void main(String[] args) {
        new Main();
    }


}
