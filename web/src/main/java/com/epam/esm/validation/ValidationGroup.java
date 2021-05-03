package com.epam.esm.validation;

import javax.validation.groups.Default;

public class ValidationGroup {

    public static interface Update extends Default {
    }

    public static interface Create extends Default {
    }
}
