package com.yusurg.demo.state;

import com.yusurg.demo.model.Patient;

/**
 * Critical State: Faster pulse decay rate
 */
public class CriticalState implements PatientState {

    @Override
    public void update(Patient patient, float delta) {
        // Faster pulse decay in critical state
        if (patient.getPulse() > 0) {
            float pulseDecay = delta * 2.0f; // 2x normal rate

            // Increase decay if bleeding
            if (patient.getIncisions() > 0) {
                pulseDecay += patient.getBloodLossRate() * delta * 1.5f;
            }

            patient.setPulse(patient.getPulse() - pulseDecay);
        }

        // Temperature changes faster
        if (patient.isFeverActive()) {
            patient.setTemperature(patient.getTemperature() + delta * 1.0f);
        }
    }

    @Override
    public String getStateName() {
        return "CRITICAL";
    }

    @Override
    public PatientState checkTransition(Patient patient) {
        // Don't transition to dead if heart is just stopped (recoverable with Defibrillator)
        if (patient.getPulse() <= 0 && !patient.isHeartStopped()) {
            return new DeadState();
        } else if (patient.getPulse() >= 50) {
            return new StableState();
        }
        return this;
    }
}

