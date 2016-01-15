<%@ page import="opennlp.tools.tokenize.TokenizerModel" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="opennlp.tools.tokenize.Tokenizer" %>
<%@ page import="opennlp.tools.tokenize.TokenizerME" %><%@ page import="util.CommandParser" %><%!

    TokenizerModel model = null;

    HashMap<String,String[]> place_location_map = new HashMap<String,String[]>();

    {
        try {
            InputStream is = new FileInputStream("C:\\programs\\apache-opennlp-1.6.0\\lib\\en-token.bin");
            model = new TokenizerModel(is);
            is.close();
            loadPlaceLocationMap();
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    //TODO a better way to return the action
    public String understandCommand(String cmd) throws Exception {

        String result = "";

        Tokenizer tokenizer = new TokenizerME(model);

        String tokens[] = tokenizer.tokenize(cmd);

        int max = tokens.length;
        for (int i =0;i<max;i++) {
            String token = tokens[i];
            System.out.println(token);

            token = token.toLowerCase();
            token = token.trim();

            if(token.equals("show")) {
                result = handleShow(tokens,i);
                break;
            }
        }

        return result;
    }

    private String handleShow(String[] tokens, int i) {
        String next_token = tokens[i+1];
        next_token = next_token.trim();
        next_token = next_token.toLowerCase();

        System.out.println(next_token);
        return findEntity(next_token);
    }

    private String findEntity(String entity) {
        boolean found = false;
        if(!place_location_map.containsKey(entity)) {
            found = false;
        } else {
            String[] location = place_location_map.get(entity);
            return displayLocation(location);
        }

        return "";
    }


    private String displayLocation(String[] location) {
        System.out.println(location[0]+"|"+location[1]);
        String action = "{\"action\":\"display\",  \"params\": { \"lat\": \""+location[0]+"\", \"long\":\""+location[1]+"\"} }";

        return action;
    }

    private void loadPlaceLocationMap() {

        String[] loc1 = {"37.0000", "-120.0000"};
        place_location_map.put("california",loc1);

        String[] loc2 = {"38.8833","-77.0167"};
        place_location_map.put("usa",loc2);

        String[] loc3 = {"21.000","78.000"};
        place_location_map.put("india",loc3);

        String[] loc4 = {"40.7127","74.0059"};
        place_location_map.put("ny",loc4);

        String[] loc5 = {"35.0000","103.000"};
        place_location_map.put("china",loc5);

        String[] loc6 = {"54.900","25.316"};
        place_location_map.put("europe",loc6);

    }


%>