package uk.gov.justice.digital.model;

import java.util.ArrayList;
import java.util.List;

public class Params {
    private List<String> words = new ArrayList<>();

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}
