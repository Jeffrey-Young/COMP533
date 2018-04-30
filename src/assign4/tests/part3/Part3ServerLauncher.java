package assign4.tests.part3;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.BLOCKING_RPC_SERVER})
public class Part3ServerLauncher {

	public static void main(String[] args) {
		assignments.util.A4TraceUtility.setTracing();
		Part3Server.launch();
	}

}
