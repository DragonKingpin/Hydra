package com.walnut.odin.patrol;

import com.pinecone.framework.system.prototype.Pinenut;

public class PatrolWatchdogRuleSnapshot implements Pinenut {

    protected String  mszRuleCode;
    protected String  mszRuleName;
    protected String  mszTargetType;
    protected boolean mbSystemRule;
    protected boolean mbEnabled;
    protected String  mszDescription;

    public String getRuleCode() {
        return this.mszRuleCode;
    }

    public void setRuleCode( String ruleCode ) {
        this.mszRuleCode = ruleCode;
    }

    public String getRuleName() {
        return this.mszRuleName;
    }

    public void setRuleName( String ruleName ) {
        this.mszRuleName = ruleName;
    }

    public String getTargetType() {
        return this.mszTargetType;
    }

    public void setTargetType( String targetType ) {
        this.mszTargetType = targetType;
    }

    public boolean isSystemRule() {
        return this.mbSystemRule;
    }

    public void setSystemRule( boolean systemRule ) {
        this.mbSystemRule = systemRule;
    }

    public boolean isEnabled() {
        return this.mbEnabled;
    }

    public void setEnabled( boolean enabled ) {
        this.mbEnabled = enabled;
    }

    public String getDescription() {
        return this.mszDescription;
    }

    public void setDescription( String description ) {
        this.mszDescription = description;
    }
}
