/* Logging all the Headers and Properties set during Message Processing */

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    map = message.getHeaders();
    pmap = message.getProperties();
    
    // Checking if Logging is Enabled by Setting the External Parameter
    logenabled = map.get(("ENABLE_LOGGING"));
    if(logenabled == null || logenabled == false){
        logenabled = "FALSE";	
     }
 
   // Reading all Properties/Headers 
   KR_LAST_MODIFIED_ON = map.get(("KR_LAST_MODIFIED_ON"));
   ECKR_LAST_MODIFIED_ON = map.get(("ECKR_LAST_MODIFIED_ON"));
   LOCAL_LAST_MODIFIED_ON = pmap.get(("LOCAL_LAST_MODIFIED_ON"));
   TEMP_LAST_MODIFIED_ON = pmap.get(("TEMP_LAST_MODIFIED_ON"));
   QueryFilter = pmap.get(("QueryFilter"));
   whereString = map.get(("whereString"));
   
   //Writing all extracted values into one String
   String properties = "KR_LAST_MODIFIED_ON:->"+KR_LAST_MODIFIED_ON+"" +'"\n"'+ "ECKR_LAST_MODIFIED_ON:->"+ECKR_LAST_MODIFIED_ON+"" +'"\n"'+ "LOCAL_LAST_MODIFIED_ON:->"+LOCAL_LAST_MODIFIED_ON+"" +'"\n"'+ "TEMP_LAST_MODIFIED_ON:->"+TEMP_LAST_MODIFIED_ON+"" +'"\n"'+ "QueryFilter:->"+QueryFilter+"" +'"\n"'+ "whereString:->"+whereString+"";
   
   	def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null && logenabled !="FALSE" ){
         messageLog.addAttachmentAsString("Properties && Headers", properties, "text/xml");
     }
     
    return message;
}