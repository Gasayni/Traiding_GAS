package com.company;
// Программа принимает данные свечи, а мах, мин, цена открытия и цена закрытия
// Программа будет определять вид свечи и заданный тренд

// В программе будут заданы правила, которые будут определять вероятность дальнейшего тренда
// Программа будет с определенной вероятностью определять дальнейший тренд
// и давать рекомендации по покупке и продаже фьючерсов


import java.util.ArrayList;

public class Main {
    TradeCheckMinMaxMethod tradeCheckMinMaxMethod = new TradeCheckMinMaxMethod();
    ReadFileDB readFileDB = new ReadFileDB();


    {
        readFileDB.init();

        ArrayList<Candle> candlesList = readFileDB.getCandlesList();
        tradeCheckMinMaxMethod.trendCheck(150, 25, candlesList);
    }

    public static void main(String[] args) {
        new Main();
    }


}
