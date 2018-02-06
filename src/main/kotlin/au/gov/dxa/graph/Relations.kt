package au.gov.dxa.graph

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.net.URL

data class Relation(val from: String, val direction:String, val to: String, val toName: String)

class Relations(var id:String) {

    var relationsURL = ""
    var relationMap = mutableMapOf<String, MutableList<Relation>>()
    var urlsRead = mutableListOf<String>()
    var nameLookup = mutableMapOf<String, String>()


    init {
        val definitionUrl = URL(id)
        val definitionText = definitionUrl.readText()

        val definitionJson = Parser().parse(StringBuilder().append(definitionText)) as JsonObject
        val links = definitionJson["links"] as JsonArray<JsonObject>
        val content = definitionJson["content"] as JsonObject
        val identifier = content["identifier"] as String
        val relations = links.filter { it["rel"] == "relations" }.firstOrNull()
        if (relations != null) {
            relationsURL = relations["href"] as String ?: ""
            if (relationsURL != "") populateMap(identifier, relationsURL)
        }
    }


    fun populateMap(theId: String, url: String) {

        if (urlsRead.contains(url)) return
        urlsRead.add(url)

        //println("Reading ${theId}'s relations from ${url}")

        val relationURL = URL(url)
        val relationText = relationURL.readText()

        val relationsJson = Parser().parse(StringBuilder().append(relationText)) as Map<String, JsonObject>

        for (relationName in relationsJson.keys) {
            val list = mutableListOf<Relation>()
            for (relationJson in relationsJson[relationName] as List<JsonObject>) {
                var to = relationJson["to"] as String
                val relation = Relation(theId, relationJson["direction"] as String, to, relationJson["toName"] as String)

                // all relations are bi-directional. Only getting the froms removes dupes
                if (relation.direction == "FROM") list.add(relation)
                nameLookup[relation.to] = relation.toName

                val newUrl = "http://ausdx.tk/api/relations/" + to.removePrefix("http://dxa.gov.au/definition/")
                populateMap(to, newUrl)
            }
            if (!relationMap.containsKey(relationName)) relationMap[relationName] = mutableListOf<Relation>()
            relationMap[relationName]!!.addAll(list)
        }
    }

}