package com.lifeplus.lifeplus.model.Dto;

/**
 * @author Manuel Pedrozo
 */
public enum WeightUpdateFrequency {
    DAILY,
    DAYS3,
    WEEKLY,
    DAYS15,
    MONTHLY;

    public static int toDays(WeightUpdateFrequency frequency) {
        switch (frequency) {
            case DAILY:
                return 1;
            case DAYS3:
                return 3;
            case WEEKLY:
                return 7;
            case DAYS15:
                return 15;
            case MONTHLY:
                return 30;
        }
        return 7;
    }

    public static WeightUpdateFrequency toWeightUpdateFrequency(int days) {
        switch (days) {
            case 1:
                return DAILY;
            case 3:
                return DAYS3;
            case 7:
                return WEEKLY;
            case 15:
                return DAYS15;
            case 30:
                return MONTHLY;
        }
        return WEEKLY;
    }
}
