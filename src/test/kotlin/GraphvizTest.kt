import info.colugo.swagger2uml.Puml2Dot
import com.kicksolutions.swagger.plantuml.PlantUMLCodegen
import io.swagger.parser.SwaggerParser
import org.junit.Assert
import org.junit.Test

class GraphvizTest{

    val puml = """
@startuml
hide empty members
set namespaceSeparator none

skinparam class {
	BackgroundColor PaleGreen
	ArrowColor RoyalBlue
	BorderColor DimGray
}

class  Definition {
	 - name <b>:String</b>
	 - domain <b>:String</b>
	 - definition <b>:String</b>
	 - guidance <b>:String</b>
	 - identifier <b>:Uri</b>
	 - usage <b>:String[]</b>
}

class  Results {
	 - content <b>:Definition[]</b>
	 - numberOfElements <b>:Integer</b>
	 - firstPage <b>:Boolean</b>
	 - lastPage <b>:Boolean</b>
	 - totalPages <b>:Integer</b>
	 - id <b>:Link[]</b>
	 - links <b>:Link[]</b>
}

class  Link {
	 - rel <b>:String</b>
	 - href <b>:String</b>
}


interface DefinitionsApi {
	 + <i>null(String domain,String id)</i><b>:Definition</b>
}

interface DefinitionsApi {
	 + <i>getDefinitions(Integer page,Integer size)</i><b>:Results</b>
}

interface SearchApi {
	 + <i>null(String query,String domain,Integer page,Integer size)</i><b>:Results</b>
}


Results  *--   Definition
Results  *--   Link
DefinitionsApi -->    Definition
DefinitionsApi -->    Results
SearchApi -->    Results


@enduml
"""


    @Test
    fun can_get_puml_from_swagger(){
        val swaggerText = GraphvizTest::class.java.getResource("swagger.json").readText()
        Assert.assertNotNull(swaggerText)
        Assert.assertNotEquals("",swaggerText)

        val swaggerObject = SwaggerParser().parse(swaggerText)
        val codegen = PlantUMLCodegen(swaggerObject,  false, false)
        var puml = codegen.generatePuml()

        Assert.assertNotNull(puml)
        Assert.assertNotEquals("",puml)

        //println(puml)
    }


    @Test
    fun can_get_dot_from_puml(){
        val puml2Dot = Puml2Dot(puml)
        Assert.assertTrue(puml2Dot.classes.filter { it.name == "Definition" }.isNotEmpty())
        Assert.assertEquals(3,puml2Dot.classes.size)
        //println(puml2Dot.dot())
    }
}