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

        System.out.println("step 1.");
        collectResources();

        System.out.println("step 2.");
        buildStructures();

        if (checkDefeat()) {
            System.out.println("optional step 2,5.");
            System.out.println(getDefeatMessage());
            isDefeated = true;
            return;
        }

        System.out.println("step 3.");
        buildUnits();
        performAttackPhase(enemies);
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
            System.out.println("   [WORK] Generic workers gathered +" + gainedGold + "G, +" + gainedWood + "W.");
        }
    }

    // Abstract methods to be overwritten by subclasses
    protected abstract void buildStructures();
    protected abstract void buildUnits();

    // The attack phase logic
    protected final void performAttackPhase(List<GameAI> enemies) {
        List<GameAI> validTargets = enemies.stream()
            .filter(e -> e != this && !e.isDefeated())
            .toList();

        // Subclasses may optionally decide whether to attack or not
        if (!shouldAttack(validTargets)) {
            System.out.println("   [IDLE] No battles fought this turn.");
            return;
        }

        GameAI target = validTargets.get(random.nextInt(validTargets.size()));
        System.out.println("   [ATTACK] " + name + " engages " + target.getName() + "! (" + units + " units vs " + target.units + " units)");
        executeCombat(target);
    }

    protected boolean shouldAttack(List<GameAI> validTargets) {
        return !validTargets.isEmpty() && units > 0;
    }

    protected void executeCombat(GameAI target) {
        if (this.units > target.units) {
            onVictory(target);
        } else {
            onDefeat(target);
        }
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

        int minDestroy = Math.min(2, target.structures);
        int destroyed = target.structures > 0 ? minDestroy + random.nextInt(target.structures - minDestroy + 1) : 0;
        target.structures -= destroyed;

        System.out.println("   [VICTORY] DECISIVE VICTORY! Pillaged " + stolenGold + "G & " + stolenWood + "W.");
        System.out.println("       Casualties: Lost " + winnerLosses + " units. Slain " + loserLosses + " enemies.");
        if (destroyed > 0) System.out.println("       [DESTROY] Razed " + destroyed + " enemy structures to the ground!\n");
    }

    protected void onDefeat(GameAI target) {
        int loss = (int)(this.units * 0.4);
        this.units -= loss;
        System.out.println("   [DEFEAT] DISASTROUS DEFEAT. The assault failed, losing " + loss + " units in the bloody retreat.");
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
}
