package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadFileDB {
    // Для удобства написания кода, выделим последний и предпоследний индексы
    private int countCandles;
    private final int lastIndex = countCandles - 1;
    private final int penultimateIndex = countCandles - 2;
    private final ArrayList<Candle> candlesList = new ArrayList<>();

    public int getCountCandles() {
        return countCandles;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public int getPenultimateIndex() {
        return penultimateIndex;
    }

    public ArrayList<Candle> getCandlesList() {
        return candlesList;
    }

    public void init() {
        String dbLine = readFile("C:\\Users\\Gasayni\\Desktop\\testJavaFile.txt");
        String[] masDB = dbLine.split("\n");

        for (String dbOneLine : masDB) {
            String[] pricesCandle = dbOneLine.split("\t");
            candlesList.add(new Candle(
                    // сразу оставили только 5 знаков после запятой (просто отсекли)
                    Double.parseDouble(pricesCandle[0]),
                    Double.parseDouble(pricesCandle[1]),
                    Double.parseDouble(pricesCandle[2]),
                    Double.parseDouble(pricesCandle[3]),
                    pricesCandle[4]));


        }
        /*// Проверка
        for (int i = 0; i < candlesList.size(); i++) {
            System.out.println(candlesList.get(i).getMinPrice()
                    + "\t\t" + candlesList.get(i).getMaxPrice()
                    + "\t\t" + candlesList.get(i).getOpenDayPrice()
                    + "\t\t" + candlesList.get(i).getCloseDayPrice());
        }*/
    }

    private String readFile(String path) {
        StringBuilder s = new StringBuilder();
        String line;
        try {
            FileReader file = new FileReader(path);
            BufferedReader br = new BufferedReader(file);
            while ((line = br.readLine()) != null) {
                s.append(line).append("\n");    // Записываем все строки в "s"
            }
            br.close();
            file.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return s.toString();
    }

//    public void readMyData() {
//        /*Scanner input = new Scanner(System.in);
//        System.out.println("Сколько свеч вы хотите ввести? Наберите число свеч, мин 2");
//        countCandles = input.nextInt();
//        input.nextLine();
////        Когда я nextInt читаете в буфере остается нетронутый символ конца строки (\n или \r\n), который вы и читаете следующим nextLine.
//        System.out.println("Введите " + countCandles + " последних свеч в формате:");
//
//
//        for (int i = 0; i < countCandles; i++) {
//            System.out.println("мин, мах, цена открытия, цена закрытия:");
//            String inputLine = input.nextLine();
//            System.out.println("Введена " + (i + 1) + " свеча : " + inputLine);
//            String[] pricesCandle = inputLine.split(" ");
//            candlesList.add(new Candle(
//                    Double.parseDouble(pricesCandle[0]),
//                    Double.parseDouble(pricesCandle[1]),
//                    Double.parseDouble(pricesCandle[2]),
//                    Double.parseDouble(pricesCandle[3])));
//        }*/
//
//        countCandles = 5;
//        /*// пример нисходящего потока с "молотом" в конце
//        candlesList.add(new Candle(169.41, 172.54, 171.51, 169.80));
//        candlesList.add(new Candle(165.94, 171.08, 170.00, 166.23));
//        candlesList.add(new Candle(164.18, 169.68, 166.98, 164.51));
//        candlesList.add(new Candle(162.30, 166.33, 164.42, 162.41));
//        candlesList.add(new Candle(154.70, 162.30, 160.02, 161.62)); // "молот"*/
//
//        // пример восходящего потока
//        candlesList.add(new Candle(154.70, 162.30, 160.02, 161.62));
//        candlesList.add(new Candle(162.30, 166.33, 164.42, 162.41));
//        candlesList.add(new Candle(164.18, 169.68, 166.98, 164.51));
//        candlesList.add(new Candle(165.94, 171.08, 170.00, 166.23));
//        candlesList.add(new Candle(169.41, 172.54, 171.51, 169.80));
//    }
}