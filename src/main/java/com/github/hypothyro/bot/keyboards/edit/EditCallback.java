package com.github.hypothyro.bot.keyboards.edit;

public enum EditCallback {
    EDIT_NAME_MENU { @Override public String getPrfix() { return "EDIT_NAME_MENU"; } } ,
    EDIT_DOB_MENU { @Override public String getPrfix() { return "EDIT_DOB_MENU"; } } ,
    EDIT_GENDER_MENU { @Override public String getPrfix() { return "EDIT_GENDER_MENU"; } } ,
    EDIT_WEIGHT_MENU { @Override public String getPrfix() { return "EDIT_WEIGHT_MENU"; } } ,
    EDIT_IS_PREGNANT_MENU { @Override public String getPrfix() { return "EDIT_IS_PREGNANT_MENU"; } } ,
    EDIT_PRETREATMENT_MENU { @Override public String getPrfix() { return "EDIT_PRETREATMENT_MENU"; } } ,
    EDIT_PRETREATMENT_DRUG_MENU { @Override public String getPrfix() { return "EDIT_PRETREATMENT_DRUG_MENU"; } } ,
    EDIT_OPERATION_MENU { @Override public String getPrfix() { return "EDIT_OPERATION_MENU"; } } ,
    EDIT_OPERATION_DATE_MENU { @Override public String getPrfix() { return "EDIT_OPERATION_DATE_MENU"; } } ,
    EDIT_TREATMENT_MENU { @Override public String getPrfix() { return "EDIT_TREATMENT_MENU"; } } ,
    EDIT_TREATMENT_DRUG_MENU { @Override public String getPrfix() { return "EDIT_TREATMENT_DRUG_MENU"; } } ,
    EDIT_PATHOLOGY_MENU { @Override public String getPrfix() { return "EDIT_PATHOLOGY_MENU"; } } ,


    EDIT_GENDER_MALE { @Override public String getPrfix() { return "EDIT_GENDER"; } } ,
    EDIT_GENDER_FEMALE { @Override public String getPrfix() { return "EDIT_GENDER"; } },
    EDIT_GENDER_OTHER { @Override public String getPrfix() { return "EDIT_GENDER"; } },

    EDIT_PREGNANT_Y { @Override public String getPrfix() { return "EDIT_PREGNANT"; } },
    EDIT_PREGNANT_N { @Override public String getPrfix() { return "EDIT_PREGNANT"; } },

    EDIT_PRETREATMENT_NAME_EUTIROX { @Override public String getPrfix() { return "EDIT_PRETREATMENT_NAME"; } },
    EDIT_PRETREATMENT_NAME_L_TYROXIN { @Override public String getPrfix() { return "EDIT_PRETREATMENT_NAME"; } },
    EDIT_PRETREATMENT_NAME_OTHER { @Override public String getPrfix() { return "EDIT_PRETREATMENT_NAME"; } },
    EDIT_PRETREATMENT_NAME_NOTHING { @Override public String getPrfix() { return "EDIT_PRETREATMENT_NAME"; } },

    EDIT_TREATMENT_NAME_EUTIROX { @Override public String getPrfix() { return "EDIT_TREATMENT_NAME"; } },
    EDIT_TREATMENT_NAME_L_TYROXIN { @Override public String getPrfix() { return "EDIT_TREATMENT_NAME"; } },
    EDIT_TREATMENT_NAME_OTHER { @Override public String getPrfix() { return "EDIT_TREATMENT_NAME"; } },
    EDIT_TREATMENT_NAME_NOTHING { @Override public String getPrfix() { return "EDIT_TREATMENT_NAME"; } },

    EDIT_OP_ALL { @Override public String getPrfix() { return "EDIT_OP"; } },
    EDIT_OP_HALF { @Override public String getPrfix() { return "EDIT_OP"; } },
    EDIT_OP_ISTHMUS { @Override public String getPrfix() { return "EDIT_OP"; } },
    EDIT_OP_REMAINDER { @Override public String getPrfix() { return "EDIT_OP"; } },
    EDIT_OP_OTHER { @Override public String getPrfix() { return "EDIT_OP"; } },

    EDIT_PATHOLOGY_1 { @Override public String getPrfix() { return "EDIT_PATHOLOGY"; } },
    EDIT_PATHOLOGY_2 { @Override public String getPrfix() { return "EDIT_PATHOLOGY"; } },
    EDIT_PATHOLOGY_3 { @Override public String getPrfix() { return "EDIT_PATHOLOGY"; } },
    EDIT_PATHOLOGY_4 { @Override public String getPrfix() { return "EDIT_PATHOLOGY"; } },
    EDIT_PATHOLOGY_5 { @Override public String getPrfix() { return "EDIT_PATHOLOGY"; } },


    EDIT_SAME_DRUG_YES { @Override public String getPrfix() { return "EDIT_SAME_DRUG"; } },
    EDIT_SAME_DRUG_NO { @Override public String getPrfix() { return "EDIT_SAME_DRUG"; } },

    EDIT_TTG_YES { @Override public String getPrfix() { return "EDIT_TTG"; } },
    EDIT_TTG_NO { @Override public String getPrfix() { return "EDIT_TTG"; } },

    EDIT_FINISH { @Override public String getPrfix() { return "EDIT_FINISH"; } },
    EDIT_REWORK { @Override public String getPrfix() { return "EDIT_REWORK"; } },

    EDIT_PATIENT_TTG_YES { @Override public String getPrfix() { return "EDIT_PATIENT_TTG"; } },
    EDIT_PATIENT_TTG_NO { @Override public String getPrfix() { return "EDIT_PATIENT_TTG"; } },

    EDIT_RAD_YES { @Override public String getPrfix() { return "EDIT_RAD"; } },
    EDIT_RAD_NO { @Override public String getPrfix() { return "EDIEDIT_RAD"; } },
    ;

    public abstract String getPrfix();
}
