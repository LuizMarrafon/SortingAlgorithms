import java.io.IOException;
import java.io.RandomAccessFile;

public class Arquivo {
    private String nomearquivo;
    private RandomAccessFile arquivo;
    private int comp, mov;

    public Arquivo(String nomearquivo)
    {
        try
        {
            arquivo = new RandomAccessFile(nomearquivo, "rw");
        } catch (IOException e)
        { }
    }

    public void truncate(long pos) //desloca eof
    {
        try
        {
            arquivo.setLength(pos * Registro.length());
        } catch (IOException exc)
        { }
    }

    //semelhante ao feof() da linguagem C
    //verifica se o ponteiro esta no <EOF> do arquivo
    public boolean eof()
    {
        boolean retorno = false;
        try
        {
            if (arquivo.getFilePointer() == arquivo.length())
                retorno = true;
        } catch (IOException e)
        { }
        return (retorno);
    }

    public void exibir(){
        Registro aux = new Registro();
        seekArq(0);
        while(!eof()){
            aux.leDoArq(arquivo);
            System.out.print(aux.getNumero()+" ");
        }
    }
    //insere um Registro no final do arquivo, passado por par�metro
    public void inserirRegNoFinal(Registro reg)
    {
        seekArq(filesize());//ultimo byte
        reg.gravaNoArq(arquivo);
    }

    public int filesize()
    {
        try{
            return (int)arquivo.length()/Registro.length();
        }catch (IOException e)
        {  }
        return 0;
    }

    public void seekArq(int pos)
    {
        try
        {
            arquivo.seek(pos * Registro.length());
        } catch (IOException e)
        { }
    }

    public void fecharArq()
    {
        try
        {
            arquivo.close();
        } catch (IOException e)
        { }
    }

    //tem q testar sla
    public void insercao_direta(){
        int tl = filesize(), pos;
        Registro regi = new Registro();
        Registro regj = new Registro();
        for (int i = 1; i < tl; i++) {
            seekArq(i);
            regi.leDoArq(arquivo);
            pos = i;
            seekArq(pos-1);
            regj.leDoArq(arquivo);
            while(pos > 0 && regi.getNumero() < regj.getNumero()){
                seekArq(pos);
                regj.gravaNoArq(arquivo);
                pos--;
                seekArq(pos-1);
                regj.leDoArq(arquivo);
            }
            seekArq(pos);
            regi.gravaNoArq(arquivo);
        }
    }


    public void selecao_direta(){
        Registro regi = new Registro();
        Registro regj = new Registro();
        Registro regmenor = new Registro();
        int menor, tl = filesize(), posmenor;
        for (int i = 0; i < tl-1; i++){
            seekArq(i);
            regi.leDoArq(arquivo);
            menor = regi.getNumero();
            posmenor = i;
            for (int j = i+1; j < tl; j++) {
                regj.leDoArq(arquivo);
                if(regj.getNumero() < menor){
                    menor = regj.getNumero();
                    posmenor = j;
                }
            }
            seekArq(posmenor);
            regmenor.leDoArq(arquivo);
            seekArq(posmenor);
            regi.gravaNoArq(arquivo);
            seekArq(i);
            regmenor.gravaNoArq(arquivo);
        }
    }

    public void bubble_sort(){
        int tl = filesize();
        Registro regi = new Registro();
        Registro regj = new Registro();
        boolean flag = true;
        while(tl > 1 && flag){
            flag = false;
            for (int j = 0; j < tl-1; j++) {
                seekArq(j);
                regj.leDoArq(arquivo);
                seekArq(j+1);
                regi.leDoArq(arquivo);
                if(regj.getNumero() > regi.getNumero()){
                    seekArq(j);
                    regi.gravaNoArq(arquivo);
                    seekArq(j+1);
                    regj.gravaNoArq(arquivo);
                    flag = true;
                }
            }
            tl--;
        }
    }

    public void shake_sort(){
        int inicio = 0, fim = filesize()-1;
        Registro regi = new Registro();
        Registro regj = new Registro();
        boolean flag = true;
        while(inicio < fim && flag){
            flag = false;
            for (int i = inicio; i < fim; i++) {
                seekArq(i);
                regj.leDoArq(arquivo);
                seekArq(i+1);
                regi.leDoArq(arquivo);
                if(regj.getNumero() > regi.getNumero()){
                    seekArq(i);
                    regi.gravaNoArq(arquivo);
                    seekArq(i+1);
                    regj.gravaNoArq(arquivo);
                    flag = true;
                }
            }
            fim--;
            for (int i = fim; i > inicio; i--) {
                seekArq(i-1);
                regj.leDoArq(arquivo);
                seekArq(i);
                regi.leDoArq(arquivo);
                if(regj.getNumero() > regi.getNumero()){
                    seekArq(i-1);
                    regi.gravaNoArq(arquivo);
                    seekArq(i);
                    regj.gravaNoArq(arquivo);
                    flag = true;
                }
            }
            inicio++;
        }
    }

