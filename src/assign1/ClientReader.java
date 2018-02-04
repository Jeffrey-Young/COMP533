package assign1;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import inputport.nio.manager.NIOManagerFactory;
import stringProcessors.HalloweenCommandProcessor;

public class ClientReader implements Runnable {
	private ArrayBlockingQueue<ByteBuffer> readQueue;
	private HalloweenCommandProcessor commandProcessor;
	
	public ClientReader(ArrayBlockingQueue<ByteBuffer> readQueue, HalloweenCommandProcessor commandProcessor) {
		this.readQueue = readQueue;
		this.commandProcessor = commandProcessor;
		
	}

	@Override
	public void run() {
		ByteBuffer message = null;
		while (true) {
			try {
				message = readQueue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// process command
			String command = new String(message.array(), message.position(), message.array().length);
			System.out.println("Client receives command: " + command);
			commandProcessor.processCommand(command);
		}
	}
}
