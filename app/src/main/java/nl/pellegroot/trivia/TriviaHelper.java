package nl.pellegroot.trivia;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TriviaHelper implements Response.Listener<JSONArray>, Response.ErrorListener {
    public Context context;
    public Callback activity;

    public interface Callback{
        void gotQuestion(Question question);
        void gotError(String Error);
    }

    // the constructor
    public TriviaHelper(Context activityContext){
        context = activityContext;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("onErrorResponse: ", " " + error);
        activity.gotError(error.getMessage());
    }

    @Override
    public void onResponse(JSONArray response) {
        Question question = new Question();

        try{

            for(int i=0; i<response.length(); i++){
                JSONObject jsonObject = response.getJSONObject(i);
                question.setQuestion(jsonObject.getString("question"));
                question.setDificulty(jsonObject.getInt("difficulty"));
                question.setCorrectAnswer(jsonObject.getString("answer"));
                question.setCategory_id(jsonObject.getInt("category_id"));
            }

        }catch(JSONException e){
            e.printStackTrace();
        }
        activity.gotQuestion(question);
    }

    public void getQuestion(Callback activityGame){
        activity = activityGame;

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://jservice.io/api/random", this, this);
        queue.add(jsonArrayRequest);
    }
}
