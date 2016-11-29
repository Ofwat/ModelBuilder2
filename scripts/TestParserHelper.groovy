File f = new File("D:/workspace/FountainModelBuilder/src/main/java/uk/gov/ofwat/fountain/modelbuilder/repository")

static String toCamelCase(String text) {
    return text[0].toLowerCase() + text.substring(1)
}

def generateInjects(){

}

def generateRepoStubs(File f){
    def injects = []
    def importClasses = []
    def imports = []
    def fout = new File("d:/temp/generated.txt")
    f.eachFile{
        if(it.name.contains('Repository')){
            def repoClass = it.name.substring(0, it.name.size()-5)
            def repoObject = toCamelCase(repoClass)
            def className
            className = repoClass.substring(0, repoClass.size()-10)
            def objectName = toCamelCase(className)
            
            //println "${repoClass}:${repoObject}:${className}:${objectName}"
            injects.add("@Inject ${repoClass} ${repoObject}")        
            imports.add("import uk.gov.ofwat.fountain.modelbuilder.repository.${repoClass}")
            importClasses.add("import uk.gov.ofwat.fountain.modelbuilder.domain.${className}")
            def template = """
modelParserService.${repoObject} = Stub(${repoClass}){
    save(_ as ${className}) >> { ${className} ${objectName} ->
        ${objectName}.id = Random.newInstance().nextLong()
        return ${objectName}
    }
    save(_ as HashSet<${className}>) >> { paramsArr ->
        HashSet<${className}> ${objectName}s = paramsArr[0]
        def ret${className}s = []
        Random random = Random.newInstance()
        ${objectName}s.each{ ${objectName} ->
            ${objectName}.id = random.nextLong()
            ret${className}s.add(${objectName})
        }
        return ret${className}s
    }
}
"""                     
            fout << template
        }
    }
    /*
    injects.each{
        println it
    }
    
    imports.each{
        println it
    }
    */
    importClasses.each{
        println it
    }
}

generateRepoStubs(f)
null