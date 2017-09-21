/* Property and Header Logging based on the value passed to Enable_Logging Property*/


import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
    hMap =  message.getHeaders();
    pMap = message.getProperties();
  
	StringBuffer str = new StringBuffer();
	
	// Checking value of Enable_Logging Property
	/*
	enable_log = pMap.get(("ENABLE_LOGGING"));
	
	if(enable_log == null || enable_log == 'false' || enable_log == ''){
		enable_log = 'FALSE';
	}
	*/
	
	//Accessing all Properties and Header Values set till now
	def person_id_external_ext = pMap.get("person_id_external");
	def company_territory_code_ext = pMap.get("company_territory_code");
	def company_ext = pMap.get("legal_entity");
	def temp_last_modified_on = pMap.get(("TEMP_LAST_MODIFIED_ON"));
	def whereString = hMap.get("whereString");
	def selectString = hMap.get("selectString");
	def vss_last_modified_on = hMap.get("VSS_LAST_MODIFIED_ON");
	def ecvss_last_modified_on = hMap.get("ECVSS_LAST_MODIFIED_ON");
	
	// Writing All External Parameters
	str.append("External Parameters"+"\n");
	
	if(person_id_external_ext != ''){
			str.append("'person_id_external' =  " + person_id_external_ext +"\n");
		}
	else 	str.append("'person_id_external' =  NULL (Value Not Set)" + "\n");
		
	if(company_territory_code_ext != '')
		{
			str.append("'company_territory_code' =  " + company_territory_code_ext +"\n");
		}
	else 	str.append("'company_territory_code' =  NULL (Value Not Set)" + "\n");    
	
	if(company_ext != '')
		{
				str.append("'legal_entity' =  " + company_ext +"\n");
		}
	else 	str.append("'legal_entity' =  NULL (Value Not Set)" + "\n");   
	
	if(vss_last_modified_on != '')
		{
			str.append("'vss_last_modified_on' = " + vss_last_modified_on +"\n");
		}
	else 	str.append("'vss_last_modified_on' =  NULL (Value Not Set)" + "\n");
	
	
	// Writing Locally Stored and Retrieved Parameter
	str.append("\n" + "Stored Local Parameter" + "\n");
	if(ecvss_last_modified_on != '')
		{
			if(vss_last_modified_on != '')
				{
					str.append("'ecvss_last_modified_on' = Parameter not used when vss_last_modified_on is provided Externally" +"\n");
				}
			else str.append("'ecvss_last_modified_on' = " + ecvss_last_modified_on +"\n");		
		}
	else 	str.append("'ecvss_last_modified_on' =  NULL (No stored last Value)" + "\n");
		
	
	// Writing the Query Parameter passed to Compound Employee API
	str.append("\n" + "Query Parameters passed to SuccessFactors CE API" + "\n");
	str.append("'SelectString' = " + selectString  + "\n");
	str.append("'WhereString' = " + whereString  + "\n");		
	if(temp_last_modified_on != '')
	{
		if((vss_last_modified_on != '')){
			str.append("'last_modified_on' = " + vss_last_modified_on + " (Value provided via External Header Parameter VSS_LAST_MODIFIED_ON)" +"\n");
		}
		else str.append("'last_modified_on' = " + temp_last_modified_on + " (Value derived from Last Query Execution Time)" +"\n");
	}
				   
		
	def messageLog = messageLogFactory.getMessageLog(message);
	
    if(messageLog != null){
		 messageLog.setStringProperty("Properties", "Printing Properties/Headers/Parameters As Attachment")
		 messageLog.addAttachmentAsString("1.Properties", str.toString(), "text/plain");
     }
    return message;
}