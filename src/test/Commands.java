package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Commands {

	///
	public TimeSeries tsTrain ;
	public TimeSeries tsTest ;
	public TimeSeries anomalies ;
	public SimpleAnomalyDetector sAd;
	///

	// Default IO interface
	public interface DefaultIO{
		public String readText() throws IOException;
		public void write(String text);
		public float readVal();
		public void write(float val);

		// you may add default methods here
	}
	
	// the default IO to be used in all commands
	DefaultIO dio;
	public Commands(DefaultIO dio) {
		this.dio=dio;
	}
	
	// you may add other helper classes here
	
	
	
	// the shared state of all commands
	private class SharedState{
		// implement here whatever you need
		
	}
	
	private  SharedState sharedState=new SharedState();

	
	// Command abstract class
	public abstract class Command{
		protected String description;
		
		public Command(String description) {
			this.description=description;
		}
		
		public abstract void execute();
	}

	public class Command_0_printMenu extends Command{

		public Command_0_printMenu(){
			super("menu\n");
		}
		@Override
		public void execute()  {
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


	public class Command_1_UpCsvFile extends Command{//first Command//1//
		public Command_1_UpCsvFile() {
			super("1. upload a time series csv file\n");
		}

		@Override
		public void execute() {
			//first upload//
			dio.write("Please upload your local train CSV file.\n");

			String csvFileTrainName = null;
			String csvFileTestName = null;
			try {
				csvFileTrainName = dio.readText();
			} catch (IOException e) {
				e.printStackTrace();
			}
			tsTrain = new TimeSeries(csvFileTrainName);
			File trainFromClient = new File(csvFileTrainName);
			File trainFile = new File("anomalyTrain.csv");
			try {
				Files.copy(trainFromClient.toPath() , trainFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			dio.write("Upload complete.\n");

			//second upload//
			dio.write("Please upload your local test CSV file.\n");
			//
			try {
				csvFileTestName = dio.readText();
			} catch (IOException e) {
				e.printStackTrace();
			}
			tsTest = new TimeSeries(csvFileTrainName);
			File testFromClient = new File(csvFileTestName);
			File testFile = new File("anomalyTest.scv");
			try {
				Files.copy(testFromClient.toPath() , testFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			dio.write("Upload complete.\n");
			return;
		}
	}

	public class Command_2_AlgoSetting extends Command{//secondCommand2//
		public Command_2_AlgoSetting() {
			super("2. algorithm setting");
		}

		@Override
		public void execute() {
			sAd = new SimpleAnomalyDetector();
			dio.write("The current correlation threshold is " +
					sAd.CORRELATION_THRESHOLD + "\n");
			dio.write("Type a new threshold\n");
			double newTresHold = (double) dio.readVal();
			while (newTresHold <= 0 || newTresHold >= 1){
				dio.write("please choose a value between 0 and 1.");
				newTresHold = (double) dio.readVal();
			}
			sAd.CORRELATION_THRESHOLD = newTresHold;
			return;
		}
	}

	public class Command_3_Detect extends Command{
		public Command_3_Detect() {
			super("3. detect anomalies");
		}

		@Override
		public void execute() {
		sAd.learnNormal(tsTrain);
		sAd.detect(tsTest);

		dio.write("anomaly detection complete.");
		return;
		}
	}

	public class Command_4_Results extends Command{
		public Command_4_Results() {
			super("4. display results\n");
		}

		@Override
		public void execute() {
			for(int i = 0 ; i < sAd.anomalyReports.size() ; i++){
				dio.write(sAd.anomalyReports.get(i).timeStep +"\t" +
						sAd.anomalyReports.get(i).description + "\n");
			}
			dio.write("Done.\n");
			return;
		}
	}

	public class Command_5_UpAndAnalayze extends Command{
		public Command_5_UpAndAnalayze() {
			super("5. upload anomalies and analyze results\n");
		}

		@Override
		public void execute() {
			dio.write("Please upload your local anomalies file.\n");



		}
	}






	
}
