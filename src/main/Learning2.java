package main;

import game.GuessWhoGame;

public class Learning2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		checkArgs(args);
		GuessWhoGame game = new GuessWhoGame(args[0]);
		game.startGame();
	}

	public static void checkArgs(String[] args){
		// check command line argument
		if(args.length < 1){
			System.out.println("usage: java -jar Learning2.jar network_file");
			System.exit(0);
		}
	}
	
}
