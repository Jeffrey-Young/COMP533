package assign4.tests.part2;

import util.annotations.Tags;
import util.annotations.Comp533Tags;

@Tags({ Comp533Tags.CUSTOM_RPC_CLIENT2})
public class Part2Client2Launcher {

	public static void main(String[] args) {
		assignments.util.A4TraceUtility.setTracing();
		Part2Client.launch("Part2Client2");

	}

}
