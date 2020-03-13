package com.zi;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class OpenNLP {

    public static String LANG_DETECT_MODEL = "models/langdetect-183.bin";
    public static String TOKENIZER_MODEL = "models/en-token.bin";
    public static String SENTENCE_MODEL = "models/en-sent.bin";
    public static String POS_MODEL = "models/en-pos-maxent.bin";
    public static String CHUNKER_MODEL = "models/en-chunker.bin";
    public static String LEMMATIZER_DICT = "models/en-lemmatizer.dict";
    public static String NAME_MODEL = "models/en-ner-person.bin";
    public static String ENTITY_XYZ_MODEL = "models/en-ner-xxx.bin";

    public static void main(String[] args) throws IOException {
        OpenNLP openNLP = new OpenNLP();
        openNLP.run();
    }

    public void run() throws IOException {

//		languageDetection();
//		 tokenization();
//         sentenceDetection();
//		 posTagging();
//		 lemmatization();
//		 stemming();
//		 chunking();
        nameFinding();
    }

    private void languageDetection() throws IOException {
        File modelFile = new File(LANG_DETECT_MODEL);
        LanguageDetectorModel model = new LanguageDetectorModel(modelFile);
        LanguageDetectorME languageDetectorME = new LanguageDetectorME(model);

        String text = "";
//		text = "cats"; // plt (0.012078)
//		 text = "cats like milk"; // nob (0.0139247)
//		 text = "Many cats like milk because in some ways it reminds them of their mother's milk."; //eng(0.092666)
//		 text = "The two things are not really related. Many cats like milk because in " +
//				 "some ways it reminds them of their mother's milk."; // eng(0.210905)
//		text = "The two things are not really related. Many cats like milk because in some ways it reminds them of their mother's milk. "
//				+ "It is rich in fat and protein. They like the taste. They like the consistency . "
//				+ "The issue as far as it being bad for them is the fact that cats often have difficulty digesting " +
//				"milk and so it may give them "
//				+ "digestive upset like diarrhea, bloating and gas. " +
//				"After all, cow's milk is meant for baby calves, not cats. "
//				+ "It is a fortunate quirk of nature that human digestive systems can" +
//				" also digest cow's milk. But humans and cats are not cows."; //eng(0.954817)
//		 text = "Many cats like milk because in some ways it reminds them of their " +
//				 "mother's milk. Le lait n'est pas forcement mauvais pour les chats"; //eng(0.151544)
//		 text = "Many cats like milk because in some ways it reminds them of their mother's milk. " +
//				 "Le lait n'est pas forcwment mauvais pour les chats. "
//		 			+ "Der Normalfall ist allerdings der, dass Salonlewen " +
//				 "Milch weder brauchen noch gut verdauen kennen."; //eng(0.271207)

        /*
         * Im dłuższy jest tekst tym większe prawdopodobieństwo że przewidywany język jest poprawny
         * Jeżeli text jest pisany w kilku językach to prawdopodobieństwo spada, a wybrany język
         * to ten który jest najdłuższy w tekście.
         * */
        Language language = languageDetectorME.predictLanguage(text);
        System.out.println(language);
    }

    private void tokenization() throws IOException {
        File modelFile = new File(TOKENIZER_MODEL);
        TokenizerModel model = new TokenizerModel(modelFile);
        TokenizerME tokenizerME = new TokenizerME(model);

        String text = "";

        text = "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
                + "but there may have been instances of domestication as early as the Neolithic from around 9500 years ago (7500 BC).";
//		text = "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
//				+ "but there may have been instances of domestication as early as the Neolithic from around 9,500 years ago (7,500 BC).";
//		text = "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
//		 + "but there may have been instances of domestication as early as the Neolithic from around 9 500 years ago ( 7 500 BC).";

        String[] data = tokenizerME.tokenize(text);
        double[] value = tokenizerME.getTokenProbabilities();
        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i] + " : " + value[i]);
        }

        /*
         * Tak wyniki się różnią, wynika to m.in. z form zapisu daty, jeden z nich jest bardziej typowy dla
         * nomenklatury języka angielskikego.
         *
         * W przypadku przepuszczenia tekstów przez de-token.bin
         * ostatni  i pierwszy tekst mają bardzo podobne wyniki co ten dla ankielskiego języka
         * natomiast w  2 tekście wyniki od siebie odbiegają
         * */
    }

    private void sentenceDetection() throws IOException {
        File modelFile = new File(SENTENCE_MODEL);
        SentenceModel model = new SentenceModel(modelFile);
        SentenceDetectorME sentenceDetectorME = new SentenceDetectorME(model);

        String text = "";
//		text = "Hi. How are you? Welcome to OpenNLP. "
//				+ "We provide multiple built-in methods for Natural Language Processing."; //brak błędów
//		text = "Hi. How are you?! Welcome to OpenNLP? "
//				+ "We provide multiple built-in methods for Natural Language Processing."; //brak błędów
//		text = "Hi. How are you? Welcome to OpenNLP.?? "
//				+ "We provide multiple . built-in methods for Natural Language Processing.";
        // kropka rodzielająca sentencję powoduje rozdzielenie jej na dwie osobne

//		text = "The interrobang, also known as the interabang (often represented by ?! or !?), "
//				+ "is a nonstandard punctuation mark used in various written languages. " // podział sentencji na dwie
//				+ "It is intended to combine the functions of the question mark (?), or interrogative point, " // podzial po znaku ?
//				+ "and the exclamation mark (!), or exclamation point, known in the jargon of printers and programmers as a \"bang\". ";
//		//podział po wykrzykniku

//		text = "The interrobang, also known as the interabang (often represented by ?! or !?), "
//				+ "is a nonstandard ?? punctuation mark used !! in various written languages. "
//				+ "It is intended to ?? combine the functions of the question mark (?), or interrogative point, "
//				+ "and the exclamation mark (!), or exclamation point, known in the jargon of printers and programmers as a \"bang\". ";
        // podwójne znaki zapytania nie wpływajaą na rozdzieleniee sentencji na osobne

        System.out.println(Arrays.toString(sentenceDetectorME.sentDetect(text)));
    }

    private void posTagging() throws IOException {
        File modelFile = new File(POS_MODEL);
        POSModel model = new POSModel(modelFile);
        POSTaggerME posTaggerME = new POSTaggerME(model);

        String[] sentence = new String[0];
//		sentence = new String[] { "Cats", "like", "milk" }; // NNS-ok IN-nie ok NN-ok
//		sentence = new String[]{"Cat", "is", "white", "like", "milk"};  //NNP-ok VBZ-ok JJ-ok IN-ok NN-ok
//		sentence = new String[] { "Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
//				"built-in", "methods", "for", "Natural", "Language", "Processing" };
        // NNP-ok WRB-ok VBP-ok PRP-ok VB-ok TO-ok VB-nie ok PRP-ok VB-ok JJ-ok JJ-ok NNS-ok IN-ok JJ-ok NN-ok VBG-ok
//		sentence = new String[] { "She", "put", "the", "big", "knives", "on", "the", "table" };
        // PRP-ok VBD-ok DT-ok JJ-ok NNS-ok IN-ok DT-ok NN-ok

        /*
         * Like - powinien być rozpoznawany jako spójnik lub czasownik
         * Jednak tagowanie określa że like jest zawsze spójnikiem
         * */

        System.out.println(Arrays.toString(posTaggerME.tag(sentence)));
    }

    private void lemmatization() throws IOException {
        File modelFile = new File(LEMMATIZER_DICT);
        DictionaryLemmatizer dictionaryLemmatizer = new DictionaryLemmatizer(modelFile);

        String[] text = new String[0];
        text = new String[]{"Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
                "built-in", "methods", "for", "Natural", "Language", "Processing"};
        String[] tags = new String[0];
        tags = new String[]{"NNP", "WRB", "QWE", "PRP", "VB", "TO", "VB", "PRP", "VB", "JJ", "JJ", "NNS", "IN", "JJ",
                "NN", "VBG"};

        /*
         * W stemmerze zamiana na formę podstawową odbyła się przez uciącie e ze słowa are
         * w przypadku usunięcia tokena dla are, zamiast zmiany formy na podstawową, wstawiane jest 0
         * Wyniki z Lemmatizera są dokładniejsze i trafniejsze niz te produkowane przez Stemmer.
         * */

        System.out.println(Arrays.toString(dictionaryLemmatizer.lemmatize(text, tags)));
    }

    private void stemming() {
        PorterStemmer porterStemmer = new PorterStemmer();

        String[] sentence = new String[0];
        sentence = new String[]{"Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
                "built-in", "methods", "for", "Natural", "Language", "Processing"};

        ArrayList<String> res = new ArrayList<>();
        for (String str : sentence) {
            res.add(porterStemmer.stem(str));
        }
        System.out.println(res);
    }

    private void chunking() throws IOException {
        File modelFile = new File(CHUNKER_MODEL);
        ChunkerModel model = new ChunkerModel(modelFile);
        ChunkerME chunkerME = new ChunkerME(model);

        String[] sentence = new String[0];
        sentence = new String[]{"She", "put", "the", "big", "knives", "on", "the", "table"};

        String[] tags = new String[0];
        tags = new String[]{"PRP", "VBD", "DT", "JJ", "NNS", "IN", "DT", "NN"};

        System.out.println(Arrays.toString(chunkerME.chunk(sentence, tags)));
        /*
         * POS tagi są potrzebne aby rozróżniać kolejne elementy sentencji przez chunker
         * B - analizowane słowo jest elementem innej sentencji
         * I - wewnątrz bieżącej sentencji
         * W tej sentencji znajdują się 3 chunki
         * Uważam ze ten wynik jest poprawny
         * */
    }

    private void nameFinding() throws IOException {
        File tokenModelFile = new File(TOKENIZER_MODEL);
        TokenizerModel tokenModel = new TokenizerModel(tokenModelFile);
        TokenizerME tokenizerME = new TokenizerME(tokenModel);

        File modelFile = new File(ENTITY_XYZ_MODEL);
        TokenNameFinderModel model = new TokenNameFinderModel(modelFile);
        NameFinderME nameFinderME = new NameFinderME(model);

        String text = "he idea of using computers to search for relevant pieces of information was popularized in the article "
                + "As We May Think by Vannevar Bush in 1945. It would appear that Bush was inspired by patents "
                + "for a 'statistical machine' - filed by Emanuel Goldberg in the 1920s and '30s - that searched for documents stored on film. "
                + "The first description of a computer searching for information was described by Holmstrom in 1948, "
                + "detailing an early mention of the Univac computer. Automated information retrieval systems were introduced in the 1950s: "
                + "one even featured in the 1957 romantic comedy, Desk Set. In the 1960s, the first large information retrieval research group "
                + "was formed by Gerard Salton at Cornell. By the 1970s several different retrieval techniques had been shown to perform "
                + "well on small text corpora such as the Cranfield collection (several thousand documents). Large-scale retrieval systems, "
                + "such as the Lockheed Dialog system, came into use early in the 1970s.";

        String[] tokens = tokenizerME.tokenize(text);
        Span[] namesSpan = nameFinderME.find(tokens);
        for (var span : namesSpan) {
            System.out.println(tokens[span.getStart()]);
        }
        /*
         * en-ner-xxx-.bin -> return dates from the text
         * en-ner-person.bin -> return names from the text
         * */
    }
}
