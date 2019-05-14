package uk.gov.justice.digital.service;

import dk.dren.hunspell.Hunspell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpellcheckService {
    private String dictionaries;

    private String rootDictDirectory;

    private List<Hunspell.Dictionary> loadedDictionaries;

    private final static Logger LOGGER = LoggerFactory.getLogger(SpellcheckService.class);

    @Autowired
    public SpellcheckService(@Value("${spellcheck.dictionaries}") String dictionaries,
                             @Value("${spellcheck.rootdir}") String rootDictDirectory) {

        this.dictionaries = dictionaries;
        this.rootDictDirectory = rootDictDirectory;

        loadedDictionaries = new ArrayList<>();
        loadDictionaries();
    }

    public void loadDictionaries() {
        List<String> dictList = getDictionariesAsList();
        loadedDictionaries.clear();

        for (String dict : dictList) {
            try {
                loadedDictionaries.add(Hunspell.getInstance().getDictionary(getDictionaryPath(dict)));
                LOGGER.info("Loaded dictionary successfully: " + dict);
            } catch(Exception e) {
                LOGGER.error("Error instantiating dictionary: " + dict, e);
            }
        }
    }

    public String getSpellcheckSuggestionsString(String[] wordsToCheck) {
        String words = "";
        boolean misspelled = false;
        for (String s : wordsToCheck) {
            List<String> allSuggestionsForThisWord = new ArrayList<>();
            for (Hunspell.Dictionary dict : loadedDictionaries) {
                if (dict.misspelled(s)) {
                    allSuggestionsForThisWord.addAll(dict.suggest(s));
                    misspelled = true;
                }
                StringBuilder sb = new StringBuilder();
                if(!allSuggestionsForThisWord.isEmpty()) {
                    sb.append("\"" + s + "\" : ");
                    sb.append(allSuggestionsForThisWord.stream().map(str -> "\"" + str + "\"").collect(Collectors.toList()));
                    sb.append(",");
                }
                words = words + sb.toString();
            }
        }
        int comma = words.lastIndexOf(",");
        String substring = words.substring(0, comma);

        if(misspelled == false) {
            return "{ \"result\" : {}}";
        }

        return String.format("{ \"result\" : { \"words\" : { %s } } }", substring);
    }

    public List<String> getDictionariesAsList() {
        List<String> dictList = new ArrayList<>();
        String[] dictArray = dictionaries.split(",");
        for (String s : dictArray) {
            dictList.add(s.trim());
        }
        return dictList;
    }

    public List<Hunspell.Dictionary> getLoadedDictionaries() {
        return loadedDictionaries;
    }

    private String getDictionaryPath(String dict) {
        return rootDictDirectory + dict;
    }

}
