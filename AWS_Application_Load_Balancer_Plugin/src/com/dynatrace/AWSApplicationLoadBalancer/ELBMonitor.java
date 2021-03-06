package com.dynatrace.AWSApplicationLoadBalancer;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.dynatrace.diagnostics.pdk.Monitor;
import com.dynatrace.diagnostics.pdk.MonitorEnvironment;
import com.dynatrace.diagnostics.pdk.MonitorMeasure;
import com.dynatrace.diagnostics.pdk.Status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class ELBMonitor implements Monitor {
	
	private static final Logger log = Logger.getLogger(ELBMonitor.class.getName());	
	
	//List of Available Application loadbalancer metrics 
	private List<String> appLbMetrics = new ArrayList<String>();
	
	private  LBMetricExtraction metricExtractor;
	
	//Input variables
	protected static final String CONFIG_ACCCESS_KEY = "accessKey";
	protected static final String CONFIG_SECRET_ACCESS_KEY = "secretAccessKey";
	protected static final String CONFIG_LOADBALANCER_NAME = "loadBalancerName";
	protected static final String CONFIG_REGIGON = "region";
	protected static final String CONFIG_GRANULARITY = "granularity";

	//setting up when creating the monitor
	@Override
	public Status setup(MonitorEnvironment env) throws Exception {

		metricExtractor = new LBMetricExtraction(env);
			
		    appLbMetrics.add("ActiveConnectionCount");
		    appLbMetrics.add("ClientTLSNegotiationErrorCount");
		    appLbMetrics.add("HealthyHostCount");
		    appLbMetrics.add("HTTPCode_ELB_4XX_Count");
		    appLbMetrics.add("HTTPCode_ELB_5XX_Count");
		    appLbMetrics.add("HTTPCode_Target_2XX_Count");
		    appLbMetrics.add("HTTPCode_Target_3XX_Count");
		    appLbMetrics.add("HTTPCode_Target_4XX_Count");
		    appLbMetrics.add("HTTPCode_Target_5XX_Count");
		    appLbMetrics.add("IPv6ProcessedBytes");
		    appLbMetrics.add("IPv6RequestCount");
		    appLbMetrics.add("NewConnectionCount");
		    appLbMetrics.add("ProcessedBytes");
		    appLbMetrics.add("RejectedConnectionCount");
		    appLbMetrics.add("RequestCount");
		    appLbMetrics.add("TargetConnectionErrorCount");
		    appLbMetrics.add("TargetResponseTime");
		    appLbMetrics.add("TargetTLSNegotiationErrorCount");
		    appLbMetrics.add("UnHealthyHostCount");
		   
		    
	
		return new Status(Status.StatusCode.Success);
	}

	
	@Override
	public Status execute(MonitorEnvironment env) throws Exception {
		
		// grab the input parameters 
		String accessKey = env.getConfigString(CONFIG_ACCCESS_KEY);
		String secretAccessKey = env.getConfigPassword(CONFIG_SECRET_ACCESS_KEY);
		String lbName = env.getConfigString(CONFIG_LOADBALANCER_NAME);
		String region = env.getConfigString(CONFIG_REGIGON);
		String granularity = env.getConfigString(CONFIG_GRANULARITY);
		
		//check if there is a value 
		if(accessKey == null | accessKey.isEmpty()){
			throw new ApplicationLoadBalancerException("No Access Key provided");
		}
		if(secretAccessKey == null | secretAccessKey.isEmpty()){
			throw new ApplicationLoadBalancerException("No Secret Access Key provided");
		}
		if(lbName == null | lbName.isEmpty()){
			throw new ApplicationLoadBalancerException("No Load Balancer Name provided");
		}
		if(region == null | region.isEmpty()){
			throw new ApplicationLoadBalancerException("No Region provided");
		}
		
		//Login to access the cloudwatch metrics
		AmazonCloudWatch watcher = metricExtractor.Login(accessKey, secretAccessKey, region);
		
		Collection<MonitorMeasure> monitorMeasures;
		
		// start extracting values for metrics which are subscribed to
		for(String metricName: appLbMetrics){
		monitorMeasures = env.getMonitorMeasures("AWS Application Load Balancer Metrics",metricName);
		
		GetMetricStatisticsResult result = metricExtractor.getResultsLBMetrics(watcher,lbName,metricName,granularity);
		List<Datapoint> dp = result.getDatapoints();

			for (MonitorMeasure subscribedMonitorMeasure : monitorMeasures) {
				
				double sCount = 0;
				double average = 0;
				double sum = 0;
				double min = 0;
				double max = 0;
		 		
		 		for(Datapoint p : dp){
		 			sCount = p.getSampleCount();
		 			average = p.getAverage();
		 			sum = p.getSum();
		 			min = p.getMinimum();
		 			max = p.getMaximum();
		 		}
		 		
		 		MonitorMeasure dynamicMeasure; 
		 		
		 		switch (metricName){
	        	case "HTTPCode_ELB_4XX_Count":
	        	case "HTTPCode_ELB_5XX_Count":
	        	case "HTTPCode_Target_2XX_Count":
	        	case "HTTPCode_Target_3XX_Count":
	        	case "HTTPCode_Target_4XX_Count":
	        	case "HTTPCode_Target_5XX_Count":
	        	case "IPv6RequestCount":
	        	case "RequestCount": 
	        	case "IPv6ProcessedBytes":
	        		if(sum!=-1){
	        			dynamicMeasure = env.createDynamicMeasure(subscribedMonitorMeasure, "Aggregation", "Sum");
	        			dynamicMeasure.setValue(sum);
	        		}
	        		break;
	        		
	        	default:
	        	if(sCount!=-1){
	        		dynamicMeasure = env.createDynamicMeasure(subscribedMonitorMeasure, "Aggregation", "Sample Count");
	        		dynamicMeasure.setValue(sCount);
	        	}
	        	if(average!=-1){
	        		dynamicMeasure = env.createDynamicMeasure(subscribedMonitorMeasure, "Aggregation", "Average");
	        		dynamicMeasure.setValue(average);
	        	}
				if(sum!=-1){
					dynamicMeasure = env.createDynamicMeasure(subscribedMonitorMeasure, "Aggregation", "Sum");
					dynamicMeasure.setValue(sum);
				}
				if(min!=-1){
					dynamicMeasure = env.createDynamicMeasure(subscribedMonitorMeasure, "Aggregation", "Minimum");
					dynamicMeasure.setValue(min);
				}
				if(max!=-1){
					dynamicMeasure = env.createDynamicMeasure(subscribedMonitorMeasure, "Aggregation", "Maximum");
					dynamicMeasure.setValue(max);
				}
				break;
		 		}
		}
	}
		return new Status(Status.StatusCode.Success);
	}

		@Override
	public void teardown(MonitorEnvironment env) throws Exception {

	}

}
