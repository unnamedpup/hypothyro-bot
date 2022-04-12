package com.github.hypothyro.bot.keyboards.registration;

public enum RegistrationCallback {
    REGISTRATION_GENDER_MALE { @Override public String getPrfix() { return "REGISTRATION_GENDER"; } } ,
    REGISTRATION_GENDER_FEMALE { @Override public String getPrfix() { return "REGISTRATION_GENDER"; } },
    REGISTRATION_GENDER_OTHER { @Override public String getPrfix() { return "REGISTRATION_GENDER"; } },

    REGISTRATION_PREGNANT_Y { @Override public String getPrfix() { return "REGISTRATION_PREGNANT"; } },
    REGISTRATION_PREGNANT_N { @Override public String getPrfix() { return "REGISTRATION_PREGNANT"; } },

    REGISTRATION_PRETREATMENT_NAME_EUTIROX { @Override public String getPrfix() { return "REGISTRATION_PRETREATMENT_NAME"; } },
    REGISTRATION_PRETREATMENT_NAME_L_TYROXIN { @Override public String getPrfix() { return "REGISTRATION_PRETREATMENT_NAME"; } },
    REGISTRATION_PRETREATMENT_NAME_OTHER { @Override public String getPrfix() { return "REGISTRATION_PRETREATMENT_NAME"; } },
    REGISTRATION_PRETREATMENT_NAME_NOTHING { @Override public String getPrfix() { return "REGISTRATION_PRETREATMENT_NAME"; } },

    REGISTRATION_OP_ALL { @Override public String getPrfix() { return "REGISTRATION_OP"; } },
    REGISTRATION_OP_HALF { @Override public String getPrfix() { return "REGISTRATION_OP"; } },
    REGISTRATION_OP_ISTHMUS { @Override public String getPrfix() { return "REGISTRATION_OP"; } },
    REGISTRATION_OP_REMAINDER { @Override public String getPrfix() { return "REGISTRATION_OP"; } },
    REGISTRATION_OP_OTHER { @Override public String getPrfix() { return "REGISTRATION_OP"; } },

    REGISTRATION_PATHOLOGY_1 { @Override public String getPrfix() { return "REGISTRATION_PATHOLOGY"; } },
    REGISTRATION_PATHOLOGY_2 { @Override public String getPrfix() { return "REGISTRATION_PATHOLOGY"; } },
    REGISTRATION_PATHOLOGY_3 { @Override public String getPrfix() { return "REGISTRATION_PATHOLOGY"; } },
    REGISTRATION_PATHOLOGY_4 { @Override public String getPrfix() { return "REGISTRATION_PATHOLOGY"; } },
    REGISTRATION_PATHOLOGY_5 { @Override public String getPrfix() { return "REGISTRATION_PATHOLOGY"; } },


    REGISTRATION_SAME_DRUG_YES { @Override public String getPrfix() { return "REGISTRATION_SAME_DRUG"; } },
    REGISTRATION_SAME_DRUG_NO { @Override public String getPrfix() { return "REGISTRATION_SAME_DRUG"; } },

    REGISTRATION_TTG_YES { @Override public String getPrfix() { return "REGISTRATION_TTG"; } },
    REGISTRATION_TTG_NO { @Override public String getPrfix() { return "REGISTRATION_TTG"; } },

    REGISTRATION_FINISH { @Override public String getPrfix() { return "REGISTRATION_FINISH"; } },
    REGISTRATION_REWORK { @Override public String getPrfix() { return "REGISTRATION_REWORK"; } },

    REGISTRATION_PATIENT_TTG_YES { @Override public String getPrfix() { return "REGISTRATION_PATIENT_TTG"; } },
    REGISTRATION_PATIENT_TTG_NO { @Override public String getPrfix() { return "REGISTRATION_PATIENT_TTG"; } },

    REGISTRATION_RAD_YES { @Override public String getPrfix() { return "REGISTRATION_RAD"; } },
    REGISTRATION_RAD_NO { @Override public String getPrfix() { return "REGISTRATION_RAD"; } },

    CONTROL_PRETREATMENT_NAME_EUTIROX { @Override public String getPrfix() { return "CONTROL_PRETREATMENT_NAME"; } },
    CONTROL_PRETREATMENT_NAME_L_TYROXIN { @Override public String getPrfix() { return "CONTROL_PRETREATMENT_NAME"; } },
    CONTROL_PRETREATMENT_NAME_OTHER { @Override public String getPrfix() { return "CONTROL_PRETREATMENT_NAME"; } },
    CONTROL_PRETREATMENT_NAME_NOTHING { @Override public String getPrfix() { return "CONTROL_PRETREATMENT_NAME"; } },
    ;

    public abstract String getPrfix();
}
