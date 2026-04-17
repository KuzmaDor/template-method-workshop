public class OrcAI extends GameAI {

    public OrcAI(String name) {
        super(name);
    }

    @Override
    protected boolean checkDefeat() {
        return units <= 0 && structures <= 0;
    }

    @Override
    protected String getDefeatMessage() {
        return "    The horde has been completely wiped out... " + name + " is DEFEATED!\n";
    }

    @Override
    protected double getPillageMultiplier() {
        return 0.8; // Orcs steal 80% instead of 50% to make up for no resource generation from buildings
    }

    @Override
    protected void buildStructures() {}

    @Override
    protected void buildUnits() {
        int trained = 0;
        while (gold >= 30 && wood >= 10 && structures > 0) {
            gold -= 30;
            wood -= 10;
            units++;
            trained++;
        }
        if (trained > 0) System.out.println("   [TRAIN] " + trained + " Grunts emerged screaming from the pits! (-" + (trained*12) + "G)");
    }

    @Override
    protected boolean shouldAttack(java.util.List<GameAI> validTargets) {
        return !validTargets.isEmpty() && units >= 2;
    }

    @Override
    protected void onSkipAttack(java.util.List<GameAI> validTargets) {
        System.out.println("   [IDLE] The warlord grumbles: 'Not enough boyz yet... next turn, BIG WAAAGH.'");
    }

    @Override
    protected void announceAttack(GameAI target) {
        System.out.println("   [ATTACK] " + name + " unleashes a WAAAGH upon " + target.getName() + " ("
            + units + " vs " + target.units + ")");
    }
}
