package HomeExam.scr.Main;

public class ExplodingKittens {
    /**
     * starts the games and takes the given argument when starting the game
     * Arguemtn 0: number of online players
     * Argument 1: number of bots
     * Argument 2: version
     * 
     * @param argv
     * @throws Exception
     */
    private ExplodingKittens(String argv[]) throws Exception {
        View view = new View();

        if (argv.length == 3) {
            int numOnlinePlayers = Integer.parseInt(argv[0]);
            int numBots = Integer.parseInt(argv[1]);
            String version = argv[2];
            Options options = new Options(version + "GameVariables", numOnlinePlayers, numBots);
            Server server = new Server(options, view);
            server.startGame();
        } else if (argv.length == 1) {
            OnlineClient OnlineClient = new OnlineClient(argv[0]);
        } else {
            System.out.println(
                    "Server syntax: java ExplodingKittens numPlayers numBots Version\nAvailible Versions: Original");
            System.out.println("Client syntax: java ExplodingKittens IP");
        }
    }

    /**
     * Arguemtn 0: number of online players
     * Argument 1: number of bots
     * Argument 2: version
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String argv[]) {
        try {
            new ExplodingKittens(argv);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
