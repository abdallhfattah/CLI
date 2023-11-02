import java.util.ArrayList;

public class Filter {

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
		String quoted = "";
		while (input.charAt(index) != toStop) {
			quoted += input.charAt(index);
			index++;
		}
		return quoted;
	}

	public String getCommandFiltered() {
		return command;
	}

	public ArrayList<String> getArgsFiltered() {
		return args;
	}
}
