# CCTB Server Module

## Usage

The server module exposes an HTTP Post endpoint that receives ccdl files and translates them into cql queries. The mapping and context files needed for the translation are passed to the application via environment variables or command line arguments.

For example:
```
CCTB_SERVER_MAPPING=./mapping_cql.json CCTB_SERVER_CONCEPT_TREE=./mapping_tree.json mvn spring-boot:run
# or: mvn spring-boot:run --cctb.server.mapping=./mapping_cql.json --cctb.server.concept-tree=./mapping_tree.json

...

curl -X POST -H "Content-Type: application/x+ccdl+json" -d @./test-query.json http://localhost:8080/translate
```