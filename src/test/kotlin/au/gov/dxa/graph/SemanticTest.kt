package au.gov.dxa.graph


import au.gov.dxa.graph.RelationBuilder
import au.gov.dxa.graph.Relations
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
        val id = "http://definitions.ausdx.tk/api/definition/ce/ce13"
        val relations = Relations(id)
        val map = relations.relationMap
        val nameMap = relations.nameLookup
        println(RelationBuilder(relations.identifier, map, nameMap).dot())


    }
}