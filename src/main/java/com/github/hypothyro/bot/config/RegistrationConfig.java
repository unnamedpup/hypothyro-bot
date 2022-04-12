package com.github.hypothyro.bot.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;


@Component
public class RegistrationConfig {

    public final Map<String, String> pathologies = new HashMap<>();
    public final String changeUpthslevFieldIndex = "2";
    public final String pathologyText = "Выберите дозу прописанных вам препаратов";

    public final String confirmRegistrationn = "Подтверждение регистрации";

    public final String thsDate = "Когда вы сдали анализ ?";
    public final String thsRes = "Введите значение ТТГ (в мкМЕ/мл):";

    public final String noTtg = "В срок 4-8 недель после операции на щитовидной железе следует определить уровень ТТГ для оценки необходимости ";
    public final String msgNoPretreatment = "назначения";
    public final String msgYesPretreatment = "коррекции терапии";

    public final String zeroTtgFirst = "Есть риск что у вас тиреотоксикоз. Если вы ранее не сталкивались с этим нужно сдать кровь на свободные фракции T3 и Т4 и Антитела к рецептору ТТГ и обратится к эндокринологу.";

    public final String zeroTreatmentTtgSecond1 = "Вероятно, ваша железа не справляется с обеспечением вас гормонами.";
    public final String zeroTreatmentTtgSecond21 = "Повышение ТТГ означает что щитовидная железа (то, что от нее осталось после операции) вырабатывает меньше гормонов, чем нужно, однако разница не настолько значима. Если вы себя нормально чувствуете можно не спешить с назначением гормонов. Имеются ли у Вас жалобы на сонливость, редкий пульс?";
    public final String ttgWannaBePregnant = " Не планируете ли вы беременность ?";
    public final String zeroTreatmentTtgSecondAnswer1 = "Вам нужно начинать заместительную терапию. Обычно эндокринологи начинают с дозы в 50 мкг левотироксина. Контроль ТТГ через 2 месяца. ";
    public final String zeroTreatmentTtgSecondAnswer2 = "В случаях, когда нет клинических симптомов гипотиреоза, решение о заместительной терапии можно отложить на 2 месяца. Давайте проконтролируем ТТГ";
    public final String zeroTreatmentTtgSecond3 = "У вас недостаток гормонов. Вам нужно начинать заместительную терапию. Обычно эндокринологи начинают с дозы в 50 мкг левотироксина. Контроль ТТГ через 2 месяца.";
    public final String ttgThird = "У вас отличный уровень ТТГ. Следующий раз контроль через ";

    public final String hasTreatmentTtgFirst = "Это может быть нормальным для супрессивной терапии для пациентов высокой и умеренной группы риска. У вас были множественные метастазы, операции по поводу рецидивов, планируется терапия радиоактивным йодом, имеются высокие уровни тиреоглобуллина? ";


    public RegistrationConfig() {
        setupPathologies();
    }

    private void setupPathologies() {
        pathologies.put("1", "Фолликулярная аденома/ узловой нетоксический зоб");
        pathologies.put("2", "Тиреоидит Хашимото / диффузный токсический зоб");
        pathologies.put("3", "Папиллярная/фолликулярная карцинома");
        pathologies.put("4", "Медуллярная карцинома");
        pathologies.put("5", "другое");
    }
}
