package com.yusurg.demo.model;

import com.yusurg.demo.state.PatientState;
import com.yusurg.demo.state.StableState;
import com.yusurg.demo.state.DeadState;
import com.yusurg.demo.manager.GameManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Patient {
    private float pulse;
    private float temperature;
    private int incisions;
    private float bloodLossRate;
    private boolean isAwake;
    private boolean siteSanitary;

    private int brokenBoneCount;
    private int shatteredBoneCount;
    private boolean antibioticUnlocked;
    private boolean isBleeding;
    private boolean feverActive;
    private boolean visibilityLow;

    private int actionsSinceAnesthesia;
    private boolean isScreamingInPain;
    private String awakenessStatus;

    private boolean isVisibilityObstructed;

    private boolean isHeartStopped;
    private int movesUntilDeath;

    private Malady activeMalady;
    private boolean isInternalTraumaFixed;
    private boolean isMaladyFixed;
    private int surgicalDepth;

    private PatientState currentState;

    private List<PatientObserver> observers;

    private Random random;

    public Patient() {
        this.observers = new ArrayList<>();
        this.random = new Random();
        reset();
        notifyObservers();
    }

    public void reset() {
        pulse = 100f;
        temperature = 98.6f;
        incisions = 0;
        bloodLossRate = 0f;
        isAwake = true;
        siteSanitary = true;
        brokenBoneCount = 0;
        shatteredBoneCount = 0;
        antibioticUnlocked = false;
        isBleeding = false;
        feverActive = false;
        visibilityLow = false;
        activeMalady = null;
        isInternalTraumaFixed = false;
        isMaladyFixed = false;
        surgicalDepth = 0;
        actionsSinceAnesthesia = 999;
        isScreamingInPain = false;
        awakenessStatus = "Awake";
        isVisibilityObstructed = false;
        isHeartStopped = false;
        movesUntilDeath = 2;
        currentState = new StableState();
        notifyObservers();
    }

    public void addObserver(PatientObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PatientObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (PatientObserver observer : observers) {
            observer.onVitalsChanged(this);
        }

        if (pulse <= 0 && !isHeartStopped && currentState instanceof DeadState) {
            for (PatientObserver observer : observers) {
                observer.onPatientDied();
            }
        }
    }

    public void update(float delta) {
    }

    public boolean resolveTurn(String toolName) {
        if (toolName.equals("Defibrillator") && isHeartStopped) {
            return true;
        }

        if (toolName.equals("Anesthetic")) {
            actionsSinceAnesthesia = 0;
            isScreamingInPain = false;
            isAwake = false;
            awakenessStatus = "Unconscious";
            notifyObservers();
            return true;
        }

        if (isScreamingInPain && !toolName.equals("Defibrillator")) {
            pulse = 0;
            currentState = new DeadState();
            GameManager.getInstance().setGameLost();
            notifyObservers();
            return false;
        }

        if (actionsSinceAnesthesia >= 4 && incisions > 0) {
            if (toolName.equals("Scalpel")) {
                pulse = 0;
                currentState = new DeadState();
                GameManager.getInstance().setGameLost();
                notifyObservers();
                return false;
            }

            if (!toolName.equals("Defibrillator")) {
                isScreamingInPain = true;
                awakenessStatus = "Patient Screams Painfully!";
                notifyObservers();
            }
            return true;
        }

        actionsSinceAnesthesia++;
        updateAwakenessStatus();
        notifyObservers();
        return true;
    }

    private void updateAwakenessStatus() {
        if (actionsSinceAnesthesia < 3) {
            awakenessStatus = "Unconscious";
            isAwake = false;
        } else if (actionsSinceAnesthesia == 3) {
            awakenessStatus = "Coming To!";
            isAwake = false;
        } else if (actionsSinceAnesthesia >= 4) {
            awakenessStatus = "Awake";
            isAwake = true;
        }
    }

    public void onToolUsed(String toolName) {
        boolean skipRandomEffects = toolName.equals("Defibrillator") || toolName.equals("Sponge");

        if (incisions > 0) {
            float pulseDecay = incisions * 2.0f;
            setPulse(pulse - pulseDecay);
        }

        if (!siteSanitary && isBleeding) {
            float tempIncrease = 1.0f;
            setTemperature(temperature + tempIncrease);
        }

        if (isAwake && incisions > 0) {
            float extraDecay = incisions * 1.0f;
            setPulse(pulse - extraDecay);
        }

        if (!skipRandomEffects && activeMalady != null && activeMalady.requiresFeverManagement()) {
            float feverChance = 0.05f + random.nextFloat() * 0.05f;
            if (random.nextFloat() < feverChance) {
                float tempRise = 0.5f + random.nextFloat() * 1.0f;
                setTemperature(temperature + tempRise);
            }
        }

        DifficultyLevel currentDifficulty = GameManager.getInstance().getCurrentDifficulty();
        boolean heartStopAllowed = (currentDifficulty == DifficultyLevel.MEDIUM || currentDifficulty == DifficultyLevel.HARD);
        float heartStopChance = currentDifficulty == DifficultyLevel.MEDIUM ? 0.10f : 0.20f;

        if (!skipRandomEffects && !isHeartStopped && heartStopAllowed && !isAwake && random.nextFloat() < heartStopChance) {
            isHeartStopped = true;
            pulse = 0;
            movesUntilDeath = 2;
        }

        if (!skipRandomEffects && !isVisibilityObstructed && random.nextFloat() < 0.10f) {
            isVisibilityObstructed = true;
        }

        PatientState newState = currentState.checkTransition(this);
        if (newState != currentState) {
            currentState = newState;
            if (currentState instanceof DeadState) {
                GameManager.getInstance().setGameLost();
            }
        }

        notifyObservers();
    }

    public void onToolUsed() {
        onToolUsed("");
    }

    private void processChainReactions() {
        if (incisions > 0) {
            isBleeding = true;
            bloodLossRate = incisions * 2.0f;
        } else {
            isBleeding = false;
            bloodLossRate = 0f;
        }

        if (isBleeding && siteSanitary) {
            if (random.nextFloat() < 0.02f) {
                siteSanitary = false;
            }
        }

        if (!siteSanitary && isBleeding) {
            feverActive = true;
        }

        if (isAwake && incisions > 0) {
            bloodLossRate *= 2.0f;
            visibilityLow = true;
        } else {
            visibilityLow = false;
        }
    }

    public void setPulse(float pulse) {
        this.pulse = Math.max(0, Math.min(100, pulse));
        notifyObservers();
    }

    public void setTemperature(float temperature) {
        this.temperature = Math.max(90f, Math.min(110f, temperature));
        notifyObservers();
    }

    public void addIncision() {
        this.incisions++;
        notifyObservers();
    }

    public void removeIncision() {
        this.incisions = Math.max(0, this.incisions - 1);
        notifyObservers();
    }

    public void setAwake(boolean awake) {
        this.isAwake = awake;
        notifyObservers();
    }

    public void setSiteSanitary(boolean sanitary) {
        this.siteSanitary = sanitary;
        if (sanitary) {
            feverActive = false;
        }
        notifyObservers();
    }

    public void unlockAntibiotics() {
        this.antibioticUnlocked = true;
        notifyObservers();
    }

    public void addBrokenBone() {
        this.brokenBoneCount++;
        notifyObservers();
    }

    public void removeBrokenBone() {
        this.brokenBoneCount = Math.max(0, this.brokenBoneCount - 1);
        notifyObservers();
    }

    public void addShatteredBone() {
        this.shatteredBoneCount++;
        notifyObservers();
    }

    public void removeShatteredBone() {
        this.shatteredBoneCount = Math.max(0, this.shatteredBoneCount - 1);
        notifyObservers();
    }

    public void setVisibilityLow(boolean low) {
        this.visibilityLow = low;
        notifyObservers();
    }

    public void incrementSurgicalDepth() {
        this.surgicalDepth++;
        notifyObservers();
    }

    public void setMaladyFixed(boolean fixed) {
        this.isMaladyFixed = fixed;
        notifyObservers();
    }

    public void setVisibilityObstructed(boolean obstructed) {
        this.isVisibilityObstructed = obstructed;
        notifyObservers();
    }

    public boolean isVisibilityObstructed() {
        return isVisibilityObstructed;
    }

    public void setHeartStopped(boolean stopped) {
        this.isHeartStopped = stopped;
        if (!stopped) {
            movesUntilDeath = 2;
        }
        notifyObservers();
    }

    public boolean isHeartStopped() {
        return isHeartStopped;
    }

    public int getMovesUntilDeath() {
        return movesUntilDeath;
    }

    public void setMovesUntilDeath(int moves) {
        this.movesUntilDeath = moves;
        notifyObservers();
    }

    public boolean decrementHeartStopCounter() {
        if (isHeartStopped) {
            movesUntilDeath--;
            if (movesUntilDeath <= 0) {
                isHeartStopped = false;
                pulse = 0;
                currentState = new DeadState();
                GameManager.getInstance().setGameLost();
                notifyObservers();
                return false;
            }
            notifyObservers();
        }
        return true;
    }

    public float getPulse() { return pulse; }
    public float getTemperature() { return temperature; }
    public int getIncisions() { return incisions; }
    public float getBloodLossRate() { return bloodLossRate; }
    public boolean isAwake() { return isAwake; }
    public boolean isSiteSanitary() { return siteSanitary; }
    public int getBrokenBoneCount() { return brokenBoneCount; }
    public int getShatteredBoneCount() { return shatteredBoneCount; }
    public boolean isAntibioticUnlocked() { return antibioticUnlocked; }
    public boolean isBleeding() { return isBleeding; }
    public boolean isFeverActive() { return feverActive; }
    public boolean isVisibilityLow() { return visibilityLow; }
    public String getCurrentStateName() { return currentState.getStateName(); }
    public boolean isDead() { return currentState instanceof DeadState; }
    public Malady getActiveMalady() { return activeMalady; }
    public int getSurgicalDepth() { return surgicalDepth; }
    public boolean isMaladyFixed() { return isMaladyFixed; }
    public int getActionsSinceAnesthesia() { return actionsSinceAnesthesia; }
    public boolean isScreamingInPain() { return isScreamingInPain; }
    public String getAwakenessStatus() { return awakenessStatus; }

    public void setActiveMalady(Malady malady) {
        this.activeMalady = malady;

        if (malady == null) {
            return;
        }

        String name = malady.getName();

        if (name.contains("Flu")) {
            float highFever = 105.0f + random.nextFloat() * 4.0f;
            this.temperature = highFever;
            this.feverActive = true;
            notifyObservers();
        }
    }

    public void markInternalTraumaFixed() {
        this.isInternalTraumaFixed = true;
    }

    public boolean isMaladyCured() {
        if (activeMalady == null) {
            return true;
        }

        String name = activeMalady.getName();

        if (activeMalady.requiresFixItButton() && !isMaladyFixed) {
            return false;
        }

        boolean bonesOk = (brokenBoneCount == 0 && shatteredBoneCount == 0);
        boolean tempOk = !activeMalady.requiresFeverManagement() || temperature <= 98.6f;
        boolean incisionsOk = (incisions == 0);

        if (activeMalady.requiresFixItButton()) {
            if (name.contains("Broken") || name.contains("Bone") || name.contains("Trauma") ||
                name.contains("Shattered") || name.contains("Grumbleteeth")) {
                return incisionsOk && tempOk && bonesOk;
            }
            return incisionsOk && tempOk;
        }

        if (name.contains("Flu")) {
            return temperature <= 98.6f;
        }

        if (name.contains("Broken") || name.contains("Bone") || name.contains("Arm") || name.contains("Leg")) {
            return bonesOk;
        }

        return pulse > 50 && tempOk && incisionsOk;
    }

    public boolean isReadyToFinish() {
        if (isDead()) {
            return false;
        }

        if (temperature > 98.6f) {
            return false;
        }

        if (incisions > 0) {
            return false;
        }

        if (brokenBoneCount > 0 || shatteredBoneCount > 0) {
            return false;
        }

        if (!isMaladyCured()) {
            return false;
        }

        return true;
    }

    public boolean isSurgeryComplete() {
        return isReadyToFinish();
    }
}

