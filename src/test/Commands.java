package test;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Commands {
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

		public static TimeSeries tsTrain ;
		public static TimeSeries tsTest ;
		public SimpleAnomalyDetector sAd;
		int numLineTestCsv;
		int Negative;
		int FP;
		int TP;
		int FN;
		int TN;

		public static class Anomolies{
			public int start;
			public int end;
			public Anomolies(int start , int end){
				this.start = start;
				this.end = end;
			}
		}
		public static ArrayList<Anomolies> anomalies;
		public static ArrayList<Anomolies> myAnomaliesReports;
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
			dio.write("Welcome to the Anomaly Detection Server.\n" +
					"Please choose an option:\n" +
					"1. upload a time series csv file\n" +
					"2. algorithm settings\n" +
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
			try {
				PrintWriter printWriter1;
				printWriter1 = new PrintWriter(new FileWriter("anomalyTrain.csv"));
				String line1 = dio.readText();
				line1 = dio.readText();
				while (!line1.equals("done")) {
					printWriter1.println(line1);
					line1 = dio.readText();
				}
				printWriter1.close();
			}catch (FileNotFoundException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			SharedState.tsTrain = new TimeSeries("anomalyTrain.csv");
			dio.write("Upload complete.\n");


			//second upload
			dio.write("Please upload your local test CSV file.\n");
			try {
				PrintWriter printWriter2;
				printWriter2 =new PrintWriter(new FileWriter("anomalyTest.csv"));

				sharedState.numLineTestCsv = 0;
				String line2 = dio.readText();
				while(!line2.equals("done")){
					sharedState.numLineTestCsv++;
					printWriter2.println(line2);
					line2 = dio.readText();
				}
				printWriter2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sharedState.tsTest = new TimeSeries("anomalyTest.csv");
			sharedState.numLineTestCsv--;//minus one --> first line are letters//
			dio.write("Upload complete.\n");
		}
	}

	public class Command_2_AlgoSetting extends Command{//secondCommand2//
		public Command_2_AlgoSetting() {
			super("2. algorithm setting");
		}

		@Override
		public void execute() {
			sharedState.sAd = new SimpleAnomalyDetector();
			dio.write("The current correlation threshold is " +
					sharedState.sAd.CORRELATION_THRESHOLD + "\n");
			dio.write("Type a new threshold\n");
			double newTresHold = (double) dio.readVal();
			while (newTresHold <= 0 || newTresHold >= 1){
				dio.write("please choose a value between 0 and 1.");
				newTresHold = (double) dio.readVal();
			}
			sharedState.sAd.CORRELATION_THRESHOLD = newTresHold;
		}
	}

	public class Command_3_Detect extends Command{
		public Command_3_Detect() {
			super("3. detect anomalies");
		}

		@Override
		public void execute() {
		sharedState.sAd.learnNormal(sharedState.tsTrain);
		sharedState.sAd.detect(sharedState.tsTest);

		dio.write("anomaly detection complete.\n");
		}
	}

	public class Command_4_Results extends Command{
		public Command_4_Results() {
			super("4. display results\n");
		}

		@Override
		public void execute() {
			for(int i = 0 ; i < sharedState.sAd.anomalyReports.size() ; i++){
				dio.write(sharedState.sAd.anomalyReports.get(i).timeStep +"\t" +
						sharedState.sAd.anomalyReports.get(i).description + "\n");
			}
			dio.write("Done.\n");
		}
	}

	public class Command_5_UpAndAnalyze extends Command{
		public Command_5_UpAndAnalyze() {
			super("5. upload anomalies and analyze results\n");
		}

		@Override
		public void execute() {
			dio.write("Please upload your local anomalies file.\n");
			int p = 0;
			sharedState.anomalies = new ArrayList<>();
			try {
				String[] anomoliesInt = new String[2];
				PrintWriter printWriter = new PrintWriter(new FileWriter("anomalies.txt"));
				String line = dio.readText();
				line = dio.readText();

				while(!line.equals("done")){
					p++;
					printWriter.println(line);
					 anomoliesInt = line.split(",");
					line = dio.readText();
				}
				sharedState.anomalies.add(new SharedState.Anomolies(Integer.parseInt(anomoliesInt[0]), Integer.parseInt(anomoliesInt[1])));
				printWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			dio.write("Upload complete.\n");


			sharedState.myAnomaliesReports = new ArrayList<>();
			int i;
			for(i = 0 ; i < sharedState.sAd.anomalyReports.size() - 1 ; i++){
				if(((sharedState.sAd.anomalyReports.get(i).description).equals(sharedState.sAd.anomalyReports.get(i+1).description)
						&& ((sharedState.sAd.anomalyReports.get(i).timeStep + 1) == (sharedState.sAd.anomalyReports.get(i+1).timeStep)))){
					sharedState.myAnomaliesReports.add(new SharedState.Anomolies((int)(sharedState.sAd.anomalyReports.get(i).timeStep),
							(int)(sharedState.sAd.anomalyReports.get(i+1).timeStep)));
					int j = i + 1;

					while(((j < sharedState.sAd.anomalyReports.size() - 1)
							&& (sharedState.sAd.anomalyReports.get(j).description).equals(sharedState.sAd.anomalyReports.get(j+1).description)
							&& (sharedState.sAd.anomalyReports.get(j).timeStep + 1 == sharedState.sAd.anomalyReports.get(j+1).timeStep))){
						sharedState.myAnomaliesReports.get(sharedState.myAnomaliesReports.size() -1).end = (int) sharedState.sAd.anomalyReports.get(j+1).timeStep;
						j++;
					}
					i=j;
				}
			}

			int numOfAnomaliesSteps = 0 ;
			for(int k = 0 ; k < sharedState.myAnomaliesReports.size() ; k++){
				numOfAnomaliesSteps += (sharedState.myAnomaliesReports.get(k).end - sharedState.myAnomaliesReports.get(k).start) +1;
			}
			sharedState.Negative = sharedState.numLineTestCsv - numOfAnomaliesSteps;

			sharedState.FN = 0;
			sharedState.FP = 0;
			sharedState.TN = 0;
			sharedState.TP = 0;

			for(int s = 0 ; s < sharedState.anomalies.size() ; s++){
				for(int k = 0 ; k < sharedState.myAnomaliesReports.size() ; k++){
					if((sharedState.anomalies.get(s).start >= sharedState.myAnomaliesReports.get(k).start
							&& sharedState.anomalies.get(s).end <= sharedState.myAnomaliesReports.get(k).end)
					||(sharedState.anomalies.get(s).start <= sharedState.myAnomaliesReports.get(k).end &&
							sharedState.anomalies.get(s).end >= sharedState.myAnomaliesReports.get(k).end )
							||(sharedState.anomalies.get(s).start <= sharedState.myAnomaliesReports.get(k).start &&
							sharedState.anomalies.get(s).end >= sharedState.myAnomaliesReports.get(k).start)
							||(sharedState.anomalies.get(s).start <= sharedState.myAnomaliesReports.get(k).start &&
							sharedState.anomalies.get(s).end >= sharedState.myAnomaliesReports.get(k).end)){
						sharedState.TP++;
					}
					else
						sharedState.FP++;
				}
			}

		dio.write("True Positive Rate: " +(new DecimalFormat("0.0").format((float)sharedState.TP / (float)p) + "\n"));
		dio.write("False Positive Rate: " +(new DecimalFormat("0.00").format((float)sharedState.FP / (float)sharedState.Negative) + "\n"));












			//float truePositiveRate =  /p;

		}
	}
}
