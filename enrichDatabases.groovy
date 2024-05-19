// Copyright (c) 2023  Egon Willighagen <egon.willighagen@gmail.com>
//
// GPL v3

@Grab(group='io.github.egonw.bacting', module='managers-rdf', version='0.3.2')
@Grab(group='io.github.egonw.bacting', module='managers-ui', version='0.3.2')
@Grab(group='io.github.egonw.bacting', module='net.bioclipse.managers.jsoup', version='0.3.2')

import groovy.json.JsonSlurper

bioclipse = new net.bioclipse.managers.BioclipseManager(".");
rdf = new net.bioclipse.managers.RDFManager(".");

url = "https://data.enanomapper.net/solr/metadata/select?q=*:*&rows=1000000&fl=owner_name:owner_name_s,substanceType:substanceType_s,publicname:publicname_s,topcategory:topcategory_s,endpointcategory:endpointcategory_s,E_method_synonym:E.method_synonym_ss,E_method:E.method_s,endpoint_synonym:effectendpoint_synonym_ss,endpoint:effectendpoint_s,number_of_study:number_of_points_d,name:name_s"

ambitContent = bioclipse.download(url)

def jsonSlurper = new JsonSlurper()
def ambitData = jsonSlurper.parseText(ambitContent) 

kg = rdf.createInMemoryStore()
rdf.addPrefix(kg, "sbdbel", "https://www.sbd4nano.eu/bel/#")
rdf.addPrefix(kg, "enm", "http://purl.enanomapper.org/onto/")
rdf.addPrefix(kg, "npo", "http://purl.bioontology.org/ontology/npo#")
rdf.addPrefix(kg, "obo", "http://purl.obolibrary.org/obo/")
rdf.addPrefix(kg, "erm", "https://nanocommons.github.io/identifiers/registry#")
rdf.addPrefix(kg, "rdfs", "http://www.w3.org/2000/01/rdf-schema#")

ownerURImap = new HashMap()
ownerURImap.put("caLIBRAte", "https://enanomapper.adma.ai/about/calibrate")
ownerURImap.put("ENPRA", "https://enanomapper.adma.ai/about/enpra")
ownerURImap.put("FP7-NMP NanoREG", "https://enanomapper.adma.ai/about/nanoreg")
ownerURImap.put("GRACIOUS", "https://enanomapper.adma.ai/about/gracious/")
ownerURImap.put("H2020 Calibrate", "https://enanomapper.adma.ai/about/calibrate/")
ownerURImap.put("HARMLESS", "https://enanomapper.adma.ai/about/harmless")
ownerURImap.put("MARINA", "https://enanomapper.adma.ai/about/marina")
ownerURImap.put("NanoGenotox", "https://enanomapper.adma.ai/about/nanogenotox")
ownerURImap.put("NANOINFORMATIX", "https://enanomapper.adma.ai/about/nanoinformatix")
ownerURImap.put("NANoREG", "https://enanomapper.adma.ai/about/nanoreg")
ownerURImap.put("NanoReg2", "https://enanomapper.adma.ai/about/nanoreg2")
ownerURImap.put("NanoTest", "https://enanomapper.adma.ai/about/nanotest")
ownerURImap.put("OMICS_DATA", "https://enanomapper.adma.ai/about/omics/")
ownerURImap.put("PATROLS", "https://enanomapper.adma.ai/about/patrols/")
ownerURImap.put("RISKGONE", "https://enanomapper.adma.ai/about/riskgone")
ownerURImap.put("SANOWORK", "https://enanomapper.adma.ai/about/sanowork")
ownerURImap.put("SBD4NANO", "https://enanomapper.adma.ai/about/sbd4nano")
//ownerURImap.put("Danish Centre for Nanosafety 1", "")
//ownerURImap.put("Danish Centre for Nanosafety 2", "")
//ownerURImap.put("FP7 NanoMILE", "")
//ownerURImap.put("FP7-NMP Gladiator", "")
//ownerURImap.put("FP7-NMP NanoSustain", "")
//ownerURImap.put("Horizon 2020 NanoPack", "")
//ownerURImap.put("Horizon2020 SmartNanoTox", "")
//ownerURImap.put("Lund NanoWire", "")
//ownerURImap.put("MESOCOSM", "")
//ownerURImap.put("NPK", "")

// for the next, see also https://search.data.enanomapper.net/assets/js/search/ont.js?g=1683886584905

