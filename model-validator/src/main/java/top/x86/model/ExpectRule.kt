package top.x86.model

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class ExpectRule(
    /**
     * 验证规则的ID
     */
    val rules: Array<String> = [],
    /**
     * 验证分组的ID
     */
    val groups: Array<String> = ["default"]
)