package com.example.urinemonitoring.helpers;

import com.example.urinemonitoring.models.Patient;

public class PatientHolder {
    private static Patient patient;
    private static boolean saved;

    public static Patient getPatient() {
        return patient;
    }

    public static void setPatient(Patient patient) {
        PatientHolder.patient = patient;
    }

    public static void clearPatient() {
        PatientHolder.patient = null;
    }

    public static boolean isEmpty() {
        return PatientHolder.patient == null;
    }

    public static boolean isSaved() {
        return saved;
    }

    public static void setSaved(boolean saved) {
        PatientHolder.saved = saved;
    }

    private static Patient mutablePatient;

    public static Patient getMutablePatient() {
        return mutablePatient;
    }

    public static void setMutablePatient(Patient mutablePatient) {
        PatientHolder.mutablePatient = mutablePatient;
    }
    public static void clearMutablePatient() {
        PatientHolder.mutablePatient = null;
    }

}
