import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Terminal {
	Parser parser;
	Path newResultPath;
	//Implement each command in a method, for example:
	public Terminal() {}
	public String pwd(){return " ";}
	public void cd(ArrayList<String> args){}
	public void mkdir(ArrayList<String> args){}
	public void rmdir(ArrayList<String> args){}
	public void echo (ArrayList<String> args){}
	
	//This method will choose the suitable command method to be called
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
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		Terminal terminal = new Terminal();
		while(true){
			String command  = scanner.nextLine();
			Parser parse = new Parser();
			parse.parse(command);
			boolean isItQuit = terminal.chooseCommandAction(parse.getCommandName(),parse.getArgs());
			if(isItQuit){break;}
		}
		scanner.close();
	}
}