
package TelegramBot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.io.InvalidObjectException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BotTest extends TelegramLongPollingBot {
    final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String ЗЗР_НАЗВА = "/ЗЗР (назва)\uD83D\uDEE2";
    public static final String ЗЗР_ДІЮЧА_РЕЧОВИНА = "/ЗЗР (Діюча речовина)\uD83E\uDDEC";
    public static final String ПОСІВНИЙ_МАТЕРІАЛ = "/Посівний матеріал\uD83C\uDF3D";
    List<Message> messageList = new ArrayList<>();

    @PostConstruct
    public void init() {
        ApiContextInitializer.init();
    }

    public static void main(String[] args) throws InvalidObjectException, UnsupportedEncodingException {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new BotTest());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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

    public void onUpdateReceived(Update update) {
        Message message = new Message();
        SendMessage sendMessage = new SendMessage();

        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals(ЗЗР_НАЗВА)) {
            sendMessage.setChatId(update.getMessage().getChatId());
            sendMessage.setText("Введіть назву препарату");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            Update update1 = new Update();
            BotTest botTest = new BotTest();
            for (String s : botTest.zzRposhukName(update1.getMessage())) {
                if (!update1.getMessage().getText().contains(s)) {
                    SendMessage sendMessage1 = new SendMessage();
                    sendMessage1.setChatId(update1.getMessage().getChatId());
                    try {
                        execute(sendMessage1.setText(s));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }



    private SendMessage getResponceMessege(Message message) {
        switch (message.getText()) {
            case ЗЗР_НАЗВА:
                return getDVZZRMenu(message);

            default:
                return greetingUser(message);
        }
    }

    private SendMessage getDVZZRMenu(Message message) {
        BotTest botTest = new BotTest();
        Update update = new Update();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Введіть назву препарату");
        Message message1 = update.getMessage();

        for (String s : botTest.zzRposhukName(message1)) {
            try {
                execute(sendMessage.setText(s));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        sendMessage.setChatId(message.getChatId());
        return sendMessage;
    }

    private SendMessage greetingUser(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(" Hello " + message.getFrom().getFirstName() + " " + message.getFrom().getLastName());
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyMarkup(getMainMenu());
        return sendMessage;
    }

    private ReplyKeyboardMarkup getMainMenu() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton(ЗЗР_НАЗВА);
        KeyboardButton button2 = new KeyboardButton(ЗЗР_ДІЮЧА_РЕЧОВИНА);
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        row1.add(button1);
        row1.add(button2);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(ПОСІВНИЙ_МАТЕРІАЛ);
        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(row1);
        rowList.add(row2);
        markup.setKeyboard(rowList);
        return markup;
    }


    public List<String> zzRposhukName(Message message) {
        BotTest bot = new BotTest();
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
                    System.out.println(sms);
                    System.out.println("___________________");
                    listGetZzr.add(sms);
                }
            }
            if (sms == null) {
                listGetZzr.add("ЗЗР не знайдено\uD83E\uDD72");
            }
        }
        return listGetZzr;
    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "ANSTabBot";
    }

    public String getBotToken() {
        return "1727806810:AAF49TOPcO_NUcM6hxsCk9t4uCZzxUZ22gI";
    }
}

