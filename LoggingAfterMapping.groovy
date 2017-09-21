/* Payload Logging Before Mapping */


import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
	def pmap = message.getProperties();
	
	// Checking value of Enable_Logging Property
	enable_log = pmap.get(("ENABLE_LOGGING"));
	if(enable_log == null || enable_log == 'false'){
		enable_log = 'FALSE';
	}
	
	def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null && enable_log != 'FALSE'){
        messageLog.setStringProperty("LoggingAfterMapping", "Printing Payload As Attachment")
        messageLog.addAttachmentAsString("3.Payload After Mapping:", body, "text/xml");
     }
    return message;
}