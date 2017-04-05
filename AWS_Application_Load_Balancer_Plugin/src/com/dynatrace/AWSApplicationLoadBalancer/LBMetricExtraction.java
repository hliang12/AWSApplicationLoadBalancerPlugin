package com.dynatrace.AWSApplicationLoadBalancer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.*;
import com.dynatrace.diagnostics.pdk.MonitorEnvironment;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;


public class LBMetricExtraction {
	
	private List<String> aggregationList = new ArrayList<String>();
    
	private static final Logger log = Logger.getLogger(LBMetricExtraction.class.getName());
	
	public LBMetricExtraction(MonitorEnvironment env){
		
		aggregationList.add("SampleCount");
	    aggregationList.add("Average");
	    aggregationList.add("Sum");
	    aggregationList.add("Minimum");
	    aggregationList.add("Maximum");

	}


	public AmazonCloudWatch Login(String accessKey, String secretAccessKey,String region){

	        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretAccessKey);
	        AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
	        String strEndpoint = "https://monitoring." + region + ".amazonaws.com"; // monitoring.eu-west-2.amazonaws.com
	        EndpointConfiguration endpointConfig = new EndpointConfiguration(strEndpoint, region);
	        AmazonCloudWatch client = AmazonCloudWatchClientBuilder.standard().withCredentials(provider).withEndpointConfiguration(endpointConfig).build();

	        return client;
	 
	}
	
	
	private int calculateDataGranularity(String strDataGranularity) throws ApplicationLoadBalancerException{
		
		switch(strDataGranularity){
		
		case "1 Minute":
			log.info("1min granularity");
			return 60 *1000;
	
		case "5 Minute" :
			log.info("5mins granularity");
			return 60 * 5 * 1000;
		
		case "10 Minute" : 
			log.info("5mins granularity");
			return 60 * 10 * 1000;
		
		case "15 Minute": 
			log.info("5mins granularity");
			return 60 * 15 * 1000;
		
		case "30 Minute":
			log.info("5mins granularity");
			return 60 * 30 * 1000;
			
		case "60 Minute":
			log.info("5mins granularity");
			return 60 * 60 * 1000;
			
		default: 
			
			throw new ApplicationLoadBalancerException("Strange data granularity selected");
		}
	}
	
	
	public  GetMetricStatisticsResult getResultsLBMetrics(AmazonCloudWatch client, String lbName, String metricName, String granularity) throws ApplicationLoadBalancerException{
		 
        Date endTime = new Date();
        int time = calculateDataGranularity(granularity);
        Date startTime = new Date(endTime.getTime() - time);

        String nameSpace = "AWS/ApplicationELB";
        String name = "LoadBalancer";

	Dimension oDimension = getDimension(client, name, lbName);
        
        if (oDimension == null)
        {
        	log.severe("Unable to find requested load balancer. Please check monitor configuration.");
        }
		
        GetMetricStatisticsRequest request1 = new GetMetricStatisticsRequest()
                .withStartTime(startTime)
                .withNamespace(nameSpace)
                .withPeriod(time)
                //.withDimensions(new Dimension().withName(name).withValue(lbName))
		.withDimensions(oDimension)
                .withMetricName(metricName)
                .withStatistics(aggregationList)
                .withEndTime(endTime);

        return client.getMetricStatistics(request1);

    }
	
	/*
	 * We've provided the LB name eg. "load1" which forms part of the ARN.
	 * We need to search for the dimension that contains the LB name so we can dynamically extract the entire ARN
	 * and then use that to build the GetMetricStatisticsRequest object.
	 * 
	 * This is needed in case we destroy an instance of "load1" then create a new LB with the same name.
	 * We want monitoring to continue and using this method, it will.
	 */
	private Dimension getDimension(AmazonCloudWatch client, String dimensionName, String lbName)
	{
		List<Metric> oMetricList = new ArrayList<Metric>();
		try
		{
			oMetricList = client.listMetrics().getMetrics();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}
		
		for (Metric oMetric : oMetricList)
		{
			for (Dimension oDim : oMetric.getDimensions())
			{
				if (oDim.getName().contains(dimensionName) && oDim.getValue().contains(lbName))return oDim; 
			}
		}
		return null;
	}

}
