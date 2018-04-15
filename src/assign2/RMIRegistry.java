package assign2;

import java.rmi.registry.LocateRegistry;
import util.tags.DistributedTags;
import java.util.Scanner;

import assignments.util.mainArgs.RegistryArgsProcessor;
import assignments.util.mainArgs.ServerArgsProcessor;
import util.annotations.Tags;

@Tags ({DistributedTags.REGISTRY, DistributedTags.RMI})
public class RMIRegistry {
	public static void main (String[] args) {
		try {
			LocateRegistry.createRegistry(RegistryArgsProcessor.getRegistryPort(args));
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