    public int buscaBinaria(int num, int tl){
        int inicio = 0, fim = tl-1, meio = (inicio + fim)/2;
        Registro reg = new Registro();
        seekArq(meio);
        reg.leDoArq(arquivo);
        while (inicio != fim && reg.getNumero() != num){
            if(reg.getNumero() > num )
                fim = meio - 1;
            else
                inicio = meio + 1;
            meio = (inicio + fim)/2;
            seekArq(meio);
            reg.leDoArq(arquivo);
        }
        if(num > reg.getNumero())
            return meio +1;
        return meio;
    }

    public void insercao_binaria(){
        int tl = filesize();
        int pos;
        Registro aux = new Registro();
        Registro reg = new Registro();
        for (int i = 1; i < tl; i++) {
            seekArq(i);
            aux.leDoArq(arquivo);
            pos = buscaBinaria(aux.getNumero(), i);
            for (int j = i; j > pos; j--) {
                seekArq(i-1);
                reg.leDoArq(arquivo);
                seekArq(j);
                reg.gravaNoArq(arquivo);
            }
            seekArq(pos);
            aux.gravaNoArq(arquivo);
        }
    }

    public void heap_sort(){
        int pai, f1, f2, tl = filesize(), maior;
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();
        while(tl > 1){
            for(pai = tl/2-1; pai >= 0; pai--){
                f1 = 2 * pai + 1;
                f2 = f1 + 1;
                maior = f1;
                seekArq(f1);
                reg1.leDoArq(arquivo);
                seekArq(f2);
                reg2.leDoArq(arquivo);
                if(f2 < tl && reg1.getNumero() < reg2.getNumero())
                    maior = f2;
                seekArq(maior);
                reg2.leDoArq(arquivo);
                seekArq(pai);
                reg1.leDoArq(arquivo);
                if(reg1.getNumero() < reg2.getNumero()){
                    seekArq(pai);
                    reg2.gravaNoArq(arquivo);
                    seekArq(maior);
                    reg1.gravaNoArq(arquivo);
                }
            }
            seekArq(0);
            reg1.leDoArq(arquivo);
            seekArq(tl-1);
            reg2.leDoArq(arquivo);
            seekArq(0);
            reg2.gravaNoArq(arquivo);
            seekArq(tl-1);
            reg1.gravaNoArq(arquivo);
            tl--;
        }
    }

    public void shell_sort(){
        int dist = 1, pos, tl = filesize();
        Registro reg = new Registro();
        Registro regdist = new Registro();
        while(dist < tl)
            dist = 3 * dist + 1;
        dist = dist/3;
        while(dist > 0){
            for(int i = dist; i < tl; i++){
                seekArq(i);
                reg.leDoArq(arquivo);
                pos = i;
                seekArq(pos - dist);
                regdist.leDoArq(arquivo);
                while(pos >= dist && reg.getNumero() < regdist.getNumero()){
                    seekArq(pos);
                    regdist.gravaNoArq(arquivo);
                    pos = pos - dist;
                    if(pos >= dist){
                        seekArq(pos - dist);
                        regdist.leDoArq(arquivo);
                    }
                }
                seekArq(pos);
                reg.gravaNoArq(arquivo);
            }
            dist = dist/3;
        }
    }

    // ============================= METODOS DE LIVRO ==========================================

