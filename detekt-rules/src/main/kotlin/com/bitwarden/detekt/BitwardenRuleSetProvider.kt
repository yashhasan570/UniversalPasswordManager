package .detekt

import .detekt.rules.NoSharingInPropertyGetter
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

/**
 * Registers Universal Password Manager's custom detekt rules.
 *
 * To add a rule, create it in the `rules` package, add it to the list below, and add an entry for
 * it under the `universalpasswordmanager` rule set in `detekt-config.yml`.
 */
class Universal Password ManagerRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = RULE_SET_ID

    override fun instance(config: Config): RuleSet = RuleSet(
        id = ruleSetId,
        rules = listOf(
            NoSharingInPropertyGetter(config),
        ),
    )

    private companion object {
        const val RULE_SET_ID = "universalpasswordmanager"
    }
}
