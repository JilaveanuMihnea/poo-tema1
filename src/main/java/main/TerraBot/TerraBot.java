package main.TerraBot;

import fileio.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.Behaviours.Movable;
import main.MapUtility.Cell;
import main.MapUtility.TerraMap;

import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
public class TerraBot implements Movable {
    private Battery battery;
    private PairInput position;
    private boolean hasEnergyForMove = true;

    private PairInput[] directions = {
            new PairInput(0, 1),   // Up
            new PairInput(1, 0),   // Right
            new PairInput(0, -1),  // Down
            new PairInput(-1, 0)   // Left
    };
    private TerraMap terraMap;

    public TerraBot(final int initialCharge, final TerraMap terraMap) {
        position = new PairInput(0, 0);
        this.battery = new Battery(initialCharge);
        this.terraMap = terraMap;
    }

    public void recharge(int extraCharge, int currentTimestamp) {
        battery.setCurrentCharge(battery.getCurrentCharge() + extraCharge);
        battery.setRechargingStopTime(currentTimestamp + extraCharge);
        battery.setCharging(true);
    }

    public boolean isCharging() {
        return battery.isCharging();
    }

    @Override
    public void move() {
        int bestConsumption = Integer.MAX_VALUE;
        int choiceIndex = -1;
        for (PairInput direction : directions) {
            int newX = position.getX() + direction.getX();
            int newY = position.getY() + direction.getY();
            Cell nextCell = terraMap.getCell(newX, newY);

            if (nextCell != null) {
                if (nextCell.computeConsumption() < bestConsumption) {
                    bestConsumption = nextCell.computeConsumption();
                    choiceIndex = Arrays.asList(directions).indexOf(direction);
                }
            }
        }
        if (battery.consumeCharge(bestConsumption)) {
            hasEnergyForMove = true;
            position.setX(position.getX() + directions[choiceIndex].getX());
            position.setY(position.getY() + directions[choiceIndex].getY());
        } else {
            hasEnergyForMove = false;
        }
    }
}