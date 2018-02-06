package info.colugo.swaggarr

class RelationBuilder(var map:Map<String, MutableList<Relation>>, var nameMap: Map<String, String>){

    private val classes = mutableMapOf<String, MutableList<String>>()
    private val relations = mutableListOf<String>()
    private val classAttributePadding = mutableMapOf<String, Int>()
    private val things = mutableSetOf<String>()


    init{
        val members = map["skos:member"]
        if(members != null) {
            for (member in members) {
                val className = nameMap[member.from]?:member.from
                val attributeName = nameMap[member.to]?:member.to

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
                val fromName = nameMap[see.from]?:see.from
                val toName = nameMap[see.to]?:see.to
                things.add(fromName)
                things.add(toName)

                relations.add("\"${fromName}\" -> \"${toName}\"[arrowhead=none;label=\"  rdfs:seeAlso\"]")
            }
        }
    }


    fun dot():String{
        val head = """
digraph G {

fontname = "Bitstream Vera Sans";
fontsize = 8

node [
fontname = "Bitstream Vera Sans"
fontsize = 8
shape="none"
]

edge [
arrowsize=0.8
minlen=1
fontname = "Courier"
fontsize="6"
]

nodesep=0.486111;
ranksep=0.833333;
remincross=true;
searchsize=500;
"""

        var output = ""

        for(theClass in things) {
            var classStr = """"${theClass}"[label=<<font face="Courier" size="6"><table border="0" cellspacing="0">"""
            classStr += """<tr><td port="port1" border="1"  bgcolor="#cce5ff">${theClass} </td></tr></table></font>>]"""

            output += "\n${classStr}\n"
        }

        for(theClass in classes.keys){
            var classStr = """"${theClass}"[label=<<font face="Courier" size="6"><table border="0" cellspacing="0">"""
            classStr += """<tr><td port="port1" border="1"  bgcolor="#99ff99">${theClass} </td></tr>"""
            classStr += """<tr > <td port = "port2" border ="1" >"""
            for(member in classes[theClass]!!) {
                classStr += """<font color="#8B0000"> &#x25A1; </font>${member + " ".repeat(classAttributePadding[theClass]!! - member.length)}   <br/>"""
            }
            classStr+="""</td></tr>"""
            classStr += """<tr><td port="port3" border="1"></td></tr></table></font>>];"""

            output += "\n${classStr}\n"
        }

        output += relations.joinToString("\n")

        return  head +  output + "\n}"
    }
}