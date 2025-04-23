package br.edu.ifsuldeminas.mch.calc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CalcApp";

    private TextView textViewResultado;
    private TextView textViewUltimaExpressao;
    private StringBuilder expressaoBuilder = new StringBuilder();
    private String ultimoResultado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Calc); // garante aplicação do tema
        setContentView(R.layout.activity_main);

        // Views
        textViewResultado = findViewById(R.id.textViewResultadoID);
        textViewUltimaExpressao = findViewById(R.id.textViewUltimaExpressaoID);

        int[] botoesNumericos = {
                R.id.buttonZeroID, R.id.buttonUmID, R.id.buttonDoisID,
                R.id.buttonTresID, R.id.buttonQuatroID, R.id.buttonCincoID,
                R.id.buttonSeisID, R.id.buttonSeteID, R.id.buttonOitoID, R.id.buttonNoveID
        };

        for (int i = 0; i < botoesNumericos.length; i++) {
            final String numero = String.valueOf(i);
            Button botao = findViewById(botoesNumericos[i]);
            botao.setOnClickListener(v -> adicionarNaExpressao(numero));
        }

        atribuirOperador(R.id.buttonSomaID, "+");
        atribuirOperador(R.id.buttonSubtracaoID, "-");
        atribuirOperador(R.id.buttonMultiplicacaoID, "*");
        atribuirOperador(R.id.buttonDivisaoID, "/");

        findViewById(R.id.buttonVirgulaID).setOnClickListener(v -> adicionarNaExpressao("."));

        findViewById(R.id.buttonPorcentoID).setOnClickListener(v -> aplicarPorcentagem());

        findViewById(R.id.buttonIgualID).setOnClickListener(v -> calcularResultado());

        findViewById(R.id.buttonResetID).setOnClickListener(v -> {
            expressaoBuilder.setLength(0);
            textViewResultado.setText("0");
            textViewUltimaExpressao.setText("");
            ultimoResultado = "";
        });
        findViewById(R.id.buttonDeleteID).setOnClickListener(v -> {
            int length = expressaoBuilder.length();
            if (length > 0) {
                expressaoBuilder.deleteCharAt(length - 1);
                textViewUltimaExpressao.setText(expressaoBuilder.toString());
            }
        });
    }

    private void atribuirOperador(int botaoId, String operador) {
        Button botao = findViewById(botaoId);
        botao.setOnClickListener(v -> adicionarNaExpressao(operador));
    }

    private void adicionarNaExpressao(String valor) {
        if (!ultimoResultado.isEmpty() && expressaoBuilder.length() == 0) {
            expressaoBuilder.append(ultimoResultado);
        }
        expressaoBuilder.append(valor);
        textViewUltimaExpressao.setText(expressaoBuilder.toString());
    }
    private void aplicarPorcentagem() {
        try {
            String textoAtual = expressaoBuilder.toString();
            if (textoAtual.isEmpty()) return;

            double valor = Double.parseDouble(textoAtual);
            double porcentagem = valor / 100.0;

            expressaoBuilder.setLength(0);
            expressaoBuilder.append(porcentagem);
            textViewUltimaExpressao.setText(expressaoBuilder.toString());

        } catch (Exception e) {
            Log.e(TAG, "Erro ao aplicar porcentagem: " + e.getMessage());
            textViewResultado.setText("Erro");
        }
    }
    private void calcularResultado() {
        String expressao = expressaoBuilder.toString();

        try {
            expressao = expressao.replace("÷", "/");
            Calculable calculo = new ExpressionBuilder(expressao).build();
            double resultado = calculo.calculate();

            textViewUltimaExpressao.setText(expressao);
            textViewResultado.setText(String.valueOf(resultado));
            ultimoResultado = String.valueOf(resultado);
            expressaoBuilder.setLength(0);

        } catch (Exception e) {
            Log.e(TAG, "Erro ao calcular: " + e.getMessage());
            textViewResultado.setText("Erro");
        }
    }
}