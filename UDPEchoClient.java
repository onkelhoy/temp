/*
  UDPEchoClient.java
  A simple echo client with no error handling
*/

package lab1;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Date;

public class UDPEchoClient extends Transport {
    
    public UDPEchoClient(String[] args) throws IOException, InterruptedException { //maybe have throw instead of try catch..
    	super(args);

		DatagramSocket socket = new DatagramSocket(null);
		SocketAddress local = new InetSocketAddress(super.getPort()),
					remote = new InetSocketAddress(super.getIP(), super.getPort());
		socket.bind(local);
		int rate = super.getRate();
			
		int sendTime = 1000/rate; //rate per second
			/* should check if sendTime is too small.. 
			 * then notify user that only a certain 
			 * amount can be sent on 1s, the rest will over due*/
			
		byte[] buf = new byte[super.getBufSize()];
		DatagramPacket sendPacket = new DatagramPacket(super.getMSG().getBytes(), super.getMSG().length(), remote);

			/* Create datagram packet for receiving echoed message */
		DatagramPacket receivePacket= new DatagramPacket(buf, buf.length);

		long estimated = 0;
		Date start = new Date();
		Date started = new Date();
			
		while(rate > 0){ //have the waiting time after date is created..
			
			Thread.sleep(sendTime+estimated); //should include esitmated time 
						
			socket.send(sendPacket);
			socket.receive(receivePacket);
					
			long comp = Comp(start, receivePacket);
			estimated = sendTime-comp; //this is not esitmated time, but the delta for this..
			start = new Date();
				
			rate--;
		}
		long duration = new Date().getTime()-started.getTime();
		int over = (int)(1000-duration);
		socket.close();
		super.log.append(String.format("The operation took: %dms, %s%dms", duration, over < 0 ? "+" : "-", Math.abs(over)));
		System.out.print(super.log.toString());
    }
    
    private long Comp(Date start, DatagramPacket r){ //compares with time check
    	/* Compare sent and received message */
    	String newMSG = new String(r.getData(), r.getOffset(), r.getLength());
    	long comp = (new Date().getTime()-start.getTime());
    	super.log.append(String.format("%dms ", comp));
    	super.Verify(newMSG, r.getLength());
    	return comp;
    }
}
