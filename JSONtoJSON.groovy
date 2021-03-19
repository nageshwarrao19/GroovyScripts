/* This script reads input JSON payload and creates JSON relevant to ECC Odata (JSON) service 

Source JSON =
{
  "EtSoStatusSet": {
    "EtSoStatus": [
      {
        "SalesOrderNumber": "0550008218"
      },
      {
        "SalesOrderNumber": "0550008219"
      },
      {
        "SalesOrderNumber": "0550008220"
      }
    ]
  }
}

Target JSON =
{
  "ordRef": "TEST",
  "salesOrderSets": [
    {
      "ordRef": "TEST",
      "ordNum": "0550008220"
    },
    {
      "ordRef": "TEST",
      "ordNum": "0550008220"
    },
    {
      "ordRef": "TEST",
      "ordNum": "0550008220"
    }
  ],
  "SalesOrdStatuses": []
}


*/



import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.MarkupBuilder;
import groovy.json.*;

def Message processData(Message message) {
    
    def body = message.getBody(String)
    Map parsedJSON = new JsonSlurper().parseText(body)
	//Extracting Sales Order numbers passed in source and adding to a list
	def salesOrderSets = []
	def ordNo;
	parsedJSON.EtSoStatusSet.EtSoStatus?.each {
		ordNo = it.SalesOrderNumber
		salesOrderSets.add(ordRef : { 'TEST' }, ordNum : { ordNo })	
	}


/*	def salesOrderSets = [
		[ ordRef : { 'TEST' }, ordNum : { '0550008220' } ],
		[ ordRef : { 'TEST' }, ordNum : { '0550008220' } ]
	]
*/
	// JSON builder for generating outbound json 
    def builder = new JsonBuilder(
		ordRef: 'TEST', 
		salesOrderSets: salesOrderSets.collect { 
            [ 
                ordRef: it.ordRef(), 
                ordNum: it.ordNum()
            ]
            },
		SalesOrdStatuses: []
 )

    message.setBody(builder.toPrettyString())

return message
}
