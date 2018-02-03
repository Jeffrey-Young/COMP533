package assign1;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

public class ServerReader implements Runnable {

	private ArrayBlockingQueue<ByteBuffer> readQueue;

	public ServerReader(ArrayBlockingQueue<ByteBuffer> readQueue) {
		this.readQueue = readQueue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				ByteBuffer message = readQueue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// send out to the client
			NIOManager
		}
	}

}
