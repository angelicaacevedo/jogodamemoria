/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp3.jogodamemoria;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author everton
 */
public class JogoDaMemoriaController implements Initializable {

    @FXML
    private Pane pane_tabuleiro;

    @FXML
    private Label lb_venceu;

    @FXML
    private Label lb_vezJogador;

    @FXML
    private Label lb_numVitJ1;

    @FXML
    private Label lb_numVitJ2;

    //Variáveis para controle do jogo
    //Essas são as variáveis que eu criei para desenolvover a lógica do jogo
    //Você pode criar outras caso ache necessário.
    //Coloque uma descrição da utilização dessa variável no código
    private int vetorCartas[]; //Armazena as cartas do jogo
    private final static int NUM_CARTAS = 10; //Número de cartas. Neste caso sempre será 10
    private int idAtJogador; //Armazana o id atual do jogador (1 ou 2)
    private int numVitoriasJ1; //Armazena o número de vitórias de jogador 1
    private int numVitoriasJ2; //Armazena o número de vitórias de jogador 2
    private int jogadas; //Armazena o número jogadas. A cada 2 jogadas, verifa-se se o jogador acertou as 2 cartas
    private int parCartas[]; //Armazena as 2 cartas viradas pelo jogador atual
    private int pontosJ1; //Armazena o pontos do jogador 1
    private int pontosJ2; //Armazena o pontos do jogador 2

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //NÃO ALTERAR
        novoJogo();
    }

    //Ao pressionar qualquer botão (célula) do tabuleiro, este método será chamado.
    @FXML
    public void jogar(ActionEvent event) {

        //Obter o índice do botão e setar o texto com base no vetor de cartas
        Button bt = (Button) event.getSource();
        int ind = pane_tabuleiro.getChildren().indexOf(bt);
        bt.setText(String.valueOf(vetorCartas[ind]));

        //Alterar cor do texto para identificar o jogador
        if (idAtJogador == 1) {
            bt.setTextFill(Color.web("#227dec"));
        } else {
            bt.setTextFill(Color.web("#f27b7b"));
        }

        //Armazenar o índice do botão pressionado no vetor parCartas
        //Pode-se utilizar a variável jogadas para indexar o vetor
        parCartas[jogadas] = ind;

        //Atualizar variável jogadas
        jogadas++;

        //Se o número de jogadas for igual a 2, deve-se analisar o par virado pelo jogador
        if (jogadas == 2) {

            analisarPar();

            //Atualizar a variável idAtJogador
            if (idAtJogador == 1) {
                idAtJogador = 2;
            } else {
                idAtJogador = 1;
            }

            //Atualizar o label lb_vezJogador
            lb_vezJogador.setText("Vez do jogador " + idAtJogador);
        }

        //Verificar vencedor
        verificarVencedor();
    }

    // Método para reiniciar o jogo
    @FXML
    public void novoJogo() {

        // Reiniciar as variáveis de controle do jogo. Por exemplo: idAt = 1;
        idAtJogador = 1;
        pontosJ1 = 0;
        pontosJ2 = 0;
        jogadas = 0;

        //Setar lb_venceu como vazio
        lb_venceu.setText("");


        //Resetar lb_vezJogador para o jogador 1
        lb_vezJogador.setText("Agora é a vez do Jogador 1");


        //Limpar o tabuleiro - botões
        //O método getChildren retorna uma lista de Node
        //Use downcast para converter um objeto Node para Button
        List<Node> nodes = pane_tabuleiro.getChildren();
        for (Node node : nodes) {
            if (node instanceof Button) {
                //Use os métodos setText e setDisable
                Button bt = (Button) node;
                bt.setText("");
                bt.setDisable(false);
            }
        }

        //Limpar o vetor de cartas, ou seja, instânciar novamente
        vetorCartas = new int[NUM_CARTAS];


        //Inicializar o vetor de cartas com os valores possíveis
        //vetorCartas = {1,1,2,2,3,3,4,4,5,5}
        int tamanho = NUM_CARTAS / 2;
        for (int i = 0; i < tamanho; i++) {
            vetorCartas[i] = i + 1;
            vetorCartas[i + tamanho] = i + 1;
        }


        //Embaralhar as cartas usando um objeto da classe Random
        Random random = new Random();
        for (int i = 0; i < vetorCartas.length; i++) {
            int randomIndex = random.nextInt(vetorCartas.length);
            int aux = vetorCartas[i];
            vetorCartas[i] = vetorCartas[randomIndex];
            vetorCartas[randomIndex] = aux;
        }

        //Você pode por exemplo trocar as cartas de lugar N vezes
        for (int i = 0; i < NUM_CARTAS; i++) {
            int index1 = random.nextInt(NUM_CARTAS);
            int index2 = random.nextInt(NUM_CARTAS);

            if (index1 != index2) {
                int aux = vetorCartas[index1];
                vetorCartas[index1] = vetorCartas[index2];
                vetorCartas[index2] = aux;
            }
        }

        //Instânciar o vetor parCartas, o qual controla as cartão viradas pelo jogador da vez
        parCartas = new int[2];
    }

    @FXML
    public void sairDoJogo() {
        //Obter o stage (palco) através de um elemento gráfico
        //Utilizar o método close para fechar a aplicação
        Stage stage = (Stage) pane_tabuleiro.getScene().getWindow();
        stage.close();
    }

    //Método para analisar a jogada
    //Caso as duas cartas sejam iguais, o jogador atual recebe 1 ponto
    private void analisarPar() {

        //Verificar se as 2 cartas viradas são iguais
        if (vetorCartas[parCartas[0]] == vetorCartas[parCartas[1]]) {

            //Com base no id do jogador atual, atualuzar a variável pontosJ1 ou pontosJ2
            if (idAtJogador == 1) {
                pontosJ1++;
            } else {
                pontosJ2++;
            }
            //Desabilirar botões
            pane_tabuleiro.getChildren().get(parCartas[0]).setDisable(true);
            pane_tabuleiro.getChildren().get(parCartas[1]).setDisable(true);
        } else {
            //Alterar texto dos botõs como vazio ""
            Button bt = (Button) pane_tabuleiro.getChildren().get(parCartas[0]);
            bt.setText("");
            bt = (Button) pane_tabuleiro.getChildren().get(parCartas[1]);
            bt.setText("");
        }

        //Zerar variável jogadas
        jogadas = 0;

        //Criar nova instância do vetor indicePar
        parCartas = new int[2];
    }

    private void verificarVencedor() {
        //Quando a soma de pontuação dos jogadores for 5, o jogo acabou
        //Vence quem tiver feito mais pontos
        //Aqui é preciso chamar a função atualizarScore passando o id (1 ou 2) do jogador que venceu

        if (pontosJ1 + pontosJ2 == 5) {
            int idVencedor = 0;

            if (pontosJ1 > pontosJ2) {
                idVencedor = 1;
            } else {
                idVencedor = 2;
            }
            atualizarScore(idVencedor);
        }
    }

    //Método para atualizar o placar (número de vitórias e empates)
    private void atualizarScore(int idJogador) {

        //Atualizar lb_venceu
        if (pontosJ1 > pontosJ2) {
            lb_venceu.setText("O Jogador 1 VENCEU!!");
        } else {
            lb_venceu.setText("O Jogador 2 VENCEUU!!");
        }

        //Atualizar lb_numVitJ1 ou lb_numVitJ2 e incrementar numVitoriasJ1 ou numVitoriasJ2
        if (idJogador == 1) {
            numVitoriasJ1++;
            lb_numVitJ1.setText(Integer.toString(numVitoriasJ1));
        } else if (idJogador == 2) {
            numVitoriasJ2++;
            lb_numVitJ2.setText(Integer.toString(numVitoriasJ2));
        }

        //NÃO ALTERAR
        aplicarAnimacao();

    }

    //NÃO ALTERAR
    //Método para aplicar uma animação quando o jogo acaba
    private void aplicarAnimacao() {
        TranslateTransition animacao = new TranslateTransition(Duration.seconds(1), lb_venceu);
        animacao.setByY(-40); // Define a quantidade de deslocamento vertical como -50 pixels
        animacao.setCycleCount(4); // Define a quantidade de saltos como 4
        animacao.setAutoReverse(true); // Faz a animação retornar ao ponto inicial após terminar
        animacao.play(); // Executa a animação
    }
}
