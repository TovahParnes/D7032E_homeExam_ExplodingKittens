package HomeExam.scr.Main;

public class ExplodingKittens {
    /**
     * starts the games and takes the given argument when starting,
     * If you put in a single number, it will host a game with that many slots
     * An ip adress as argument will join the game at that ip
     * and no argument at all will start a game with bots
     * @param argv
     * @throws Exception
     */
    public ExplodingKittens(String argv[]) throws Exception
    {
        View view = new View();
        if(argv.length == 0)
        {
            try
            {
                Server server = new Server(0, 0, view, false);
            }
            catch (Exception e)
            {
                e.printStackTrace(System.out);
            }
        }
        else
        {
            try
            {
                int numOnlinePlayers = Integer.parseInt(argv[0]);
                int numBots = Integer.parseInt(argv[0]);
                Server server = new Server(numOnlinePlayers, numBots, view, false);
            }
            catch(NumberFormatException e)
            {
                e.printStackTrace(System.out);
                view.printErrorStart();
            }
        }
    }

    public static void main(String argv[]) {
		try {
			new ExplodingKittens(argv);
		} catch(Exception e) {

		}
	}
}
