package com.yusurg.demo.model;

public interface PatientObserver {
    void onVitalsChanged(Patient patient);
    void onPatientDied();
}

