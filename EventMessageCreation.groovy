/* Creating */

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
	def pMap = message.getProperties();
	def hMap = message.getHeaders();
	
	StringBuffer str = new StringBuffer();
	
	def processid = pMap.get("SAP_MessageProcessingLogID"); // SAP unique Message Processing Log ID
	def blankBodyCheck = pMap.get("blankBodyCheck"); // To Check if No Employee Data was Changed
	def pointoffailure = pMap.get("PointOfFailure"); // To Check where the exception occurred
	def exceptionmessage = pMap.get("ExceptionMessage"); // Exception Message if any
	def blankbody = pMap.get("blankBodyCheck"); // No of CompoundEmployee nodes returned in response from CE API Query
	def numberofreturnedemployee = pMap.get("NoOfEmployee"); // Total No of Employee Records sent to Vendor Self Service
	def numberofreturnedvendor = pMap.get("NoOfVendors"); // Total No of Vendors to whom data was sent to Vendor Self Service
	
	//Fetching current datetimestamp in UTC timezone
	def datenow = new Date();
	String datetimenow = datenow.format("yyyy-MM-dd'T'HH:mm:ssz", TimeZone.getTimeZone('UTC'));

	//Checking if any exception occurred OR No Delta Changes OR Data sent successfully to VSS. Setting event related variables
	if((pointoffailure != 'NoDeltaChanges' || pointoffailure != 'CompletedSuccessfully') && (exceptionmessage != null || exceptionmessage != ''))
	{
		eventtype = "FAILED";
		eventname = "An Exception occurred during the execution of the Integration Process on SAP Cloud Integration";
		eventdesc = "Exception on Cloud Integration at '"+pointoffailure+"'.";
		eventattribute = "<eventAttributes><EMEventAttribute><id>1</id><name>ExceptionMessage</name><value>"+exceptionmessage+"</value></EMEventAttribute></eventAttributes>";
		
	}	
	if((pointoffailure == 'NoDeltaChanges') && (blankbody.toInteger() == 0) && (exceptionmessage == null))	
	{
		eventtype = "INFO";
		eventname = "No Delta";
		eventdesc = "No Delta Changes for any Employees. Hence, No data sent from the Employee Central to Vendor Self Service";
		eventattribute = "";
	}
	else if((pointoffailure == 'CompletedSuccessfully') && (blankbody.toInteger() > 0)  && (exceptionmessage == null))
	{
		eventtype = "INFO";
		eventname = "Data Sent";
		eventdesc = "'"+numberofreturnedemployee.toInteger()+"' Employee Records sent to '"+numberofreturnedvendor.toInteger()+"' unique vendors on VendorSelfService.";
		eventattribute = "";
	}
	
	//Creating Message Body for SuccessFactors Event Notification
	
	str.append("<EMEvent><EMEvent><id>1</id><eventType>"+eventtype+"</eventType><eventTime>"+datetimenow.replaceAll('UTC','Z')+"</eventTime><eventName>"+eventname+"</eventName>");
	str.append("<eventDescription>"+eventdesc+"</eventDescription>");   
	str.append("<process><EMMonitoredProcess><processType>INTEGRATION</processType><processInstanceName>SF EC to VendorSelfService_"+datetimenow+"</processInstanceName>");       
	str.append("<processInstanceId>"+processid+"</processInstanceId><processDefinitionName>SF EC to VendorSelfService</processDefinitionName>");     
    str.append("<processDefinitionId>SF EC to VendorSelfService_"+datetimenow+"</processDefinitionId>");   
    str.append("<moduleName>ECT</moduleName><firstEventTime>"+datetimenow+"</firstEventTime><lastEventTime>"+datetimenow+"</lastEventTime>");     
    str.append("<monitoredProcessId>1</monitoredProcessId></EMMonitoredProcess></process>"); 
	str.append(eventattribute);	
	str.append("</EMEvent></EMEvent>");
	
	//Setting the new Body for SuccessFactors Event Notification
	message.setBody(str.toString()); 
	
    return message;
}