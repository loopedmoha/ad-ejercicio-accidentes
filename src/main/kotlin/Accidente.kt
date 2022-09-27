import java.time.LocalDate
import java.time.LocalTime

data class Accidente(
    val expediente: String,
    val fecha: LocalDate,
    val hora: LocalTime,
    val direccion: String,
    val numero: Int?,
    val codDistrito: Int?,
    val distrito: String,
    val tipoAccidente: String,
    val clima: String,
    val vehiculo: String,
    val persona: String,
    val sexo: String,
    val codLesividad: Int?,
    val lesividad: String,
    val alcohol: Boolean
    //val droga: Boolean
    )


