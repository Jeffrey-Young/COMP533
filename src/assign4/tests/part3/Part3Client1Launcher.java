package assign4.tests.part3;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.BLOCKING_RPC_CLIENT1 })
public class Part3Client1Launcher {

	public static void main(String[] args) {
		assignments.util.A4TraceUtility.setTracing();
		Part3Client.launch("Part3Client1");
	}

}
