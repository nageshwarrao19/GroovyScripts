/* Exception Logging */


import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
	def pMap = message.getProperties();
	
	def pointoffailure = pMap.get("PointOfFailure");
		
	def messageLog = messageLogFactory.getMessageLog(message);
			
		
    if(messageLog != null ){
        messageLog.setStringProperty("Exception occured at '"+ pointoffailure +"'", "Adding Exception Log as Attachment")
        messageLog.addAttachmentAsString("ExceptionLog ( Occurred at '"+ pointoffailure +"')", body, "text/xml");
     }
    return message;
}