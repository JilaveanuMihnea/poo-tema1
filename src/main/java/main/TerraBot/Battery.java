package main.TerraBot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public final class Battery {
    private int currentCharge;
    private int rechargingStopTime = 0;
    private boolean isCharging = false;

    public Battery(final int initialCharge) {
        this.currentCharge = initialCharge;
    }

    /**
     * Consumes the specified amount of charge from the battery.
     * @param amount The amount of charge to consume.
     * @return True if the charge was successfully consumed, false otherwise (not enough battery).
     */
    public boolean consumeCharge(final int amount) {
        if (currentCharge >= amount) {
            currentCharge -= amount;
            return true;
        }
        return false;
    }
}
