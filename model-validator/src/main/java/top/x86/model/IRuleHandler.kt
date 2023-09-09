package top.x86.model

interface IRuleHandler {
    val ruleId: String
    val description: String
    fun validate (propValue: String?): ValidateMessage?
}