public class UndeadAI extends GameAI {

    public UndeadAI(String name) {
        super(name);
    }

    @Override
    protected void collectResources() {
        if (structures > 0) {
            int gainedGold = (random.nextInt(41) + 30) * structures;
            int gainedWood = (random.nextInt(11) + 5) * structures;
            gold += gainedGold;
            wood += gainedWood;
            System.out.println("   [WORK] Mindless Acolytes tirelessly mined the haunted mines: +" + gainedGold + "G, +" + gainedWood + "W.");
        }
    }

    @Override
    protected void buildStructures() {
        int built = 0;
        while (gold >= 60) {
            gold -= 60;
            structures++;
            built++;
        }
        if (built > 0) System.out.println("   [BUILD] The blight spread as " + built + " jagged Ziggurats materialized! (-" + (built*60) + "G)");
    }

    @Override
    protected void buildUnits() {
        int trained = 0;
        while (gold >= 20 && structures > 0) {
            gold -= 20;
            units += 3;
            trained += 3;
        }
        if (trained > 0) System.out.println("   [TRAIN] " + trained + " ravenous Ghouls violently clawed their way out of the graveyard! (-" + (trained*6) + "G)");
    }

    @Override
    protected boolean shouldAttack(java.util.List<GameAI> enemies) {
        return units > 5 && !enemies.isEmpty();
    }

    @Override
    protected void onSkipAttack(java.util.List<GameAI> validTargets) {
        System.out.println("   [IDLE] The Scourge whispers in the dark... more corpses must be prepared.");
    }

    @Override
    protected void announceAttack(GameAI target) {
        System.out.println("   [ATTACK] " + name + " surges toward " + target.getName() + " ("
            + units + " vs " + target.units + ") — 'For the Lich King.'");
    }

    @Override
    protected void applyCasualties(GameAI target, int winnerLosses, int loserLosses) {
        this.units += loserLosses; // Gain the defeated enemies!
        this.units -= winnerLosses;
        target.units -= loserLosses;
        System.out.println("   [NECROMANCY] " + loserLosses + " fallen enemies were raised to serve the Scourge!");
    }
}
