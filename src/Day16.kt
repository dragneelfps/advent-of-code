object Day16 : Problem<Day16.Inp, Long> {
    data class Inp(val bin: String)

    override val day: Int = 16
    override val parts: List<Part<Inp, Long>> = listOf(::part1, ::part2)

    override fun parse(rawInput: Sequence<String>): Inp {
        val line = rawInput.first()
        val bin = line.map { it.digitToInt(16).toString(2).padStart(4, '0') }
            .joinToString("")
//            .also { println(it) }
        return Inp(bin)
    }

    var packetVersionSum = 0L
//    var idx = 'A'

    data class PacketParseResult(val bitsRead: Long, val packetValue: Long)

    fun CharIterator.read(n: Int): String {
        return buildString {
            repeat(n) {
                if (hasNext()) {
                    append(next())
//                    print(idx)
                } else {
                    throw IllegalStateException("No enough chars left: $n")
                }
            }
//            idx++
        }
    }

    fun String.binaryToDec(): Long = try { toLong(2) } catch (e: Exception) { println("Errored binToDec: $this"); throw e}

    fun parsePacketMain(bin: CharIterator): PacketParseResult {
        val parseResult = parsePacket(bin)
        val ignoredBits = (4 - (parseResult.bitsRead) % 4).takeIf { it != 4L } ?: 0L
        return parseResult.copy(bitsRead = parseResult.bitsRead + ignoredBits)
    }

    fun parsePacket(bin: CharIterator): PacketParseResult {
        val packetVersion = bin.read(3).binaryToDec().also { packetVersionSum += it }
        val packetId = bin.read(3).binaryToDec()
        val parseResult = when (packetId) {
            4L -> parseLiteral(bin)
            else -> parseOperator(bin, packetId)
        }
        return parseResult.copy(bitsRead = parseResult.bitsRead + 3 + 3)
    }

    fun parseOperator(bin: CharIterator, op: Long): PacketParseResult {
        val lengthTypeId = bin.read(1)
        val parseResult = when(lengthTypeId) {
            "0" -> parseOperatorOfType0(bin, op)
            "1" -> parseOperatorOfType1(bin, op)
            else -> throw Exception("Invalid lengthTypeId: $lengthTypeId")
        }
        return parseResult.copy(bitsRead = parseResult.bitsRead + 1)
    }

    private fun parseOperatorOfType1(bin: CharIterator, op: Long): PacketParseResult {
        val numOfSubPackets = bin.read(11).binaryToDec()
        val subPacketResult = (1..numOfSubPackets).fold(null as PacketParseResult?) { resultAcc, _ ->
            val subParseResult = parsePacket(bin)
            if (resultAcc == null) {
                subParseResult
            } else {
                combine(resultAcc, subParseResult, op)
            }
        } ?: throw Exception("There should be some packet result!!")
        return subPacketResult.copy(bitsRead = subPacketResult.bitsRead + 11)
    }

    private fun parseOperatorOfType0(bin: CharIterator, op: Long): PacketParseResult {
        val totalLengthInBits = bin.read(15).binaryToDec()
        var subPacketTotalResult: PacketParseResult? = null
        while( (subPacketTotalResult?.bitsRead ?: 0) < totalLengthInBits) {
            val parseResult = parsePacket(bin)
            subPacketTotalResult = if (subPacketTotalResult == null) {
                parseResult
            } else {
                combine(subPacketTotalResult, parseResult, op)
            }
        }
        requireNotNull(subPacketTotalResult)
        require(totalLengthInBits == subPacketTotalResult.bitsRead) { "total: $totalLengthInBits, bitsRead: ${subPacketTotalResult.bitsRead}" }
        return PacketParseResult(
            15 + subPacketTotalResult.bitsRead,
            subPacketTotalResult.packetValue
        )
    }


    fun parseLiteral(bin: CharIterator): PacketParseResult {
        var bitsRead = 0L
        val packetValue = buildString {
            while (true) {
                val groupIndicator = bin.read(1)
                val group = bin.read(4)
                append(group)
                bitsRead += 5
                if (groupIndicator == "0")
                    break
            }
        }.binaryToDec()
        return PacketParseResult(bitsRead, packetValue)
    }

    fun combine(p1: PacketParseResult, p2: PacketParseResult, op: Long) = PacketParseResult(
        bitsRead = p1.bitsRead + p2.bitsRead,
        packetValue = when(op) {
            0L -> p1.packetValue + p2.packetValue
            1L -> p1.packetValue * p2.packetValue
            2L -> minOf(p1.packetValue, p2.packetValue)
            3L -> maxOf(p1.packetValue, p2.packetValue)
            5L -> if(p1.packetValue > p2.packetValue) 1 else 0
            6L -> if(p1.packetValue < p2.packetValue) 1 else 0
            7L -> if(p1.packetValue == p2.packetValue) 1 else 0
            else -> throw Exception("Invalid operator type: $op")
        }
    )

    fun part1(inp: Inp): Long {
        packetVersionSum = 0
        parsePacket(inp.bin.iterator())
        return packetVersionSum
    }

    fun part2(inp: Inp): Long {
        return parsePacketMain(inp.bin.iterator()).packetValue
    }


}

