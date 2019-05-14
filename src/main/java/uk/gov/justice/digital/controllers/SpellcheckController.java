package uk.gov.justice.digital.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.justice.digital.model.WordsRequested;
import uk.gov.justice.digital.service.SpellcheckService;

@RestController
@CrossOrigin
public class SpellcheckController {
    @Autowired
    private SpellcheckService service;

    @RequestMapping(value="/", method= RequestMethod.POST)
    public String words(@RequestBody WordsRequested words) {
        return service.getSpellcheckSuggestionsString(words.getParams().getWords().stream().toArray(String[]::new));
    }
}
