package assign4.tests.part2;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.CUSTOM_RPC_CLIENT1 })
public class Part2Client1Launcher {

	public static void main(String[] args) {
		assignments.util.A4TraceUtility.setTracing();
		Part2Client.launch("Part2Client1");
	}

}
