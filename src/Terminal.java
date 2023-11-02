import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Terminal {
	Parser parser;
	Path newResultPath;
	String currDir;

	// Implement each command in a method, for example:
	public Terminal() {
		// Initialize member variables in the constructor.
		parser = new Parser(); // You may need to provide appropriate arguments.
		newResultPath = null; // Initialize as needed.
		currDir = System.getProperty("user.dir"); // Initialize with the current directory
	}

	public String pwd() {
		return currDir;
	}

	public void cd(ArrayList<String> args) {
	}

	public void mkdir(ArrayList<String> args) {
		for (String arg : args) {
			Path newPath;
			 // Check if the argument is an absolute path (e.g., "C:/example").
            if (arg.length() >= 2 && arg.charAt(1) == ':') {
                newPath = Paths.get(arg);
            } else {
                // Construct the full path by combining the current directory and the argument.
                newPath = Paths.get(currDir, arg);
            }
			// Create a File object for the new directory.
			File newDir = newPath.toFile();
			if (!newDir.exists()) {
				if (newDir.mkdirs()) {
					System.out.println("Created directory: " + newPath);
				} else {
					System.out.println("Failed to create directory: " + newPath);
				}
			} else {
				System.out.println("Directory already exists: " + newPath);
			}
		}
	}

	public void rmdir(ArrayList<String> args) {
	}

	public void echo(ArrayList<String> args) {
	}

	// This method will choose the suitable command method to be called
	public boolean chooseCommandAction(String command, ArrayList<String> args) {
		switch (command) {
			case "pwd":
				System.out.println(pwd());
				break;
			case "cd":
				cd(args);
				break;
			case "mkdir":
				mkdir(args);
				break;
			case "rmdir":
				rmdir(args);
				break;
			case "echo":
				echo(args);
			case "exit":
				return true;
			// Add more command cases here.
			default:
				System.out.println("Unknown command: " + command);
		}
		return false;
	}

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		Terminal terminal = new Terminal();
		while (true) {
			String command = scanner.nextLine();
			Parser parse = new Parser();
			parse.parse(command);
			boolean isItQuit = terminal.chooseCommandAction(parse.getCommandName(), parse.getArgs());
			if (isItQuit) {
				break;
			}
		}
		scanner.close();
	}
}