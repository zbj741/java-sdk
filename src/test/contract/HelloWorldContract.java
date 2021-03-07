import java.util.HashMap;
import java.util.Map;

public class HelloWorldContract { private Map<String, Object> db;

    public HelloWorldContract() {
    }

    public HelloWorldContract(Map storage) {
       storage.getOrDefault("db", new HashMap<>());
    }

    public void setName(String name){
       db.put("name", name);
    }

    public String getName(){
        return (String) db.get("name");
    }
}
