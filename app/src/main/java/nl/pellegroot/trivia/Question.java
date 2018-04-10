package nl.pellegroot.trivia;


import java.io.Serializable;

public class Question implements Serializable {

    String question;
    int category_id;
    String correctAnswer;

    public String getQuestion() {
        return question;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
