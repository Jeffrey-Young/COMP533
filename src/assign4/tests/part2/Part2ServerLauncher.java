package assign4.tests.part2;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.CUSTOM_RPC_SERVER})
public class Part2ServerLauncher {

	public static void main(String[] args) {
		assignments.util.A4TraceUtility.setTracing();
		Part2Server.launch();
	}

}
