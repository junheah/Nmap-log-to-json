package ml.junheah.reader;

import java.util.List;
public class Data {
	String host;
	int status;
	// 0 : down
	// 1 : up
	// 2 : filtered
	List<Port> scannedPorts;
	public Data(String host, List<Port> scannedPorts, int status) {
		this.host = host;
		this.scannedPorts = scannedPorts;
		this.status = status;
	}
	
	List<Port> getScannedPorts(){return scannedPorts;}
	String getHost() {return host;}
	int getStatus() {return status;}
}
