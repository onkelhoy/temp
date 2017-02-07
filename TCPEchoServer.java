package lab1;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class TCPEchoServer {
    public static final int MYPORT= 4950;

    public static void main(String[] args) throws IOException {
	
		ServerSocket serverSocket = new ServerSocket(MYPORT);
		ExecutorService exec = Executors.newCachedThreadPool();
		System.out.println("server is running");
	
		while(true){
			//create new thread
			Socket client_socket = serverSocket.accept(); //a new socket connected
			try {
				exec.execute(new Client(client_socket));
			} finally {
				//client_socket.close();
			}
		}
    }
}

class Client implements Runnable {
	private int BUFSIZE = 1024;
	
	private Socket socket;
	private BufferedReader in;
	private BufferedInputStream input;
    private PrintWriter out;
	
	public Client(Socket socket){
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()), BUFSIZE);
		    out = new PrintWriter(socket.getOutputStream(), true);
		    input = new BufferedInputStream(socket.getInputStream());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

    @Override
	public void run() {
		System.out.println("new connection");
		
		while(true){
			try{
				if(out.checkError()){ //client disconnected
					socket.close();
					out.close();
					in.close();
					break;
				}
				byte[] buf = new byte[BUFSIZE];
				int inputBytes = input.read(buf);
	    		// String inputText = in.readLine();
	    		
	    		System.out.printf("TCP echo request from %s", socket.getInetAddress().getHostAddress());
	    	    System.out.printf(" using port %d, Read %dbytes\n", socket.getPort(), inputBytes);

	    	    out.println( new String(buf, 0, 16) );
	    	    // out.flush();
			}
			catch( IOException e){
				e.printStackTrace();
				// out.close();
				break;
			}
		}
	}
}
