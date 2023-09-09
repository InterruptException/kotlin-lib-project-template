package top.x86.model

class ValidateRule(
    override val ruleId: String,
    override val description: String,
    private val handler: (String?)-> ValidateMessage?
): IRuleHandler {
    override fun validate(propValue: String?): ValidateMessage? {
        return handler.invoke(propValue)
    }
}