materialsURImap = new HashMap()
materialsURImap.put("NPO_1541", "http://purl.bioontology.org/ontology/npo#NPO_1541")
materialsURImap.put("NPO_1373", "http://purl.bioontology.org/ontology/npo#NPO_1373")
materialsURImap.put("NPO_122", "http://purl.bioontology.org/ontology/npo#NPO_122")
materialsURImap.put("NPO_126", "http://purl.bioontology.org/ontology/npo#NPO_126")
materialsURImap.put("NPO_1374", "http://purl.bioontology.org/ontology/npo#NPO_1374")
materialsURImap.put("NPO_1375", "http://purl.bioontology.org/ontology/npo#NPO_1375")
materialsURImap.put("NPO_1542", "http://purl.bioontology.org/ontology/npo#NPO_1542")
materialsURImap.put("NPO_1544", "http://purl.bioontology.org/ontology/npo#NPO_1544")
materialsURImap.put("NPO_1548", "http://purl.bioontology.org/ontology/npo#NPO_1548")
materialsURImap.put("NPO_1550", "http://purl.bioontology.org/ontology/npo#NPO_1550")
materialsURImap.put("NPO_1559", "http://purl.bioontology.org/ontology/npo#NPO_1559")
materialsURImap.put("NPO_157", "http://purl.bioontology.org/ontology/npo#NPO_157")
materialsURImap.put("NPO_1892", "http://purl.bioontology.org/ontology/npo#NPO_1892")
materialsURImap.put("NPO_199", "http://purl.bioontology.org/ontology/npo#NPO_199")
materialsURImap.put("NPO_354", "http://purl.bioontology.org/ontology/npo#NPO_354")
materialsURImap.put("NPO_371", "http://purl.bioontology.org/ontology/npo#NPO_371")
materialsURImap.put("NPO_401", "http://purl.bioontology.org/ontology/npo#NPO_401")
materialsURImap.put("NPO_589", "http://purl.bioontology.org/ontology/npo#NPO_589")
materialsURImap.put("NPO_606", "http://purl.bioontology.org/ontology/npo#NPO_606")
materialsURImap.put("NPO_707", "http://purl.bioontology.org/ontology/npo#NPO_707")
materialsURImap.put("NPO_729", "http://purl.bioontology.org/ontology/npo#NPO_729")
materialsURImap.put("NPO_943", "http://purl.bioontology.org/ontology/npo#NPO_943")
materialsURImap.put("NPO_965", "http://purl.bioontology.org/ontology/npo#NPO_965")
materialsURImap.put("CHEBI_131897", "http://purl.obolibrary.org/obo/CHEBI_131897")
materialsURImap.put("CHEBI:132889", "http://purl.obolibrary.org/obo/CHEBI_132889")
materialsURImap.put("CHEBI_132889", "http://purl.obolibrary.org/obo/CHEBI_132889")
materialsURImap.put("CHEBI_133326", "http://purl.obolibrary.org/obo/CHEBI_133326")
materialsURImap.put("CHEBI_133333", "http://purl.obolibrary.org/obo/CHEBI_133333")
materialsURImap.put("CHEBI_133337", "http://purl.obolibrary.org/obo/CHEBI_133337")
materialsURImap.put("CHEBI_133340", "http://purl.obolibrary.org/obo/CHEBI_133340")
materialsURImap.put("CHEBI_133349", "http://purl.obolibrary.org/obo/CHEBI_133349")
materialsURImap.put("CHEBI_133354", "http://purl.obolibrary.org/obo/CHEBI_133354")
materialsURImap.put("CHEBI_140503", "http://purl.obolibrary.org/obo/CHEBI_140503")
materialsURImap.put("CHEBI_16412", "http://purl.obolibrary.org/obo/CHEBI_16412")
materialsURImap.put("CHEBI:18246", "http://purl.obolibrary.org/obo/CHEBI_18246")
materialsURImap.put("CHEBI_18246", "http://purl.obolibrary.org/obo/CHEBI_18246")
materialsURImap.put("CHEBI_24431", "http://purl.obolibrary.org/obo/CHEBI_24431")
materialsURImap.put("CHEBI_3311", "http://purl.obolibrary.org/obo/CHEBI_3311")
materialsURImap.put("CHEBI_33416", "http://purl.obolibrary.org/obo/CHEBI_33416")
materialsURImap.put("CHEBI_33418", "http://purl.obolibrary.org/obo/CHEBI_33418")
materialsURImap.put("CHEBI_46661", "http://purl.obolibrary.org/obo/CHEBI_46661")
materialsURImap.put("CHEBI_46666", "http://purl.obolibrary.org/obo/CHEBI_46666")
materialsURImap.put("CHEBI_46727", "http://purl.obolibrary.org/obo/CHEBI_46727")
materialsURImap.put("CHEBI_48730", "http://purl.obolibrary.org/obo/CHEBI_48730")
materialsURImap.put("CHEBI_50595", "http://purl.obolibrary.org/obo/CHEBI_50595")
materialsURImap.put("CHEBI_50596", "http://purl.obolibrary.org/obo/CHEBI_50596")
materialsURImap.put("CHEBI_50814", "http://purl.obolibrary.org/obo/CHEBI_50814")
materialsURImap.put("CHEBI_51050", "http://purl.obolibrary.org/obo/CHEBI_51050")
materialsURImap.put("CHEBI_51135", "http://purl.obolibrary.org/obo/CHEBI_51135")
materialsURImap.put("CHEBI_59999", "http://purl.obolibrary.org/obo/CHEBI_59999")
materialsURImap.put("CHEBI_599999", "http://purl.obolibrary.org/obo/CHEBI_59999")
materialsURImap.put("CHEBI_82297", "http://purl.obolibrary.org/obo/CHEBI_82297")
materialsURImap.put("ENM_8000245", "http://purl.enanomapper.org/onto/ENM_8000245")
materialsURImap.put("ENM_8000305", "http://purl.enanomapper.org/onto/ENM_8000305")
materialsURImap.put("ENM_9000005", "http://purl.enanomapper.org/onto/ENM_9000005")
materialsURImap.put("ENM_9000006", "http://purl.enanomapper.org/onto/ENM_9000006")
materialsURImap.put("ENM_9000007", "http://purl.enanomapper.org/onto/ENM_9000007")
materialsURImap.put("ENM_9000008", "http://purl.enanomapper.org/onto/ENM_9000008")
materialsURImap.put("ENM_9000073", "http://purl.enanomapper.org/onto/ENM_9000073")
materialsURImap.put("ENM_9000239", "http://purl.enanomapper.org/onto/ENM_9000239")
materialsURImap.put("ENM_9000241", "http://purl.enanomapper.org/onto/ENM_9000241")
materialsURImap.put("ENM_9000254", "http://purl.enanomapper.org/onto/ENM_9000254")
materialsURImap.put("ENM_9000257", "http://purl.enanomapper.org/onto/ENM_9000257")
materialsURImap.put("ENVO_00002008", "http://purl.obolibrary.org/obo/ENVO_00002008")
materialsURImap.put("ENVO:00002985", "http://purl.obolibrary.org/obo/ENVO_00002985")
materialsURImap.put("ENVO_01000060", "http://purl.obolibrary.org/obo/ENVO_01000060")
materialsURImap.put("ENVO:02000102", "http://purl.obolibrary.org/obo/ENVO_02000102")
materialsURImap.put("C62387", "http://purl.obolibrary.org/obo/NCIT_C62387")
materialsURImap.put("C569492", "http://purl.obolibrary.org/obo/NCIT_C569492")
materialsURImap.put("C113679", "http://purl.obolibrary.org/obo/NCIT_C113679")

