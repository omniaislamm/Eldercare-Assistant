// تحديد مكان الكلاس (الباقة)
package com.mycompany.project.controller;

// استيراد المكتبات اللازمة
import com.mycompany.project.model.Doctor;
import com.mycompany.project.model.Medicine;
import com.mycompany.project.model.User;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import java.util.List;

// هذا الكلاس مسؤول عن إدارة التنبيهات للأدوية ومواعيد الأطباء
// تم تبسيطه ليسهل فهمه وتعديله
public class AlertController {

    private MedicineController medicineController;
    private DoctorController doctorController;

    // الموعد يظهر قبل 30 دقيقة
    private static final int REMINDER_MINUTES_BEFORE = 30;

    public AlertController() {
        this.medicineController = new MedicineController();
        this.doctorController = new DoctorController();
    }

    // دالة الترحيب البسيطة
    public String getWelcomeMessage(String username) {
        return "Welcome back, " + username + "!";
    }

    // الدالة الرئيسية لفحص التنبيهات
    // تقوم بفحص الأدوية والأطباء وترجع رسالة إذا وجد تنبيه قريب
    public String checkForAlerts(User user) {
        // التنبيهات للمرضى فقط
        if (!"PATIENT".equals(user.getRole())) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        StringBuilder alertMessage = new StringBuilder();

        // 1. فحص الأدوية
        String medicineAlert = checkMedicines(user.getId(), now);
        if (medicineAlert != null) {
            alertMessage.append(medicineAlert);
        }

        // 2. فحص الأطباء
        String doctorAlert = checkDoctors(user.getId(), now);
        if (doctorAlert != null) {
            if (alertMessage.length() > 0)
                alertMessage.append("\n\n"); // سطر جديد للفصل
            alertMessage.append(doctorAlert);
        }

        // إذا لم يكن هناك أي تنبيه، نرجع null
        if (alertMessage.length() == 0) {
            return null;
        }

        return alertMessage.toString();
    }

    // دالة فرعية لفحص مواعيد الأدوية
    private String checkMedicines(int patientId, LocalDateTime now) {
        List<Medicine> medicines = medicineController.getMedicinesForPatient(patientId);
        StringBuilder msg = new StringBuilder();
        boolean hasAlert = false;

        for (Medicine m : medicines) {
            // نحصل على وقت الدواء القادم
            LocalTime nextTime = getNextMedicineTime(m);

            // نحسب الفرق بالدقائق
            long minutesUntil = ChronoUnit.MINUTES.between(now.toLocalTime(), nextTime);

            // لو الوقت فات النهاردة، بنحسبه لبكرة (بنضيف 24 ساعة)
            if (minutesUntil < -REMINDER_MINUTES_BEFORE) {
                minutesUntil += 24 * 60;
            }

            // إذا باقي 30 دقيقة أو أقل (والوقت لسه ما جاش أو لسه معدي حالا)
            if (minutesUntil <= REMINDER_MINUTES_BEFORE && minutesUntil >= 0) {
                if (!hasAlert) {
                    msg.append("💊 Medicine Reminder:\n");
                    hasAlert = true;
                }
                msg.append("- Take ").append(m.getName())
                        .append(" (").append(m.getDosage()).append(")\n");
            }
        }
        return hasAlert ? msg.toString() : null;
    }

    // دالة فرعية لفحص مواعيد الأطباء
    private String checkDoctors(int patientId, LocalDateTime now) {
        List<Doctor> doctors = doctorController.getDoctorsForPatient(patientId);
        StringBuilder msg = new StringBuilder();
        boolean hasAlert = false;

        for (Doctor d : doctors) {
            // نحاول قراءة وقت الموعد
            LocalTime appointmentTime = parseTime(d.getAppointmentTime());

            if (appointmentTime != null) {
                long minutesUntil = ChronoUnit.MINUTES.between(now.toLocalTime(), appointmentTime);

                // معالجة فرق الوقت لليوم التالي
                if (minutesUntil < -REMINDER_MINUTES_BEFORE) {
                    minutesUntil += 24 * 60;
                }

                if (minutesUntil <= REMINDER_MINUTES_BEFORE && minutesUntil >= 0) {
                    if (!hasAlert) {
                        msg.append("🩺 Doctor Appointment:\n");
                        hasAlert = true;
                    }
                    msg.append("- Dr. ").append(d.getName())
                            .append(" at ").append(d.getClinicAddress()).append("\n");
                }
            }
        }
        return hasAlert ? msg.toString() : null;
    }

