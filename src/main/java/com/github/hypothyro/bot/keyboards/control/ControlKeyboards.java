package com.github.hypothyro.bot.keyboards.control;

import java.util.LinkedList;
import java.util.List;

import com.github.hypothyro.bot.keyboards.registration.RegistrationCallback;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import lombok.Getter;

@Component
@Getter
public class ControlKeyboards {

    public final ReplyKeyboardMarkup controlButtons = construcrControlButtons();
    public final InlineKeyboardMarkup treatmentButtons = constructTreatmentNamesButtons();

    private ReplyKeyboardMarkup construcrControlButtons() {
        ReplyKeyboardMarkup kb = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new LinkedList<>();

        KeyboardRow kbr1 = new KeyboardRow();
        kbr1.add(new KeyboardButton("Отправить новый результат"));
        KeyboardRow kbr2 = new KeyboardRow();
        kbr2.add(new KeyboardButton("Когда нужно сдать следующий анализ ?"));
        KeyboardRow kbr3 = new KeyboardRow();
        kbr3.add(new KeyboardButton("Последние мои результаты"));
        KeyboardRow kbr4 = new KeyboardRow();
        kbr3.add(new KeyboardButton("Новая терапия"));

        keyboard.add(kbr1);
        keyboard.add(kbr2);
        keyboard.add(kbr3);
        keyboard.add(kbr4);

        kb.setKeyboard(keyboard);

        return kb;
    }


    private InlineKeyboardMarkup constructTreatmentNamesButtons() {
        InlineKeyboardMarkup genderKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new LinkedList<>();
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        List<InlineKeyboardButton> row3 = new LinkedList<>();
        List<InlineKeyboardButton> row4 = new LinkedList<>();

        InlineKeyboardButton btn1 = new InlineKeyboardButton();
        btn1.setText("Эутирокс");
        btn1.setCallbackData(RegistrationCallback.CONTROL_PRETREATMENT_NAME_EUTIROX.toString());

        InlineKeyboardButton btn2 = new InlineKeyboardButton();
        btn2.setText("L-тироксин");
        btn2.setCallbackData(RegistrationCallback.CONTROL_PRETREATMENT_NAME_L_TYROXIN.toString());

        InlineKeyboardButton btn3 = new InlineKeyboardButton();
        btn3.setText("Другой");
        btn3.setCallbackData(RegistrationCallback.CONTROL_PRETREATMENT_NAME_OTHER.toString());

        InlineKeyboardButton btn4 = new InlineKeyboardButton();
        btn4.setText("Не было назначено препарата");
        btn4.setCallbackData(RegistrationCallback.CONTROL_PRETREATMENT_NAME_NOTHING.toString());

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
}
