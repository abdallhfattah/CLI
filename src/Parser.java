import java.util.ArrayList;

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
		// System.out.println("command : "   + commandName);
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