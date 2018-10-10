package info.noip.fideo.habitisync;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author fran
 */
public class HabiticaResource implements Resource {

    private final String user, key;
    private final Map<String, String> tagsIdToName = new HashMap<>();
    private final Map<String, String> tagsNameToId = new HashMap<>();

    public HabiticaResource(String user, String key) throws Exception {
        this.user = user;
        this.key = key;
        List<JSONObject> tagList = (List<JSONObject>) new RestClient.Builder("https://habitica.com/api/v3/tags")
                .header("x-api-user", this.user)
                .header("x-api-key", this.key).request().get("data");
        for (JSONObject tag : tagList) {
            tagsIdToName.put((String)tag.get("id"), (String)tag.get("name"));
            tagsNameToId.put((String)tag.get("name"), (String)tag.get("id"));
        }
    }

    @Override
    public Collection<Task> getTasks() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "todos");

        JSONObject req = new RestClient.Builder("https://habitica.com/api/v3/tasks/user")
                .header("x-api-user", this.user)
                .header("x-api-key", this.key)
                .urlParam("type", "dailys").request();
        if (!(boolean)req.get("success")) {
            throw new Exception("Request failed");
        }
        JSONArray data = (JSONArray) req.get("data");
        for (Object object : data) {
            JSONObject obj = (JSONObject) object;
            System.out.println("keys: " + obj.keySet().stream().map(key -> key.toString() + ":" + obj.get(key).getClass().getSimpleName()).collect(Collectors.joining(" ")));
            System.out.println("tags = " + obj.get("tags").toString());
        }

        return null;
    }

    @Override
    public void create(Task task) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
