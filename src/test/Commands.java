package test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Commands {

	public TimeSeries tsTrain ;
	public TimeSeries tsTest ;
	public SimpleAnomalyDetector sAd;

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
		
		public abstract void execute() throws IOException;
	}
	
	// Command class for example:
	public class ExampleCommand extends Command{

		public ExampleCommand() {
			super("this is an example of command");
		}

		@Override
		public void execute() {
			dio.write(description);
		}
	}

	public class Command1UpCsvFile extends Command{//first Command//1//
		public Command1UpCsvFile() {
			super("1. upload a time series csv file\n");
		}

		@Override
		public void execute() throws IOException {
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
			Files.copy(trainFromClient.toPath() , trainFile.toPath());
			dio.write("Upload comple.\n");

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
			Files.copy(testFromClient.toPath() , testFile.toPath());
			dio.write("Upload comple.\n");
			return;
		}
	}

	public class Command2AlgoSetting extends Command{//secondCommand2//
		public Command2AlgoSetting() {
			super("2. algorithm setting");
		}

		@Override
		public void execute() {
			dio.write("The current correlation threshold is " +
					SimpleAnomalyDetector.CORRELATION_THRESHOLD);
			dio.write("Type a new threshold");
			double newTresHold = (double) dio.readVal();
			while (newTresHold <= 0 || newTresHold >= 1){
				dio.write("please choose a value between 0 and 1.");
				newTresHold = (double) dio.readVal();
			}
			SimpleAnomalyDetector.CORRELATION_THRESHOLD = newTresHold;
			return;
		}
	}

	public class Command3Detect extends Command{
		public Command3Detect() {
			super("3. detect anomalies");
		}

		@Override
		public void execute() {
		sAd = new SimpleAnomalyDetector();
		sAd.learnNormal(tsTrain);
		sAd.detect(tsTest);

		dio.write("anomaly detection complete.");
		return;
		}
	}

	public class Command4Results extends Command{
		public Command4Results() {
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

	public class Command5UpAndAnalayze extends Command{
		public Command5UpAndAnalayze() {
			super("5. upload anomalies and analyze results\n");
		}

		@Override
		public void execute() {
			dio.write("Please upload your local anomalies file.\n");


		}
	}

	public class Command6Exit extends Command{
		public Command6Exit() {
			super("6. exit\n");
		}

		@Override
		public void execute() {



		}
	}





	
}
