package top.x86.model

import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers


object DataValidator {
    private val validateRules = ValidateRulesBuilder()
        .defineRule(Rules.PhoneNumber, "手机号", "(^[1-9][0-9]{9}$)")
        .defineRule(Rules.ContactPhoneNumber, "联系人手机号", "^(\\+?)[0-9]+")
        .defineRule(Rules.OtpCode, "短信验证码", "^[0-9a-zA-Z]{6,}$")
        .defineRule(Rules.Password, "密码", "^[0-9a-zA-Z]{8,20}$")
        .defineRule(Rules.PinCode, "PIN", "^[0-9]{6}$")
        .defineRule(Rules.AreaCode, "国际区号", "^([0+])[0-9]{2}$")
        .defineRule(Rules.PersonName, "姓名", "^\\S+(\\s+\\S*)*\$")
        .defineRule(Rules.EmailAddress, "电子邮箱地址", "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$")
        .defineRule(Rules.NotBlank, "非空值") {
            if (it.isNullOrBlank()) {
                ValidateMessage("Can't be blank")
            } else {
                null
            }
        }
        .defineRule(Rules.Address, "地址") {
            if (it.isNullOrBlank()) {
                ValidateMessage("Can't be blank")
            } else {
                null
            }
        }
        .defineRule(Rules.CompanyName, "公司名" ){
            if (it.isNullOrBlank()) {
                ValidateMessage("Can't be blank")
            } else {
                null
            }
        }
        .defineRule(Rules.EnumId, "枚举ID", "^[0-9]{1,}$")
        .defineRule(Rules.IdCardNumber, "身份证", "^[0-9]{12}$")
        //.defineRule(Rules.TaxCardNumber, "税卡", "^[a-zA-Z]{5}[0-9]{4}[a-zA-Z]")
        .defineRule(Rules.TaxCardNumber, "税卡", "^[a-zA-Z0-9]{10}$")
        .defineRule(Rules.IFSC, "IFSC", "^[a-zA-Z0-9]{11}")
        .defineRule(Rules.BankAccountNumber, "银行卡号", "^[a-zA-Z0-9]{1,50}")
        .defineRule(Rules.Checked, "已选中", "^true$")

    private fun validateProperty(receiver: Any, property: KProperty<*>, ruleIdList: Array<String>): ValidateResult? {
        val propValue = property.getter.call(receiver) as? String
        val msgList = validateRules.findRuleHandlers(ruleIdList).map { it.validate(propValue) }.filterNotNull()
        return if (msgList.isEmpty()) {
            null
        } else {
            ValidateResult(
                messageList = msgList,
                propertyName = property.name
            )
        }
    }

    fun validate(obj: Any, groupId: String = "default", properties: Set<String> = emptySet()) : Set<ValidateResult> {
        return obj::class.declaredMembers.asSequence()
            .filterIsInstance<KProperty<*>>()
            .filter {
                if (properties.isNotEmpty()) {
                    properties.contains(it.name)
                } else {
                    true
                }
            }
            .filter { prop-> //过滤出符合指定验证组的属性集
                prop.annotations.filter { it.annotationClass == ExpectRule::class }
                    .map { it as ExpectRule }
                    .any { rule -> rule.groups.contains(groupId) }
            }
            .map { prop -> //对属性进行验证，映射成对应的验证结果
                prop.annotations.asSequence()
                    .filter { it.annotationClass == ExpectRule::class }
                    .map { ann -> ann as ExpectRule }
                    .filter { rule -> rule.groups.contains(groupId) }
                    .map { rule ->
                        validateProperty(obj, prop, rule.rules)
                    }.singleOrNull()
            }.filterNotNull().toSet()
    }
}