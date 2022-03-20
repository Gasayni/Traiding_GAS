package com.company;

public class Candle {
    private Double minPrice;
    private Double maxPrice;
    private Double openDayPrice;
    private Double closeDayPrice;
    private boolean whiteOrBlack = false;

    public Candle(Double minPrice, Double maxPrice, Double openDayPrice, Double closeDayPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.openDayPrice = openDayPrice;
        this.closeDayPrice = closeDayPrice;

        // !!! Какой выбрать цвет, если цена открытия равна цене закрытия
        if (openDayPrice < closeDayPrice) {  // определяем цвет свечи
            this.whiteOrBlack = true;
        }

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

    public boolean isWhiteOrBlack() {
        return whiteOrBlack;
    }
}
