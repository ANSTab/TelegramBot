package TelegramBot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.api.methods.send.SendMessage;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Bot extends TelegramLongPollingBot {
    final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String ЗЗР_НАЗВА = "/ЗЗР (назва)\uD83D\uDEE2";
    public static final String ЗЗР_ДІЮЧА_РЕЧОВИНА = "/ЗЗР (Діюча речовина)\uD83E\uDDEC";
    public static final String ПОСІВНИЙ_МАТЕРІАЛ = "/Посівний матеріал\uD83C\uDF3D";

    @PostConstruct
    public void init() {
        ApiContextInitializer.init();
    }

    public static void main(String[] args) throws InvalidObjectException, UnsupportedEncodingException {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
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

/*
    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("ЗЗР(назва)"));
        keyboardFirstRow.add(new KeyboardButton("ЗЗР(ДВ)"));
        keyboardFirstRow.add(new KeyboardButton("ПМ"));
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }*/

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Bot bot = new Bot();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
       sendMessage.setReplyMarkup(getMainMenu());
        try {
            execute(sendMessage.setText(" Ось, що вдалось знайти по вашому запиту:"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        for (String s : bot.zzRposhukName(update)) {
            try {
                execute(sendMessage.setText(s));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

/*
    public SendMessage buttMessege (Message message){
        SendMessage sendMessage = new SendMessage();
        Update update = new Update();
        message.getChatId();
        switch (message.getText()){
            case ЗЗР_НАЗВА:
                zzRposhukName(update);
                return ;
        }return sendMessage.setText("ad");
    }*/


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


    public List<String> zzRposhukName(Update update) {
        Message message = update.getMessage();
        Bot bot = new Bot();
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
