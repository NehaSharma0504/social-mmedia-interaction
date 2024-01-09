package eca.learnings.userdata.nlp;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.IOException;
import java.io.InputStream;

public class NlpAnalyser {

    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;

    public NlpAnalyser() {
        try {
            // Initialize OpenNLP sentence detector
            InputStream sentenceModelStream = getClass().getResourceAsStream("/opennlp/en-sent.bin");
            SentenceModel sentenceModel = new SentenceModel(sentenceModelStream);
            sentenceDetector = new SentenceDetectorME(sentenceModel);

            // Initialize OpenNLP tokenizer
            InputStream tokenizerModelStream = getClass().getResourceAsStream("/opennlp/en-token.bin");
            TokenizerModel tokenizerModel = new TokenizerModel(tokenizerModelStream);
            tokenizer = new TokenizerME(tokenizerModel);

            // Close the streams
            sentenceModelStream.close();
            tokenizerModelStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] detectSentences(String text) {
        return sentenceDetector.sentDetect(text);
    }

    public String[] tokenizeSentence(String sentence) {
        return tokenizer.tokenize(sentence);
    }
}

