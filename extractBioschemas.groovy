// Copyright (c) 2022  Egon Willighagen <egon.willighagen@gmail.com>
//
// GPL v3

@Grab(group='io.github.egonw.bacting', module='managers-rdf', version='0.0.45')
@Grab(group='io.github.egonw.bacting', module='managers-ui', version='0.0.45')
@Grab(group='io.github.egonw.bacting', module='net.bioclipse.managers.jsoup', version='0.0.45')

bioclipse = new net.bioclipse.managers.BioclipseManager(".");
rdf = new net.bioclipse.managers.RDFManager(".");
jsoup = new net.bioclipse.managers.JSoupManager(".");

databases = [
   "https://search.data.enanomapper.net/projects/gracious/" ,
   "https://search.data.enanomapper.net/projects/gov4nano/" ,
   "https://search.data.enanomapper.net/projects/nanoinformatix/" ,
   "https://search.data.enanomapper.net/projects/riskgone/" ,
   "https://search.data.enanomapper.net/projects/patrols/" ,
   "https://search.data.enanomapper.net/projects/biorima/" ,
   "https://search.data.enanomapper.net/projects/sabydoma/" ,
   "https://search.data.enanomapper.net/projects/sbd4nano/" ,
   "https://search.data.enanomapper.net/projects/harmless/" ,
   "https://search.data.enanomapper.net/projects/sunshine/" ,
   "https://search.data.enanomapper.net/projects/charisma/" ,
   "https://search.data.enanomapper.net/projects/polyrisk/" ,
   "https://search.data.enanomapper.net/projects/plasticheal/" ,
   "https://search.data.enanomapper.net/projects/plasticfate/" ,
   "https://search.data.enanomapper.net/projects/cusp/" ,
  "https://search.data.enanomapper.net/projects/sbd4nano/"
]

kg = rdf.createInMemoryStore()

for (database : databases) {
    htmlContent = bioclipse.download(database)

    htmlDom = jsoup.parseString(htmlContent)

    // application/ld+json

    bioschemasSections = jsoup.select(htmlDom, "script[type='application/ld+json']");

    for (section : bioschemasSections) {
    bioschemasJSON = section.html()
    rdf.importFromString(kg, bioschemasJSON, "JSON-LD")
    }
}

turtle = rdf.asTurtle(kg);

println "#" + rdf.size(kg) + " triples detected in the JSON-LD"
// println turtle

sparql = """
PREFIX schema: <http://schema.org/>
SELECT ?dataset ?url ?name ?license ?description WHERE {
?dataset a schema:Dataset ;
    schema:url ?url .
OPTIONAL { ?dataset schema:name ?name }
OPTIONAL { ?dataset schema:license ?license }
OPTIONAL { ?dataset schema:description ?description }
} ORDER BY ASC(?dataset)
"""

results = rdf.sparql(kg, sparql)

println "@prefix dc:    <http://purl.org/dc/elements/1.1/> ."
println "@prefix dct:   <http://purl.org/dc/terms/> ."
println "@prefix foaf:  <http://xmlns.com/foaf/0.1/> ."
println "@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> ."
println "@prefix sbd:   <https://www.sbd4nano.eu/rdf/#> ."
println "@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> ."
println "@prefix void:  <http://rdfs.org/ns/void#> ."
println ""
println "<https://search.data.enanomapper.net/closed/>"
println " a                    void:DatasetDescription ;"
println " dct:title            \"Datasets hosted at search.data.enanomapper.net\"@en ;"
println " foaf:img             <https://search.data.enanomapper.net/assets/img/logo.png> ."
println ""

for (i=1;i<=results.rowCount;i++) {
println "<${results.get(i, "dataset")}> a sbd:Dataset ;"
println "  dc:source <https://search.data.enanomapper.net/closed/> ;"
if (results.get(i, "name") != null) println "  rdfs:label \"${results.get(i, "name")}\"@en ;"
if (results.get(i, "description") != null) println "  dc:description \"${results.get(i, "description")}\"@en ;"
if (results.get(i, "license") != null) println "  dct:license <${results.get(i, "license")}> ;"
println "  foaf:page <${results.get(i, "url")}> ."
println ""
}
