package lab1;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class TCPEchoClient extends Transport {
	private BufferedReader input;
    private PrintWriter output;
	
	public TCPEchoClient(String args[]) throws IOException, InterruptedException {
		super(args); // start with load
		Socket socket = new Socket(super.getIP(), super.getPort());
		super.log.append(String.format("[%s:%d] : connected\n", socket.getLocalAddress(), socket.getLocalPort()));
		try { 
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()), super.getPort());
		    output = new PrintWriter(socket.getOutputStream(), true);
		}
		catch(IOException e){
			e.printStackTrace();
		}

		int rate = super.getRate(), sendTime = 1000/rate;
		Date start = new Date();
		
		// do the work
		while(rate > 0){
			Thread.sleep(sendTime); //no measurement on time..
			output.println(super.getMSG()); // sends the message
			super.log.append(String.format("%dms ", (new Date().getTime()-start.getTime()))); //appends the time it took
			super.Verify(input.readLine(), socket.getReceiveBufferSize()); // verifies the send and receive msg
			start = new Date();
			rate--;
		}

		super.log.append(String.format("[%s:%d] : closed\n\n", socket.getLocalAddress(), socket.getLocalPort())); // appends a client ended (to be able to read it)
		socket.close(); //closed when finished
		System.out.print(super.log.toString()); //print the log
	}
	
}
