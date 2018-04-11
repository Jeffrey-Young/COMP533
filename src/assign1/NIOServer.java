package assign1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import javax.net.ServerSocketFactory;

import assignments.util.inputParameters.ASimulationParametersController;
import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ClientArgsProcessor;
import assignments.util.mainArgs.ServerArgsProcessor;
import global.SimulationParameters;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.factories.SelectorFactorySet;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.nio.SocketChannelBound;
import util.trace.port.rpc.rmi.RMITraceUtility;
import inputport.nio.manager.AnNIOManager;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import inputport.nio.manager.SelectionManager;
import inputport.nio.manager.factories.SelectionManagerFactory;
import inputport.nio.manager.factories.classes.AReadingAcceptCommandFactory;
import inputport.nio.manager.factories.selectors.AcceptCommandFactorySelector;
import inputport.nio.manager.listeners.SocketChannelAcceptListener;

import util.annotations.Tags;
import util.interactiveMethodInvocation.ConsensusAlgorithm;
import util.interactiveMethodInvocation.IPCMechanism;
import util.interactiveMethodInvocation.SimulationParametersController;
import util.tags.DistributedTags;

@Tags({ DistributedTags.SERVER })
public class NIOServer implements SocketChannelAcceptListener {
	public static final String READ_THREAD_NAME = "Read Thread";
	ServerReceiver serverReceiver;
	ServerSocketChannel serverSocketChannel;
	private ArrayBlockingQueue<Map<SocketChannel, ByteBuffer>> readQueue;
	private List<SocketChannel> clients;
	
	private boolean atomic;
	private boolean localProcessing;

	public NIOServer() {
		readQueue = new ArrayBlockingQueue<Map<SocketChannel, ByteBuffer>>(4096);
		clients = new ArrayList<SocketChannel>();

		// Dynamic Invocation Params
		atomic = false;
		localProcessing = false;

		// start read processor
		ServerReader reader = new ServerReader(this, readQueue, clients);
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
		NIOManagerFactory.getSingleton().enableListenableAccepts(serverSocketChannel, this);
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
		NIOManagerFactory.getSingleton().addReadListener(aSocketChannel, serverReceiver);
	}

	protected void addListeners(SocketChannel aSocketChannel) {
		// addWriteBufferListener(aSocketChannel);
		addReadListener(aSocketChannel);
	}

	@Override
	public void socketChannelAccepted(ServerSocketChannel aServerSocketChannel, SocketChannel aSocketChannel) {
		clients.add(aSocketChannel);
		addListeners(aSocketChannel);
	}
	// @Override
	// public void writeBufferIsEmpty(SocketChannel aSocketChannel) {
	// NIOManagerFactory.getSingleton().enableReads(aSocketChannel);
	// }

	public static void main(String[] args) {
		args = ServerArgsProcessor.removeEmpty(args);
		FactoryTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		BeanTraceUtility.setTracing();// not really needed, but does not hurt
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();

		NIOServer aServer = new NIOServer();
		SimulationParametersController aSimulationParametersController = new ASimulationParametersController();
		aSimulationParametersController.addSimulationParameterListener(SimulationParameters.getSingleton());
		aServer.initialize(ServerArgsProcessor.getServerPort(args));
		aSimulationParametersController.processCommands();
	}

	

	// @Override
	public void quit(int aCode) {
		System.exit(aCode);

	}
}
