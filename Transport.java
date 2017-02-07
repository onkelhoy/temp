package lab1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Date;

public abstract class Transport {
	private int port, rate, bufsize;
	private String ip, MSG = "An Echo Message!";
	protected StringBuilder log;
	
	public Transport(String[] args){
		log = new StringBuilder();
		if(args.length != 4){
			printErr("Please specify all arguments ip, port, bufsize, message-transfer-rate");
		}
		else {
			try {
				validateIP(args[0]);
				ip = args[0];
			}
			catch(Exception e){
				printErr(e.getMessage());
			}
			try{
				port = Integer.valueOf(args[1]);
				bufsize = Integer.valueOf(args[2]);
				if(bufsize < 0 || bufsize > 1450) printErr("Invalid buffer size"); 
				//standard is 1500 so a little space for IP and UDP header p.425 in the book

				rate = Integer.valueOf(args[3]);
				if(rate == 0) rate = 1;
				else if(rate < 0) printErr("Invalid message rate");
				
				if(port < 0 || port > 65535) printErr("Invalid port");
				// based on wiki
			}
			catch(Exception e){
				printErr("invalid buffer, port or message-rate");
			}
		}
	}

	public int getBufSize(){return bufsize;}
	public int getRate(){return rate;}
	public int getPort(){return port;}
	public String getIP(){return ip;}
	public String getMSG(){return MSG;}
	
	
	public void Verify(String newMSG, int bytes){
		if (newMSG.compareTo(MSG) == 0)
			log.append(bytes+" bytes sent and received\n");
    	else
    		log.append(newMSG+" Sent and received msg not equal\n");
	}
	
	// IP validation
	private void validateIP(String ip) throws Exception {
		String[] split = ip.split("\\."); //splits the ip into all octats
		if(split.length != 4) throw new Exception("Invalid ip: not a valid octat length: " + split.length);
		else { //if octat is the correct length
			for(int i = 0; i < 4; i++){ //check every octat is correct, if not then fail
				try{
					int octat = Integer.valueOf(split[i]);
					if(octat < 0 || octat > 255) throw new Exception("Invalid ip: invalid octat:" + octat);
				}
				catch(Exception e){
					throw new Exception("Invalid ip: invalid octat:" + split[i]);
				}
			}
		}
	}
	// error printout
	private void printErr(String msg){
		System.err.print(msg+"\n"); //printing the error msg
		System.exit(1);
	}
}
