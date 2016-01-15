package util;

import java.io.*;
import java.util.*;

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


public class Basic {


    public static void main(String[] args) throws Exception {
        basicNameFinder();
        SentenceDetect();
        Tokenize();
        findName();
        POSTag();
        chunk();
        Parse();
    }

    public static void basicNameFinder() throws Exception {


        String[] sentences = {
                "If President John F. Roosevelt, after  visiting France in 1961 with his immensely popular wife,"
                        + " famously described himself as 'the man who had accompanied Jacqueline Kennedy to Paris,'"
                        + " Mr. Hollande has been most conspicuous on this state visit for traveling alone.",
                "Mr. Draghi spoke on the first day of an economic policy conference here organized by"
                        + " the E.C.B. as a sort of counterpart to the annual symposium held in Jackson"
                        + " Hole, Wyo., by the Federal Reserve Bank of Kansas City. Shankar Kondur " };

        // Load the model file downloaded from OpenNLP
        // http://opennlp.sourceforge.net/models-1.5/en-ner-person.bin
        TokenNameFinderModel model = new TokenNameFinderModel(new File("C:\\programs\\apache-opennlp-1.6.0\\lib\\en-ner-person.bin"));

        // Create a NameFinder using the model
        NameFinderME finder = new NameFinderME(model);

        Tokenizer tokenizer = SimpleTokenizer.INSTANCE;

        for (String sentence : sentences) {

            // Split the sentence into tokens
            String[] tokens = tokenizer.tokenize(sentence);

            // Find the names in the tokens and return Span objects
            Span[] nameSpans = finder.find(tokens);

            // Print the names extracted from the tokens using the Span data
            System.out.println( Arrays.toString(Span.spansToStrings(nameSpans, tokens)) );
        }
    }


    public static void SentenceDetect() throws InvalidFormatException,
            IOException {
        String paragraph = "Hi. How are you? This is Mike.";

        // always start with a model, a model is learned from training data
        InputStream is = new FileInputStream("C:\\programs\\apache-opennlp-1.6.0\\lib\\en-sent.bin");
        SentenceModel model = new SentenceModel(is);
        SentenceDetectorME sdetector = new SentenceDetectorME(model);

        String sentences[] = sdetector.sentDetect(paragraph);

        System.out.println(sentences[0]);
        System.out.println(sentences[1]);
        is.close();
    }


    public static void Tokenize() throws InvalidFormatException, IOException {
        InputStream is = new FileInputStream("C:\\programs\\apache-opennlp-1.6.0\\lib\\en-token.bin");

        TokenizerModel model = new TokenizerModel(is);

        Tokenizer tokenizer = new TokenizerME(model);

        String tokens[] = tokenizer.tokenize("Hi. How are you? This is Mike.");

        for (String a : tokens)
            System.out.println(a);

        is.close();
    }

    public static void findName() throws IOException {
        InputStream is = new FileInputStream("C:\\programs\\apache-opennlp-1.6.0\\lib\\en-ner-person.bin");

        TokenNameFinderModel model = new TokenNameFinderModel(is);
        is.close();

        NameFinderME nameFinder = new NameFinderME(model);

        String []sentence = new String[]{
                "Mike",
                "Smith",
                "is",
                "a",
                "good",
                "person"
        };

        Span nameSpans[] = nameFinder.find(sentence);

        for(Span s: nameSpans)
            System.out.println(s.toString());
    }


    public static void POSTag() throws IOException {
        POSModel model = new POSModelLoader()
                .load(new File("C:\\programs\\apache-opennlp-1.6.0\\lib\\en-pos-maxent.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);

        String input = "Hi. How are you? This is Mike.";
        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(input));

        perfMon.start();
        String line;
        while ((line = lineStream.read()) != null) {

            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                    .tokenize(line);
            String[] tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.toString());

            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();
    }

    public static void chunk() throws IOException {
        POSModel model = new POSModelLoader()
                .load(new File("C:\\programs\\apache-opennlp-1.6.0\\lib\\en-pos-maxent.bin"));
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        POSTaggerME tagger = new POSTaggerME(model);

        String input = "Hi. How are you? This is Mike.";
        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(input));

        perfMon.start();
        String line;
        String whitespaceTokenizerLine[] = null;

        String[] tags = null;
        while ((line = lineStream.read()) != null) {
            whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
                    .tokenize(line);
            tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.toString());
            perfMon.incrementCounter();
        }
        perfMon.stopAndPrintFinalResult();

        // chunker
        InputStream is = new FileInputStream("C:\\programs\\apache-opennlp-1.6.0\\lib\\en-chunker.bin");
        ChunkerModel cModel = new ChunkerModel(is);

        ChunkerME chunkerME = new ChunkerME(cModel);
        String result[] = chunkerME.chunk(whitespaceTokenizerLine, tags);

        for (String s : result)
            System.out.println(s);

        Span[] span = chunkerME.chunkAsSpans(whitespaceTokenizerLine, tags);
        for (Span s : span)
            System.out.println(s.toString());
    }


    public static void Parse() throws InvalidFormatException, IOException {
        // http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool
        InputStream is = new FileInputStream("C:\\programs\\apache-opennlp-1.6.0\\lib\\en-parser-chunking.bin");

        ParserModel model = new ParserModel(is);

        Parser parser = ParserFactory.create(model);

        String sentence = "Programcreek is a very huge and useful website.";
        Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

        for (Parse p : topParses)
            p.show();

        is.close();

	/*
	 * (TOP (S (NP (NN Programcreek) ) (VP (VBZ is) (NP (DT a) (ADJP (RB
	 * very) (JJ huge) (CC and) (JJ useful) ) ) ) (. website.) ) )
	 */
    }

}