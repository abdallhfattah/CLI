public class Filter {

	private String toFilter;
	private String command;
	private String[] args;

	Filter(String input) {
		this.toFilter = input;
        this.command = "";
        this.args = new String[0];

		int itr = 0;
		for (int i = 0; i < input.length(); i++) {
			while (input.charAt(i) != ' ') {
				this.command += input.charAt(i);
				i++;
			}
			itr = i;
		}
		for (int i = itr; i < input.length(); i++) {
			String arg = new String();
			while (input.charAt(i) != ' ') {
				if (input.charAt(i) == '\"') {
					i++;
					while (input.charAt(i) != '\"') {
						arg += input.charAt(++i);
					}
					args.
				}
				if (input.charAt(i) != '\"') {
					arg += input.charAt(i);
				}
			}
			
		}
	}

	public String getCommandFiltered() {
		return command;
	}

	public String[] getArgsFiltered() {
		return args;
	}
}
