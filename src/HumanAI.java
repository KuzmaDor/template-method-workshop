public class HumanAI extends GameAI {

    public HumanAI(String name) {
        super(name);
    }

    @Override
    protected void collectResources() {
        if (structures > 0) {
            int gainedGold = (random.nextInt(16) + 30) * structures;
            int gainedWood = (random.nextInt(16) + 20) * structures;
            gold += gainedGold;
            wood += gainedWood;
            System.out.println("   [WORK] 'Right-o!' Peasants diligently gathered +" + gainedGold + "G, +" + gainedWood + "W.");
        }
    }

    @Override
    protected void buildStructures() {
        int built = 0;
        int buildingsCost = 25; // Cheaper buildings
        while (gold >= buildingsCost && wood >= buildingsCost) {
            gold -= buildingsCost;
            wood -= buildingsCost;
            structures++;
            built++;
        }
        if (built > 0) System.out.println("   [BUILD] 'Job's done!' " + built + " splendid buildings were erected! (-" + (built*buildingsCost) + "G/-" + (built*buildingsCost) +"W)");
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
        if (units >= 2 && !enemies.isEmpty()) {
            System.out.println("   [CHARGE] [For the Alliance!] The King's army raises their shields and charges forth!");
            return true;
        } else {
            System.out.println("   [IDLE] The garrison maintains their formation, waiting for sufficient numbers to attack.");
            return false;
        }
    }
}
