package com.github.hypothyro.bot.keyboards.registration;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import lombok.Getter;

@Component
@Getter
public class RegistrationKeyboards {

    private final InlineKeyboardMarkup verifyRegistrationFinishKeyboard = constructRegistrationKeyboard();
    private final InlineKeyboardMarkup pregnantRegistrationKeyboard = constructPregnantRegistrationKeyboard();
    private final InlineKeyboardMarkup genderRegistrationKeyboard = constructGenderRegistrationKeyboard();
    private final InlineKeyboardMarkup drugRegistrationKeyboard = constructDrugRegistrationKeyboard();
    private final InlineKeyboardMarkup operationRegistrationKeyboard = constructOperationRegistrationKeyboard();
    private final InlineKeyboardMarkup pathologyKeyboard = constructPathologyKeyboard();
    private final InlineKeyboardMarkup sameDrugRegistrationKeyboard = constructSameDrugRegistrationKeyboard();
    private final InlineKeyboardMarkup ttgKeyboard = constructTtgKeyboard();
    private final InlineKeyboardMarkup patientTtgKeyboard = constructPatientTtgKeyboard();
    private final InlineKeyboardMarkup patientLolTtgKeyboard = constructPatientLolTtgKeyboard();


    private InlineKeyboardMarkup constructRegistrationKeyboard() {
        InlineKeyboardMarkup registrationKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Подтвердить данные");
        btn1.setCallbackData(RegistrationCallback.REGISTRATION_FINISH.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("Изменить данные");
        btn2.setCallbackData(RegistrationCallback.REGISTRATION_REWORK.toString());

        row1.add(btn1);
        row2.add(btn2);

        rowsInline.add(row1);
        rowsInline.add(row2);

        registrationKeyboard.setKeyboard(rowsInline);

        return registrationKeyboard;
    }

    private InlineKeyboardMarkup constructPregnantRegistrationKeyboard() {
        InlineKeyboardMarkup pregnantKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Да");
        btn1.setCallbackData(RegistrationCallback.REGISTRATION_PREGNANT_Y.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("Нет");
        btn2.setCallbackData(RegistrationCallback.REGISTRATION_PREGNANT_N.toString());

        row1.add(btn1);
        row2.add(btn2);

        rowsInline.add(row1);
        rowsInline.add(row2);

        pregnantKeyboard.setKeyboard(rowsInline);

        return pregnantKeyboard;
    }

    private InlineKeyboardMarkup constructGenderRegistrationKeyboard() {
        InlineKeyboardMarkup genderKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        List<InlineKeyboardButton> row3 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Мужской");
        btn1.setCallbackData(RegistrationCallback.REGISTRATION_GENDER_MALE.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("Женский");
        btn2.setCallbackData(RegistrationCallback.REGISTRATION_GENDER_FEMALE.toString());

        InlineKeyboardButton btn3 = new InlineKeyboardButton();
        btn3.setText("Отказываюсь отвечать");
        btn3.setCallbackData(RegistrationCallback.REGISTRATION_GENDER_OTHER.toString());

        row1.add(btn1);
        row2.add(btn2);
        row3.add(btn3);

        rowsInline.add(row1);
        rowsInline.add(row2);
        rowsInline.add(row3);

        genderKeyboard.setKeyboard(rowsInline);

        return genderKeyboard;
    }

    private InlineKeyboardMarkup constructDrugRegistrationKeyboard() {
        InlineKeyboardMarkup genderKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        List<InlineKeyboardButton> row3 = new LinkedList<>();
        List<InlineKeyboardButton> row4 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Эутирокс");
        btn1.setCallbackData(RegistrationCallback.REGISTRATION_PRETREATMENT_NAME_EUTIROX.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("L-тироксин");
        btn2.setCallbackData(RegistrationCallback.REGISTRATION_PRETREATMENT_NAME_L_TYROXIN.toString());

        InlineKeyboardButton btn3 = new InlineKeyboardButton();
        btn3.setText("Другой");
        btn3.setCallbackData(RegistrationCallback.REGISTRATION_PRETREATMENT_NAME_OTHER.toString());

        InlineKeyboardButton btn4 = new InlineKeyboardButton();
        btn4.setText("Не было назначено препарата");
        btn4.setCallbackData(RegistrationCallback.REGISTRATION_PRETREATMENT_NAME_NOTHING.toString());

        row1.add(btn1);
        row2.add(btn2);
        row3.add(btn3);
        row4.add(btn4);

        rowsInline.add(row1);
        rowsInline.add(row2);
        rowsInline.add(row3);
        rowsInline.add(row4);

        genderKeyboard.setKeyboard(rowsInline);

        return genderKeyboard;
    }

    private InlineKeyboardMarkup constructOperationRegistrationKeyboard() {
        InlineKeyboardMarkup genderKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        List<InlineKeyboardButton> row3 = new LinkedList<>();
        List<InlineKeyboardButton> row4 = new LinkedList<>();
        List<InlineKeyboardButton> row5 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Да");
        btn1.setCallbackData(RegistrationCallback.REGISTRATION_OP_ALL.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("Половина");
        btn2.setCallbackData(RegistrationCallback.REGISTRATION_OP_HALF.toString());

        InlineKeyboardButton btn3 = new InlineKeyboardButton();
        btn3.setText("Перешеек");
        btn3.setCallbackData(RegistrationCallback.REGISTRATION_OP_ISTHMUS.toString());

        InlineKeyboardButton btn4 = new InlineKeyboardButton();
        btn4.setText("Оставлен остаток доли");
        btn4.setCallbackData(RegistrationCallback.REGISTRATION_OP_REMAINDER.toString());

        InlineKeyboardButton btn5 = new InlineKeyboardButton();
        btn5.setText("Затрудняюсь ответить");
        btn5.setCallbackData(RegistrationCallback.REGISTRATION_OP_OTHER.toString());

        row1.add(btn1);
        row2.add(btn2);
        row3.add(btn3);
        row4.add(btn4);
        row5.add(btn5);

        rowsInline.add(row1);
        rowsInline.add(row2);
        rowsInline.add(row3);
        rowsInline.add(row4);
        rowsInline.add(row5);

        genderKeyboard.setKeyboard(rowsInline);

        return genderKeyboard;
    }

    private InlineKeyboardMarkup constructPathologyKeyboard() {
        InlineKeyboardMarkup genderKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        List<InlineKeyboardButton> row3 = new LinkedList<>();
        List<InlineKeyboardButton> row4 = new LinkedList<>();
        List<InlineKeyboardButton> row5 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Фолликулярная аденома/ узловой нетоксический зоб");
        btn1.setCallbackData(RegistrationCallback.REGISTRATION_PATHOLOGY_1.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("ПоловинаТиреоидит Хашимото / диффузный токсический зоб");
        btn2.setCallbackData(RegistrationCallback.REGISTRATION_PATHOLOGY_2.toString());

        InlineKeyboardButton btn3 = new InlineKeyboardButton();
        btn3.setText("ПерешеекПапиллярная/фолликулярная карцинома");
        btn3.setCallbackData(RegistrationCallback.REGISTRATION_PATHOLOGY_3.toString());

        InlineKeyboardButton btn4 = new InlineKeyboardButton();
        btn4.setText("Оставлен остаток долиМедуллярная карцинома");
        btn4.setCallbackData(RegistrationCallback.REGISTRATION_PATHOLOGY_4.toString());

        InlineKeyboardButton btn5 = new InlineKeyboardButton();
        btn5.setText("Другое");
        btn5.setCallbackData(RegistrationCallback.REGISTRATION_PATHOLOGY_5.toString());

        row1.add(btn1);
        row2.add(btn2);
        row3.add(btn3);
        row4.add(btn4);
        row5.add(btn5);

        rowsInline.add(row1);
        rowsInline.add(row2);
        rowsInline.add(row3);
        rowsInline.add(row4);
        rowsInline.add(row5);

        genderKeyboard.setKeyboard(rowsInline);

        return genderKeyboard;
    }

    private InlineKeyboardMarkup constructSameDrugRegistrationKeyboard() {
        InlineKeyboardMarkup registrationKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Да");
        btn1.setCallbackData(RegistrationCallback.REGISTRATION_SAME_DRUG_YES.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("Нет");
        btn2.setCallbackData(RegistrationCallback.REGISTRATION_SAME_DRUG_NO.toString());

        row1.add(btn1);
        row2.add(btn2);

        rowsInline.add(row1);
        rowsInline.add(row2);

        registrationKeyboard.setKeyboard(rowsInline);

        return registrationKeyboard;
    }

    private InlineKeyboardMarkup constructTtgKeyboard() {
        InlineKeyboardMarkup pregnantKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Да");
        btn1.setCallbackData(RegistrationCallback.REGISTRATION_TTG_YES.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("Нет");
        btn2.setCallbackData(RegistrationCallback.REGISTRATION_TTG_NO.toString());

        row1.add(btn1);
        row2.add(btn2);

        rowsInline.add(row1);
        rowsInline.add(row2);

        pregnantKeyboard.setKeyboard(rowsInline);

        return pregnantKeyboard;
    }

    private InlineKeyboardMarkup constructPatientTtgKeyboard() {
        InlineKeyboardMarkup pregnantKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Да");
        btn1.setCallbackData(RegistrationCallback.REGISTRATION_PATIENT_TTG_YES.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("Нет");
        btn2.setCallbackData(RegistrationCallback.REGISTRATION_PATIENT_TTG_NO.toString());

        row1.add(btn1);
        row2.add(btn2);

        rowsInline.add(row1);
        rowsInline.add(row2);

        pregnantKeyboard.setKeyboard(rowsInline);

        return pregnantKeyboard;
    }

    private InlineKeyboardMarkup constructPatientLolTtgKeyboard() {
        InlineKeyboardMarkup pregnantKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Да");
        btn1.setCallbackData(RegistrationCallback.REGISTRATION_RAD_YES.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("Нет");
        btn2.setCallbackData(RegistrationCallback.REGISTRATION_RAD_NO.toString());

        row1.add(btn1);
        row2.add(btn2);

        rowsInline.add(row1);
        rowsInline.add(row2);

        pregnantKeyboard.setKeyboard(rowsInline);

        return pregnantKeyboard;
    }
}

