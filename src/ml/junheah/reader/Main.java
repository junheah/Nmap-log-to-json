package ml.junheah.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length<1) System.out.println("usage: ./executable.jar <folder location>");
		else{
			System.out.println("location = "+args[0]);
			new Main().run(args[0]);
		}
	}
	
	public void run(String path) {
		try {
			List<Data> data;
			File file  = new File(path);
			int count = 0;
			File output = new File("."+File.separator+"result_"+count+".json");
			while(output.exists()) {
				count++;
				output = new File("."+File.separator+"result_"+count+".json");
			}
			//write to file
			try {
			      String result = new Gson().toJson(readRecursive(file));
			      FileWriter fw = new FileWriter(output);
			      fw.write(result);
			      fw.close();
			      System.out.println("output saved to " + output.getAbsolutePath());
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
		    String host = "";
		    int status = 0;
		    List<Port> ports = null;
		    while ((line = br.readLine()) != null) {
		       if(line.contains("#")) continue;
		       else if(line.contains("\tStatus")) {
		    	   //parse status
		    	   String raw[] = line.split("\t");
		    	   host = raw[0].split(" ")[1];
		    	   String stat = raw[1].split(" ")[1];
		    	   for(int i=0; i<statStr.length; i++) {
		    		   if(stat.contains(statStr[i])) {
		    			   status = i;
		    			   break;
		    		   }
		    	   }
		       }else if (line.contains("\tPorts")) {
		    	   ports = new ArrayList<>();
		    	   String pRaw = line.split("\t")[1];
		    	   String[] portTmp = pRaw.substring(pRaw.indexOf(" ")+1).split(",");
		    	   for(String p : portTmp) {
		    		   ports.add(new Port(p));
		    	   }
		       }
		    }
		    d = new Data(host,ports,status);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return d;
	}

}
