# AWSApplicationLoadBalancerPlugin

Author: Hao-lin Liang (hao-lin.liang@dynatrace.com)

Available Metrics

Amazon provide guidance on which metrics and aggregation combinations are most useful (see here) and these are listed in bold below. These are the only metrics that have been tested during development of this plugin. However, others should work.

- _ActiveConnectionCount - Sum (Average, Min, Max "not typically useful")
- _ClientTLSNegotiationErrorCount - Sum (Average, Min, Max "not typically useful")
- _HealthyHostCount - Average, Min, Max (Sum "not typically useful")
- _HTTPCode_ELB_4XX_Count - Sum (Average, Min, Max will always return 1, therefore these metrics are not used)
- _HTTPCode_ELB_5XX_Count -  Sum (Average, Min, Max will always return 1, therefore these metrics are not used)
- _HTTPCode_Target_2XX_Count - Sum (Average, Min, Max will always return 1, therefore these metrics are not used)
- _HTTPCode_Target_3XX_Count - Sum (Average, Min, Max will always return 1, therefore these metrics are not used)
- _HTTPCode_Target_4XX_Count - Sum (Average, Min, Max will always return 1, therefore these metrics are not used)
- _HTTPCode_Target_5XX_Count - Sum (Average, Min, Max will always return 1, therefore these metrics are not used)
- _IPv6ProcessedBytes - Sum, Average, Min, Max
- _IPv6RequestCount - Sum (Average, Min, Max will always return 1, therefore these metrics are not used)
- _NewConnectionCount - Sum (Average, Min, Max "not typically useful")
- _ProcessedBytes - Sum, Average, Min, Max
- _RejectedConnectionCount - Sum (Average, Min, Max "not typically useful")
- _RequestCount - Sum (Average, Min, Max will always return 1, therefore these metrics are not used)
- _TargetConnectionErrorCount - Sum (Average, Min, Max "not typically useful")
- _TargetResponseTime - Average (Sum, Min, Max "not typically useful")
- _TargetTLSNegotiationErrorCount - Sum (Average, Min, Max "not typically useful")
- _UnHealthyHostCount - Average, Min, Max (Sum "not typically useful")


## Prerequisites
To use this plugin, you'll need the following details:

- **Access Key**: User must be in a group which has the *CloudWatchReadOnlyAccess* permission.
- **Secret Access Key**: Corresponding Secret Access key for the above.
- **AWS Region**: eg. *eu-west-2* defaults to *eu-west-2*
- **Load Balancer Name**: *app/load1/2118d2a05c1c8908* in the screenshot below.

![](http://i65.tinypic.com/a455pi.png)

## Usage

1. Download the latest release from the Dynatrace Community.
2. Install the plugin via the client (or use the [REST interface](https://community.dynatrace.com/community/pages/viewpage.action?pageId=221381697) to automate).
3. Create a monitor and configure the monitor (the *host* setting is not used so setting this to localhost is acceptable).

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
