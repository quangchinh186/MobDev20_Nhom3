package com.example.myapplication.system;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BatoSensei  {
    String responseChat;
    public String SENT_BY_ME = "user";
    public String SENT_BY_OPPO="oppo";
    public static String API_Key = "Bearer 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";
    public List<Message> messageList;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    public final CompletableFuture<Response> future = new CompletableFuture<>();

    public BatoSensei(){}

    public BatoSensei(String sent_by_me, String sent_by_oppo){
        SENT_BY_ME = sent_by_me;
        SENT_BY_OPPO = sent_by_oppo;
    }

    public String chatToSensei(String question){
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model","gpt-3.5-turbo");
            JSONArray array = new JSONArray();

            for (Message m : messageList) {
                //q is Message type.
                JSONObject item = new JSONObject();
                item.put("role", m.getSentBy());
                item.put("content", m.getSentBy());
                array.put(item);
            }

            jsonBody.put("messages", array);
            jsonBody.put("max_tokens",4000);
            jsonBody.put("temperature",0.7);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonBody.toString());
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization",API_Key)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject  jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        JSONObject resultJSON = jsonArray.getJSONObject(0).getJSONObject("message");
                        String result = resultJSON.getString("content");
                        responseChat = result.trim();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    responseChat = "Failed to load response due to "+response.body().toString();
                }
            }
        });
        return responseChat;
    }



    public void asyncSetRecommendation(List<Message> listMsg, String question, String gender, Method callback) {
        String prev_prompt = "Tôi cần từ một tới ba câu trả lời khác nhau với định dạng như sau. '<item>/<item>/<item>'";
        String someInfo = "Tôi tên là " + this.SENT_BY_ME + ". Tên người bạn đang trò chuyện là " + this.SENT_BY_OPPO;
        String genderInfo = "Giới tính của người tôi đang chat là " + gender;
        String questionInfo = "Câu hỏi của tôi là " + question;
        String prompt = prev_prompt + "\n" + someInfo + "\n" + genderInfo + "\n" + questionInfo;
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model","gpt-3.5-turbo");
            JSONArray array = new JSONArray();

            for (Message m : listMsg) {
                JSONObject item = new JSONObject();
                item.put("role", m.getSentBy());
                item.put("content", m.getSentBy());
                item.put("prompt", prompt);
                array.put(item);
            }

            jsonBody.put("messages", array);
            jsonBody.put("max_tokens",4000);
            jsonBody.put("temperature",0.7);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonBody.toString());
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", API_Key)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        JSONObject resultJSON = jsonArray.getJSONObject(0).getJSONObject("message");
                        String result = resultJSON.getString("content");
                        responseChat = result.trim();
                        callback.invoke(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    responseChat = "Failed to load response due to "+response.body().toString();
                }
            }
        });
    }

    public List<String> getRecommendation(List<Message> listMsg, String question, String gender) throws ExecutionException, InterruptedException {
        String prev_prompt = "Tôi cần từ một tới ba câu trả lời khác nhau với định dạng như sau. '<item>/<item>/<item>'";
        String someInfo = "Tôi tên là " + this.SENT_BY_ME + ". Tên người bạn đang trò chuyện là " + this.SENT_BY_OPPO;
        String genderInfo = "Giới tính của người tôi đang chat là " + gender;
        String questionInfo = "Câu hỏi của tôi là " + question;
        String prompt = prev_prompt + "\n" + someInfo + "\n" + genderInfo + "\n" + questionInfo;
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model","gpt-3.5-turbo");
            JSONArray array = new JSONArray();

            for (Message m : listMsg) {
                JSONObject item = new JSONObject();
                item.put("role", m.getSentBy());
                item.put("content", m.getSentBy());
                item.put("prompt", prompt);
                array.put(item);
            }

            jsonBody.put("messages", array);
            jsonBody.put("max_tokens",4000);
            jsonBody.put("temperature",0.7);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonBody.toString());
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", API_Key)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        JSONObject resultJSON = jsonArray.getJSONObject(0).getJSONObject("message");
                        String result = resultJSON.getString("content");
                        responseChat = result.trim();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    responseChat = "Failed to load response due to "+response.body().toString();
                }
            }
        });
        return Arrays.asList(responseChat.split("/"));
    }
}
