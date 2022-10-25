package HomeExam.scr.Main;

public class ExplodingKittens {
    /**
     * starts the games and takes the given argument when starting,
     * If you put in a single number, it will host a game with that many slots
     * An ip adress as argument will join the game at that ip
     * and no argument at all will start a game with bots
     * 
     * @param argv
     * @throws Exception
     */
    private ExplodingKittens(String argv[]) throws Exception {
        View view = new View();

        if (argv.length == 2) {
            int numOnlinePlayers = Integer.parseInt(argv[0]);
            int numBots = Integer.parseInt(argv[1]);
            Options options = new Options("TestGameVariables", numOnlinePlayers, numBots);
            Server server = new Server(options, view, false);
        } else if (argv.length == 1) {
            OnlineClient OnlineClient = new OnlineClient(argv[0], view);
        } else {
            System.out.println("Server syntax: java ExplodingKittens numPlayers numBots");
            System.out.println("Client syntax: IP");
        }
    }

    public static void main(String argv[]) {
        try {
            new ExplodingKittens(argv);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
