class Parser {
	String commandName;
	String[] args;

	// This method will divide the input into commandName and args
	// where "input" is the string command entered by the user
	public boolean parse(String input) {
		Filter filter = new Filter(input);
		this.commandName = filter.getCommandFiltered();
		this.args = filter.getArgsFiltered();
		return true;
	};

	public String getCommandName() {
		return commandName;
	}

	public String[] getArgs() {
		return args;
	}
}