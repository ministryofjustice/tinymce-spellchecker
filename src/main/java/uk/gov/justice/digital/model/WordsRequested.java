package uk.gov.justice.digital.model;

public class WordsRequested {
    private String id;
    private Params params;

    public String getId() {
        return id;
    }

    public Params getParams() {
        return params;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setParams(Params paramsObject) {
        this.params = paramsObject;
    }
}
