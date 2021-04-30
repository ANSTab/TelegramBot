package TelegramBot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.InvalidObjectException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {
    final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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


    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("ЗЗР (назва)"));
        keyboardFirstRow.add(new KeyboardButton("Діюча речовина"));


        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Bot bot = new Bot();
        ArrayList<ZZR> list = null;
        try {
            list = bot.returnList();
        } catch (InvalidObjectException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "ЗЗР (назва)":
                    sendMsg(message, "Введіть назву препарату");
                    break;
                case "Діюча речовина":
                    sendMsg(message, "Введіть діючу речовину препарату");
                    System.out.println(message.getText());
                    break;
                default:
            for (ZZR zzr : list) {
                if (zzr.getName().toLowerCase().contains(message.getText().toLowerCase())) {
                    String sms = "НАЗВА: " + zzr.getName() + "\n" + "НОРМА ВНЕСЕННЯ: " + zzr.getNormaVneseniy() + "\n" + "ТИП: " + zzr.getVid() + "\n" + "ВИРОБНИК: " + zzr.getVirobnik() + "\n" + "ДІЮЧА РЕЧОВИНА: " + zzr.getDv() + "\n" + "КУЛЬТУРА: " + zzr.getKultura() + "\n" + "СПЕКТР ШКІДНИКІВ: " + zzr.getSpectr() + "\n";
                    System.out.println(sms);
                    sendMsg(message, sms);
                }
            }
                    break;
            }
        }

    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
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
