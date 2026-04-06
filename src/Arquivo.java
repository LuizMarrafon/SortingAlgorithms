import java.io.IOException;
import java.io.RandomAccessFile;

public class Arquivo
{
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

    public void copiaArquivo(Arquivo arq)
    {
        Registro aux = new Registro();
        arq.seekArq(0);
        arq.truncate(0);
        seekArq(0);
        while(!eof())
        {
            aux.leDoArq(arquivo);
            aux.gravaNoArq(arq.arquivo);
        }
    }

    public RandomAccessFile getFile() {
        return arquivo;
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

    public void exibir()
    {
        Registro aux = new Registro();
        seekArq(0);
        while(!eof())
        {
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

    public void insercao_direta()
    {
        int tl = filesize(), pos;
        Registro regi = new Registro();
        Registro regj = new Registro();
        for (int i = 1; i < tl; i++)
        {

            seekArq(i);
            regi.leDoArq(arquivo);
            pos = i;
            seekArq(pos-1);
            regj.leDoArq(arquivo);
            comp++;
            while(pos > 0 && regi.getNumero() < regj.getNumero()){
                comp++;
                mov++;
                seekArq(pos);
                regj.gravaNoArq(arquivo);
                pos--;
                if(pos > 0){
                    seekArq(pos - 1);
                    regj.leDoArq(arquivo);
                }
            }
            mov++;
            seekArq(pos);
            regi.gravaNoArq(arquivo);
        }
    }


    public void selecao_direta()
    {
        Registro regi = new Registro();
        Registro regj = new Registro();
        Registro regmenor = new Registro();
        int menor, tl = filesize(), posmenor;
        for (int i = 0; i < tl-1; i++)
        {
            seekArq(i);
            regi.leDoArq(arquivo);
            menor = regi.getNumero();
            posmenor = i;
            for (int j = i+1; j < tl; j++)
            {
                regj.leDoArq(arquivo);
                comp++;
                if(regj.getNumero() < menor)
                {
                    menor = regj.getNumero();
                    posmenor = j;
                }
            }
            mov++;
            mov++;
            seekArq(posmenor);
            regmenor.leDoArq(arquivo);
            seekArq(posmenor);
            regi.gravaNoArq(arquivo);
            seekArq(i);
            regmenor.gravaNoArq(arquivo);
        }
    }

    public void bubble_sort()
    {
        int tl = filesize();
        Registro regi = new Registro();
        Registro regj = new Registro();
        boolean flag = true;
        while(tl > 1 && flag)
        {
            flag = false;
            for (int j = 0; j < tl-1; j++)
            {
                seekArq(j);
                regj.leDoArq(arquivo);
                seekArq(j+1);
                regi.leDoArq(arquivo);
                comp++;
                if(regj.getNumero() > regi.getNumero())
                {
                    mov++;
                    mov++;
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

    public void shake_sort()
    {
        int inicio = 0, fim = filesize()-1;
        Registro regi = new Registro();
        Registro regj = new Registro();
        boolean flag = true;
        while(inicio < fim && flag){
            flag = false;
            for (int i = inicio; i < fim; i++)
            {
                seekArq(i);
                regj.leDoArq(arquivo);
                seekArq(i+1);
                regi.leDoArq(arquivo);
                comp++;
                if(regj.getNumero() > regi.getNumero())
                {
                    mov++;
                    mov++;
                    seekArq(i);
                    regi.gravaNoArq(arquivo);
                    seekArq(i+1);
                    regj.gravaNoArq(arquivo);
                    flag = true;
                }
            }
            fim--;
            for (int i = fim; i > inicio; i--)
            {
                seekArq(i-1);
                regj.leDoArq(arquivo);
                seekArq(i);
                regi.leDoArq(arquivo);
                comp++;
                if(regj.getNumero() > regi.getNumero())
                {
                    mov++;
                    mov++;
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

    public int buscaBinaria(int num, int tl)
    {
        int inicio = 0, fim = tl-1, meio = (inicio + fim)/2;
        Registro reg = new Registro();
        seekArq(meio);
        reg.leDoArq(arquivo);
        comp++;
        while (inicio < fim && reg.getNumero() != num)
        {
            comp++;
            comp++;
            if(reg.getNumero() > num)
                fim = meio - 1;
            else
                inicio = meio + 1;
            meio = (inicio + fim)/2;
            seekArq(meio);
            reg.leDoArq(arquivo);
        }
        comp++;
        if(num > reg.getNumero())
            return meio +1;
        return meio;
    }

    public void insercao_binaria()
    {
        int tl = filesize();
        int pos;
        Registro aux = new Registro();
        Registro reg = new Registro();
        for (int i = 1; i < tl; i++)
        {
            seekArq(i);
            aux.leDoArq(arquivo);
            pos = buscaBinaria(aux.getNumero(), i);
            for (int j = i; j > pos; j--)
            {
                mov++;
                seekArq(j-1);
                reg.leDoArq(arquivo);
                seekArq(j);
                reg.gravaNoArq(arquivo);
            }
            mov++;
            seekArq(pos);
            aux.gravaNoArq(arquivo);
        }
    }

    public void heap_sort()
    {
        int pai, f1, f2, tl = filesize(), maior;
        int total = tl;
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();
        while(tl > 1){
            for(pai = tl/2-1; pai >= 0; pai--)
            {
                f1 = 2 * pai + 1;
                f2 = f1 + 1;
                maior = f1;
                seekArq(f1);
                reg1.leDoArq(arquivo);
                if(f2 < tl){
                    seekArq(f2);
                    reg2.leDoArq(arquivo);
                    comp++;
                    if(reg1.getNumero() < reg2.getNumero())
                        maior = f2;
                }
                seekArq(maior);
                reg2.leDoArq(arquivo);
                seekArq(pai);
                reg1.leDoArq(arquivo);
                comp++;
                if(reg1.getNumero() < reg2.getNumero())
                {
                    mov++;
                    mov++;
                    seekArq(pai);
                    reg2.gravaNoArq(arquivo);
                    seekArq(maior);
                    reg1.gravaNoArq(arquivo);
                }
            }
            mov++;
            mov++;
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

    public void shell_sort()
    {
        int dist = 1, pos, tl = filesize();
        Registro reg = new Registro();
        Registro regdist = new Registro();
        while(dist < tl)
            dist = 3 * dist + 1;
        dist = dist/3;
        while(dist > 0)
        {
            for(int i = dist; i < tl; i++)
            {
                seekArq(i);
                reg.leDoArq(arquivo);
                pos = i;
                seekArq(pos - dist);
                regdist.leDoArq(arquivo);
                comp++;
                while(pos >= dist && reg.getNumero() < regdist.getNumero())
                {
                    comp++;
                    seekArq(pos);
                    mov++;
                    regdist.gravaNoArq(arquivo);
                    pos = pos - dist;
                    if(pos >= dist)
                    {
                        seekArq(pos - dist);
                        regdist.leDoArq(arquivo);
                    }
                }
                seekArq(pos);
                mov++;
                reg.gravaNoArq(arquivo);
            }
            dist = dist/3;
        }
    }

    // ------------------- QUICK SEM PIVO -------------------

    public void quickSemPivo(){
        int tl = filesize();
        if (tl > 1)
            quickSP(0, tl-1);
    }

    public void quickSP(int ini, int fim)
    {
        int i = ini, j = fim;
        Registro regI = new Registro();
        Registro regJ = new Registro();
        boolean flag = true;
        while(i < j)
        {
            seekArq(i);
            regI.leDoArq(arquivo);
            seekArq(j);
            regJ.leDoArq(arquivo);
            if(flag)
            {
                comp++;
                while(i < j && regI.getNumero() <= regJ.getNumero())
                {
                    comp++;
                    i++;
                    seekArq(i);
                    regI.leDoArq(arquivo);
                }
            }
            else
            {
                comp++;
                while(i < j && regJ.getNumero() >= regI.getNumero())
                {
                    comp++;
                    j--;
                    seekArq(j);
                    regJ.leDoArq(arquivo);
                }
            }
            mov++;
            mov++;
            seekArq(i);
            regJ.gravaNoArq(arquivo);
            seekArq(j);
            regI.gravaNoArq(arquivo);
            flag = !flag;
        }
        if(ini < i-1)
            quickSP(ini, i-1);
        if(j+1 < fim)
            quickSP(j+1, fim);
    }

    // ------------------- QUICK COM PIVO -------------------

    public void quickComPivo(){
        int tl = filesize();
        if (tl > 1)
            quickCP(0, tl-1);
    }

    public void quickCP(int ini, int fim)
    {
        int i = ini, j = fim, pivo;
        Registro regI = new Registro();
        Registro regJ = new Registro();
        seekArq((ini+fim)/2);
        regI.leDoArq(arquivo);
        pivo = regI.getNumero();
        while(i < j)
        {
            seekArq(i);
            regI.leDoArq(arquivo);
            seekArq(j);
            regJ.leDoArq(arquivo);

            while(regI.getNumero() < pivo)
            {
                comp++;
                i++;
                seekArq(i);
                regI.leDoArq(arquivo);
            }
            comp++;

            while(regJ.getNumero() > pivo)
            {
                comp++;
                j--;
                seekArq(j);
                regJ.leDoArq(arquivo);
            }
            comp++;

        if(i <= j)
        {
            mov++;
            mov++;
            seekArq(i);
                regJ.gravaNoArq(arquivo);
                seekArq(j);
                regI.gravaNoArq(arquivo);
                i++;
                j--;
            }
        }
        if(ini < j)
            quickCP(ini, j);
        if(i < fim)
            quickCP(i, fim);
    }

    // -------------- MERGE PRIMEIRA IMPLEMENTACAO ----------------

    public void particao(RandomAccessFile arquivo1, RandomAccessFile arquivo2)
    {
        int meio = filesize()/2;
        Registro reg = new Registro();
        int j = meio;
        for (int i = 0; i < meio; i++)
        {
            mov++;
            mov++;
            seekArq(i);
            reg.leDoArq(arquivo);
            reg.gravaNoArq(arquivo1);
            seekArq(j);
            reg.leDoArq(arquivo);
            reg.gravaNoArq(arquivo2);
            j++;
        }
    }

    public void merge(Arquivo arquivo1, Arquivo arquivo2, int seq)
    {
        int i=0, j = 0, k = 0, tam_seq = seq;
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();
        while(k < filesize())
        {
            while(i < seq && j < seq)
            {
                arquivo1.seekArq(i);
                reg1.leDoArq(arquivo1.arquivo);
                arquivo2.seekArq(j);
                reg2.leDoArq(arquivo2.arquivo);
                comp++;
                if(reg1.getNumero() < reg2.getNumero())
                {
                    mov++;
                    seekArq(k++);
                    reg1.gravaNoArq(arquivo);
                    i++;
                }
                else
                {
                    mov++;
                    seekArq(k++);
                    reg2.gravaNoArq(arquivo);
                    j++;
                }
            }

            while(i < seq)
            {
                mov++;
                arquivo1.seekArq(i++);
                reg1.leDoArq(arquivo1.arquivo);
                seekArq(k++);
                reg1.gravaNoArq(arquivo);
            }

            while(j < seq)
            {
                mov++;
                arquivo2.seekArq(j++);
                reg2.leDoArq(arquivo2.arquivo);
                seekArq(k++);
                reg2.gravaNoArq(arquivo);
            }
            seq = seq + tam_seq;
        }
    }

    public void merge_sort()
    {
        Arquivo arquivo1 = new Arquivo("arquivo1.dat");
        Arquivo arquivo2 = new Arquivo("arquivo2.dat");
        int seq = 1, tl = filesize();
        while(seq < tl)
        {
            arquivo1.truncate(0);
            arquivo2.truncate(0);
            particao(arquivo1.arquivo, arquivo2.arquivo);
            merge(arquivo1, arquivo2, seq);
            seq = seq * 2;
        }
    }

    // -------------- MERGE SEGUNDA IMPLEMENTACAO ----------------
    public void mergeSort_segunda()
    {
        int tl = filesize();
        Arquivo aux = new Arquivo("arqAux.dat");
        aux.truncate(0);

        mergeSegunda(0, tl - 1, aux);
        aux.fecharArq();
    }

    private void mergeSegunda(int esq, int dir, Arquivo aux)
    {
        if (esq < dir)
        {
            int meio = (esq + dir) / 2;

            mergeSegunda(esq, meio, aux);
            mergeSegunda(meio + 1, dir, aux);
            fusaoSegunda(esq, meio, meio + 1, dir, aux);
        }
    }

    private void fusaoSegunda(int ini1, int fim1, int ini2, int fim2, Arquivo aux)
    {
        int i = ini1, j = ini2, k = 0;
        Registro regEsq = new Registro();
        Registro regDir = new Registro();
        Registro regAux = new Registro();

        while (i <= fim1 && j <= fim2)
        {
            seekArq(i);
            regEsq.leDoArq(arquivo);
            seekArq(j);
            regDir.leDoArq(arquivo);

            comp++;
            if (regEsq.getNumero() < regDir.getNumero())
            {
                mov++;
                aux.seekArq(k);
                regEsq.gravaNoArq(aux.arquivo);
                i++;
            }
            else
            {
                mov++;
                aux.seekArq(k);
                regDir.gravaNoArq(aux.arquivo);
                j++;
            }
            k++;
        }

        while (i <= fim1)
        {
            seekArq(i);
            regEsq.leDoArq(arquivo);
            mov++;
            aux.seekArq(k);
            regEsq.gravaNoArq(aux.arquivo);
            i++;
            k++;
        }

        while (j <= fim2)
        {
            seekArq(j);
            regDir.leDoArq(arquivo);
            mov++;
            aux.seekArq(k);
            regDir.gravaNoArq(aux.arquivo);
            j++;
            k++;
        }

        for (i = 0; i < k; i++)
        {
            aux.seekArq(i);
            regAux.leDoArq(aux.arquivo);
            mov++;
            seekArq(ini1+i);
            regAux.gravaNoArq(arquivo);
        }
    }

    // ======================== METODOS PESQUISADOS EM LIVROS ====================

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
            comp++;
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
            mov++;
            regAux.gravaNoArq(arquivoAux.arquivo);

            count[regAux.getNumero()]--;
        }

        // por fim, basta copiar de volta para o arquivo original

        for (int i = 0; i < tl; i++)
        {
            arquivoAux.seekArq(i);
            regAux.leDoArq(arquivoAux.arquivo);

            mov++;
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
                comp++;
                if(regI.getNumero() > regG.getNumero())
                {
                    mov++;
                    mov++;
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
            if(pos != 0)
                comp++;
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
                mov++;
                regPosAnt.gravaNoArq(arquivo);

                regPos.setNumero(aux);
                seekArq(pos);
                mov++;
                regPos.gravaNoArq(arquivo);

                posAnt--;
                pos--;
                seekArq(posAnt);
                regPosAnt.leDoArq(arquivo);
                regPos.leDoArq(arquivo);
            }
        }
    }

    // --------------- RADIX -------------------

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
            mov++;
            regAux.gravaNoArq(arquivoAux.arquivo);
            count[digito]--;
        }

        // por fim, basta copiar de volta para o arquivo original

        for (int i = 0; i < tl; i++)
        {
            arquivoAux.seekArq(i);
            regAux.leDoArq(arquivoAux.arquivo);

            seekArq(i);
            mov++;
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

    // --------------- BUCKET -------------------

    public void bucket_sort()
    {
        Registro reg = new Registro();
        int maior = 0, menor = 0, i = 0, intervalo, pos;
        int quantidadeBuckets = (int)Math.sqrt(filesize());

        if(quantidadeBuckets == 0)
            quantidadeBuckets = 1;

        Arquivo[] buckets = new Arquivo[quantidadeBuckets];

        for(i = 0; i < quantidadeBuckets; i++)
        {
            buckets[i] = new Arquivo("bucket"+i+".dat");
            buckets[i].truncate(0);
        }

        seekArq(i);
        reg.leDoArq(arquivo);
        menor = maior = reg.getNumero();
        for(i = 1; i < filesize(); i++)
        {
            seekArq(i);
            reg.leDoArq(arquivo);
            comp++;
            if(reg.getNumero() > maior)
                maior = reg.getNumero();

            comp++;
            if(reg.getNumero() < menor)
                menor = reg.getNumero();
        }

        intervalo = (maior - menor + 1)/quantidadeBuckets;

        for (i = 0; i < filesize(); i++)
        {
            reg = new Registro();
            seekArq(i);
            reg.leDoArq(arquivo);
            if (intervalo == 0)
            {
                mov++;
                reg.gravaNoArq(buckets[0].arquivo);
            }
            else
            {
                pos = (reg.getNumero() - menor) / intervalo;
                if (pos >= quantidadeBuckets)
                    pos = quantidadeBuckets - 1;
                mov++;
                reg.gravaNoArq(buckets[pos].arquivo);
            }
        }

        for(i = 0; i < quantidadeBuckets; i++)
        {
            for(int j = 1; j < buckets[i].filesize(); j++)
            {
                Registro aux = new Registro();
                buckets[i].seekArq(j);
                aux.leDoArq(buckets[i].arquivo);
                int p = j;
                buckets[i].seekArq(p - 1);
                Registro regp = new Registro();
                regp.leDoArq(buckets[i].arquivo);
                comp++;
                while(p > 0 && aux.getNumero() < regp.getNumero())
                {
                    comp++;
                    mov++;
                    regp.gravaNoArq(buckets[i].arquivo);
                    buckets[i].seekArq(p - 1);
                    regp.leDoArq(buckets[i].arquivo);
                    p--;
                }
                buckets[i].seekArq(p);
                mov++;
                aux.gravaNoArq(buckets[i].arquivo);
            }
        }

        seekArq(0);
        for(i = 0; i < quantidadeBuckets; i++)
        {
            for(int j = 0; j < buckets[i].filesize(); j++)
            {
                Registro aux = new Registro();
                buckets[i].seekArq(j);
                aux.leDoArq(buckets[i].arquivo);
                mov++;
                aux.gravaNoArq(arquivo);
            }
        }
    }

    // ---------------------- TIM --------------------------------
    private void MergeTim(int esquerda, int meio, int direita)
    {
        int tlLista1 = meio - esquerda + 1;
        int tlLista2 = direita - meio;
        int i = 0, j = 0, pos = esquerda;

        Registro regEsq = new Registro();
        Registro regDir = new Registro();

        Arquivo arq1 = new Arquivo("tim1.dat");
        Arquivo arq2 = new Arquivo("tim2.dat");
        arq1.truncate(0);
        arq2.truncate(0);

        for (int k = 0; k < tlLista1; k++)
        {
            seekArq(esquerda + k);
            regEsq.leDoArq(arquivo);
            mov++;
            arq1.seekArq(k);
            regEsq.gravaNoArq(arq1.arquivo);
        }

        for (int k = 0; k < tlLista2; k++)
        {
            seekArq(meio + 1 + k);
            regDir.leDoArq(arquivo);
            mov++;
            arq2.seekArq(k);
            regDir.gravaNoArq(arq2.arquivo);
        }

        while (i < tlLista1 && j < tlLista2)
        {
            arq1.seekArq(i);
            regEsq.leDoArq(arq1.arquivo);

            arq2.seekArq(j);
            regDir.leDoArq(arq2.arquivo);

            comp++;
            if (regEsq.getNumero() <= regDir.getNumero())
            {
                mov++;
                seekArq(pos);
                regEsq.gravaNoArq(arquivo);
                i++;
            }
            else
            {
                mov++;
                seekArq(pos);
                regDir.gravaNoArq(arquivo);
                j++;
            }
            pos++;
        }

        while (i < tlLista1)
        {
            arq1.seekArq(i);
            regEsq.leDoArq(arq1.arquivo);
            mov++;
            seekArq(pos);
            regEsq.gravaNoArq(arquivo);
            i++;
            pos++;
        }

        while (j < tlLista2)
        {
            arq2.seekArq(j);
            regDir.leDoArq(arq2.arquivo);
            mov++;
            seekArq(pos);
            regDir.gravaNoArq(arquivo);
            j++;
            pos++;
        }

        arq1.fecharArq();
        arq2.fecharArq();
    }

    public void insercao_diretaTim(int inicio, int fim)
    {
        int pos;
        Registro regi = new Registro();
        Registro regj = new Registro();
        for (int i = inicio + 1; i <= fim; i++)
        {
            seekArq(i);
            regi.leDoArq(arquivo);
            pos = i;
            seekArq(pos-1);
            regj.leDoArq(arquivo);
            comp++;
            while(pos > inicio && regi.getNumero() < regj.getNumero())
            {
                comp++;
                mov++;
                seekArq(pos);
                regj.gravaNoArq(arquivo);
                pos--;
                if(pos > inicio)
                {
                    seekArq(pos-1);
                    regj.leDoArq(arquivo);
                }
            }
            mov++;
            seekArq(pos);
            regi.gravaNoArq(arquivo);
        }
    }

    public void timSort()
    {
        int min_elementos = 32;
        int inicio = 0, fim;
        int meio, esquerda, direita;
        int qntd = filesize();

        while (inicio < qntd)
        {
            if (inicio + min_elementos - 1 < qntd - 1)
                fim = inicio + min_elementos - 1;
            else
                fim = qntd - 1;

            insercao_diretaTim(inicio, fim);
            inicio = inicio + min_elementos;
        }

        for (int tamanho = min_elementos; tamanho < qntd; tamanho = tamanho * 2)
        {
            for (esquerda = 0; esquerda < qntd; esquerda = esquerda + 2 * min_elementos)
            {
                meio = esquerda + tamanho - 1;

                if (esquerda + 2 * min_elementos - 1 < qntd - 1)
                    direita = esquerda + 2 * min_elementos - 1;
                else
                    direita = qntd - 1;

                if (meio < direita)
                    MergeTim(esquerda, meio, direita);
            }
        }
    }

    void initComp(){
        this.comp = 0;
    }

    public void initMov(){
        this.mov = 0;
    }

    public int getComp(){
        return this.comp;
    }

    public int getMov(){
        return this.mov;
    }

    public void geraArquivoOrdenado()
    {
        truncate(0); // limpa o arquivo
        for (int i = 0; i < 1024; i++)
            inserirRegNoFinal(new Registro(i));
    }

    public void geraArquivoReverso()
    {
        truncate(0); // limpa o arquivo
        for (int i = 1023; i >= 0; i--)
            inserirRegNoFinal(new Registro(i));
    }

    public void geraArquivoRandomico()
    {
        truncate(0); // limpa o arquivo
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 1024; i++)
            inserirRegNoFinal(new Registro(random.nextInt(100)));
    }
}
