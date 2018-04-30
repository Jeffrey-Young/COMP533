package assign4.tests.part1;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.EXPLICIT_RECEIVE_CLIENT1 })
public class Part1Client1Launcher {

	public static void main(String[] args) {
		assignments.util.A4TraceUtility.setTracing();
		Part1Client.launch("Part1Client1");
	}

}
