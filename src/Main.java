import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String orcName = "Junior devs";
        String elfName = "Dutch people";
        String undeadName = "Senior devs";
        String humanName = "Jonathan and his book club";

        List<GameAI> players = new ArrayList<>();
        players.add(new OrcAI(orcName));
        players.add(new ElfAI(elfName));
        players.add(new UndeadAI(undeadName));
        players.add(new HumanAI(humanName));

        System.out.println("  ________  ___________   __        _______  ___________       _______       __       ___      ___   _______  \n" +
                " /\"       )(\"     _   \") /\"\"\\      /\"      \\(\"     _   \")     /\" _   \"|     /\"\"\\     |\"  \\    /\"  | /\"     \"| \n" +
                "(:   \\___/  )__/  \\\\__/ /    \\    |:        |)__/  \\\\__/     (: ( \\___)    /    \\     \\   \\  //   |(: ______) \n" +
                " \\___  \\       \\\\_ /   /' /\\  \\   |_____/   )   \\\\_ /         \\/ \\        /' /\\  \\    /\\\\  \\/.    | \\/    |   \n" +
                "  __/  \\\\      |.  |  //  __'  \\   //      /    |.  |         //  \\ ___  //  __'  \\  |: \\.        | // ___)_  \n" +
                " /\" \\   :)     \\:  | /   /  \\\\  \\ |:  __   \\    \\:  |        (:   _(  _|/   /  \\\\  \\ |.  \\    /:  |(:      \"| \n" +
                "(_______/       \\__|(___/    \\___)|__|  \\___)    \\__|         \\_______)(___/    \\___)|___|\\__/|___| \\_______) \n" +
                "                                                                                                              ");

        System.out.println("\n================== FACTION BRIEFING ==================");
        System.out.println("This project demonstrates the Template Method pattern.");
        System.out.println("Each faction follows the same turn skeleton:");
        System.out.println("  1) Collect resources  2) Build structures  3) Train units  4) Attack");
        System.out.println("...but each faction overrides *how* those steps behave.");
        System.out.println();
        System.out.println("Faction differences:");
        System.out.println(" - ORCS   : No building. They only craft units and live by the WAAAGH.\n" +
                "           They lose only when their last unit falls. They also pillage extra on victory.");
        System.out.println(" - HUMANS : Builders. Cheaper buildings let them scale their economy quickly and snowball.");
        System.out.println(" - ELVES  : Balanced. Reasonable buildings + units, with a slight edge in wood income.");
        System.out.println(" - UNDEAD : Swarm & steal. On victory they don't just kill foes... they raise them.\n" +
                "           Enemy losses get added into the Undead army.");
        System.out.println("======================================================\n");

        System.out.println("\nFactions participating:");
        for (GameAI player : players) {
             System.out.println(" - " + player.getName());
        }
        System.out.println("\n========================================\n");

        int round = 1;

        while (true) {
            List<GameAI> alivePlayers = players.stream()
                .filter(p -> !p.isDefeated())
                .collect(Collectors.toList());

            if (alivePlayers.size() <= 1) {
                System.out.println("====== GAME OVER ======");
                if (alivePlayers.size() == 1) {
                    System.out.println("WINNER IS: " + alivePlayers.get(0).getName() + "!!!");
                } else {
                    System.out.println("Everyone was defeated... It is a draw.");
                }
                break;
            }

            System.out.println("========== ROUND " + round + " ==========");

            for (GameAI player : players) {
                if (!player.isDefeated()) {
                    player.turn(players);
                }
            }

            System.out.println("\nPress Enter to continue to the next round...");
            scanner.nextLine();

            round++;

            if (round > 20) {
                 System.out.println("Game lasted too long. Halting.");
                 break;
            }
        }
        scanner.close();
    }
}
