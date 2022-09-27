fun main(args: Array<String>) {
    val lista = AccidenteControllerCollections.loadFromCSV()
    AccidenteControllerCollections.accidentesHombres(lista)
    AccidenteControllerCollections.accidentesCruces(lista)
    
}