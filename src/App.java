
// tester
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) throws Exception {
        // String comand = "echo \"hello wolrd\" uselessDummyCommands";
        // Parser parse = new Parser();
        // parse.parse(comand);
        String input = "This is a \"quoted phrase\" and another one";
        String[] parts = splitWithQuotes(input);

        for (String part : parts) {
            System.out.println(part);
        }
    }

    public static String[] splitWithQuotes(String input) {
        Pattern pattern = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");
        Matcher matcher = pattern.matcher(input);
        int matchCount = 0;

        while (matcher.find()) {
            matchCount++;
        }

        String[] parts = new String[matchCount];
        matcher.reset();

        for (int i = 0; i < matchCount; i++) {
            if (matcher.find()) {
                parts[i] = matcher.group(1).replaceAll("\"", "");
            }
        }

        return parts;
    }
}