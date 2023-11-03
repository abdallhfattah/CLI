import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Objects;
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
		if(args.size() == 0){
			System.err.println("mkdir: missing operand");
			return;
		}
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
		if(args.size() > 1 || args.size() == 0){
			System.err.println("Missing arguments!");
			return;
		}
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

	public void echo(ArrayList<String> args) {
		if (args.size() > 1) {
			System.err.println("Too many arguments!");
			return;
		}
		for (String Print : args) {
			System.out.println(Print);
		}
	}

	public void cat(ArrayList<String> args) throws IOException {
		if (args.size() == 1) {
			FileReader file = new FileReader(args.get(0));
			BufferedReader reader = new BufferedReader(file);
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		}
		else if (args.size() == 2) {
			FileReader file = new FileReader(args.get(0));
			BufferedReader reader = new BufferedReader(file);
			FileReader file2 = new FileReader(args.get(1));
			BufferedReader reader2 = new BufferedReader(file2);
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			while ((line = reader2.readLine()) != null) {
				System.out.println(line);
			}
		}
		else {
			System.out.println("Cat takes 1 or 2 arguments!");
			return;
		}
	}

	public void cp(ArrayList<String> args) throws IOException {
		if (args.size() == 3 && Objects.equals(args.get(0), "r")) {
			Path sourceDirectory = Paths.get(args.get(1));
			Path destinationDirectory = Paths.get(args.get(2));

			try {
				Files.walkFileTree(sourceDirectory, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						// Calculate the relative path to the destination directory
						Path relativePath = sourceDirectory.relativize(file);
						Path destinationFile = destinationDirectory.resolve(relativePath);

						// Copy the file to the destination directory
						Files.copy(file, destinationFile, StandardCopyOption.REPLACE_EXISTING);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
						// Calculate the relative path to the destination directory
						Path relativePath = sourceDirectory.relativize(dir);
						Path destinationDir = destinationDirectory.resolve(relativePath);

						// Create the destination directory if it doesn't exist
						if (!Files.exists(destinationDir)) {
							Files.createDirectories(destinationDir);
						}
						return FileVisitResult.CONTINUE;
					}
				});

				System.out.println("Directory copy completed successfully.");
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Failed to copy the directory.");
			}
		}
		else if (args.size() == 2) {
			FileReader file = new FileReader(args.get(0));
			BufferedReader reader = new BufferedReader(file);
			String line;

			FileWriter file2 = new FileWriter(args.get(1));
			BufferedWriter writer = new BufferedWriter(file2);

			while ((line = reader.readLine()) != null) {
				writer.write(line);
				writer.newLine();
			}
			writer.close();
		}
		else {
			System.out.println("Usage: cp file1 file2\n\t   cp -r dir1 dir2");
		}
	}

	// This method will choose the suitable command method to be called
	public boolean chooseCommandAction(String command, ArrayList<String> args) throws IOException {
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
				break;
			case "cat":
				cat(args);
				break;
			case "cp":
				cp(args);
				break;
			case "exit":
				return true;
			// Add more command cases here.
			default:
				System.out.println("Unknown command: " + command);
		}
		return false;
	}

	public static void main(String[] args) throws IOException {

		Scanner scanner = new Scanner(System.in);
		Terminal terminal = new Terminal();
		while (true) {
			System.out.print("> ");
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