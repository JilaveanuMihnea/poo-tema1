package main.TerraBot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Battery {
    private int currentCharge;
    private int rechargingStopTime = 0;
    private boolean isCharging = false;

    public Battery(int initialCharge) {
        this.currentCharge = initialCharge;
    }

    public boolean consumeCharge(int amount) {
        if (currentCharge >= amount) {
            currentCharge -= amount;
            return true;
        }
        return false;
    }
}
