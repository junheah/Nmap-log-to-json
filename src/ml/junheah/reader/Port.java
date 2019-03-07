package ml.junheah.reader;

public class Port {
	int portNo;
	String state;
	String protocol;
	String owner;
	String service;
	String sunRpcInfo;
	String versionInfo;
	
	public Port(int portNo, String state, String protocol, String owner, String service, String sunRpcInfo, String versionInfo) {
		//parse from data str
		//remove whitespace
		this.portNo = portNo;
		this.state = state;
		this.protocol = protocol;
		this.owner = owner;
		this.service = service;
		this.sunRpcInfo = sunRpcInfo;
		this.versionInfo = versionInfo;
	}
}
