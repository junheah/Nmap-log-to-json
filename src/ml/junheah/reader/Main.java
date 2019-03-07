package ml.junheah.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length<1) System.out.println("arguments: <String folder location> <Boolean output html (default: false)>");
		else {
			System.out.println("scan location = "+args[0]);
			Boolean html = false;
			if(args.length == 2) html = Boolean.parseBoolean(args[1]);
			new Main().run(args[0], html);
		}
	}
	
	public void run(String path, Boolean js) {
		try {
			File file  = new File(path);
			int count = 0;
			File output = new File("result_"+count+".json");
			while(output.exists()) {
				count++;
				output = new File("result_"+count+".json");
			}
			//write to file
			try {
			      String result = new Gson().toJson(readRecursive(file));
			      FileWriter fw = new FileWriter(output);
			      fw.write(result);
			      fw.close();
			      System.out.println("json output: " + output.getAbsolutePath());
			      if(js) {
			    	  //create web folder
			    	  File webFolder = new File("result_web_"+count);
			    	  while(webFolder.exists()) {
			    		  count++;
			    		  webFolder = new File("result_web_"+count);
			    	  }
			    	  webFolder.mkdir();
			    	  //save as js var
			    	  fw = new FileWriter(new File(webFolder, "data.js"));
				      fw.write("var data = " + result);
				      fw.close();
				      //copy html from res
				      
				      InputStream is = ClassLoader.getSystemClassLoader().getResource("result.html").openStream();
				      FileOutputStream  os = new FileOutputStream(new File(webFolder,"result.html"));
				      byte[] buffer = new byte[1024];
				      int length;
				      while ((length = is.read(buffer)) > 0) {
				    	  os.write(buffer, 0, length);
				      }
				      is.close();
				      os.close();
				      System.out.println("web output: " + webFolder.getAbsolutePath());
			      }
			    } catch (Exception e) {
			      e.printStackTrace();
			    }
				
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	List<Data> readRecursive(File f) {
		List<Data> result = new ArrayList<>();
		if(f.isDirectory()) {
			for(File c : f.listFiles()) {
				result.addAll(readRecursive(c));
			}
		}else {
			Data d = parseFile(f);
			if(d.getStatus()>0) result.add(parseFile(f));
		}
		return result;
	}
	
	
	static String[] statStr = {"Down","Up","Filtered"};
	Data parseFile(File f) {
		Data d = null;
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
		    String line;
		    String host = "", ignoredState = "", ipIdSeq = "", os = "";
		    int status = 0, seqIndex = 0;
		    List<Port> ports = null;
		    while ((line = br.readLine()) != null) {
		       if(line.contains("#")) continue;
		       else if(line.contains("\tStatus")) {
		    	   //parse status
		    	   String raw[] = line.split("\t");
		    	   host = raw[0].split(" ")[1];
		    	   System.out.println("host: "+host);
		    	   String stat = raw[1].split(" ")[1];
		    	   for(int i=0; i<statStr.length; i++) {
		    		   if(stat.contains(statStr[i])) {
		    			   status = i;
		    			   break;
		    		   }
		    	   }
		       }else if (line.contains("\tPorts")) {
		    	   ports = new ArrayList<>();
		    	   String[] datas = line.split("\t");
		    	   for(String data : datas) {
		    		   if(data.contains("Ports: ")) {
				    	   data = data.substring(data.indexOf(":")+2);
				    	   while(data.length()>0) {
				    		   int portNo = 0;
				    		   String state = "", protocol = "", owner = "", service = "", sunRpcInfo = "", versionInfo = "";
				    		   int next = data.indexOf("/");
				    		   for(int i=0;true;i++) {
				    			   try {
										if(i==7) break;
										next = data.indexOf("/")>-1 ? data.indexOf("/") : data.length();
										String tmp = data.substring(data.indexOf(" ") == 0 ? 1: 0,next);
										switch(i) {
										case 0:
											portNo = Integer.parseInt(tmp);
											break;
										case 1:
											state = tmp;
											break;
										case 2:
											protocol = tmp;
											break;
										case 3:
											owner = tmp;
											break;
										case 4:
											service = tmp;
											break;
										case 5:
											sunRpcInfo = tmp;
											break;
										case 6:
											versionInfo = tmp;
											break;
										}
										data = data.substring(next+1);
									}catch(Exception e) {
										e.printStackTrace();
										break;
									}
								}
				    		   ports.add(new Port(portNo, state, protocol, owner, service, sunRpcInfo, versionInfo));
				    		   //get next portStr
				    		   int nextComma = data.indexOf(",");
				    		   if(nextComma>-1) data = data.substring(nextComma+1);
				    	   }
		    		   }else if(data.contains("Ignored State: ")) {
		    			   ignoredState =  data.substring(data.indexOf(":")+2);
		    		   }else if(data.contains("Seq Index: ")) {
		    			   seqIndex = Integer.parseInt(data.substring(data.indexOf(":")+2));
		    		   }else if(data.contains("IP ID Seq: ")) {
		    			   ipIdSeq = data.substring(data.indexOf(":")+2);
		    		   }else if(data.contains("OS: ")) {
		    			   os = data.substring(data.indexOf(":")+2);
		    		   }
			       }
		       }
		    }
		    d = new Data(host,ports,status,ignoredState,seqIndex,ipIdSeq,os);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return d;
	}

}
