package TelegramBot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.InvalidObjectException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyTestBot extends TelegramLongPollingBot {

    final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String ЗЗР_НАЗВА = "/ЗЗР\uD83D\uDEE2";
    public static final String ЗЗР_ДІЮЧА_РЕЧОВИНА = "/ЗЗР (Діюча речовина)\uD83E\uDDEC";
    public static final String ПОСІВНИЙ_МАТЕРІАЛ = "/Посівний матеріал\uD83C\uDF3D";

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                String text = update.getMessage().getText();
                if (!text.equals(ЗЗР_НАЗВА)) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getMessage().getChatId());
                    sendMessage.setReplyMarkup(getMainMenu());
                    sendMessage.setText("скористуйтесь меню");
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                if (text.equals(ЗЗР_НАЗВА)) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getMessage().getChatId());
                    sendMessage.setReplyMarkup(getMainMenu());
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton("/ЗЗР (назва)");
                    InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton("/ЗЗР (Діюча речовина)");
                    List<List<InlineKeyboardButton>> listList = new ArrayList<>();
                    List<InlineKeyboardButton> keyboard = new ArrayList<>();
                    keyboard.add(inlineKeyboardButton1);
                    keyboard.add(inlineKeyboardButton2);
                    listList.add(keyboard);
                    inlineKeyboardButton1.setCallbackData("/ЗЗР (назва)");
                    inlineKeyboardButton2.setCallbackData("/ЗЗР (Діюча речовина)");
                    try {
                        execute(sendMessage.setText("Введіть назву ЗЗР").setChatId(update.getMessage().getChatId()).setReplyMarkup(inlineKeyboardMarkup.setKeyboard(listList)));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            Message message = update.getCallbackQuery().getMessage();
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            SendMessage sendMessage1 = new SendMessage().setParseMode(ParseMode.MARKDOWN).setChatId(message.getChatId());
            if (data.equals("/ЗЗР (назва)")) {
                sendMessage1.setText("Введіть назву препарату");
            } else if (data.equals("/ЗЗР (Діюча речовина)")) {
                sendMessage1.setText("Введіть ДВ препарату");
            }
            try {
                execute(sendMessage1);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private ReplyKeyboardMarkup getMainMenu() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton(ЗЗР_НАЗВА);
      //  KeyboardButton button2 = new KeyboardButton(ЗЗР_ДІЮЧА_РЕЧОВИНА);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        row1.add(button1);
        //row1.add(button2);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(ПОСІВНИЙ_МАТЕРІАЛ);
        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);
        markup.setKeyboard(rowList);
        return markup;
    }

    public List<String> zzRposhukName(Update update) {
        Message message = update.getMessage();
        MyTestBot bot = new MyTestBot();
        String sms = null;
        List<String> listGetZzr = new ArrayList<>();
        ArrayList<ZZR> list = null;
        try {
            list = bot.returnList();
        } catch (InvalidObjectException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (message != null && message.hasText()) {
            for (ZZR zzr : list) {
                if (zzr.getName().toLowerCase().contains(message.getText().toLowerCase())) {
                    sms = "НАЗВА: " + zzr.getName() + "\n" + "НОРМА ВНЕСЕННЯ: " + zzr.getNormaVneseniy() + "\n"
                            + "ТИП: " + zzr.getVid() + "\n" + "ВИРОБНИК: " + zzr.getVirobnik() + "\n" + "ДІЮЧА РЕЧОВИНА: "
                            + zzr.getDv() + "\n" + "КУЛЬТУРА: " + zzr.getKultura() + "\n" + "СПЕКТР ШКІДНИКІВ: " + zzr.getSpectr() + "\n";
                    System.out.println(message.getChatId());
                    String firstName = update.getMessage().getChat().getFirstName();
                    String secondName = update.getMessage().getChat().getLastName();
                    Date date = new Date();
                    LocalDate localDate = LocalDate.now();
                    System.out.println(localDate + "/" + date.getHours() + ":" + date.getMinutes());
                    String mestext = update.getMessage().getText();
                    System.out.println(firstName + "" + "\n" + secondName + "\n" + "" + mestext);
                    System.out.println(sms);
                    System.out.println("___________________");
                    listGetZzr.add(sms);
                }
            }
            if (sms == null) {
                listGetZzr.add("ЗЗР не знайдено\uD83E\uDD72");
                System.out.println(message.getChatId());
                String firstName = update.getMessage().getChat().getFirstName();
                String secondName = update.getMessage().getChat().getLastName();
                String mestext = update.getMessage().getText();
                Date date = new Date();
                LocalDate localDate = LocalDate.now();
                System.out.println(localDate + "/" + date.getHours() + ":" + date.getMinutes());
                System.out.println(firstName + "" + "\n" + secondName + "\n" + "" + mestext);
            }
        }
        return listGetZzr;
    }
    public ArrayList<ZZR> returnList() throws InvalidObjectException, UnsupportedEncodingException {
        BaseJson baseJson = new BaseJson();

        //читання укр - символів
        ArrayList<ZZR> list = new ArrayList<ZZR>();
        String s = String.valueOf(baseJson.deserializatior());
        String s1 = new String(s.getBytes("ISO-8859-1"), "UTF-8");

        ZZR[] taskWorks = GSON.fromJson(s1, ZZR[].class);
        for (ZZR t : taskWorks) {
            ZZR zzr = new ZZR(t.getName(), t.getDv(), t.getVid(), t.getVirobnik(), t.getNormaVneseniy(), t.getKultura(), t.getSpectr());
            list.add(zzr);
        }
        return list;
    }

    @Override
    public String getBotUsername() {
        return "ANSTabBot";
    }

    @Override
    public String getBotToken() {
        return "1727806810:AAF49TOPcO_NUcM6hxsCk9t4uCZzxUZ22gI";
    }

    public static void main(String[] args) throws InvalidObjectException, UnsupportedEncodingException {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new MyTestBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
