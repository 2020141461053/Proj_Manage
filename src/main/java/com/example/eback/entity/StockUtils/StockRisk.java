package com.example.eback.entity.StockUtils;

import java.util.List;

public class StockRisk {
    private List<Float> priceHistory;

    public StockRisk(List<Float> priceHistory) {
        this.priceHistory = priceHistory;
    }

    public float calculateMovingAverage() {
        float sum = 0;
        for (Float price : priceHistory) {
            sum += price;
        }
        return sum / priceHistory.size();
    }

    public float calculateStandardDeviation() {
        float mean = calculateMovingAverage();
        float sum = 0;
        for (Float price : priceHistory) {
            sum += Math.pow(price - mean, 2);
        }
        return (float)Math.sqrt(sum / priceHistory.size());
    }
    public String determineRiskType() {
        float standardDeviation = calculateStandardDeviation();
        if (standardDeviation <= 1) {
            return "Low Risk";
        } else if (standardDeviation <= 2) {
            return "Medium Risk";
        } else {
            return "High Risk";
        }
    }

}
