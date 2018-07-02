import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.util.Zip4jConstants
import net.lingala.zip4j.io.*;
import net.lingala.zip4j.core.ZipFile
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
 

def Message processData(Message message) {
	
	String body = message.getBody(java.lang.String) as String;	
		
	byte[] bytesToWrite = body.getBytes();
	
	InMemoryOutputStream inMemoryOutputStream = new InMemoryOutputStream();
	
	ZipOutputStream zos = new ZipOutputStream(inMemoryOutputStream);
	
	
	// Initiate Zip Parameters which define various properties such as compression method, etc.
	ZipParameters parameters = new ZipParameters();
	
	//Deflate compression or store(no compression) can be set below
	parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
	
	// Set the compression level. This value has to be in between 0 to 9
	// Several predefined compression levels are available
	// DEFLATE_LEVEL_FASTEST - Lowest compression level but higher speed of compression
	// DEFLATE_LEVEL_FAST - Low compression level but higher speed of compression
	// DEFLATE_LEVEL_NORMAL - Optimal balance between compression level/speed
	// DEFLATE_LEVEL_MAXIMUM - High compression level with a compromise of speed
	// DEFLATE_LEVEL_ULTRA - Highest compression level but low speed
	parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
	
	//this flag identifies if encryption is required or not
	parameters.setEncryptFiles(true);
	
	
	//Zip4j supports AES or Standard Zip Encryption (also called ZipCrypto)
	parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
	
	// Name of the file inside the zip
	parameters.setFileNameInZip("Filename.html");
	
	// Decrypting Password of the file inside the zip
	parameters.setPassword("password");
	
	// this flag identifies that the data will not be from a file but directly from a stream
	parameters.setSourceExternalStream(true);
	
	zos.putNextEntry(null, parameters);
	zos.write(bytesToWrite);
	zos.closeEntry();
	zos.finish();
	zos.close();
   
	message.setBody(inMemoryOutputStream.getZipContent());
	
	return message;
}