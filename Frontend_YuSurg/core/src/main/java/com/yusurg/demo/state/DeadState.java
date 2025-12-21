package com.yusurg.demo.state;

import com.yusurg.demo.model.Patient;

/**
 * Dead State: Patient has died
 */
public class DeadState implements PatientState {

    @Override
    public void update(Patient patient, float delta) {
        // No updates when dead
        patient.setPulse(0);
    }

    @Override
    public String getStateName() {
        return "DEAD";
    }

    @Override
    public PatientState checkTransition(Patient patient) {
        // Dead is final (unless defibrillator is used)
        return this;
    }
}

