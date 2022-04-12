package com.github.hypothyro.bot.keyboards.edit;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.hypothyro.domain.Patient;
import com.github.hypothyro.service.impl.DefaultDateChecker;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EditKeyboard {

    public final InlineKeyboardMarkup editPregnantKeyboard = constructPregnantEditKeyboard();
    public final InlineKeyboardMarkup editGenderKeyboard = constructGenderEditKeyboard();
    public final InlineKeyboardMarkup editPretreatmentName = constructPretreatmentNameKeyboard();
    public final InlineKeyboardMarkup editOperationKeyboard = constructOperationEditKeyboard();
    public final InlineKeyboardMarkup editTreatmentName = constructTreatmentNameKeyboard();
    public final InlineKeyboardMarkup editPathologyKeyboard = constructPathologyKeyboard();

    private static Map<String, String> drugs = Map.of(
        "eutirox", "Эутирокс",
        "l_tyroxin", "L-тироксин",
        "other", "Другой",
        "nothing", "Не было назначено препарата"
    );

    private static Map<String, String> operation = Map.of(
        "all", "Да",
        "half", "Половина",
        "isthmus", "Перешеек",
        "remainder", "Оставлен остаток доли",
        "other", "Затрудняюсь ответить"
    );

    public InlineKeyboardMarkup constructEditKeyboard(Patient patient) {
        InlineKeyboardMarkup editKeyboard = new InlineKeyboardMarkup();


        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        List<InlineKeyboardButton> row3 = new LinkedList<>();
        List<InlineKeyboardButton> row4 = new LinkedList<>();
        List<InlineKeyboardButton> row5 = new LinkedList<>();
        List<InlineKeyboardButton> row6 = new LinkedList<>();
        List<InlineKeyboardButton> row7 = new LinkedList<>();
        List<InlineKeyboardButton> row8 = new LinkedList<>();
        List<InlineKeyboardButton> row9 = new LinkedList<>();
        List<InlineKeyboardButton> row10 = new LinkedList<>();
        List<InlineKeyboardButton> row11 = new LinkedList<>();
        List<InlineKeyboardButton> row12 = new LinkedList<>();
        List<InlineKeyboardButton> row13 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Имя: " + patient.getName());
        btn1.setCallbackData(EditCallback.EDIT_NAME_MENU.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("Дата рождения: " + new DefaultDateChecker(patient.getDateOfBirthday()).getDateInRuFormat());
        btn2.setCallbackData(EditCallback.EDIT_DOB_MENU.toString());

        InlineKeyboardButton btn3 = new InlineKeyboardButton();
        btn3.setText("Пол: " + formatGender(patient.getGender()));
        btn3.setCallbackData(EditCallback.EDIT_GENDER_MENU.toString());

        InlineKeyboardButton btn4 = new InlineKeyboardButton();
        btn4.setText("Вес: " + patient.getWeight() + " кг");
        btn4.setCallbackData(EditCallback.EDIT_WEIGHT_MENU.toString());

        InlineKeyboardButton btn5 = new InlineKeyboardButton();
        btn5.setText("Беременность: " + patient.getIsPregnant());
        btn5.setCallbackData(EditCallback.EDIT_IS_PREGNANT_MENU.toString());

        InlineKeyboardButton btn6 = new InlineKeyboardButton();
        btn6.setText("Доза препарата до операции: " + patient.getPretreatment() + " мкг");
        btn6.setCallbackData(EditCallback.EDIT_PRETREATMENT_MENU.toString());

        InlineKeyboardButton btn7 = new InlineKeyboardButton();
        btn7.setText("Название препарата до операции: " + drugs.get(patient.getPretreatmentDrug()));
        btn7.setCallbackData(EditCallback.EDIT_PRETREATMENT_DRUG_MENU.toString());

        InlineKeyboardButton btn8 = new InlineKeyboardButton();
        btn8.setText("Тип операции: " + operation.get(patient.getOperation()));
        btn8.setCallbackData(EditCallback.EDIT_OPERATION_MENU.toString());

        InlineKeyboardButton btn9 = new InlineKeyboardButton();
        btn9.setText("Дата операции: " + new DefaultDateChecker(patient.getDateOperation()).getDateInRuFormat());
        btn9.setCallbackData(EditCallback.EDIT_OPERATION_DATE_MENU.toString());

        InlineKeyboardButton btn10 = new InlineKeyboardButton();
        btn10.setText("Доза препарата после операции: " + patient.getTreatment() + " мкг");
        btn10.setCallbackData(EditCallback.EDIT_TREATMENT_MENU.toString());

        InlineKeyboardButton btn11 = new InlineKeyboardButton();
        btn11.setText("Название препарата после операции: " + drugs.get(patient.getTreatmentDrug()));
        btn11.setCallbackData(EditCallback.EDIT_TREATMENT_DRUG_MENU.toString());

        InlineKeyboardButton btn12 = new InlineKeyboardButton();
        btn12.setText("По гистологии пришло: " + patient.getPathologyName());
        btn12.setCallbackData(EditCallback.EDIT_PATHOLOGY_MENU.toString());

        InlineKeyboardButton btn13 = new InlineKeyboardButton();
        btn13.setText("Подтвердить данные");
        btn13.setCallbackData(EditCallback.EDIT_FINISH.toString());

        row1.add(btn1);
        row2.add(btn2);
        row3.add(btn3);
        row4.add(btn4);
        row5.add(btn5);
        row6.add(btn6);
        row7.add(btn7);
        row8.add(btn8);
        row9.add(btn9);
        row10.add(btn10);
        row11.add(btn11);
        row12.add(btn12);
        row13.add(btn13);

        rowsInline.add(row1);
        rowsInline.add(row2);
        rowsInline.add(row3);
        rowsInline.add(row4);
        rowsInline.add(row5);
        rowsInline.add(row6);
        rowsInline.add(row7);
        rowsInline.add(row8);
        rowsInline.add(row9);
        rowsInline.add(row10);
        rowsInline.add(row11);
        rowsInline.add(row12);
        rowsInline.add(row13);


        editKeyboard.setKeyboard(rowsInline);

        return editKeyboard;
    }

    private String formatGender(int gender) {
        if (gender == 0) {
            return "мужской";
        } else if (gender == 1) {
            return "женский";
        }
        return "Не указан";
    }


    private InlineKeyboardMarkup constructPregnantEditKeyboard() {
        InlineKeyboardMarkup pregnantKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Да");
        btn1.setCallbackData(EditCallback.EDIT_PREGNANT_Y.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("Нет");
        btn2.setCallbackData(EditCallback.EDIT_PREGNANT_N.toString());

        row1.add(btn1);
        row2.add(btn2);

        rowsInline.add(row1);
        rowsInline.add(row2);

        pregnantKeyboard.setKeyboard(rowsInline);

        return pregnantKeyboard;
    }

    private InlineKeyboardMarkup constructGenderEditKeyboard() {
        InlineKeyboardMarkup genderKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        List<InlineKeyboardButton> row3 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Мужской");
        btn1.setCallbackData(EditCallback.EDIT_GENDER_MALE.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("Женский");
        btn2.setCallbackData(EditCallback.EDIT_GENDER_FEMALE.toString());

        InlineKeyboardButton btn3 = new InlineKeyboardButton();
        btn3.setText("Отказываюсь отвечать");
        btn3.setCallbackData(EditCallback.EDIT_GENDER_OTHER.toString());

        row1.add(btn1);
        row2.add(btn2);
        row3.add(btn3);

        rowsInline.add(row1);
        rowsInline.add(row2);
        rowsInline.add(row3);

        genderKeyboard.setKeyboard(rowsInline);

        return genderKeyboard;
    }

    private InlineKeyboardMarkup constructPretreatmentNameKeyboard() {
        InlineKeyboardMarkup genderKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        List<InlineKeyboardButton> row3 = new LinkedList<>();
        List<InlineKeyboardButton> row4 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Эутирокс");
        btn1.setCallbackData(EditCallback.EDIT_PRETREATMENT_NAME_EUTIROX.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("L-тироксин");
        btn2.setCallbackData(EditCallback.EDIT_PRETREATMENT_NAME_L_TYROXIN.toString());

        InlineKeyboardButton btn3 = new InlineKeyboardButton();
        btn3.setText("Другой");
        btn3.setCallbackData(EditCallback.EDIT_PRETREATMENT_NAME_OTHER.toString());

        InlineKeyboardButton btn4 = new InlineKeyboardButton();
        btn4.setText("Не было назначено препарата");
        btn4.setCallbackData(EditCallback.EDIT_PRETREATMENT_NAME_NOTHING.toString());

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

    private InlineKeyboardMarkup constructOperationEditKeyboard() {
        InlineKeyboardMarkup genderKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        List<InlineKeyboardButton> row3 = new LinkedList<>();
        List<InlineKeyboardButton> row4 = new LinkedList<>();
        List<InlineKeyboardButton> row5 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Да");
        btn1.setCallbackData(EditCallback.EDIT_OP_ALL.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("Половина");
        btn2.setCallbackData(EditCallback.EDIT_OP_HALF.toString());

        InlineKeyboardButton btn3 = new InlineKeyboardButton();
        btn3.setText("Перешеек");
        btn3.setCallbackData(EditCallback.EDIT_OP_ISTHMUS.toString());

        InlineKeyboardButton btn4 = new InlineKeyboardButton();
        btn4.setText("Оставлен остаток доли");
        btn4.setCallbackData(EditCallback.EDIT_OP_REMAINDER.toString());

        InlineKeyboardButton btn5 = new InlineKeyboardButton();
        btn5.setText("Затрудняюсь ответить");
        btn5.setCallbackData(EditCallback.EDIT_OP_OTHER.toString());

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

    private InlineKeyboardMarkup constructTreatmentNameKeyboard() {
        InlineKeyboardMarkup genderKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        List<InlineKeyboardButton> row3 = new LinkedList<>();
        List<InlineKeyboardButton> row4 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Эутирокс");
        btn1.setCallbackData(EditCallback.EDIT_TREATMENT_NAME_EUTIROX.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("L-тироксин");
        btn2.setCallbackData(EditCallback.EDIT_TREATMENT_NAME_L_TYROXIN.toString());

        InlineKeyboardButton btn3 = new InlineKeyboardButton();
        btn3.setText("Другой");
        btn3.setCallbackData(EditCallback.EDIT_TREATMENT_NAME_OTHER.toString());

        InlineKeyboardButton btn4 = new InlineKeyboardButton();
        btn4.setText("Не было назначено препарата");
        btn4.setCallbackData(EditCallback.EDIT_TREATMENT_NAME_NOTHING.toString());

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
        btn1.setCallbackData(EditCallback.EDIT_PATHOLOGY_1.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("Тиреоидит Хашимото / диффузный токсический зоб");
        btn2.setCallbackData(EditCallback.EDIT_PATHOLOGY_2.toString());

        InlineKeyboardButton btn3 = new InlineKeyboardButton();
        btn3.setText("Папиллярная/фолликулярная карцинома");
        btn3.setCallbackData(EditCallback.EDIT_PATHOLOGY_3.toString());

        InlineKeyboardButton btn4 = new InlineKeyboardButton();
        btn4.setText("Медуллярная карцинома");
        btn4.setCallbackData(EditCallback.EDIT_PATHOLOGY_4.toString());

        InlineKeyboardButton btn5 = new InlineKeyboardButton();
        btn5.setText("Другое");
        btn5.setCallbackData(EditCallback.EDIT_PATHOLOGY_5.toString());

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
}
