package au.gov.dxa.graph


import au.gov.dxa.graph.RelationBuilder
import au.gov.dxa.graph.Relations
import org.junit.Assert
import org.junit.Test


class SemanticTest {

    @Test
    fun can_get_relations_url(){
        val id = "http://definitions.ausdx.io/api/definition/trc/de17"
        val relations = Relations(id)
        Assert.assertEquals("http://definitions.ausdx.io/api/relations/trc/de17", relations.relationsURL)
    }

    @Test
    fun can_get_relations(){
        val id = "http://definitions.ausdx.io/api/definition/ce/ce13"
        val relations = Relations(id)
        val map = relations.relationMap
        val nameMap = relations.nameLookup
        //println(RelationBuilder(relations.identifier, map, nameMap).dot())


    }
}