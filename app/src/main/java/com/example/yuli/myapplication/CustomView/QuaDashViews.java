package com.example.yuli.myapplication.CustomView;

public class QuaDashViews {

    private QuaDashBoardView first_qua_view;
    private QuaDashBoardView second_qua_view;
    private QuaDashBoardView third_qua_view;
    private QuaDashBoardView fourth_qua_view;

    private static QuaDashViews instance;

    public QuaDashViews getInstance(){
        if (instance == null){
            instance = new QuaDashViews();
        }
        return instance;
    }

    public QuaDashBoardView getFirst_qua_view() {
        return first_qua_view;
    }

    public void setFirst_qua_view(QuaDashBoardView first_qua_view) {
        this.first_qua_view = first_qua_view;
    }

    public QuaDashBoardView getSecond_qua_view() {
        return second_qua_view;
    }

    public void setSecond_qua_view(QuaDashBoardView second_qua_view) {
        this.second_qua_view = second_qua_view;
    }

    public QuaDashBoardView getThird_qua_view() {
        return third_qua_view;
    }

    public void setThird_qua_view(QuaDashBoardView third_qua_view) {
        this.third_qua_view = third_qua_view;
    }

    public QuaDashBoardView getFourth_qua_view() {
        return fourth_qua_view;
    }

    public void setFourth_qua_view(QuaDashBoardView fourth_qua_view) {
        this.fourth_qua_view = fourth_qua_view;
    }
}