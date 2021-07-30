package com.lifeplus.lifeplus.model.form;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Manuel Pedrozo
 */
public class OfflineSensorDataForm {

    @NotNull
    private int bpm;

    @NotNull
    private LocalDateTime date;

    public OfflineSensorDataForm() {
    }

    public OfflineSensorDataForm(@NotNull int bpm, @NotNull LocalDateTime date) {
        this.bpm = bpm;
        this.date = date;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
