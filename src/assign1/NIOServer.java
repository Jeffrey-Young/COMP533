package assign1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import javax.net.ServerSocketFactory;

import assignments.util.mainArgs.ServerArgsProcessor;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.factories.SelectorFactorySet;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.nio.SocketChannelBound;
import inputport.nio.manager.AnNIOManager;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import inputport.nio.manager.SelectionManager;
import inputport.nio.manager.factories.SelectionManagerFactory;
import inputport.nio.manager.factories.classes.AReadingAcceptCommandFactory;
import inputport.nio.manager.factories.selectors.AcceptCommandFactorySelector;
import inputport.nio.manager.listeners.SocketChannelAcceptListener;

import util.annotations.Tags;
import util.tags.DistributedTags;
@Tags({DistributedTags.SERVER})
public class NIOServer implements SocketChannelAcceptListener {
	public static final String READ_THREAD_NAME = "Read Thread";
	ServerReceiver serverReceiver;
	ServerSocketChannel serverSocketChannel;
	private ArrayBlockingQueue<ByteBuffer> readQueue;
	
	public NIOServer() {
		readQueue = new ArrayBlockingQueue<ByteBuffer>(4096);
		
		//start read processor
		ServerReader reader = new ServerReader(readQueue);
		Thread readThread = new Thread(reader);
		readThread.setName(READ_THREAD_NAME);
		readThread.start();
	}

	protected void createCommunicationObjects() {
		createReceiver();
	}
	protected void createReceiver() {
		serverReceiver = new ServerReceiver(readQueue);
	}

	
	protected void setFactories() {
		AcceptCommandFactorySelector.setFactory(new AReadingAcceptCommandFactory());
	}

	protected void makeServerConnectable(int aServerPort) {
		NIOManagerFactory.getSingleton().enableListenableAccepts(
				serverSocketChannel, this);
	}

	public void initialize(int aServerPort) {
		setFactories();		
		serverSocketChannel = createSocketChannel(aServerPort);
		createCommunicationObjects();
		makeServerConnectable(aServerPort);
	}

	protected ServerSocketChannel createSocketChannel(int aServerPort) {
		try {
			ServerSocketChannel retVal = ServerSocketChannel.open();
			InetSocketAddress isa = new InetSocketAddress(aServerPort);
			retVal.socket().bind(isa);
			SocketChannelBound.newCase(this, retVal, isa);
			return retVal;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	protected void addReadListener(SocketChannel aSocketChannel) {
		NIOManagerFactory.getSingleton().addReadListener(aSocketChannel,
				serverReceiver);
	}

	protected void addListeners(SocketChannel aSocketChannel) {
//		addWriteBufferListener(aSocketChannel);
		addReadListener(aSocketChannel);		
	}
	@Override
	public void socketChannelAccepted(ServerSocketChannel aServerSocketChannel,
			SocketChannel aSocketChannel) {
		addListeners(aSocketChannel);
	}
//	@Override
//	public void writeBufferIsEmpty(SocketChannel aSocketChannel) {
//		NIOManagerFactory.getSingleton().enableReads(aSocketChannel);
//	}

	public static void main(String[] args) {
		FactoryTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		BeanTraceUtility.setTracing();// not really needed, but does not hurt
		NIOServer aServer = new NIOServer();
		aServer.initialize(ServerArgsProcessor.getServerPort(args));
	}
}
