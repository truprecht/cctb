package de.medizininformatikinitiative.cctb.server;

import de.medizininformatikinitiative.cctb.model.structured_query.StructuredQuery;
import de.medizininformatikinitiative.cctb.Translator;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.databind.ObjectMapper;


@RestController
public class TranslationController {
  private final ContextProvider contextProvider;
  private final ObjectMapper json = new ObjectMapper();

  public TranslationController(ContextProvider contextProvider) {
    this.contextProvider = contextProvider;
  }

  @PostMapping(value = "/translate", consumes = "application/x+ccdl+json", produces = "text/cql")
  public @ResponseBody String translate_ccdl(@RequestBody String body) {
    var mappingContext = this.contextProvider.getContext();
    var structuredQuery = this.json.readValue(body, StructuredQuery.class);
    return Translator.of(mappingContext).toCql(structuredQuery).print();
  }
}