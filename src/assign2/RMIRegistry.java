package assign2;

import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

import assignments.util.mainArgs.ServerArgsProcessor;

public class RMIRegistry {
	public static void main (String[] args) {
		try {
			LocateRegistry.createRegistry(ServerArgsProcessor.getRegistryPort(args));
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
