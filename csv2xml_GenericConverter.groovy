/* Below groovy script takes a csv stream as input and converts it into an xml. The first row is treated as header and next lines as data lines.
   Header row values are taken as XML Tags and data lines are taken as values. 
   ** To conform to xml standards, all special chars,numbers are removed from first header row before creation of xml tags. Data lines remain as it is.
   ** XML tags cannot start with 
   e.g. input.csv
   
   HDR1,HDR2,3HDR3,HDR:4
   A1,A2,A3,A4
   B1,2B,B:3,B&4
   C@1,C#2,,C4
   
   e.g output.xml
   
   <?xml version="1.0" encoding="utf-8"?>
    <CSV>
    <Row>
        <HDR1>A1</HDR1>
        <HDR2>A2</HDR2>
        <HDR3>A3</HDR3>
        <HDR4>A4</HDR4>
    </Row>
    <Row>
        <HDR1>B1</HDR1>
        <HDR2>2B</HDR2>
        <HDR3>B:3</HDR3>
        <HDR4>B&amp;4</HDR4>
    </Row>
    <Row>
        <HDR1>C@1</HDR1>
        <HDR2>C#2</HDR2>
        <HDR3>C4</HDR3>
        <HDR4>null</HDR4>
    </Row>
    </CSV>
*/

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.ArrayList.*;

def Message processData(Message message) {


def body = message.getBody(String.class);
def reader = new StringReader(body);
def HeadersList, RowList;
int NoOfHeaders;
String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><CSV>"


reader.eachLine()
    {   String line, count ->
        
          
        if(count == 1)   // count = 1 is first row i.e. Header 
        {
            line = line.replaceAll("[^A-Za-z,0-9]", "");            // Removing all non-alphabets, excludes separator ','.
            HeadersList = line.tokenize(',');                       // if line is 'H1,H2,H3,4H4' then HeadersList is an array [H1,H2,H3,4H4].
            HeadersList = HeadersList*.replaceFirst("^[0-9]", "");  //If the first character is a number, replace it. Because XML tags cannot start with a number. Eg if data is "3HDR3" then value is changed to "HDR3".
            NoOfHeaders = HeadersList.size(); 
        }       
        if(count > 1) // count > 1 is Second row onwards i.e. Data lines
        {           
            xml = xml + "<Row>";  
            line = line.replaceAll("&","&amp;");  // Replacing & with &amp;
            RowList = line.tokenize(',');           
            0.upto(NoOfHeaders-1) // Looping 
            {
               xml = xml + "<" + HeadersList[it] + ">" + RowList[it] + "</" + HeadersList[it] + ">";
            }
            xml = xml + "</Row>";
        }
    }


xml = xml + "</CSV>"
message.setBody(xml);

return message;

}