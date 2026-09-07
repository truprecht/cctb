package de.medizininformatikinitiative.cctb.server;

import de.medizininformatikinitiative.cctb.model.Mapping;
import de.medizininformatikinitiative.cctb.model.MappingContext;
import de.medizininformatikinitiative.cctb.model.MappingTreeBase;
import de.medizininformatikinitiative.cctb.model.MappingTreeModuleRoot;
import de.medizininformatikinitiative.cctb.model.structured_query.ContextualTermCode;

import java.io.File;
import java.util.Arrays;
import java.util.function.Function;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tools.jackson.databind.ObjectMapper;


@Service
public class ContextProvider {
  private final Map<ContextualTermCode, Mapping> mapping;
  private final MappingTreeBase conceptTree;
  private final Map<String, String> codeSystemAliases;

  private static Map<ContextualTermCode, Mapping> readMapping(ObjectMapper o, String path) {
    var file = new File(path);
    return Stream.of(o.readValue(file, Mapping[].class))
            .collect(Collectors.toMap(Mapping::key, Function.identity(), (a, b) -> a));
  }
  
  private static MappingTreeBase readConceptTree(ObjectMapper o, String path) {
    var file = new File(path);
    var object = o.readValue(file, MappingTreeModuleRoot[].class);
    return new MappingTreeBase(Arrays.stream(object).toList());
  }

  private static Map<String, String> readCodeSystemAliases(ObjectMapper o, String path) {
    if (path == null || path.isEmpty())
      return Map.of();
    return o.readValue(new File(path), Map.class);
  }

  public ContextProvider(
    @Value("${cctb.server.mapping}") String mappingPath,
    @Value("${cctb.server.concept-tree}") String conceptTreePath,
    @Value("${cctb.server.code-system-aliases}") String codeSystemAliasesPath
  ) {
    var json = new ObjectMapper();
    this.mapping = readMapping(json, mappingPath);
    this.conceptTree = readConceptTree(json, conceptTreePath);
    this.codeSystemAliases = readCodeSystemAliases(json, codeSystemAliasesPath);
  }

  public MappingContext getContext() {
    return MappingContext.of(
      this.mapping,
      this.conceptTree,
      this.codeSystemAliases
    );
  }
}