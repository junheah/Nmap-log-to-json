package ml.junheah.reader;

public class Port {
	int port;
	String status;
	String protocol;
	String service;
	String os;
	
	public Port(String data) {
		//parse from data str
		//remove whitespace
		String d = data.substring(data.indexOf(" ") == 0 ? 1 : 0);
		int prev = 0;
		int next = d.indexOf("/");
		for(int i=0;true;i++) {
			try {
				next = d.indexOf("/");
				String tmp = d.substring(0,next);
				switch(i) {
				case 0:
					port = Integer.parseInt(tmp);
					break;
				case 1:
					status = tmp;
					break;
				case 2:
					protocol = tmp;
					break;
				case 3:
					break;
				case 4:
					service = tmp;
					break;
				case 5:
					break;
				case 6:
					os = tmp;
					break;
				}
				d = d.substring(next+1);
			}catch(Exception e) {
				break;
			}
		}
	}
}
