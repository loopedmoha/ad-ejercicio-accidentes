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
                    tipoAccidente = it[7],
                    clima = it[8],
                    vehiculo = it[9],
                    persona = TipoPersona.valueOf(it[10].uppercase()),
                    sexo = it[12],
                    codLesividad = it[13].toIntOrNull(),
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
                    //droga = it[18] != "N"


                )
            }
        return accidentes
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
}