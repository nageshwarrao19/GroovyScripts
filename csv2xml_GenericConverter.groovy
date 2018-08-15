/* Below groovy script takes a csv stream as input and converts it into an xml structure.
   The first row is assumed to be header row and next lines as data lines.
   The first row column values are used as the XML element names, after sanitizing by replacing all non-alphanumeric
characters with underscores.
   ** To conform to xml standards, all special chars,numbers are replaced from first header row before creation of xml tags. Data lines remain as it is.
   ** XML tags cannot start with numbers and hence if the header values start with number, the starting number is removed.
************************************************* 
e.g. input.csv

H?1,H2, H3,4H4,5H H2
A1,A2,A3,A4,,
Ab1,,,Ab4,,
B1,2B,B:3,B&4,
C@1,C#2,,C4,,

************************************************* 
e.g output.xml

<?xml version="1.0" encoding="utf-8"?>
<CSV>
    <Row>
        <H_1>A1</H_1>
        <H2>A2</H2>
        <H3>A3</H3>
        <H4>A4</H4>
        <H_H2>null</H_H2>
    </Row>
    <Row>
        <H_1>Ab1</H_1>
        <H2/>
        <H3/>
        <H4>Ab4</H4>
        <H_H2>null</H_H2>
    </Row>
    <Row>
        <H_1>B1</H_1>
        <H2>2B</H2>
        <H3>B:3</H3>
        <H4>B&amp;4</H4>
        <H_H2>null</H_H2>
    </Row>
    <Row>
        <H_1>C@1</H_1>
        <H2>C#2</H2>
        <H3/>
        <H4>C4</H4>
        <H_H2>null</H_H2>
    </Row>
</CSV>
**************************************************/

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
       if(count == 1)   // count = 1 is first row i.e. Header Row
       {
            HeadersList = line.split(',')*.trim();                         // if line is 'H?1,H2, H3,4H4,5H H5' then HeadersList is an array [H?1,H2,H3,4H4,5H H5].
            HeadersList = HeadersList*.replaceAll("[^A-Za-z,0-9]", "_");   // Replacing all non-alphanumeric chars with '_', excludes separator ','. if array is [H?1,H2,H3,4H4,5H H5]then it is changed to array [H_1,H2,H3,4H4,5H_H5].
            HeadersList = HeadersList*.replaceFirst("^[0-9]", "");         //If the first character is a number, removing it. Because XML tags cannot start with a number. Eg if data array is [H_1,H2,H3,4H4,5H_H5] then value is changed to [H_1,H2,H3,H4,H_H5].
            NoOfHeaders = HeadersList.size(); 
       }       
       if(count > 1) // count > 1 is Second row onwards i.e. Data lines
       {           
            xml = xml + "<Row>";  
            line = line.replaceAll("&","&amp;");  // Replacing & with &amp;
            RowList = line.split(',')*.trim();;           
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
