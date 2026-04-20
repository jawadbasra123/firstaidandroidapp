package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatViewModel extends ViewModel {
    private static final String GEMINI_API_KEY = BuildConfig.GEMINI_API_KEY;
    
    private static final String SYSTEM_PROMPT = "You are a First Aid Emergency Assistant. " +
            "Provide clear, concise, and accurate first aid advice. " +
            "Always remind the user to call emergency services (911) if the situation is life-threatening. " +
            "Do not provide medical diagnoses, only first aid procedures.";

    private final MutableLiveData<List<ChatMessage>> _messages = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<ChatMessage>> getMessages() { return _messages; }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsLoading() { return _isLoading; }

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> getError() { return _error; }

    private final GeminiApiService apiService;

    public ChatViewModel() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://generativelanguage.googleapis.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(GeminiApiService.class);

        // Initial Greeting
        List<ChatMessage> current = _messages.getValue();
        if (current != null && current.isEmpty()) {
            current.add(new ChatMessage("Hello! I'm your First Aid AI Assistant. How can I help you today?", ChatMessage.TYPE_AI));
            _messages.setValue(current);
        }
    }

    public void sendMessage(String userText) {
        if (userText.trim().isEmpty()) return;

        List<ChatMessage> current = new ArrayList<>(_messages.getValue());
        current.add(new ChatMessage(userText, ChatMessage.TYPE_USER));
        _messages.setValue(current);

        _isLoading.setValue(true);

        // Construct the prompt with system instruction
        String prompt = SYSTEM_PROMPT + "\n\nUser: " + userText;
        GeminiModels.Part part = new GeminiModels.Part(prompt);
        GeminiModels.Content content = new GeminiModels.Content(Collections.singletonList(part));
        GeminiModels.Request request = new GeminiModels.Request(Collections.singletonList(content));

        apiService.generateContent(GEMINI_API_KEY, request).enqueue(new Callback<GeminiModels.Response>() {
            @Override
            public void onResponse(Call<GeminiModels.Response> call, Response<GeminiModels.Response> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && 
                    response.body().candidates != null && !response.body().candidates.isEmpty()) {
                    String aiResponse = response.body().candidates.get(0).content.parts.get(0).text;
                    List<ChatMessage> updated = new ArrayList<>(_messages.getValue());
                    updated.add(new ChatMessage(aiResponse, ChatMessage.TYPE_AI));
                    _messages.setValue(updated);
                } else {
                    Log.e("GeminiAPI", "Error Code: " + response.code());
                    Log.e("GeminiAPI", "Error Message: " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            Log.e("GeminiAPI", "Error Body: " + response.errorBody().string());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    if (response.code() == 404) {
                        _error.setValue("Error 404: The API endpoint was not found. Please ensure the model name 'gemini-1.5-flash' is correct for your region.");
                    } else if (response.code() == 403) {
                        _error.setValue("API Key Error: Please check if the key is valid and Gemini API is enabled in Google AI Studio.");
                    } else {
                        _error.setValue("Error: " + response.code() + " " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<GeminiModels.Response> call, Throwable t) {
                _isLoading.setValue(false);
                Log.e("GeminiAPI", "Failure: " + t.getMessage(), t);
                _error.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void clearError() {
        _error.setValue(null);
    }
}