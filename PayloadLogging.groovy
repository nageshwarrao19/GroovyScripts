/* Logging Blank Response Payload from CE API*/

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
	def pMap = message.getProperties();
	def hMap = message.getHeaders();
	
	def whereString = hMap.get("whereString");
	
	/* Checking value of Enable_Logging Property
	enable_log = pMap.get(("ENABLE_LOGGING"));
	if(enable_log == null || enable_log == 'false'){
		enable_log = 'FALSE';
	} */
	
	def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null){
        messageLog.setStringProperty("LoggingBlankPayload", "Printing Payload As Attachment")
        messageLog.addAttachmentAsString("2.Compound Employee Response (No Delta Changes):", "Blank Response from the Query with Parameters -> "+ whereString + "\n" + "\n" + body, "text/xml");
     }
    return message;
}