package com.zi;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MovieReviewStatictics
{
    private static final String DOCUMENTS_PATH = "movies/";
    private int _verbCount = 0;
    private int _nounCount = 0;
    private int _adjectiveCount = 0;
    private int _adverbCount = 0;
    private int _totalTokensCount = 0;

    private PrintStream _statisticsWriter;

    private SentenceModel _sentenceModel;
    private TokenizerModel _tokenizerModel;
    private DictionaryLemmatizer _lemmatizer;
    private PorterStemmer _stemmer;
    private POSModel _posModel;
    private TokenNameFinderModel _peopleModel;
    private TokenNameFinderModel _placesModel;
    private TokenNameFinderModel _organizationsModel;

    public static void main(String[] args)
    {
        MovieReviewStatictics statictics = new MovieReviewStatictics();
        statictics.run();
    }

    private void run()
    {
        try
        {
            initModelsStemmerLemmatizer();

            File dir = new File(DOCUMENTS_PATH);
            File[] reviews = dir.listFiles((d, name) -> name.endsWith(".txt"));

            _statisticsWriter = new PrintStream("statistics.txt", "UTF-8");

            Arrays.sort(reviews, Comparator.comparing(File::getName));
            for (File file : reviews)
            {
                System.out.println("Movie: " + file.getName().replace(".txt", ""));
                _statisticsWriter.println("Movie: " + file.getName().replace(".txt", ""));

                String text = new String(Files.readAllBytes(file.toPath()));
                processFile(text);

                _statisticsWriter.println();
            }

            overallStatistics();
            _statisticsWriter.close();

        } catch (IOException ex)
        {
            Logger.getLogger(MovieReviewStatictics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initModelsStemmerLemmatizer()
    {
        try
        {
//         TODO: load all OpenNLP models (+Porter stemmer + lemmatizer) from files (use class variables)
            this._stemmer = new PorterStemmer();
            File file = new File("models/en-pos-maxent.bin");
            this._posModel = new POSModel(file);
            file = new File("models/en-sent.bin");
            this._sentenceModel = new SentenceModel(file);
            file = new File("models/en-token.bin");
            this._tokenizerModel = new TokenizerModel(file);
            file = new File("models/en-ner-person.bin");
            this._peopleModel = new TokenNameFinderModel(file);
            file = new File("models/en-ner-organization.bin");
            this._organizationsModel = new TokenNameFinderModel(file);
            file = new File("models/en-ner-location.bin");
            this._placesModel = new TokenNameFinderModel(file);
            file = new File("models/en-lemmatizer.dict");
            this._lemmatizer = new DictionaryLemmatizer(file);

        } catch (IOException ex)
        {
            Logger.getLogger(MovieReviewStatictics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processFile(String text)
    {
        // TODO: process the text to find the following statistics:
        // For each movie derive:
        //    - number of sentences
        SentenceDetectorME sentenceDetectorME = new SentenceDetectorME(this._sentenceModel);
        String[] sentences = sentenceDetectorME.sentDetect(text);
        int noSentences = sentences.length;
        //    - number of tokens
        TokenizerME tokenizerME = new TokenizerME(this._tokenizerModel);
        String[] tokens = tokenizerME.tokenize(text);
        int noTokens = tokens.length;
        //    - number of (unique) stemmed forms
        String[] splittedText = text.split(" ");
        ArrayList<String> res = new ArrayList<>();
        for (String s : splittedText) {
            if (!res.contains(s)) {
                res.add(s);
            }
        }
        int noStemmed = res.size();
        //    - number of (unique) words from a dictionary (lemmatization)
        POSTaggerME posTaggerME = new POSTaggerME(this._posModel);
        ArrayList<String> posTags = new ArrayList<>(Arrays.asList(posTaggerME.tag(text.split(" "))));
//        int noWords = this._lemmatizer.lemmatize(/);
        int noWords =0 ;
        //    -  people
        Span[] people = new Span[] { };
        NameFinderME nameFinderME = new NameFinderME(this._peopleModel);
        people = nameFinderME.find(tokens);
        //    - locations
        Span[] locations = new Span[] { };
        nameFinderME = new NameFinderME(this._placesModel);
        locations = nameFinderME.find(tokens);
        //    - organisations
        Span[] organisations = new Span[] { };
        nameFinderME = new NameFinderME(this._organizationsModel);
        organisations = nameFinderME.find(tokens);

        // TODO + compute the following overall (for all movies) POS tagging statistics:
        //    - percentage number of adverbs (class variable, private int _verbCount = 0)
        //    - percentage number of adjectives (class variable, private int _nounCount = 0)
        //    - percentage number of verbs (class variable, private int _adjectiveCount = 0)
        //    - percentage number of nouns (class variable, private int _adverbCount = 0)
        //    + update _totalTokensCount
        // ------------------------------------------------------------------

        // TODO derive sentences (update noSentences variable)


        // TODO derive tokens and POS tags from text
        // (update noTokens and _totalTokensCount)


        // TODO perform stemming (use derived tokens)
        // (update noStemmed)
        Set <String> stems = new HashSet <>();

        for (String token : tokens)
        {
            String replace = token.toLowerCase().replace("[^a-z0-9]", "");//thereafter, ignore "" tokens
            if(replace.equals("")){
                noStemmed--;
            }
            stems.add(replace);
        }


        // TODO perform lemmatization (use derived tokens)
//         (remove "O" from results - non-dictionary forms, update noWords)
        for(String lam: posTags){
            if(lam.equals("0")){
                posTags.remove(lam);
                noWords--;
            }
        }


        // TODO derive people, locations, organisations (use tokens),
        // (update people, locations, organisations lists).

        // TODO update overall statistics - use tags and check first letters
        // (see https://www.clips.uantwerpen.be/pages/mbsp-tags; first letter = "V" = verb?)

        // ------------------------------------------------------------------

        saveResults("Sentences", noSentences);
        saveResults("Tokens", noTokens);
        saveResults("Stemmed forms (unique)", noStemmed);
        saveResults("Words from a dictionary (unique)", noWords);

        saveNamedEntities("People", people, new String[] { });
        saveNamedEntities("Locations", locations, new String[] { });
        saveNamedEntities("Organizations", organisations, new String[] { });
    }


    private void saveResults(String feature, int count)
    {
        String s = feature + ": " + count;
        System.out.println("   " + s);
        _statisticsWriter.println(s);
    }

    private void saveNamedEntities(String entityType, Span spans[], String tokens[])
    {
        StringBuilder s = new StringBuilder(entityType + ": ");
        for (int sp = 0; sp < spans.length; sp++)
        {
            for (int i = spans[sp].getStart(); i < spans[sp].getEnd(); i++)
            {
                s.append(tokens[i]);
                if (i < spans[sp].getEnd() - 1) s.append(" ");
            }
            if (sp < spans.length - 1) s.append(", ");
        }

        System.out.println("   " + s);
        _statisticsWriter.println(s);
    }

    private void overallStatistics()
    {
        _statisticsWriter.println("---------OVERALL STATISTICS----------");
        DecimalFormat f = new DecimalFormat("#0.00");

        if (_totalTokensCount == 0) _totalTokensCount = 1;
        String verbs = f.format(((double) _verbCount * 100) / _totalTokensCount);
        String nouns = f.format(((double) _nounCount * 100) / _totalTokensCount);
        String adjectives = f.format(((double) _adjectiveCount * 100) / _totalTokensCount);
        String adverbs = f.format(((double) _adverbCount * 100) / _totalTokensCount);

        _statisticsWriter.println("Verbs: " + verbs + "%");
        _statisticsWriter.println("Nouns: " + nouns + "%");
        _statisticsWriter.println("Adjectives: " + adjectives + "%");
        _statisticsWriter.println("Adverbs: " + adverbs + "%");

        System.out.println("---------OVERALL STATISTICS----------");
        System.out.println("Adverbs: " + adverbs + "%");
        System.out.println("Adjectives: " + adjectives + "%");
        System.out.println("Verbs: " + verbs + "%");
        System.out.println("Nouns: " + nouns + "%");
    }

}
