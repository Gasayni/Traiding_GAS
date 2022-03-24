package com.company;

import static java.lang.Math.*;

public class Candle {
    private final Double minPrice;
    private final Double maxPrice;
    private final Double openDayPrice;
    private final Double upBodyPrice;
    private final Double downBodyPrice;
    private final Double closeDayPrice;
    private boolean whiteColor = false;
    private final double candleLength;
    private final double bodyLength;
    private double upShadowLength;
    private double downShadowLength;
    private final double bodyToCandleRatio;
    private final double downShadowToCandle;
    private final double downShadowToBody;
    private final double upShadowToCandle;
    private final double upShadowToBody;
    private final String date;

    public Candle(Double minPrice, Double maxPrice, Double openDayPrice, Double closeDayPrice, String date) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.openDayPrice = openDayPrice;
        this.closeDayPrice = closeDayPrice;
        this.date = date;


        if (openDayPrice < closeDayPrice) {  // определяем цвет свечи
            this.whiteColor = true;
        }
        upBodyPrice = maxPrice - upShadowLength;
        downBodyPrice = minPrice + downShadowLength;
        // длина всей свечи
        candleLength = abs(maxPrice - minPrice);
        // длина тела свечи
        bodyLength = abs(openDayPrice - closeDayPrice);
        // длина верхней тени
        if (whiteColor) upShadowLength = maxPrice - closeDayPrice;
        else upShadowLength = maxPrice - openDayPrice;
        // длина нижней тени
        if (whiteColor) downShadowLength = openDayPrice - minPrice;
        else downShadowLength = closeDayPrice - minPrice;
        // отношение длины тела свечи ко всей длине свечи
        bodyToCandleRatio = bodyLength / candleLength;
        // отношение длины нижней тени ко всей длине свечи
        downShadowToCandle = downShadowLength / candleLength;
        // отношение длины нижней тени к длине тела свечи
        downShadowToBody = downShadowLength / bodyLength;
        // отношение длины верхней тени ко всей длине свечи
        upShadowToCandle = upShadowLength / candleLength;
        // отношение длины верхней тени к длине тела свечи
        upShadowToBody = upShadowLength / bodyLength;

        // !!! Какой выбрать цвет, если цена открытия равна цене закрытия?
        // Тут еще д.б условие Дожи (нужно точно знать его характеристики)
        // маленькое тело относительно теней (какое д.б. отношение)
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public Double getOpenDayPrice() {
        return openDayPrice;
    }

    public Double getCloseDayPrice() {
        return closeDayPrice;
    }

    public double getCandleLength() {
        return candleLength;
    }

    public double getBodyLength() {
        return bodyLength;
    }

    public double getUpShadowLength() {
        return upShadowLength;
    }

    public double getDownShadowLength() {
        return downShadowLength;
    }

    public double getBodyToCandleRatio() {
        return bodyToCandleRatio;
    }

    public double getDownShadowToCandle() {
        return downShadowToCandle;
    }

    public double getDownShadowToBody() {
        return downShadowToBody;
    }

    public double getUpShadowToCandle() {
        return upShadowToCandle;
    }

    public double getUpShadowToBody() {
        return upShadowToBody;
    }

    public Double getUpBodyPrice() {
        return upBodyPrice;
    }

    public Double getDownBodyPrice() {
        return downBodyPrice;
    }

    public boolean isWhiteColor() {
        return whiteColor;
    }

    public String getDate() {
        return date;
    }
}
