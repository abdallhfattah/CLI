// tester
public class App {
    public static void main(String[] args) throws Exception {
        String comand = "echo \"hello wolrd\" uselessDummyCommands";
        Parser parse = new Parser();
        parse.parse(comand);
    }
}
