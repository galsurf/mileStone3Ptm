package test;

import java.util.ArrayList;
import java.util.List;

public class SimpleAnomalyDetector  implements TimeSeriesAnomalyDetector {
	public List<CorrelatedFeatures> correlatedFeaturesList = new ArrayList<CorrelatedFeatures>();
public static double CORRELATION_THRESHOLD =  0.9;
	public List<AnomalyReport> anomalyReports = new ArrayList<AnomalyReport>();

	@Override
	public void learnNormal(TimeSeries ts) {
		CorrelatedFeatures tempCor = null;
		for (int i = 0; i < ts.columns.size(); i++) {
			float maxPearson = 0;
			float tempPearson = 0;
			for (int j = i + 1; j < ts.columns.size(); j++) {
				float[] floatArrayX = new float[ts.columns.size()];
				floatArrayX = ts.stringToFloat(ts.columns.get(ts.columnNames.get(i)));

				float[] floatArrayY = new float[ts.columns.size()];
				floatArrayY = ts.stringToFloat(ts.columns.get((ts.columnNames.get(j))));

				tempPearson = Math.abs(StatLib.pearson(floatArrayX, floatArrayY));
				if (tempPearson > maxPearson && tempPearson >= CORRELATION_THRESHOLD) {
					maxPearson = tempPearson;

					Point[] pointArray = new Point[floatArrayX.length];
					float tresHsold = 0;

					for (int n = 0; n < pointArray.length; n++) {
						pointArray[n] = new Point(floatArrayX[n], floatArrayY[n]);
					}
					for (int n = 0; n < pointArray.length; n++) {
						float tempTresHold = StatLib.dev(pointArray[n], pointArray);
						if (tresHsold < tempTresHold)
							tresHsold = tempTresHold;

					}
					tresHsold *= 1.1;
					tempCor = new CorrelatedFeatures(ts.columnNames.get(i), ts.columnNames.get(j),
							maxPearson, StatLib.linear_reg(pointArray), tresHsold);
				}
			}
			if (maxPearson > CORRELATION_THRESHOLD)
				correlatedFeaturesList.add(tempCor);
		}


	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {

		for(CorrelatedFeatures c : correlatedFeaturesList) {
			float[] x = new float[ts.columns.get(c.feature1).size()];
			 x = ts.stringToFloat(ts.columns.get(c.feature1));

			float[] y = new float[ts.columns.get(c.feature2).size()];
			 y = ts.stringToFloat(ts.columns.get(c.feature2));
			for(int i=0;i<x.length;i++){
				if(Math.abs(y[i] - c.lin_reg.f(x[i]))>c.threshold){
					String d=c.feature1 + "-" + c.feature2;
					anomalyReports.add(new AnomalyReport(d,(i+1)));
				}
			}
		}
		return anomalyReports;
	}
	public List<CorrelatedFeatures> getNormalModel(){
		return correlatedFeaturesList;
	}
}
