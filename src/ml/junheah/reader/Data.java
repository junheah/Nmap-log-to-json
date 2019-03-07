package ml.junheah.reader;

import java.util.List;
public class Data {
	String host;
	int status;
	// 0 : down
	// 1 : up
	// 2 : filtered
	List<Port> scannedPorts;
	String ignoredState;
	int seqIndex;
	String ipIdSeq;
	String os;
	public Data(String host, List<Port> scannedPorts, int status, String ignoredState, int seqIndex, String ipIdSeq, String os) {
		this.host = host;
		this.scannedPorts = scannedPorts;
		this.status = status;
		this.ignoredState = ignoredState;
		this.seqIndex = seqIndex;
		this.ipIdSeq = ipIdSeq;
		this.os = os;
	}
	int getStatus() {return status;}
}
