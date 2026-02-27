package com.mycompany.project.model;

import java.sql.Date;

public class Medicine {
    private int id;
    private int patientId;
    private String name;
    private String dosage;
    private int timesPerDay;
    private Date startDate;
    private int durationDays;
    private String scheduleTimes; // e.g., "9:00,15:00,21:00"

    public Medicine() {
    }

    public Medicine(int id, int patientId, String name, String dosage, int timesPerDay, Date startDate,
            int durationDays, String scheduleTimes) {
        this.id = id;
        this.patientId = patientId;
        this.name = name;
        this.dosage = dosage;
        this.timesPerDay = timesPerDay;
        this.startDate = startDate;
        this.durationDays = durationDays;
        this.scheduleTimes = scheduleTimes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getTimesPerDay() {
        return timesPerDay;
    }

    public void setTimesPerDay(int timesPerDay) {
        this.timesPerDay = timesPerDay;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public String getScheduleTimes() {
        return scheduleTimes;
    }

    public void setScheduleTimes(String scheduleTimes) {
        this.scheduleTimes = scheduleTimes;
    }
}
