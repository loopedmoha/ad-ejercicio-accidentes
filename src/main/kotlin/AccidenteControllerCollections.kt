import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object AccidenteControllerCollections {

    fun accidentesHombres(accidentes: List<Accidente>) {
        val resultado = accidentes.filter { it.sexo == "Hombre" }.count()
        println("Accidentes de hombres:" + resultado)
    }

    fun accidentesCruces(accidentes: List<Accidente>) {
        val resultado = accidentes.filter { it.direccion.split("/").size >= 2 }.count()
        println(resultado)
    }

    fun loadFromCSV(): List<Accidente> {

        val file = File("data/2022_Accidentalidad.csv")
        if (!file.exists()) println("NO EXISTO")
        val accidentes = file.readLines()
            .drop(1)
            .map { it.split(";") }
            .map {
                it.map { field -> field.trim() }
                Accidente(
                    expediente = it[0],
                    fecha = getFecha(it[1]),
                    hora = getHora(it[2]),
                    direccion = it[3],
                    numero = it[4].toIntOrNull(),
                    codDistrito = it[5].toIntOrNull(),
                    distrito = it[6],
                    tipoAccidente = setTipoAccidente(it[7]),
                    clima = it[8],
                    vehiculo = it[9],
                    persona = getTipoPersona(it[10]),
                    sexo = it[12],
                    codLesividad = it[13].toIntOrNull() ?: 0,
                    lesividad = when (it[13]) {
                        "1" -> TipoLesividad.ATENCIONURGENCIAS
                        "2" -> TipoLesividad.INGRESO24
                        "3" -> TipoLesividad.INGRESOMAS24
                        "4" -> TipoLesividad.FALLECIDO
                        "5" -> TipoLesividad.AMBULATORIO
                        "6" -> TipoLesividad.CSINMEDIATO
                        "7" -> TipoLesividad.ASISTENCIAENSITIO
                        "14" -> TipoLesividad.SINASISTENCIA
                        else -> TipoLesividad.DESCONOCIDO
                    },
                    alcohol = it[17] != "N",
                    droga = (it[18].toIntOrNull() ?: 0) == 1


                )
            }
        return accidentes
    }

    private fun setTipoAccidente(s: String): TipoAccidente {
        return if(s.startsWith("Alcance")) TipoAccidente.ALCANCE
        else if(s.startsWith("Atropello")) TipoAccidente.ATROPELLO
        else if(s.startsWith("Caída")) TipoAccidente.CAIDA
        else if(s.startsWith("Choque")) TipoAccidente.CHOQUE
        else if(s.startsWith("Colisión")) TipoAccidente.COLISION
        else if(s.startsWith("Despeñamiento")) TipoAccidente.DESPENAMIENTO
        else if(s.startsWith("Otro")) TipoAccidente.OTRO
        else if(s.startsWith("Solo")) TipoAccidente.SALIDA
        else if(s.startsWith("Vuelco")) TipoAccidente.VUELCO
        else TipoAccidente.OTRO
    }
fun getTipoPersona(s : String) : TipoPersona{
    return if (s == "Conductor") TipoPersona.CONDUCTOR
    else if(s == "Pasajero") TipoPersona.PASAJERO
    else TipoPersona.PEATON

}
    private fun getFecha(s: String): LocalDate {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.parse(s, formatter)
    }

    private fun getHora(s: String): LocalTime {
        var hora = s
        hora = if (hora.length <= 7) "0$hora" else s
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        return LocalTime.parse(hora, formatter)
    }

    /*
    *


¿Cuantos ingresos son de algún tipo de colisión?
¿Cuantos ingresos son debido a un atropello? ¿Y fallecidos?
Agrupar los atropellos por barrrio de mayor número al menor
* */

    fun fallecidoDrogasAlcohol(accidentes: List<Accidente>){
        val resultado = accidentes.filter { it.alcohol || it.droga }.filter { it.lesividad == TipoLesividad.FALLECIDO }
        println("Numero de fallecidos en accidentes con alcohol, drogas o ambas")
        println(resultado.size)
    }

    fun barrioConMasFallecidos(accidentes: List<Accidente>){
        val resultado = accidentes.filter { it.lesividad == TipoLesividad.FALLECIDO }
            .groupBy { it.distrito }
            .maxBy { it.value.size }
        println("El barrio con más fallecidos es: ${resultado.key}")
    }
    fun barrioConMenosFallecidos(accidentes: List<Accidente>){
        val resultado = accidentes.filter { it.lesividad == TipoLesividad.FALLECIDO }
            .groupBy { it.distrito }
            .minBy { it.value.size }
        println("El barrio con menos fallecidos es: ${resultado.key}")
    }
    fun mesConMasIngresos(accidentes: List<Accidente>){
        //println(accidentes.filter { it.codLesividad in (2..3) }.groupBy { it.fecha.month }.forEach{ println("${it.key} -> ${it.value.size}") })
        val resultado = accidentes
            .filter { it.codLesividad in (2..3) }
            .groupBy { it.fecha.month }
            .maxBy { it.value.size }
        println("El mes con más ingresos es ${resultado.key} con  ${resultado.value.size} ingresados")
        //resultado.forEach{ println("${it.key}->${it.value.size}" )}
    }
    fun mesConMasFallecidos(accidentes: List<Accidente>){
        val resultado = accidentes
            .filter { it.codLesividad == 4}
            .groupBy { it.fecha.month }
            .maxBy { it.value.size }
        println("El mes con más fallecidos es ${resultado.key} con  ${resultado.value.size} fallecidos")
        //resultado.forEach{ println("${it.key}->${it.value.size}" )}
    }

    fun mediaIngresosMes(accidentes: List<Accidente>){
        val accidentesMes = accidentes.groupBy { it.fecha.month }
        val resultado = accidentes
            .filter { it.codLesividad in (2..3) }
            .groupBy { it.fecha.month }

        val cosa =resultado.keys zip resultado.map { it.value.size.toFloat() / accidentesMes.getValue(it.key).size.toFloat() }
        println("Media de ingresos por mes")
        cosa.forEach { println("${it.first} -> ${it.second}") }
    }
    fun accidentesConUnIngreso(accidentes: List<Accidente>){
        println("Los accidentes con al menos un ingreso son: " +
                "${
                    accidentes.filter { it.codLesividad in (2..3) }.count()
                }")
    }

    fun fallecidosPorBarrios(accidentes: List<Accidente>){
        println("Fallecidos por barrio:")
        val resultado = accidentes.filter { it.lesividad == TipoLesividad.FALLECIDO }.groupBy { it.distrito }

     resultado.forEach{ println("${it.key}->${it.value.size}" )}
    }

    fun fallecidosPorSexo(accidentes: List<Accidente>){
        println("Fallecidos por sexo:")
        val resultado = accidentes.filter { it.lesividad == TipoLesividad.FALLECIDO }.groupBy { it.sexo }

        resultado
            .forEach{
                println("${it.key}->${it.value.size}" ) }
    }
}