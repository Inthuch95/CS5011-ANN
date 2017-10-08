package main;

import game.GuessWhoGame;

public class Learning2 {

	public static void main(String[] args) {
		boolean earlyGuess = checkArgs(args);
		// launch the game
		GuessWhoGame game = new GuessWhoGame("network.eg", earlyGuess);
		game.startGame();
	}

	public static boolean checkArgs(String[] args){
		boolean earlyGuess = false;
		// check command line argument
		if(args.length > 1){
			System.out.println("usage: java -jar Learning2.jar <early_guess>");
			System.exit(0);
		}
		if(args.length == 1){
			if(!args[1].equals("E")){
				System.out.println("Please enter 'E' to enable early guess");
				System.exit(0);
			}
			else {
				 earlyGuess = true;
			}
		}
		
		return earlyGuess;
	}
	
}
