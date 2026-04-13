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
    protected void buildStructures() {
        // Orcs don't build structures anymore
        System.out.println("   [IDLE] Orcs don't build, they only WAAAGH!");
    }

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

    // Custom condition for Orc attack
    @Override
    protected boolean shouldAttack(java.util.List<GameAI> validTargets) {
        if (!validTargets.isEmpty() && units >= 2) {
            System.out.println("   [WAAAGH!] The warlord howls as the horde blindly charges!");
            return true;
        } else {
            System.out.println("   [IDLE] The warlord grumbles, gathering more boys for a proper WAAAGH.");
            return false;
        }
    }
}
