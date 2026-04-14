public class OrcAI extends GameAI {

    public OrcAI(String name) {
        super(name);
    }

    @Override
    protected boolean checkDefeat() {
        // Orcs only lose if they have no units and no structures (to build more)
        return units <= 0 && structures <= 0;
    }

    @Override
    protected String getDefeatMessage() {
        return "    The horde has been completely wiped out... " + name + " is DEFEATED!\n";
    }

    @Override
    protected double getPillageMultiplier() {
        return 0.8; // Orcs steal 80% instead of 50%
    }

    @Override
    protected void buildStructures() {}

    @Override
    protected void buildUnits() {
        int trained = 0;
        while (gold >= 25 && structures > 0) {
            gold -= 25;
            units += 2; // Cheap, fast unit production
            trained += 2;
        }
        if (trained > 0) System.out.println("   [TRAIN] " + trained + " Grunts emerged screaming from the pits! (-" + (trained*12) + "G)");
    }

    // Orcs attack whenever they have at least a small mob worth sending.
    // (No printing here: the template method will call onSkipAttack()/announceAttack().)
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
