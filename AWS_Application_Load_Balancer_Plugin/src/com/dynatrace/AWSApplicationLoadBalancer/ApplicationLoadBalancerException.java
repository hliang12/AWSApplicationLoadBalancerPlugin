package com.dynatrace.AWSApplicationLoadBalancer;

public class ApplicationLoadBalancerException extends Exception{

	public ApplicationLoadBalancerException(){
		
	}
	
	public ApplicationLoadBalancerException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ApplicationLoadBalancerException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ApplicationLoadBalancerException(String message, Throwable cause) {
		super(message, cause);
	}
}
