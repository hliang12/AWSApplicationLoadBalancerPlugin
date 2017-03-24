# AWSApplicationLoadBalancerPlugin

Author: Hao-lin Liang (hao-lin.liang@dynatrace.com)

Available Metrics

Amazon provide guidance on which metrics and aggregation combinations are most useful (see here) and these are listed in bold below. These are the only metrics that have been tested during development of this plugin. However, others should work.

ActiveConnectionCount - Sum (Average, Min, Max "not typically useful")
ClientTLSNegotiationErrorCount - Sum (Average, Min, Max "not typically useful")
HealthyHostCount - Average, Min, Max (Sum "not typically useful")
HTTPCode_ELB_4XX_Count - Sum (Average, Min, Max will return 1, therefore these metrics are filtered out)
HTTPCode_ELB_5XX_Count -  Sum (Average, Min, Max will return 1, therefore these metrics are filtered out)
HTTPCode_Target_2XX_Count - Sum (Average, Min, Max will return 1, therefore these metrics are filtered out)
HTTPCode_Target_3XX_Count - Sum (Average, Min, Max will return 1, therefore these metrics are filtered out)
HTTPCode_Target_4XX_Count - Sum (Average, Min, Max will return 1, therefore these metrics are filtered out)
HTTPCode_Target_5XX_Count - Sum (Average, Min, Max will return 1, therefore these metrics are filtered out)
IPv6ProcessedBytes - Sum, Average, Min, Max
IPv6RequestCount - Sum (Average, Min, Max will return 1, therefore these metrics are filtered out)
NewConnectionCount - Sum (Average, Min, Max "not typically useful")
ProcessedBytes - Sum, Average, Min, Max
RejectedConnectionCount - Sum (Average, Min, Max "not typically useful")
RequestCount - Sum (Average, Min, Max will return 1, therefore these metrics are filtered out)
TargetConnectionErrorCount - Sum (Average, Min, Max "not typically useful")
TargetResponseTime - Average (Sum, Min, Max "not typically useful")
TargetTLSNegotiationErrorCount - Sum (Average, Min, Max "not typically useful")
UnHealthyHostCount - Average, Min, Max (Sum "not typically useful")


## Prerequisites
To use this plugin, you'll need the following details:

- **Access Key**: User must be in a group which has the *CloudWatchReadOnlyAccess* permission.
- **Secret Access Key**: Corresponding Secret Access key for the above.
- **AWS Region**: eg. *eu-west-2* defaults to *eu-west-2*
- **Load Balancer Name**: *app/load1/2118d2a05c1c8908* in the screenshot below.

![](http://i65.tinypic.com/a455pi.png)

### IMPORTANT: Supported Data Granularities

The **data granularity** and the plugin schedule time **MUST** match. In the following screenshots, both are set to 5 minutes.

#### Supported Data Granularities

- "1 Minute"
- "5 Minutes"
- "10 Minutes"
- "15 Minutes"
- "30 Minutes"
- "1 Hour"

    
![](http://i63.tinypic.com/2mhwqkw.png)

![](http://i68.tinypic.com/243ex4k.png)
