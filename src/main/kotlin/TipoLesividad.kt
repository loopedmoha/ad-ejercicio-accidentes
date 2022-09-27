enum class TipoLesividad(val cod : Int, val gravedad : Gravedad) {
    ATENCIONURGENCIAS(1, Gravedad.LEVE),
    INGRESO24(2, Gravedad.LEVE),
    INGRESOMAS24(3, Gravedad.GRAVE),
    FALLECIDO(4, Gravedad.FALLECIDO),
    AMBULATORIO(5, Gravedad.LEVE),
    CSINMEDIATO(6, Gravedad.LEVE),
    ASISTENCIAENSITIO(7, Gravedad.LEVE),
    SINASISTENCIA(14, Gravedad.LEVE),
    DESCONOCIDO(77, Gravedad.LEVE)

}

enum class Gravedad {
LEVE, GRAVE, FALLECIDO
}
