package util;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.*;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;


public class CommandParser {


    TokenizerModel model = null;

    HashMap<String,String[]> place_location_map = new HashMap<String,String[]>();

    private static CommandParser SINGLETON = new CommandParser();

    public CommandParser() {
        try {
            InputStream is = new FileInputStream("C:\\programs\\apache-opennlp-1.6.0\\lib\\en-token.bin");
            model = new TokenizerModel(is);
            is.close();
            loadPlaceLocationMap();
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        String cmd = "Show California,USA";
        SINGLETON.understandCommand(cmd);
    }


    public void understandCommand(String cmd) throws Exception {

        Tokenizer tokenizer = new TokenizerME(model);

        String tokens[] = tokenizer.tokenize(cmd);

        int max = tokens.length;
        for (int i =0;i<max;i++) {
            String token = tokens[i];
            System.out.println(token);

            token = token.toLowerCase();
            token = token.trim();

            if(token.equals("show")) {
                handleShow(tokens,i);
                break;
            }
        }
    }

    private void handleShow(String[] tokens, int i) {
        String next_token = tokens[i+1];
        next_token = next_token.trim();
        next_token = next_token.toLowerCase();

        System.out.println(next_token);
        findEntity(next_token);
    }

    private void findEntity(String entity) {
        boolean found = false;
        if(!place_location_map.containsKey(entity)) {
            found = false;
        } else {
            String[] location = place_location_map.get(entity);
            displayLocation(location);
        }

    }

    private void displayLocation(String[] location) {
        System.out.println(location[0]+"|"+location[1]);
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


}