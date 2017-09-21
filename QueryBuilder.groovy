/* This script builds the Query Selection Parameters that would be passed to SuccessFactors EC Compound Employee API  */


import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

def Message processData(Message message) {
	
	StringBuffer strselct = new StringBuffer();
	StringBuffer strwhr = new StringBuffer();
	
	//Properties and Headers 
	def pMap = message.getProperties();
	def hMap = message.getHeaders();
	
	def person_id_external_ext = pMap.get("person_id_external");
	def company_territory_code_ext = pMap.get("company_territory_code");
	def company_ext = pMap.get("legal_entity");
	def vss_last_modified_on = hMap.get("VSS_LAST_MODIFIED_ON");
	def last_modified_on = pMap.get("TEMP_LAST_MODIFIED_ON");
	
	
	DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
	Date date = new Date();
	
	// Creating the 'Select' Part of the Query for using with Compound Employee API
	
	strselct.append("SELECT person, personal_information, address_information, phone_information, email_information, employment_information, job_information, alternative_cost_distribution, compensation_information, paycompensation_recurring,paycompensation_non_recurring, payment_information, job_relation, accompanying_dependent, deduction_recurring, deduction_non_recurring, global_assignment_information, direct_deposit, national_id_card, person_relation, dependent_information, emergency_contact_primary, personal_documents_information FROM CompoundEmployee ");
	
	// Creating the 'Where' Part of the Select Query for using with Compound Employee API
	if(person_id_external_ext != '')
	{
		if((person_id_external_ext.trim().isEmpty()) != null)
			{
				strwhr.append(" WHERE ");
				strwhr.append("effective_end_date>=to_date('" + dateFormat.format(date) + "') ");
				strwhr.append(" AND person_id_external in('" + person_id_external_ext +"')");
				strwhr.append(" AND last_modified_on>to_datetime('" + last_modified_on + "') ");
			}	
     }
     else
     {
     			strwhr.append(" WHERE ");
				strwhr.append("effective_end_date>=to_date('" + dateFormat.format(date) + "') ");
				strwhr.append(" AND last_modified_on>to_datetime('" + last_modified_on + "') ");
     
     		if(company_ext != '') 
     		{
     			strwhr.append(" AND company in('" + company_ext +"')");
			}		
			else if(company_territory_code_ext != '') 
     		{
     			strwhr.append(" AND company_territory_code in('" + company_territory_code_ext +"')");
			}
			
			
     }
	 	message.setProperty("SelectQuery", strselct.toString());
		message.setProperty("QueryFilter",strwhr.toString());

	return message;

}

