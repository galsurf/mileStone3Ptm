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

		// example: commands.add(c.new ExampleCommand());

		commands.add(c.new Command1UpCsvFile());
		commands.add(c.new Command2AlgoSetting());
		commands.add(c.new Command2AlgoSetting());
		commands.add(c.new Command3Detect());
		commands.add(c.new Command4Results());
		commands.add(c.new Command5UpAndAnalayze());
		commands.add(c.new Command6Exit());

		// implement
	}
	
	public void start() {
		// implement
		this.printMenu();
		this.commands.get(((int) dio.readVal())- 1);//1
		this.printMenu();
		this.commands.get(((int) dio.readVal())- 1);//2
		this.printMenu();
		this.commands.get(((int) dio.readVal())- 1);//3
		this.printMenu();
		this.commands.get(((int) dio.readVal())- 1);//4
		this.printMenu();
		this.commands.get(((int) dio.readVal())- 1);//5
		this.printMenu();

	}

	public void printMenu(){
		dio.write("welcome to the Anomaly Detection Server.\n" +
				"Please choose an option:\n" +
				"1. upload a time series csv file\n" +
				"2. algoritem setting\n" +
				"3. detect anomalies\n" +
				"4. display results\n" +
				"5. upload anomalies and analyze results\n" +
				"6. exit\n");
	}
}
