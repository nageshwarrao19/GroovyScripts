/* This script extracts the Compound Employee Query Execution time and stores it in local variable */

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	
	pMap = message.getProperties();
	hMap =  message.getHeaders();
	def body = message.getBody(java.lang.String) as String;
	def person_id_external_ext = pMap.get("person_id_external");
	def company_territory_code_ext = pMap.get("company_territory_code");
	def company_ext = pMap.get("legal_entity");
	def ecvss_last_modified_on = hMap.get("ECVSS_LAST_MODIFIED_ON");
	
	
	String modDate = pMap.get("CE_ExecutionTime");
	modDate = modDate.substring(0,modDate.size()-5)+"Z";
	
	// If any External Parameter is used, the current execution time is not stored in database, but the old existing execution time of last run is overwritten
	if((person_id_external_ext != '') || (company_territory_code_ext != '') || (company_ext != '')){
		 modDate = ecvss_last_modified_on;
	}
			
	message.setProperty("ExecutionPersistTime", modDate);
	
	return message;
}
