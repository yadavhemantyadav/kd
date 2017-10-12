package heman.kd_timetable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import utils.GoogleSheetReader;



public class ReadPdf {
	
	private ReadPdf(){
		
	}
	
	public static List<String> readPDF(String urlParam) throws IOException{
		URL url = new URL(urlParam);
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		FileOutputStream fos = new FileOutputStream(new File("downloaded.pdf"));
		
		byte[] buf = new byte[512];
		while (true) {
		    int len = in.read(buf);
		    if (len == -1) {
		        break;
		    }
		    fos.write(buf, 0, len);
		}
		in.close();
		fos.flush();
		fos.close();
		
		PDDocument document = PDDocument.load(new File(System.getProperty("user.dir") + "/downloaded.pdf"));

	      //Instantiate PDFTextStripper class
	      PDFTextStripper pdfStripper = new PDFTextStripper();

	      //Retrieving text from PDF document
	      String text = pdfStripper.getText(document);
	      
	      String[] splitted_text = text.split("\n");
	      
	      System.out.println(splitted_text[1]);
	      
	      List<String> abc = new ArrayList<String>();
	      
	      GoogleSheetReader reader = GoogleSheetReader.getInstance();
	      
	      Map<Object, Object> map = reader.getSheetMap("1adE6-7qxa0KwxqYAyPPuvhdoq8NxUebVbhma4JcmOOo", "Helper");
	      
	      String search = map.get("key").toString();
	      
	      for(String s : splitted_text){
	    	  if(s.toLowerCase().contains(search.toLowerCase())){
	    		  abc.add(s);
	    	  }
	      }
	      document.close();
		return abc;
	}
	
	public static void main(String args[]) throws IOException{
		List<String> abc = readPDF("https://kdcampus-s3-storage.s3.ap-south-1.amazonaws.com/routines/11-Oct-17_UTTAM_NAGAR_11.10.2017_UTTAM_NAGAR_TIME_TABLE_.pdf");
		System.out.println(abc);
	}
	
}
