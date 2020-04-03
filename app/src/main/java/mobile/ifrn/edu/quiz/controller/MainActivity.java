package mobile.ifrn.edu.quiz.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import mobile.ifrn.edu.quiz.R;
import mobile.ifrn.edu.quiz.model.AnalisadorQuestao;
import mobile.ifrn.edu.quiz.model.Questao;
import mobile.ifrn.edu.quiz.model.QuestaoRepositorio;

public class MainActivity extends AppCompatActivity {

    public static final String INDICE_QUESTAO = "INDICE_QUESTAO";
    private final Locale locale = new Locale("pt", "BR");
    private QuestaoRepositorio repositorio = new QuestaoRepositorio();
    private int indiceQuestao = 0;
    private TextView textViewTextoPergunta;
    private Button botaoResposta1;
    private Button botaoResposta2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Questao questao = repositorio.getListaQuestoes().get(indiceQuestao);

        textViewTextoPergunta = findViewById(R.id.texto_pergunta_textview);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String resposta = ((Button)view).getText().toString();

                AnalisadorQuestao analisadorQuestao = new AnalisadorQuestao();
                Questao questao = repositorio.getListaQuestoes().get(indiceQuestao);

                String mensagem;
                try {
                    NumberFormat format = NumberFormat.getInstance(locale);
                    Number number = format.parse(resposta);

                    if (analisadorQuestao.isRespostaCorreta(questao, number.doubleValue())) {
                        mensagem = "Parabéns, resposta correta!";
                    } else {
                        mensagem = "Aah, resposta errada :(";
                    }
                } catch (ParseException e) {
                    mensagem = e.getMessage();
                }

                Toast.makeText(MainActivity.this, mensagem, Toast.LENGTH_SHORT).show();
            }
        };

       botaoResposta1 = findViewById(R.id.button_resposta_correta);
       botaoResposta1.setOnClickListener(listener);

        botaoResposta2 = findViewById(R.id.button_resposta_incorreta);
        botaoResposta2.setOnClickListener(listener);

        View.OnClickListener listenerProximaPergunta = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indiceQuestao++;

                if (indiceQuestao >= repositorio.getListaQuestoes().size()) {
                    indiceQuestao = 0;
                }
                exibirQuestao(indiceQuestao);
            }
        };
        Button botaoProximaPergunta = findViewById(R.id.button_proxima_pergunta);
        botaoProximaPergunta.setOnClickListener(listenerProximaPergunta);

        if (savedInstanceState != null) {
            indiceQuestao = savedInstanceState.getInt(INDICE_QUESTAO);
        }
        exibirQuestao(indiceQuestao);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(INDICE_QUESTAO, indiceQuestao);
    }

    public void exibirQuestao(final int indice_questao) {
        Questao questao = repositorio.getListaQuestoes().get(indice_questao);

        textViewTextoPergunta.setText(questao.getTexto());

        String respostaCorreta = String.format(locale, "%.2f", questao.getRespostaCorreta());
        String respostaIncorreta = String.format(locale, "%.2f", questao.getRespostaIncorreta());

        botaoResposta1.setText(respostaCorreta);
        botaoResposta2.setText(respostaIncorreta);
    }
}
