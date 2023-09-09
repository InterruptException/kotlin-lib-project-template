package top.x86.model

class ValidateRulesBuilder (
    val rules: MutableMap<String, IRuleHandler> = mutableMapOf()
)

fun ValidateRulesBuilder.defineRule(ruleId: String, description: String = "",
                                    validate: (String?)-> ValidateMessage?): ValidateRulesBuilder {
    assert(!rules.contains(ruleId)) {
        "以下规则ID重复：$ruleId"
    }
    rules[ruleId] = ValidateRule(ruleId, description, validate)
    return this
}

fun ValidateRulesBuilder.defineRule(ruleId: String, description: String = "",
                                    regexPattern: String, isNullable: Boolean = false): ValidateRulesBuilder {
    assert(!rules.contains(ruleId)) {
        "以下规则ID重复：$ruleId"
    }
    rules[ruleId] = RegexValidateRule(ruleId, description, regexPattern, isNullable)
    return this
}

fun ValidateRulesBuilder.findRuleHandlers(ruleIdList: Array<String>) : List<IRuleHandler> {
    return rules.filter { ruleIdList.contains(it.key) }.map { it.value }
}
