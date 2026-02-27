package com.mycompany.project.model;

import java.sql.Date;

public class Measurement {
    private int id;
    private int patientId;
    private String type; // "BLOOD_PRESSURE" or "SUGAR"
    private String value; // e.g., "120/80" or "140"
    private Date recordDate;

    public Measurement() {
    }

    public Measurement(int id, int patientId, String type, String value, Date recordDate) {
        this.id = id;
        this.patientId = patientId;
        this.type = type;
        this.value = value;
        this.recordDate = recordDate;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }
}
