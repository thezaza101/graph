package au.gov.dxa.graph

class RelationBuilder(var url:String, var map:Map<String, MutableList<Relation>>, var nameMap: Map<String, String>){

    private val classes = mutableMapOf<String, MutableList<String>>()
    private val relations = mutableListOf<String>()
    private val classAttributePadding = mutableMapOf<String, Int>()
    private val things = mutableSetOf<String>()
    private var hightlightName = ""


    init{
        val members = map["skos:member"]
        if(members != null) {
            for (member in members) {
                val className = member.from
                if(className == url.replace("/api","")) hightlightName = className
                val attributeName = nameMap[member.to]?:member.to
                if(member.to == url.replace("/api","")) hightlightName = className

                if(!classes.containsKey(className)){
                    classes[className] = mutableListOf()
                    classAttributePadding[className] = 0
                    things.add(className)
                }

                classes[className]!!.add(attributeName)
                classAttributePadding[className] = maxOf(classAttributePadding[className]!!, attributeName.length)
            }
        }

        val seeAlso = map["rdfs:seeAlso"]
        if(seeAlso != null) {
            for (see in seeAlso) {
                val fromName = see.from//nameMap[see.from]?:see.from
                val toName = see.to//nameMap[see.to]?:see.to
                things.add(fromName)
                things.add(toName)

                relations.add("\"${fromName}\" -> \"${toName}\"[arrowhead=none;label=\"  rdfs:seeAlso\"]")
            }
        }

        val subClassOf = map["skos:subClassOf"]
        if(subClassOf != null) {
            for (sco in subClassOf) {
                val fromName = sco.from//nameMap[see.from]?:see.from
                val toName = sco.to//nameMap[see.to]?:see.to
                things.add(fromName)
                things.add(toName)

                relations.add("\"${fromName}\" -> \"${toName}\"[arrowtail=empty;dir=\"back\";label=\"  skos:subClassOf\"]")
            }
        }
    }


    fun classWithURIColour(classUri:String ): String{
        if(classUri == hightlightName) return "ED6A5A"
        return "68AFCB"
    }

    fun dot():String{
        val head = """
digraph G {

fontname = "Bitstream Vera Sans";

node [
fontname = "Bitstream Vera Sans"
fontsize = 10
shape="none"
]

edge [
arrowsize=0.8
minlen=1
fontname = "Courier"
fontsize="8"
]

nodesep=0.486111;
ranksep=0.833333;
remincross=true;
searchsize=500;
"""

        var output = ""

        for(theClass in things) {
            var classStr = """"${theClass}"[label=<<font face="Courier"><table style="rounded" border="6" color="white" cellspacing="0">"""
            classStr += """<tr><td port="port1" border="1" color="#${classWithURIColour(theClass)}" bgcolor="#${classWithURIColour(theClass)}"><font POINT-SIZE="12" color="white">${nameMap[theClass]?:theClass} </font></td></tr>"""
            classStr += """<tr > <td port = "port2" border ="1" color="#dddddd" bgcolor="#dddddd"></td></tr>"""
            classStr += """</table></font>>];"""

            output += "\n${classStr}\n"
        }


        for(theClass in classes.keys){
            var classStr = """"${theClass}"[label=<<font face="Courier"><table style="rounded" border="6" color="white" cellspacing="0">"""
            classStr += """<tr><td port="port1" border="1" color="#${classWithURIColour(theClass)}" bgcolor="#${classWithURIColour(theClass)}"><font POINT-SIZE="14" color="white">${nameMap[theClass]?:theClass} </font></td></tr>"""
            classStr += """<tr > <td port = "port2" border ="1" color="#dddddd" bgcolor="#dddddd">"""
            for(member in classes[theClass]!!) {
                classStr += """<font color="#8B0000"> &#x25A1; </font>${member + " ".repeat(classAttributePadding[theClass]!! - member.length)}   <br/>"""
            }
            classStr+="""</td></tr>"""
            classStr += """</table></font>>];"""

            output += "\n${classStr}\n"
        }

        output += relations.joinToString("\n")

        return  head +  output + "\n}"
    }
}