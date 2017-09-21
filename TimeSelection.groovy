/* This script selects and formats the Timestamp to be used in the query to be passed to Compound Employee API  */

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	
	map = message.getProperties();
	String modDate = map.get("LOCAL_LAST_MODIFIED_ON");

	if (modDate.size()>19)
   	modDate=modDate.substring(0,19)+"Z";
   		
	message.setProperty("TEMP_LAST_MODIFIED_ON", modDate);
	return message;
}

