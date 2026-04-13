public class ElfAI extends GameAI {

    public ElfAI(String name) {
        super(name);
    }

    @Override
    protected void buildStructures() {
        int built = 0;
        // Elves balance structures and units, so they save some wood for units
        while (wood >= 100) { // Keep at least 20 wood for one unit
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

    // Elf overrides the resource collection method
    @Override
    protected void collectResources() {
        if (structures > 0) {
            int gainedGold = (random.nextInt(21) + 15) * structures;
            int gainedWood = (random.nextInt(31) + 25) * structures; // Elves get more wood
            gold += gainedGold;
            wood += gainedWood;
            System.out.println("   [WORK] Wisps gently coaxed the forest: +" + gainedGold + "G, +" + gainedWood + "W.");
        }
    }
    
    // Elf overrides attack to simulate ranged combat advantage
    @Override
    protected boolean shouldAttack(java.util.List<GameAI> enemies) {
        if (units > 0 && !enemies.isEmpty()) {
            System.out.println("   [ATTACK] [Elune's Light!] A flurry of glowing arrows rains from above!");
            return true;
        } else {
             System.out.println("   [IDLE] The Sentinels remain concealed, waiting for reinforcements.");
             return false;
        }
    }
}
