/* Logging Blank Response Payload from CE API*/

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
	def pMap = message.getProperties();
	def hMap = message.getHeaders();
	
	
	def temp = pMap.get("blankBodyCheck");
	
	//temp = temp.tokenize(/\d/);
	
	def messageLog = messageLogFactory.getMessageLog(message);

        messageLog.addAttachmentAsString("Body", temp + "\n" + body , "text/xml");

    return message;
}