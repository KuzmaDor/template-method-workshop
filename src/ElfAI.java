public class ElfAI extends GameAI {

    public ElfAI(String name) {
        super(name);
    }

    @Override
    protected void buildStructures() {
        int built = 0;
        while (wood >= 100) {
            wood -= 80;
            structures++;
            built++;
        }
        if (built > 0) System.out.println("   [BUILD] " + built + " Ancient Trees rose majestically from the enchanted soil. (-" + (built*80) + "W)");
    }

    @Override
    protected void buildUnits() {
        int trained = 0;
        while (gold >= 40 && wood >= 20 && structures > 0) {
            gold -= 40;
            wood -= 20;
            units += 1;
            trained++;
        }
        if (trained > 0) System.out.println("   [TRAIN] " + trained + " Elite Archers stepped out gracefully from the shadows. (-" + (trained*40) + "G/-" + (trained*20) + "W)");
    }

    @Override
    protected void collectResources() {
        if (structures > 0) {
            int gainedGold = (random.nextInt(21) + 15) * structures;
            int gainedWood = (random.nextInt(31) + 25) * structures;
            gold += gainedGold;
            wood += gainedWood;
            System.out.println("   [WORK] Elves' custom resource collection method: " + gainedGold + "G, +" + gainedWood + "W.");
        }
    }

    @Override
    protected void onSkipAttack(java.util.List<GameAI> validTargets) {
        System.out.println("   [IDLE] The sentinels melt back into the treeline... the forest says 'not yet.'");
    }

    @Override
    protected void announceAttack(GameAI target) {
        System.out.println("   [ATTACK] " + name + " strikes from the leaves at " + target.getName() + " ("
            + units + " vs " + target.units + ") — 'You never saw us.'");
    }

}
