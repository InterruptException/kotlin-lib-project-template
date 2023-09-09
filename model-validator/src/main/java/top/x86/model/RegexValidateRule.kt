package top.x86.model

class RegexValidateRule(
    override val ruleId: String,
    override val description: String,
    regexPattern: String,
    private val isNullable: Boolean,
): IRuleHandler {
    private val regex = Regex(regexPattern)
    override fun validate(propValue: String?): ValidateMessage? {
        return if (propValue != null) {
            if (regex.matches(propValue)) {
                null
            } else {
                ValidateMessage("The value of property is $propValue, but it doesn't match the regex pattern (${regex.pattern})")
            }
        } else {
            if (isNullable) {
                null
            } else {
                ValidateMessage("The value of property can not be null")
            }
        }
    }
}