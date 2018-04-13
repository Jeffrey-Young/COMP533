package assign1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import global.SimulationParameters;
import inputport.nio.manager.NIOManagerFactory;
import util.interactiveMethodInvocation.IPCMechanism;

public class ServerReader implements Runnable {

	private ArrayBlockingQueue<Map<SocketChannel, ByteBuffer>> readQueue;
	private List<SocketChannel> clients;
	private NIOServer server;

	public ServerReader(NIOServer server, ArrayBlockingQueue<Map<SocketChannel, ByteBuffer>> readQueue, List<SocketChannel> clients) {
		this.server = server;
		this.readQueue = readQueue;
		this.clients = clients;
	}

	@Override
	public void run() {
		Map<SocketChannel, ByteBuffer> message = null;
		while (true) {

			try {
				message = readQueue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// send out to the clients
			//System.out.println("Server is now sending to clients");
			for (SocketChannel client : clients) {
				if (!SimulationParameters.getSingleton().isAtomicBroadcast() && client.equals(message.keySet().toArray()[0])) {
					continue;
				}
				NIOManagerFactory.getSingleton().write(client, message.get(message.keySet().toArray()[0]));

			}
		}
	}

}
