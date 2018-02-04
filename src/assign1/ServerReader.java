package assign1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import inputport.nio.manager.NIOManagerFactory;

public class ServerReader implements Runnable {

	private ArrayBlockingQueue<ByteBuffer> readQueue;
	private List<SocketChannel> clients;

	public ServerReader(ArrayBlockingQueue<ByteBuffer> readQueue, List<SocketChannel> clients) {
		this.readQueue = readQueue;
		this.clients = clients;
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
			// send out to the clients
			System.out.println("Server is now sending to clients");
			for (SocketChannel client : clients) {
				NIOManagerFactory.getSingleton().write(client, message);

			}
		}
	}

}