boolean isInteger(string) {
  try { Integer.parseInt(string); } catch (Exception e) { return false; }
  return true;
}

for (doc in ambitData.response.docs) {
  ownerName = doc.owner_name 
  substanceType = doc.substanceType
  name = doc.name
  if (name != null && name.length() == 11 &&
      name.startsWith("ERM0") && isInteger(name.substring(3))) {
    // the name is an ERM identifier
    rdf.addObjectProperty(kg, ownerURImap.get(ownerName), "https://www.sbd4nano.eu/bel/#NP",
      "https://nanocommons.github.io/identifiers/registry#" + name)
    rdf.addObjectProperty(kg, "https://nanocommons.github.io/identifiers/registry#" + name,
      "http://www.w3.org/2000/01/rdf-schema#subClassOf",
      materialsURImap.get(substanceType))
    rdf.addDataProperty(kg, "https://nanocommons.github.io/identifiers/registry#" + name,
      "http://www.w3.org/2000/01/rdf-schema#label", name)
  }
  if (ownerURImap.containsKey(ownerName)) {
    if (materialsURImap.containsKey(substanceType)) {
      rdf.addObjectProperty(kg, ownerURImap.get(ownerName), "https://www.sbd4nano.eu/bel/#NP", materialsURImap.get(substanceType))
    } else {
      // println "# unknown substance type: ${substanceType}"
    }
  } else {
    // println "# unknown owner: ${ownerName}"
  } 
}

println rdf.asTurtle(kg);
