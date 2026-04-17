public class HumanAI extends GameAI {

    public HumanAI(String name) {
        super(name);
    }

    @Override
    protected void buildStructures() {
        int built = 0;
        int buildingsCost = 25; // Cheaper buildings

        // Humans are smort and they only invest half their resources into
        // building so that they can also train units
        // (awful balancing decision, change it later)
        int buildGoldBudget = gold / 2;
        int buildWoodBudget = wood / 2;

        while (buildGoldBudget >= buildingsCost && buildWoodBudget >= buildingsCost) {
            buildGoldBudget -= buildingsCost;
            buildWoodBudget -= buildingsCost;
            structures++;
            built++;
        }

        int spentGold = (gold / 2) - buildGoldBudget;
        int spentWood = (wood / 2) - buildWoodBudget;
        gold -= spentGold;
        wood -= spentWood;

        if (built > 0) {
            System.out.println(
                "   [BUILD] 'Job's done!' Built " + built + " structures (spent " + spentGold + "G / "
                    + spentWood + "W of this turn's budget)"
            );
        }
    }

    @Override
    protected void buildUnits() {
        int trained = 0;
        while (gold >= 30 && wood >= 10 && structures > 0) {
            gold -= 30;
            wood -= 10;
            units++;
            trained++;
        }
        if (trained > 0) System.out.println("   [TRAIN] " + trained + " brave Footmen answered the call to arms! (-" + (trained*30) + "G/-" + (trained*10) + "W)");
    }

    @Override
    protected boolean shouldAttack(java.util.List<GameAI> enemies) {
        return units >= 2 && !enemies.isEmpty();
    }

    @Override
    protected void onSkipAttack(java.util.List<GameAI> validTargets) {
        System.out.println("   [IDLE] The garrison drills in perfect formation... waiting for the order to march.");
    }

    @Override
    protected void announceAttack(GameAI target) {
        System.out.println("   [ATTACK] " + name + " marches on " + target.getName() + " ("
            + units + " vs " + target.units + ") — 'For the Alliance!'");
    }
}