    public void countingSort()
    {
        Registro regAux = new Registro();
        int tl = filesize(), maior, pos;

        // acha o maior numero do arquivo
        seekArq(0);
        regAux.leDoArq(arquivo);
        maior = regAux.getNumero();
        for (int i = 1; i < tl; i++)
        {
            seekArq(i);
            regAux.leDoArq(arquivo);
            if(regAux.getNumero() > maior)
                maior = regAux.getNumero();
        }

        // cria um vetor para contar as frequencias dos numero do arquivo
        int[] count = new int[maior + 1];

        // a quantidade (TL) de elementos dessa lista é maior+1
        // tem que colocar zero em tds as posicoes
        for(int i=0; i<maior+1; i++)
        {
            count[i] = 0;
        }

        // efetua a contagem de fato
        for (int i = 0; i < tl; i++)
        {
            seekArq(i);
            regAux.leDoArq(arquivo);
            count[regAux.getNumero()]++;
        }

        // faz a soma da posicao "x" + "x-1"
        for(int i = 1; i < maior+1; i++)
            count[i] = count[i] + count[i - 1];

        // ao fim dessa contagem, os elementos estão me dizendo que
        // "há X elementos menores ou iguais a Y", sendo que, X e Y:
        // X = valor da posicao // Y = propria posicao

        // agora tem que criar um novo arquivo que vai receber os valores de fato ordenados, ou seja
        // esse arquivo final tem o mesmo tamanho q o primeiro arquivo
        Arquivo arquivoAux = new Arquivo("arqAux.dat");
        arquivoAux.truncate(0); // limpa esse arq movendo o EOF
        arquivoAux.truncate(tl); // preenche com lixo os registros p eu poder usar

        // para isso, vai ser uma repeticao que comeca de tras para frente, como vai funcionar a ordenação:

        // sendo que X = valor do primeiro arquivo
        // vejo qual é o valor do numero que esta na posicao X do vetor, denominando esse valor de Y
        // coloco o X na posicao Y da terceira lista
        for (int i = tl - 1; i >= 0; i--)
        {
            seekArq(i);
            regAux.leDoArq(arquivo);
            pos = count[regAux.getNumero()] - 1;
            arquivoAux.seekArq(pos);
            regAux.gravaNoArq(arquivoAux.arquivo);

            count[regAux.getNumero()]--;
        }

        // por fim, basta copiar de volta para o arquivo original

        for (int i = 0; i < tl; i++)
        {
            arquivoAux.seekArq(i);
            regAux.leDoArq(arquivoAux.arquivo);

            seekArq(i);
            regAux.gravaNoArq(arquivo);
        }

        arquivoAux.fecharArq(); // fecha o arquivo
    }

    // seria uma melhora do bubble sort, pq aumenta o intervalo (gap)
    // no bolha sempre comparamos elementos vizinhos, ja aqui, podemos
    // comparar elementos com uma distancia maior entre eles, quem dita isso
    // é o gap, portanto, se gap == 1, eh literal um bolha
    public void combSort()
    {
        Registro regI = new Registro();
        Registro regG = new Registro();
        int tl = filesize(), gap = tl, aux;
        boolean flag = true; // aparece para controlar se teve troca ou não
        // é fato que o flag só importa de fato quando gap == 1, mas essa é a inteção mesmo
        // pq quando gap = 1 o algoritmo vira praticamente um bolha, entao tem q tomar cuidado
        // para nao deixar ele fazendo comparacoes inuteis
        double fe = 1.3; // fator de encolhimento, quanto menor, mais iteracoes
        while(gap > 1 || flag)
        {
            gap = (int)(gap/fe);
            if(gap < 1)
                gap = 1;

            //if(gap == 9 || gap == 10) NÃO é necessário colocar isso, mas segundo o estudo lá fica mais otimizado
            //    gap = 11;

            flag = false;
            for (int i = 0; i+gap < tl; i++)
            {
                seekArq(i);
                regI.leDoArq(arquivo);
                seekArq(i+gap);
                regG.leDoArq(arquivo);
                if(regI.getNumero() > regG.getNumero())
                {
                    aux = regI.getNumero();
                    regI.setNumero(regG.getNumero());
                    seekArq(i);
                    regI.gravaNoArq(arquivo);
                    regG.setNumero(aux);
                    seekArq(i+gap);
                    regG.gravaNoArq(arquivo);
                    flag = true;
                }
            }
        }
    }

    // também bem parecido com o bolha, mas quando efetua a troca
    // o elemento maior fica "marcado", para comparar os elementos
    // que vem antes dele e vai comparando de "tras para frente"
    public void gnomeSort()
    {
        Registro regPos = new Registro();
        Registro regPosAnt = new Registro(); // tem q criar o regPosAnt pq la na lista
        // era duplamente encadeada, daí tinha acesso ao nó anteior, já no arquivo
        // vou andando com os 2 registros
        int tl = filesize(), aux, pos = 1, posAnt = 0;
        seekArq(posAnt);
        regPosAnt.leDoArq(arquivo);
        regPos.leDoArq(arquivo);
        while (pos != tl) {
            if (pos == 0 || regPos.getNumero() >= regPosAnt.getNumero())
            {
                pos++;
                posAnt++;
                seekArq(posAnt);
                regPosAnt.leDoArq(arquivo);
                regPos.leDoArq(arquivo);
            }
            else
            {
                aux = regPosAnt.getNumero();
                regPosAnt.setNumero(regPos.getNumero());
                seekArq(posAnt);
                regPosAnt.gravaNoArq(arquivo);

                regPos.setNumero(aux);
                seekArq(pos);
                regPos.gravaNoArq(arquivo);

                posAnt--;
                pos--;
                seekArq(posAnt);
                regPosAnt.leDoArq(arquivo);
                regPos.leDoArq(arquivo);
            }
        }
    }

