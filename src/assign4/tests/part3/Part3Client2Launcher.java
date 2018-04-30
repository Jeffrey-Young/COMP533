package assign4.tests.part3;

import util.annotations.Tags;
import util.annotations.Comp533Tags;

@Tags({ Comp533Tags.BLOCKING_RPC_CLIENT2})
public class Part3Client2Launcher {

	public static void main(String[] args) {
		assignments.util.A4TraceUtility.setTracing();
		Part3Client.launch("Part3Client2");

	}

}
