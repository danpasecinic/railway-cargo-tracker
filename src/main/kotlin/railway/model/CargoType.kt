package railway.model

@JvmInline
value class CargoType(
    val value: Int,
) : Comparable<CargoType> {
    override fun compareTo(other: CargoType): Int = value.compareTo(other.value)

    override fun toString(): String = value.toString()
}
