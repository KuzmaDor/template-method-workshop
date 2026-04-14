import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class GameAI {
    protected String name;
    protected int gold = 0;
    protected int wood = 0;
    protected int units = 0;
    protected int structures = 10;
    protected boolean isDefeated = false;
    protected Random random = new Random();

    public GameAI(String name) {
        this.name = name;
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public String getName() {
        return name;
    }

    // Behold, the template
    public final void turn(List<GameAI> enemies) {
        if (isDefeated) return;

        System.out.println("\n> " + name + " [" + "Units: " + units + ", Buildings: " + structures + ", Gold: " + gold + ", Wood: " + wood + "]");

        if (checkDefeat()) {
            System.out.println(getDefeatMessage());
            isDefeated = true;
            return;
        }

        collectResources();

        buildStructures();

        buildUnits();

        attackPhase(enemies);
    }

    // Default losing condition that can be overwritten by subclasses.
    protected boolean checkDefeat() {
        return structures <= 0;
    }

    protected String getDefeatMessage() {
        return "    Having their settlement burned to the ground " + name + " is DEFEATED!\n";
    }

    // Common behavior for all Game AIs
    protected void collectResources() {
        if (structures > 0) {
            int gainedWood = (random.nextInt(21) + 10) * structures;
            int gainedGold = (random.nextInt(31) + 20) * structures;
            gold += gainedGold;
            wood += gainedWood;
            System.out.println("   [WORK] +" + gainedGold + "G +" + gainedWood + "W (x" + structures + " structures)");
        }
    }

    // Abstract methods to be overwritten by subclasses
    protected abstract void buildStructures();

    protected abstract void buildUnits();

    protected final void attackPhase(List<GameAI> allPlayers) {
        List<GameAI> validTargets = findValidTargets(allPlayers);

        if (!shouldAttack(validTargets)) {
            onSkipAttack(validTargets);
            return;
        }

        GameAI target = chooseTarget(validTargets);
        if (target == null) {
            onSkipAttack(validTargets);
            return;
        }

        announceAttack(target);
        resolveCombat(target);
    }

    // Hook: what counts as a valid target?
    protected List<GameAI> findValidTargets(List<GameAI> allPlayers) {
        return allPlayers.stream()
            .filter(e -> e != this && !e.isDefeated())
            .collect(Collectors.toList());
    }

    // Hook: should we attack this turn?
    protected boolean shouldAttack(List<GameAI> validTargets) {
        return !validTargets.isEmpty() && units > 0;
    }

    // Hook: message when skipping an attack.
    protected void onSkipAttack(List<GameAI> validTargets) {
        System.out.println("   [IDLE] No battles this turn.");
    }

    // Hook: how do we pick a target?
    protected GameAI chooseTarget(List<GameAI> validTargets) {
        if (validTargets.isEmpty()) return null;
        return validTargets.get(random.nextInt(validTargets.size()));
    }

    // Hook: how do we announce attacks?
    protected void announceAttack(GameAI target) {
        System.out.println(
            "   [ATTACK] " + name + " attacks " + target.getName() + " ("
                + units + " vs " + target.units + ")"
        );
    }

    // Fixed combat entry point (can be extended by overriding compareForOutcome / onVictory / onDefeat).
    protected final void resolveCombat(GameAI target) {
        int outcome = compareForOutcome(target);
        if (outcome > 0) {
            onVictory(target);
        } else {
            onDefeat(target);
        }
    }

    // Hook: decide combat outcome (>0 => attacker wins, <=0 => attacker loses)
    protected int compareForOutcome(GameAI target) {
        return Integer.compare(this.units, target.units);
    }

    protected void onVictory(GameAI target) {
        double pillageMultiplier = getPillageMultiplier();
        int stolenGold = (int) (target.gold * pillageMultiplier);
        int stolenWood = (int) (target.wood * pillageMultiplier);
        this.gold += stolenGold;
        this.wood += stolenWood;
        target.gold -= stolenGold;
        target.wood -= stolenWood;
        
        int winnerLosses = calculateWinnerLosses();
        int loserLosses = calculateLoserLosses(target);
        
        applyCasualties(target, winnerLosses, loserLosses);

        int destroyed = destroyEnemyStructures(target);

        System.out.println("   [VICTORY] Pillaged " + stolenGold + "G and " + stolenWood + "W");
        System.out.println("           Losses: " + winnerLosses + " (you), " + loserLosses + " (enemy)");
        if (destroyed > 0) {
            System.out.println("           Structures destroyed: " + destroyed);
        }
    }

    protected void onDefeat(GameAI target) {
        int loss = (int)(this.units * 0.4);
        this.units -= loss;
        System.out.println("   [DEFEAT] " + name + " was repelled by " + target.getName() + " and lost " + loss + " units");
    }

    protected double getPillageMultiplier() {
        return 0.5;
    }

    protected int calculateWinnerLosses() {
        return (int) (this.units * random.nextDouble() * 0.3);
    }

    protected int calculateLoserLosses(GameAI target) {
        return (int) (target.units * (0.5 + random.nextDouble() * 0.5));
    }

    protected void applyCasualties(GameAI target, int winnerLosses, int loserLosses) {
        this.units -= winnerLosses;
        target.units -= loserLosses;
    }

    // Hook: default structure destruction rule (min 2 .. max target.structures)
    // Subclasses may override if their win condition differs.
    protected int destroyEnemyStructures(GameAI target) {
        if (target.structures <= 0) return 0;
        int minDestroy = Math.min(getMinStructureDestruction(), target.structures);
        int maxDestroy = Math.min(getMaxStructureDestruction(target), target.structures);
        if (maxDestroy <= 0) return 0;
        if (minDestroy > maxDestroy) minDestroy = maxDestroy;

        int destroyed = minDestroy + random.nextInt(maxDestroy - minDestroy + 1);
        target.structures -= destroyed;
        return destroyed;
    }

    protected int getMinStructureDestruction() {
        return 2;
    }

    protected int getMaxStructureDestruction(GameAI target) {
        return target.structures;
    }
}
