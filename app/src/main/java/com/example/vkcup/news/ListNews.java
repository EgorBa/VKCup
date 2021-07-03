package com.example.vkcup.news;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ListNews {

    public static class SwipeRightCardModel {
        String url;
        String text;

        SwipeRightCardModel(String url, String text) {
            this.url = url;
            this.text = text;
        }
    }

    final static SwipeRightCardModel SWIPE_RIGHT_CARD_MODEL_1 = new SwipeRightCardModel(
            "https://sun9-54.userapi.com/impg/Cpi-FvN6fYVGUASqq-ut6a-bY3bWR0-6fFLfjQ/P2x5uRZpB-4.jpg?size=979x1080&quality=96&sign=5a973cd79b10bc69c7f20fbdc4dd0a4b&c_uniq_tag=5zvK6Ad9bcEhV1lBq_gOe663YiUPIuzIiPX7m0yWfhU&type=album",
            "С XIII века эмблемой острова Сицилия считается трискелион – символ трех бегущих ног. Удивительно, что именно здесь 18 мая 1889 было суждено родиться трехногому Франческо Лентини – будущему прославленному циркачу.");
    final static SwipeRightCardModel SWIPE_RIGHT_CARD_MODEL_2 = new SwipeRightCardModel(
            "https://sun9-30.userapi.com/impg/xRaeNAbyNxVqLTRczpNs_bfM9SCSyXgqqYNn0g/manbdOfB01A.jpg?size=1000x1000&quality=96&sign=73ad05857d85d6447c6b2be35dc9d5fa&c_uniq_tag=gUSXNtpZAbvPn6GpwD0MKjLzfTe4AlME-LMlouoCmms&type=album",
            "Каст второй части фильма \"Достать ножи\"");
    final static SwipeRightCardModel SWIPE_RIGHT_CARD_MODEL_3 = new SwipeRightCardModel(
            "https://sun9-34.userapi.com/impg/SlE05GpB1NNDISBvCbBOlvDdO2fKJW3l8ErOUw/69C2ZfdwxKw.jpg?size=1074x480&quality=96&sign=cc2dad76eba4b5a6314181236c121e75&c_uniq_tag=JzIAm_FSP2zae0fWxED3jB4RNg32YPocPhkXd5CC7ts&type=share",
            "Фанаты и пользователи соцсетей собрали почти 90 тысяч долларов на операцию каскадёрше Дайане Грант, которая выступала дублёром Фуриосы из «Безумного Макса».\n");
    final static SwipeRightCardModel SWIPE_RIGHT_CARD_MODEL_4 = new SwipeRightCardModel(
            "https://sun9-8.userapi.com/impg/YOFIl3hxUDdkRIhZtovVxCB8xC9tkw72dViWsA/449lRmheIwc.jpg?size=782x1078&quality=96&sign=451eef92b4e53d33255f5f322d60c812&c_uniq_tag=yQWueoVBx0ruJROZwl4SZjkGM5ebebM4j96BpzouKxY&type=album",
            "Каждая по своему, но очень красивые.");
    static SwipeRightCardModel SWIPE_RIGHT_CARD_MODEL_5 = new SwipeRightCardModel(
            "https://sun9-12.userapi.com/impg/dSMxcVk-95wYNQHnREj6TshxabDSbmLA5Ejopg/zgh-unRB_eQ.jpg?size=1074x480&quality=96&sign=aa3abd9ba210540f3b1846d01c3df639&c_uniq_tag=XfHbsy5YzWjahczXTR3dGC36yO7Hd4nEkWs3oaVXKzU&type=share",
            "Ветеран BioWare Марк Дарра рассказал об отменённом спин-оффе Mass Effect для Nintendo DS");
    final static SwipeRightCardModel SWIPE_RIGHT_CARD_MODEL_6 = new SwipeRightCardModel(
            "https://sun9-37.userapi.com/v7s7V-wYaiSNfGrmSB7SoYiJ2mNsb7X6ZC8vGQ/9oG5jAsLlMw.jpg",
            "OXXXYMIRON — «СТИХИ О НЕИЗВЕСТНОМ СОЛДАТЕ» (О. МАНДЕЛЬШТАМ)\n" +
                    "\n" +
                    "#ПремьераКлипа@rhymes: МАКСИМАЛЬНЫЙ РЕПОСТ!");
    final static SwipeRightCardModel SWIPE_RIGHT_CARD_MODEL_8 = new SwipeRightCardModel(
            "https://sun9-73.userapi.com/impg/Rs1XNY63ni5nbvODucH-ZOjpDjHy8ixby9zNzA/E5qwvS05b6I.jpg?size=568x368&quality=96&sign=7bcdeca56aa6634e4e940d02b3b1875c&c_uniq_tag=5TArdLoegRYI8liqQo3boK0X15VVsfx5EQaD0xoc1AM&type=album",
            "42-летняя женщина из Индии за раз родила 11 детей.\n" +
                    "\n" +
                    "Это рекорд.\n" +
                    "\n" +
                    "кнрк");
    final static SwipeRightCardModel SWIPE_RIGHT_CARD_MODEL_9 = new SwipeRightCardModel(
            "https://sun9-27.userapi.com/impg/o_JbvtHd2tblDMK_o2EgP6TSnFb9QOq66G3UYQ/7XhousZbmZw.jpg?size=667x925&quality=96&sign=bcb6e9bb91e5d43b8d6ae18607c12b98&c_uniq_tag=qNvCZ8rYBTcnLEUHjKGR3KNHHm9lrhTGSWrAUZPRAfM&type=album",
            "Интересно,что значат эти рисочки...");
    final static SwipeRightCardModel SWIPE_RIGHT_CARD_MODEL_10 = new SwipeRightCardModel(
            "https://sun9-33.userapi.com/impg/dvYKdi4mL2g0CoqCJutwp23IKV7tzCj-4pxdhQ/JwBQfxYpy_Y.jpg?size=1080x1080&quality=96&sign=af3350f0197a6b0e542853b2be5d4658&c_uniq_tag=SLMLmpu-3UL8Eb3TcuBfhjk-Zr36rMtMpuPyNYPxWt8&type=album",
            "Сборная Украины вчера позволила себе немного подвижовать");

    public final static ArrayList<SwipeRightCardModel> NEWS = new ArrayList<>(Arrays.asList(
            SWIPE_RIGHT_CARD_MODEL_1, SWIPE_RIGHT_CARD_MODEL_2, SWIPE_RIGHT_CARD_MODEL_3, SWIPE_RIGHT_CARD_MODEL_4, SWIPE_RIGHT_CARD_MODEL_5, SWIPE_RIGHT_CARD_MODEL_6, SWIPE_RIGHT_CARD_MODEL_8, SWIPE_RIGHT_CARD_MODEL_9, SWIPE_RIGHT_CARD_MODEL_10));
}
