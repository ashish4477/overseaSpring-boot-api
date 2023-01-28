package com.bearcode.ovf.model.questionnaire;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FlowTypeTest {

    @Test
    public void isValidFormType_valid_form_type_rava() {
        // given
        String inputFormType = "rava";

        // when
        boolean result = FlowType.isValidPageSlug(inputFormType);

        // then
        assertTrue(result);
    }

    @Test
    public void isValidFormType_valid_form_type_domestic() {
        // given
        String inputFormType = "domestic_registration";

        // when
        boolean result = FlowType.isValidPageSlug(inputFormType);

        // then
        assertTrue(result);
    }

    @Test
    public void isValidFormType_invalid_form_type() {
        // given
        String inputFormType = "invalid";

        // when
        boolean result = FlowType.isValidPageSlug(inputFormType);

        // then
        assertFalse(result);
    }
}
