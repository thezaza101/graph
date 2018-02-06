package info.colugo.swaggarr


import org.junit.Assert
import org.junit.Test


class SemanticTest {

    @Test
    fun can_get_relations_url(){
        val id = "http://definitions.ausdx.tk/api/definition/other/de17"
        val relations = Relations(id)
        Assert.assertEquals("http://ausdx.tk/api/relations/other/de17", relations.relationsURL)
    }

    @Test
    fun can_get_relations(){
        val id = "http://definitions.ausdx.tk/api/definition/other/de17"
        val relations = Relations(id)
        val map = relations.relationMap
        val nameMap = relations.nameLookup
        Assert.assertEquals(2, map.size)
        Assert.assertTrue(map.containsKey("skos:member"))
        Assert.assertTrue(map.containsKey("rdfs:seeAlso"))
        println(RelationBuilder(map, nameMap).dot())
    }
}