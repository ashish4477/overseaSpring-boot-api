package com.bearcode.ovf.model.common;

import com.bearcode.ovf.model.questionnaire.FlowType;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public enum FormValidationRulesType {

    WIZARD_FLOW,
    VOTER_ACCOUNT_CREATE,
    VOTER_ACCOUNT_UPDATE;

    private static final Map<FlowType, List<String>> wizardFlowRules = new EnumMap<>(FlowType.class);

    private static final List<String> voterAccountCreateFlowRules = Arrays.asList("voterHistory","currentAddress","gender",
            "votingAddress.county","votingAddress.zip","votingAddress.street1", "birthDate","birthMonth","birthYear","phone","phoneType","voterClassificationType");

    private static final List<String> voterAccountUpdateFlowRules = Arrays.asList("voterHistory","gender","votingAddress.county",
            "currentAddress.county", "currentAddress.state","currentAddress.zip","voterClassificationType","password","confirmPassword");
    static {
        wizardFlowRules.put(FlowType.RAVA, Arrays.asList("voterHistory","gender","votingAddress.county","currentAddress.county",
        "currentAddress.state","currentAddress.zip","confirmPassword"));
        wizardFlowRules.put(FlowType.FWAB, Arrays.asList("voterHistory","gender","votingAddress.county","currentAddress.county",
        "currentAddress.state","currentAddress.zip","confirmPassword"));

        wizardFlowRules.put(FlowType.DOMESTIC_REGISTRATION, Arrays.asList("voterHistory","voterType",
                "votingAddress.county","currentAddress","gender","confirmPassword", "voterClassificationType"));
        wizardFlowRules.put(FlowType.DOMESTIC_ABSENTEE, Arrays.asList("voterHistory","voterType",
                "votingAddress.county","currentAddress","gender","confirmPassword","voterClassificationType"));
    }

    public static List<String> getSkipFieldsForWizardFlow(FlowType flowType) {
        if (wizardFlowRules.containsKey(flowType)) {
            return wizardFlowRules.get(flowType);
        }
        return Collections.emptyList();
    }

    public static List<String> getSkipFields(FormValidationRulesType formType) {
        switch (formType) {
            case VOTER_ACCOUNT_CREATE:
                return voterAccountCreateFlowRules;
            case VOTER_ACCOUNT_UPDATE:
                return voterAccountUpdateFlowRules;
        }
        return Collections.emptyList();
    }
}