    // دالة بسيطة لمعرفة وقت الدواء بناء على عدد المرات
    // تم التبسيط: بنفترض مواعيد ثابتة لسهولة المشروع
    private LocalTime getNextMedicineTime(Medicine m) {
        // لو الدواء اسمه TEST بنرجع الوقت الحالي عشان نجرب
        if (m.getName().toUpperCase().startsWith("TEST")) {
            return LocalTime.now();
        }

        LocalTime now = LocalTime.now();

        // أولاً: نشوف لو المستخدم حدد مواعيد مخصصة
        String schedule = m.getScheduleTimes();
        if (schedule != null && !schedule.trim().isEmpty()) {
            try {
                String[] timeParts = schedule.split(",");
                LocalTime[] customTimes = new LocalTime[timeParts.length];

                for (int i = 0; i < timeParts.length; i++) {
                    customTimes[i] = LocalTime.parse(timeParts[i].trim(),
                            java.time.format.DateTimeFormatter.ofPattern("H:mm"));
                }

                // نلاقي أقرب وقت جاي
                return findNextTime(customTimes, now);
            } catch (Exception e) {
                // لو فيه مشكلة في القراءة، نستخدم المواعيد الافتراضية
            }
        }

        // ثانياً: لو مافيش مواعيد مخصصة، نستخدم المواعيد الثابتة
        int times = m.getTimesPerDay();

        // مواعيد ثابتة ومبسطة
        LocalTime t1 = LocalTime.of(9, 0); // 9 صباحا
        LocalTime t2 = LocalTime.of(15, 0); // 3 عصرا
        LocalTime t3 = LocalTime.of(21, 0); // 9 مساء

        // لو مرة واحدة: الساعة 9
        if (times == 1)
            return t1;

        // لو مرتين: 9 صباحاً و 9 مساءً
        if (times == 2) {
            // لو عدينا 9 الصبح، يبقى الميعاد الجاي 9 بليل
            if (now.isAfter(t1) && now.isBefore(t3))
                return t3;
            return t1; // غير كده (الصبح بدري أو بليل متأخر) يبقى 9 الصبح
        }

        // لو 3 مرات
        if (times >= 3) {
            if (now.isAfter(t1) && now.isBefore(t2))
                return t2;
            if (now.isAfter(t2) && now.isBefore(t3))
                return t3;
            return t1;
        }

        return t1; // الافتراضي
    }

    // دالة مساعدة: تلاقي أقرب وقت جاي من مجموعة مواعيد
    private LocalTime findNextTime(LocalTime[] times, LocalTime now) {
        LocalTime nextTime = null;
        long minDiff = Long.MAX_VALUE;

        for (LocalTime time : times) {
            long diff = ChronoUnit.MINUTES.between(now, time);

            // لو الوقت فات، نحسبه لبكرة
            if (diff < 0) {
                diff += 24 * 60;
            }

            // نختار أقرب وقت
            if (diff < minDiff) {
                minDiff = diff;
                nextTime = time;
            }
        }

        return nextTime != null ? nextTime : times[0];
    }

    // دالة مساعدة لتحويل النص لوقت
    private LocalTime parseTime(String timeStr) {
        try {
            if (timeStr != null && !timeStr.trim().isEmpty()) {
                // بنحاول نقرأ الوقت بصيغة الساعة:الدقيقة
                return LocalTime.parse(timeStr.trim(), java.time.format.DateTimeFormatter.ofPattern("H:mm"));
            }
        } catch (Exception e) {
            // لو الصيغة غلط مش مشكلة، نتجاهلها
        }
        return null;
    }

    // دالة بسيطة لنصائح صحية
    public String getHealthTip() {
        return "Don't forget to drink water!";
    }
}
