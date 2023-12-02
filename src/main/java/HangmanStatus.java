import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HangmanStatus {

    private static final List<String> STATUS = new ArrayList<>(Arrays.asList(
            "|\n|\n|\n|\n|\n|",
            "|----\n|   |\n|   0\n|\n|\n|",
            "|----\n|   |\n|   0\n|   |\n|\n|",
            "|----\n|   |\n|   0\n|  /|\n|\n|",
            "|----\n|   |\n|   0\n|  /|\\\n|\n|",
            "|----\n|   |\n|   0\n|  /|\\\n|  / \n|",
            "|----\n|   |\n|   0\n|  /|\\\n|  / \\\n|"
    ));

    public static String getStatusByIndex(int index) {
        return STATUS.get(index);
    }
}
