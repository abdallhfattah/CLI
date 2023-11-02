import java.util.ArrayList;

public class Filter {

	private String toFilter;
	private String command;
	private ArrayList<String> args;

	Filter(String input) {
		this.toFilter = input;
        this.command = "";
        this.args = new ArrayList<String>();

		int itr = 0;
		for (int i = 0; i < input.length(); i++) {
			while (input.charAt(i) != ' ') {
				this.command += input.charAt(i);
				i++;
			}
			itr = i;
		}
		for (int i = itr; i < input.length(); i++) {
			String arg = "";
			while (input.charAt(i) != ' ') {
				if (input.charAt(i) == '\"') {
					i++;
					while (input.charAt(i) != '\"') {
						arg += input.charAt(++i);
					}
					args.add(arg);
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

	public ArrayList<String> getArgsFiltered() {
		return args;
	}
}
