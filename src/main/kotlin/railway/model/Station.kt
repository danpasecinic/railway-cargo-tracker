package railway.model

data class Station(
    val id: StationId,
    val unloadCargo: CargoType,
    val loadCargo: CargoType,
)
