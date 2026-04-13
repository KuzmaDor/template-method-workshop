import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a name for the Orc faction:");
        String orcName = scanner.nextLine().trim();
        if (orcName.isEmpty()) orcName = "Orc Horde";
        System.out.println("Enter a name for the Elf faction:");
        String elfName = scanner.nextLine().trim();
        if (elfName.isEmpty()) elfName = "Elf Sentinels";
        System.out.println("Enter a name for the Undead faction:");
        String undeadName = scanner.nextLine().trim();
        if (undeadName.isEmpty()) undeadName = "Undead Scourge";
        System.out.println("Enter a name for the Human faction:");
        String humanName = scanner.nextLine().trim();
        if (humanName.isEmpty()) humanName = "Human Expedition";

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
            System.out.println(); // Space between rounds

            System.out.println("Press Enter to continue to the next round...");
            scanner.nextLine();

            round++;
            // Safety break to prevent infinite loops if they just sit and do nothing
            if (round > 20) {
                 System.out.println("Game lasted too long. Halting.");
                 break;
            }
        }
        scanner.close();
    }
}
