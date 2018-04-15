package assign1;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import assignments.util.MiscAssignmentUtils;
import assignments.util.inputParameters.ASimulationParametersController;
import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ClientArgsProcessor;
import assignments.util.mainArgs.ServerPort;
import example.assignments.util.inputParameters.AnExampleSimulationParametersListener;
import examples.nio.manager.mvc.AMeaningOfLifeController;
import examples.nio.manager.mvc.AMeaningOfLifeModel;
import examples.nio.manager.mvc.AMeaningOfLifeView;
import examples.nio.manager.mvc.MeaningOfLifeController;
import examples.nio.manager.mvc.MeaningOfLifeModel;
import examples.nio.manager.mvc.MeaningOfLifeView;
import global.Client;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.PerformanceExperimentEnded;
import util.trace.port.PerformanceExperimentStarted;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMITraceUtility;
import inputport.nio.manager.AnNIOManager;
import inputport.nio.manager.NIOManager;
import inputport.nio.manager.NIOManagerFactory;
import inputport.nio.manager.factories.classes.AConnectCommandFactory;
import inputport.nio.manager.factories.classes.AReadCommandFactory;
import inputport.nio.manager.factories.classes.AReadingAcceptCommandFactory;
import inputport.nio.manager.factories.classes.AReadingWritingConnectCommandFactory;
import inputport.nio.manager.factories.selectors.ConnectCommandFactorySelector;
import inputport.nio.manager.factories.selectors.ReadCommandFactorySelector;
/**
 * Performs client tasks together with the helper listener (observer) class - AMeaningOfLifeClientSender.
 * The entry point is launchClient.
 * Implements some of  mundane listener tasks itself - look at its interface to see which listeners.
 * Can be further modularized by having separate helper listeners.
 */
import inputport.nio.manager.listeners.SocketChannelConnectListener;
import main.BeauAndersonFinalProject;
import stringProcessors.HalloweenCommandProcessor;

import util.annotations.Tags;
import util.interactiveMethodInvocation.ConsensusAlgorithm;
import util.interactiveMethodInvocation.IPCMechanism;
import util.interactiveMethodInvocation.SimulationParametersController;
import util.tags.DistributedTags;
// @Tags({DistributedTags.CLIENT})
public class NIOClient implements SocketChannelConnectListener {
	String clientName;
	HalloweenCommandProcessor commandProcessor;
	ClientSender clientSender;
	SocketChannel socketChannel;
	ClientReceiver clientReceiver;
	private ArrayBlockingQueue<ByteBuffer> readQueue;
	

	
	public NIOClient(String aClientName) {
		clientName = aClientName;
		readQueue = new ArrayBlockingQueue<ByteBuffer>(4096);
		clientReceiver = new ClientReceiver(readQueue);
		
	}
	protected void setFactories() {		
		ConnectCommandFactorySelector.setFactory(new AReadingWritingConnectCommandFactory());
		//ReadCommandFactorySelector.setFactory(new AReadCommandFactory());
	}
	public void initialize(String aServerHost, int aServerPort) {
		setFactories();
		socketChannel = createSocketChannel();
		createCommunicationObjects();
		createUI();
		addListeners();
		connectToServer(aServerHost, aServerPort);
	}

	public void createUI() {
		commandProcessor = Client.getCommandProcessor();
		//start read processor
		ClientReader reader = new ClientReader(readQueue, commandProcessor);
		Thread readThread = new Thread(reader);
		readThread.setName(NIOServer.READ_THREAD_NAME);
		readThread.start();
	}
	
	public static HalloweenCommandProcessor createSimulation(String aPrefix) {
		return 	Client.getCommandProcessor();
	}

	public void connectToServer(String aServerHost, int aServerPort) {
		createCommunicationObjects();
		// no listeners need to be registered, assuming writes go through
		connectToSocketChannel(aServerHost, aServerPort);

	}

	protected void connectToSocketChannel(String aServerHost, int aServerPort) {
		try {
			InetAddress aServerAddress = InetAddress.getByName(aServerHost);
			NIOManagerFactory.getSingleton().connect(socketChannel,
					aServerAddress, aServerPort, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected SocketChannel createSocketChannel() {
		try {
			SocketChannel retVal = SocketChannel.open();
			return retVal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void connected(SocketChannel aSocketChannel) {
		System.out.println("Ready to send messages to server");
		NIOManagerFactory.getSingleton().addReadListener(socketChannel, clientReceiver);
	}
	protected void createCommunicationObjects() {
		createSender();
	}
	
	protected void createSender() {
		clientSender = new ClientSender(socketChannel,
				clientName, this);
	}
	protected void addListeners() {
		addModelListener();
	}
	protected void addModelListener(){
		commandProcessor.addPropertyChangeListener(clientSender);
	}
	@Override
	public void notConnected(SocketChannel aSocketChannel, Exception e) {
		System.err.println("Could not connect:" +aSocketChannel);
		if (e != null)
		   e.printStackTrace();
	}
	/**
	 * Connect the client with the specified name to the specified server.
	 */
	public static void launchClient(String aServerHost, int aServerPort,
			String aClientName, SimulationParametersController aSimulationParametersController) {
		/*
		 * Put these two in your clients also
		 */
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();

		/*
		 * Instantiate the class that will process user commands.
		 */

		NIOClient aClient = new NIOClient(
				aClientName);
		/*
		 * Register with this instance our listener object for processing the user
		 * commands
		 */
		aSimulationParametersController.addSimulationParameterListener(Client.getSingleton());
		aClient.initialize(aServerHost, aServerPort);	
		aSimulationParametersController.processCommands(); // start the console loop
	}



	public static void main(String[] args) {
		args = ClientArgsProcessor.removeEmpty(args);
		MiscAssignmentUtils.setHeadless(ClientArgsProcessor.getHeadless(args));
		SimulationParametersController aSimulationParametersController = 
				new ASimulationParametersController();
		launchClient(ClientArgsProcessor.getServerHost(args),
				ClientArgsProcessor.getServerPort(args),
				ClientArgsProcessor.getClientName(args), aSimulationParametersController);
		
	}
	

	
	// TODO: WHAT TO DO ABOUT THESE THEY ARE IMPL SPECIFIC
	// @Override
	public void experimentInput() {
		long start = System.currentTimeMillis();
		PerformanceExperimentStarted.newCase(this, start, 1000);
		for (int i = 0; i < 1000; i++) {
			commandProcessor.setInputString("move 1 1");
		}
		long end = System.currentTimeMillis();
		PerformanceExperimentEnded pfe = PerformanceExperimentEnded.newCase(this, start, end, end - start, 1000);
		System.out.println("experimentInput");			
	}

	// @Override
	public void quit(int aCode) {
		System.exit(aCode);
		
	}
	// @Override
	public void simulationCommand(String aCommand) {
		commandProcessor.setInputString(aCommand);
		
	}
}