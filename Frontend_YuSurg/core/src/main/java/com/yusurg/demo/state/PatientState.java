package com.yusurg.demo.state;

import com.yusurg.demo.model.Patient;

/**
 * State Pattern Interface
 * Purpose: Handle behavior changes based on patient health
 */
public interface PatientState {
    void update(Patient patient, float delta);
    String getStateName();
    PatientState checkTransition(Patient patient);
}

