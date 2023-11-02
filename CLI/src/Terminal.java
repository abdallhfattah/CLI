import java.io.File;
import java.nio.file.Path;

public class Terminal {
	Parser parser;
	Path newResultPath;
	//Implement each command in a method, for example:
	public Terminal() {
		newResultPath = currentDirectory.resolve(relative_or_absolute_path);
		this.parser = new Parser();
    }

	public String pwd(){
		return currentDirectory;
	}
	public void cd(String[] args){}
	public void mkdir(String[] args){
		for (String arg : args) {
            File newDir = new File(currentDirectory, arg);
            if (!newDir.exists()) {
                if (newDir.mkdir()) {
                    System.out.println("Created directory: " + newDir.getAbsolutePath());
                } else {
                    System.out.println("Failed to create directory: " + newDir.getAbsolutePath());
                }
            } else {
                System.out.println("Directory already exists: " + newDir.getAbsolutePath());
            }
        }
	}
	
	public void rmdir(String[] args){}


	public void echo (String[] args){}
	
	//This method will choose the suitable command method to be called
	public void chooseCommandAction(String command, String[] args) {
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
			case 
            // Add more command cases here.
            default:
                System.out.println("Unknown command: " + command);
        }
    }
	public static void main(String[] args){...}
}