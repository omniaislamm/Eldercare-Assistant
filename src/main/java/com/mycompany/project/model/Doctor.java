package com.mycompany.project.model;

public class Doctor {
    private int id;
    private String name;
    private String phone;
    private String clinicAddress;
    private String schedule;
    private String appointmentTime; // e.g., "10:00"

    public Doctor() {
    }

    public Doctor(int id, String name, String phone, String clinicAddress, String schedule, String appointmentTime) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.clinicAddress = clinicAddress;
        this.schedule = schedule;
        this.appointmentTime = appointmentTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    @Override
    public String toString() {
        return name + " - " + clinicAddress;
    }
}
