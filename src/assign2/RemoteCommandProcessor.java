package assign2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import assign1.Simulation;
import assign1.Simulation1;
import assign3.GIPCClient;
import consensus.ProposalFeedbackKind;
import global.Client;
import main.BeauAndersonFinalProject;
import stringProcessors.AHalloweenCommandProcessor;
import stringProcessors.HalloweenCommandProcessor;
import util.interactiveMethodInvocation.ConsensusAlgorithm;
import util.interactiveMethodInvocation.IPCMechanism;
import util.trace.port.consensus.ProposalAcceptRequestReceived;
import util.trace.port.consensus.ProposalAcceptedNotificationSent;
import util.trace.port.consensus.ProposalLearnedNotificationReceived;
import util.trace.port.consensus.ProposedStateSet;
import util.trace.port.consensus.communication.CommunicationStateNames;

public class RemoteCommandProcessor extends AHalloweenCommandProcessor implements RemoteCommandProcessorInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HalloweenCommandProcessor commandProcessor;

	public RemoteCommandProcessor(RMIClient client) {
		// super();
		commandProcessor = Client.getCommandProcessor();
		commandProcessor.addPropertyChangeListener(client);
	}
	
	public RemoteCommandProcessor(GIPCClient client) {
		// super();
		commandProcessor = Client.getCommandProcessor();
		commandProcessor.addPropertyChangeListener(client);
	}

	public HalloweenCommandProcessor getCommandProcessor() {
		return commandProcessor;
	}

	public void processRemoteCommand(String command) throws RemoteException {
		ProposalLearnedNotificationReceived.newCase(this, CommunicationStateNames.COMMAND, -1, command);
		ProposedStateSet.newCase(this, CommunicationStateNames.COMMAND, -1, command);
		commandProcessor.processCommand(command);
	}

	@Override
	public void remoteSetAtomic(boolean newValue) throws RemoteException {
		ProposalLearnedNotificationReceived.newCase(this, CommunicationStateNames.BROADCAST_MODE, -1, newValue);
		Client.getSingleton().setAtomicBroadcastAfterConsensus(newValue);
	}

	@Override
	public void remoteSetIPC(IPCMechanism newValue) throws RemoteException {
		ProposalLearnedNotificationReceived.newCase(this, CommunicationStateNames.IPC_MECHANISM, -1, newValue);
		Client.getSingleton().setIPCMechanismAfterConsensus(newValue);

	}

	@Override
	public boolean receiveProposal(String communicationState, Object value) throws RemoteException {
		ProposalAcceptRequestReceived.newCase(this, communicationState, -1, value);
		ProposalAcceptedNotificationSent.newCase(this, communicationState, -1, value, ProposalFeedbackKind.SUCCESS); // idk
																														// if
																														// that
																														// last
																														// param
																														// is
																														// correct
		return !Client.getSingleton().isRejectMetaStateChange();
	}
}
