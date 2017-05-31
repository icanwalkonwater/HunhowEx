import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jesus_crie.hunhowex.utils.CommandUtils;

import java.io.IOException;
import java.net.URL;

public class Test {

    public static void main(String[] args) throws ClassNotFoundException {
        String search_query = CommandUtils.GIPHY_BASE_GIF + "search?q={QUERY}&limit=10&api_key=" + CommandUtils.GIPHY_KEY;

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readValue(new URL(search_query.replace("{QUERY}", "jesus+lol")), JsonNode.class);
            node.get("data").forEach(gif -> print(gif.get("embed_url").asText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void print(Object o) {
        System.out.println(o);
    }
}
