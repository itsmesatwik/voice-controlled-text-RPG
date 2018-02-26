import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.WordResult;

public class testMain {


    public static void configurationSetup(Configuration configuration) {
        // Set path to acoustic model.
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        // Set path to dictionary.
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        // Set language model.
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
    }

    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        configurationSetup(config);
        LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(config);
        recognizer.startRecognition(true);
        boolean flag = true;
        while (flag) {
            SpeechResult result = recognizer.getResult();
            System.out.println(result.getHypothesis());
            for (WordResult word : result.getWords()) {
                System.out.println(word);
                if (word.getWord().toString().equalsIgnoreCase("exit")) {
                    recognizer.stopRecognition();
                    flag = false;
                }
            }
        }

    }
}
