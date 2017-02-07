package lab1;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	public static void main(String[] args){

		ExecutorService exec = Executors.newCachedThreadPool();
			
		for(int i = 0; i < 1; i++){ //multible connections
			exec.execute(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Transport transport = new TCPEchoClient(args);
						
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
				}
					
			});
		}
		
		exec.shutdown();
	}
}
