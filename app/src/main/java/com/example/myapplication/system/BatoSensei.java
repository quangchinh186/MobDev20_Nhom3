package com.example.myapplication.system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client;

    public BatoSensei(String sent_by_me, String sent_by_oppo){
        SENT_BY_ME = sent_by_me;
        SENT_BY_OPPO = sent_by_oppo;
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private void sendToUser(String result, Context context){
        Intent intent = new Intent();
        intent.setAction("getRecommend");
        intent.putExtra("result", result);
        context.sendBroadcast(intent);
    }

    public void asyncGetRecommendation(String question, List<String> gender, List<String> oppoHobbies, Context context) {
        String oppoName = Objects.equals(gender.get(1), "nam") ? "anh" : "em";
        String prev_prompt = "Tôi là " + gender.get(0) + ", xưng đối phương là" + oppoName +  ". ";
        String questionInfo = "Câu hỏi là " + question;
        String oppoHobby = "Sở thích của đối phương là: ";
        oppoHobbies.forEach(hobby -> oppoHobby.concat(hobby + ", "));
        String prompt = prev_prompt + ". " + questionInfo + ". " + oppoHobby;
        JSONObject jsonBody = new JSONObject();

        System.out.println("Cau hoi cua tao la: " + question);
        try {
            jsonBody.put("model","gpt-3.5-turbo");
            //
            JSONArray array = new JSONArray();
            JSONObject item = new JSONObject();
            item.put("role", "user");
            item.put("content", prompt);
            array.put(item);
            //
            jsonBody.put("messages", array);
//            jsonBody.put("prompt", prompt);
            jsonBody.put("max_tokens",4000);
            jsonBody.put("temperature",0.7);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendToUser("Đang tải...", context);
        RequestBody body = RequestBody.create(JSON, jsonBody.toString());
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization",API_Key)
                .post(body)
                .build();

        System.out.println("Request: " + jsonBody.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //BatoSystem.sendMessage("Failed to load response due to "+ e.getMessage(), context);
                Log.e("Batosensei", e.getMessage(), e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject  jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.peekBody(2048).string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        JSONObject resultJSON = jsonArray.getJSONObject(0).getJSONObject("message");
                        String result = resultJSON.getString("content");
                        responseChat = result.trim();
                        Log.v("Response", responseChat);
                        sendToUser(responseChat, context);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    responseChat = "Failed to load response due to "+response.body().toString();
                    System.out.println(responseChat);
                }
            }
        });
    }


}
