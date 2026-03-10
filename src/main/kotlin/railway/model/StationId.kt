package railway.model

@JvmInline
value class StationId(
    val value: Int,
) : Comparable<StationId> {
    override fun compareTo(other: StationId): Int = value.compareTo(other.value)

    override fun toString(): String = value.toString()
}
