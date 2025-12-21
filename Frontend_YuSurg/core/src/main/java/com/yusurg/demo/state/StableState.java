package com.yusurg.demo.state;

import com.yusurg.demo.model.Patient;

/**
 * Stable State: Normal pulse decay rate
 */
public class StableState implements PatientState {

    @Override
    public void update(Patient patient, float delta) {
        // Normal pulse decay
        if (patient.getPulse() > 0) {
            float pulseDecay = delta * 1.0f; // 1 pulse per second

            // Increase decay if bleeding
            if (patient.getIncisions() > 0) {
                pulseDecay += patient.getBloodLossRate() * delta;
            }

            patient.setPulse(patient.getPulse() - pulseDecay);
        }

        // Temperature changes
        if (patient.isFeverActive()) {
            patient.setTemperature(patient.getTemperature() + delta * 0.5f);
        } else if (patient.getTemperature() > 98.6f) {
            patient.setTemperature(patient.getTemperature() - delta * 0.2f);
        }
    }

    @Override
    public String getStateName() {
        return "STABLE";
    }

    @Override
    public PatientState checkTransition(Patient patient) {
        // Don't transition to dead if heart is just stopped (recoverable with Defibrillator)
        if (patient.getPulse() <= 0 && !patient.isHeartStopped()) {
            return new DeadState();
        } else if (patient.getPulse() < 30 && !patient.isHeartStopped()) {
            return new CriticalState();
        }
        return this;
    }
}

