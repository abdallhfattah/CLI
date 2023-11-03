import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

class Filter {

	private String command;
	private ArrayList<String> args;

	Filter(String input) {
		input += ' ';
		this.command = "";
		this.args = new ArrayList<String>();
		int itr = 0;
		// capture command
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) != ' ') {
				this.command = getArg(input, i, ' ');
				itr = command.length() + i;
				break;
			}
		}
		String arg = "";
		// capture arguments
		for (int i = itr; i < input.length(); i++) {
			Character currChar = input.charAt(i);
			if (currChar != ' ') {
				if (currChar == '\"') {
					i++;
					arg = getArg(input , i , '\"');
					args.add(arg);
					i += arg.length();
				}
				else{
					// cases "-r"
					if(currChar == '-') i++;
					arg = getArg(input , i , ' ');
					i += arg.length();
					args.add(arg);
				}
			}

		}
	}

	String getArg(String input, int index , Character toStop) {
		String deliver = "";
		while (input.charAt(index) != toStop) {
			deliver += input.charAt(index);
			index++;
		}
		return deliver;
	}

	public String getCommandFiltered() {
		return command;
	}

	public ArrayList<String> getArgsFiltered() {
		return args;
	}
}

class Parser {
	String commandName;
	ArrayList<String> args;

	// This method will divide the input into commandName and args
	// where "input" is the string command entered by the user
	public boolean parse(String input) {
		Filter filter = new Filter(input);

		this.commandName = filter.getCommandFiltered();

		this.args = filter.getArgsFiltered();

		// System.out.println("======= Verify =======");
		// System.out.println("command : " + commandName);
		// System.out.println(args);

		return true;
	};

	public String getCommandName() {
		return commandName;
	}

	public ArrayList<String> getArgs() {
		return args;
	}
}

public class Terminal {
	Parser parser;
	Path newResultPath;
	String currDir;
	ArrayList<String> history;

	// Implement each command in a method, for example:
	public Terminal() {
		// Initialize member variables in the constructor.
		parser = new Parser(); // You may need to provide appropriate arguments.
		newResultPath = null; // Initialize as needed.
		currDir = System.getProperty("user.dir"); // Initialize with the current directory
		history = new ArrayList<String>();
	}

	public String pwd() {
		history.add("pwd");
		return currDir;
	}

	public void cd(ArrayList<String> args) {
		history.add("cd");
		if (args.size() == 0) {
			currDir = System.getProperty("user.home");
		} else if (args.get(0).equals("..")) {
			File currentDir = new File(currDir);
			this.currDir = currentDir.getParent();
		} else {
			File newPath = new File(currDir + "/" + args.get(0));
			if (newPath.exists()) {
				currDir = newPath.getAbsolutePath();
			} else {
				System.out.println("Error: Path '" + newPath.getAbsolutePath() + "' does not exist.");
			}
		}
	}

	public void mkdir(ArrayList<String> args) {
		history.add("mkdir");
		if (args.size() == 0) {
			System.err.println("mkdir: missing operand");
			return;
		}
		for (String arg : args) {
			Path newPath = Paths.get(arg);
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
		history.add("rmdir");
		if (args.size() != 1) {
			System.out.println("Usage: rmdir <directory>");
			return;
		}
		Path newPath = Paths.get(args.get(0));
		File directory = newPath.toFile();
		if (args.get(0).equals("*")) {
			// Case 1: rmdir *
			removeEmptyDirectories(currDir);
		} else {
			// Case 2: rmdir <directory>
			if (directory.exists() && directory.isDirectory()) {
				if (isDirectoryEmpty(directory)) {
					if (directory.delete()) {
						System.out.println("Deleted directory: " + directory.getAbsolutePath());
					} else {
						System.out.println("Failed to delete directory: " + directory.getAbsolutePath());
					}
				} else {
					System.out.println("Directory is not empty: " + directory.getAbsolutePath());
				}
			} else {
				System.out.println("Directory does not exist: " + directory.getAbsolutePath());
			}
		}
	}

	private void removeEmptyDirectories(String path) {
		System.out.println(path);
		File currentDir = new File(path);
		File[] files = currentDir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory() && isDirectoryEmpty(file)) {
					if (file.delete()) {
						System.out.println("Deleted directory: " + file.getAbsolutePath());
					} else {
						System.out.println("Failed to delete directory: " + file.getAbsolutePath());
					}
				}
			}
		}
	}

	public void ls(ArrayList<String> args) {
		if (args.size() > 1) {
			System.out.println("too many arguments");
			return;
		}

		File currentDir = new File(currDir);
		File[] files = currentDir.listFiles();

		if (files != null) {
			Arrays.sort(files);
		}

		if (args.size() != 0 && args.get(0).charAt(0) == 'r') {

			if (files != null) {
				File[] reversedFiles = new File[files.length];
				for (int i = 0; i < files.length; i++) {
					reversedFiles[files.length - i - 1] = files[i];
				}
				files = reversedFiles;
			}

		}

		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					System.out.println(file.getName() + " Directory");
				} else {
					System.out.println(file.getName() + " File");
				}
			}
		}
	}

	private boolean isDirectoryEmpty(File directory) {
		File[] files = directory.listFiles();
		return (files != null) && (files.length == 0);
	}

	public void echo(ArrayList<String> args) {
		history.add("echo");
		// could make is support multiable
		if (args.size() > 1) {
			System.err.println("Too many arguments!");
			return;
		}
		for (String Print : args) {
			System.out.println(Print);
		}
	}

	public void cat(ArrayList<String> args) throws IOException {
		history.add("cat");
		if (args.size() == 1) {
			FileReader file = new FileReader(args.get(0));
			BufferedReader reader = new BufferedReader(file);
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} else if (args.size() == 2) {
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
		} else {
			System.out.println("Cat takes 1 or 2 arguments!");
			return;
		}
	}

	public void cp(ArrayList<String> args) throws IOException {
		history.add("cat");
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
		} else if (args.size() == 2) {
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
		} else {
			System.out.println("Usage: cp file1 file2\n\t   cp -r dir1 dir2");
		}
	}

	public void touch(ArrayList<String> args) {
	    history.add("touch");
	    if (args.size() != 1) {
	        System.err.println("Usage: touch <filename>");
	        return;
	    }
	
	    String fileName = args.get(0);
	    File file = new File(fileName);
	    try {
	        if (file.createNewFile()) {
	            System.out.println("File created: " + fileName);
	        } else {
	            System.out.println("File already exists: " + fileName);
	        }
	    } catch (IOException e) {
	        System.err.println("Error creating the file: " + e.getMessage());
	    }
	}

        public void rm(ArrayList<String> args) {
	    history.add("rm");
	    if (args.size() != 1) {
	        System.err.println("Usage: rm <filename>");
	        return;
	    }
	
	    String fileName = args.get(0);
	    File file = new File(fileName);
	    if (file.exists()) {
	        if (file.delete()) {
	            System.out.println("File deleted: " + fileName);
	        } else {
	            System.err.println("Error deleting the file");
	        }
	    } else {
	        System.err.println("File not found: " + fileName);
	    }
	}


	public void getHistory() {
		int numbering = 1;
		for (String command : history) {
			System.out.println(numbering + " - " + command);
			numbering++;
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
			case "history":
				getHistory();
				break;
			case "ls":
				ls(args);
				break;
			case "touch":
			    touch(args);
			    break;
			case "rm":
			    rm(args);
			    break;
			case "exit":
				return true;
			// Add more command cases here.
			default:
				System.out.println(command + ": command not found");
		}
		if (this.history.size() > 15) {
			this.history.remove(this.history.size() - 1);
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