    // --------------- radix -----------------

    private int contaDigitosNum(int numero)
    {
        int contador = 0;
        if(numero != 0)
        {
            while(numero != 0)
            {
                numero = numero/10;
                contador++;
            }
            return contador;
        }
        return 1; // se o numero for zero retorna q ele tem 1 digito
    }

    private int obterDigito(int numero, int d)
    {
        int divisor = 1;

        for(int i = 1; i < d; i++)
            divisor = divisor * 10;

        return (numero / divisor) % 10;
    }

    private void countSortRadix(int d)
    {
        Registro regAux = new Registro();
        int pos, digito, tl = filesize();

        // não preciso achar o maior valor do arquivo inteira, pq
        // no radix o dígito sempre vai ser de 0 até 9 independente
        // entao o tamanho do vetor de contagem sempre vai ser de 10 posicoes

        int[] count = new int[10];
        for(int i = 0; i < 10; i++)
            count[i] = 0;

        // conto quantas vezes cada digito aparece na casa
        // "d" passada por parametro pelo radix

        for (int i = 0; i < tl; i++)
        {
            seekArq(i);
            regAux.leDoArq(arquivo);

            // pego o digito com base no numero do registro e do d
            digito = obterDigito(regAux.getNumero(), d);

            // segue a logica do counting
            count[digito]++;
        }


        // segue a logica do counting, q agora tem que fazer a soma
        for(int i = 1; i < 10; i++)
            count[i] = count[i] + count[i - 1];

        // agora tem que criar um novo arquivo que vai receber os valores de fato ordenados, ou seja
        // esse arquivo final tem o mesmo tamanho q o primeiro arquivo
        Arquivo arquivoAux = new Arquivo("arqAux.dat");
        arquivoAux.truncate(0); // limpa esse arq movendo o EOF
        arquivoAux.truncate(tl); // preenche com lixo os registros p eu poder usar


        for (int i = tl - 1; i >= 0 ; i--)
        {
            seekArq(i);
            regAux.leDoArq(arquivo);
            digito = obterDigito(regAux.getNumero(), d);
            pos = count[digito] - 1;

            arquivoAux.seekArq(pos);
            regAux.gravaNoArq(arquivoAux.arquivo);
            count[digito]--;
        }

        // por fim, basta copiar de volta para o arquivo original

        for (int i = 0; i < tl; i++)
        {
            arquivoAux.seekArq(i);
            regAux.leDoArq(arquivoAux.arquivo);

            seekArq(i);
            regAux.gravaNoArq(arquivo);
        }

        arquivoAux.fecharArq(); // fecha o arquivo
    }

    public void radixSort()
    {
        int d=0, tl = filesize();
        Registro regAux = new Registro();

        // devemos criar uma repeticao para achar o maior numero dessa lista
        // na verdade achar a quantidade maxima de digitos desse maior numero
        for (int i = 0; i < tl; i++)
        {
            seekArq(i);
            regAux.leDoArq(arquivo);
            if(contaDigitosNum(regAux.getNumero()) > d)
                d = contaDigitosNum(regAux.getNumero());
        }

        // d é a quantidade máxima de digitos dos elemtentos da lista

        // com isso, basta chamar algum método que vai ordenando por digito
        // o mais recomendavel é o counting, entretanto não poderia simplesmente
        // chamar o método de couting que criei acima, por que ele ordena
        // com base no valor inteiro, aqui no radix, queremos ir ordenando
        // cada vez por um único digito especifico, seja ele o menos significativo
        //  e por aí vai, por isso, a necessidade de criar um countSortRadix
        for (int i = 1; i <= d; i++)
            countSortRadix(i);
    }

    public void geraArquivoOrdenado() {
        truncate(0); // limpa o arquivo
        for (int i = 0; i < 1024; i++) {
            inserirRegNoFinal(new Registro(i));
        }
    }

    public void geraArquivoReverso() {
        truncate(0); // limpa o arquivo
        for (int i = 1023; i >= 0; i--) {
            inserirRegNoFinal(new Registro(i));
        }
    }

    public void geraArquivoRandomico() {
        truncate(0); // limpa o arquivo
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 8; i++) {
            inserirRegNoFinal(new Registro(random.nextInt(100)));
        }
    }
}
