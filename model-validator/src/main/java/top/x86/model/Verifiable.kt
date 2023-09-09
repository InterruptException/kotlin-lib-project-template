package top.x86.model

interface Verifiable {
    fun validate() : Set<String> {
        return DataValidator.validate(this).map { it.propertyName }.toSet()
    }

    fun verify() : Map<String, String> {
        return DataValidator.validate(this)
            .associate { Pair(it.propertyName, it.messageList.joinToString(",")) }
    }

    fun validate(properties: Set<String>): Set<String> {
        return DataValidator.validate(this, properties = properties).map { it.propertyName }.toSet()
    }
}