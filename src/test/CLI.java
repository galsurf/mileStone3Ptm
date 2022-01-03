package test;

import test.Commands.Command;
import test.Commands.DefaultIO;
import java.util.ArrayList;

public class CLI {
	ArrayList<Command> commands;
	DefaultIO dio;
	Commands c;
	
	public CLI(DefaultIO dio) {
		this.dio=dio;
		c=new Commands(dio); 
		commands=new ArrayList<>();

		commands.add(c.new Command_0_printMenu());
		commands.add(c.new Command_1_UpCsvFile());
		commands.add(c.new Command_2_AlgoSetting());
		commands.add(c.new Command_3_Detect());
		commands.add(c.new Command_4_Results());
		commands.add(c.new Command_5_UpAndAnalayze());
	}

	public void start() {

		int commandFromClient;
		this.commands.get(0).execute();//print menu
		while ( (commandFromClient = (int)dio.readVal()) != 6) {
			if(commandFromClient < 1 || commandFromClient > 6)
				dio.write("wrong index, please choose number between 1 to 6\n ");
			else {
				this.commands.get(commandFromClient).execute();
				this.commands.get(0).execute();//print menu
			}
		}
	}
}



