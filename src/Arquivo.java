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

    public void quickSemPivo(){
        quickSP(0, filesize()-1);
    }

    public void quickSP(int ini, int fim){
        int i = ini, j = fim;
        Registro regI = new Registro();
        Registro regJ = new Registro();
        boolean flag = true;
        while(i < j){
            seekArq(i);
            regI.leDoArq(arquivo);
            seekArq(j);
            regJ.leDoArq(arquivo);
            if(flag){
                while(i < j && regI.getNumero() <= regJ.getNumero()){
                    i++;
                    seekArq(i);
                    regI.leDoArq(arquivo);
                }
            }
            else{
                while(i < j && regJ.getNumero() >= regI.getNumero()){
                    j--;
                    seekArq(j);
                    regJ.leDoArq(arquivo);
                }
            }
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

    public void quickComPivo(){
        quickCP(0, filesize()-1);
    }

    public void quickCP(int ini, int fim){
        int i = ini, j = fim, pivo;
        Registro regI = new Registro();
        Registro regJ = new Registro();
        seekArq((ini+fim)/2);
        regI.leDoArq(arquivo);
        pivo = regI.getNumero();
        while(i < j){
            seekArq(i);
            regI.leDoArq(arquivo);
            seekArq(j);
            regJ.leDoArq(arquivo);
            while(regI.getNumero() < pivo){
                i++;
                seekArq(i);
                regI.leDoArq(arquivo);
            }
            while(regJ.getNumero() > pivo){
                j--;
                seekArq(j);
                regJ.leDoArq(arquivo);
            }
            if(i <= j){
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

    public void bucket_sort(){
        Registro reg = new Registro();
        int maior = 0, menor = 0, i = 0, intervalo, pos;
        int quantidadeBuckets = 5;
        Registro[][] buckets = new Registro[quantidadeBuckets][filesize()];
        int[] bucketTL = new int[quantidadeBuckets];
        seekArq(i);
        reg.leDoArq(arquivo);
        menor = maior = reg.getNumero();
        for(i = 1; i < filesize(); i++){
            seekArq(i);
            reg.leDoArq(arquivo);
            if(reg.getNumero() > maior)
                maior = reg.getNumero();
            if(reg.getNumero() < menor)
                menor = reg.getNumero();
        }
        intervalo = (maior - menor + 1)/quantidadeBuckets;
        for (i = 0; i < filesize(); i++) {
            reg = new Registro();
            seekArq(i);
            reg.leDoArq(arquivo);
            if (intervalo == 0) {
                buckets[0][bucketTL[0]++] = reg;
            } else {
                pos = (reg.getNumero() - menor) / intervalo;
                if (pos >= quantidadeBuckets) pos = quantidadeBuckets - 1;
                buckets[pos][bucketTL[pos]++] = reg;
            }
        }
        for(i = 0; i < quantidadeBuckets; i++){
            for(int j = 1; j < bucketTL[i]; j++){
                Registro aux = buckets[i][j];
                int p = j;
                while(p > 0 && aux.getNumero() < buckets[i][p-1].getNumero()){
                    buckets[i][p] = buckets[i][p - 1];
                    p--;
                }
                buckets[i][p] = aux;
            }
        }
        seekArq(0);
        for(i = 0; i < quantidadeBuckets; i++){
            for(int j = 0; j < bucketTL[i]; j++){
                buckets[i][j].gravaNoArq(arquivo);
            }
        }
    }

    public void particao(RandomAccessFile arquivo1, RandomAccessFile arquivo2){
        int meio = filesize()/2;
        Registro reg = new Registro();
        int j = meio;
        for (int i = 0; i < meio; i++) {
            seekArq(i);
            reg.leDoArq(arquivo);
            reg.gravaNoArq(arquivo1);
            seekArq(j);
            reg.leDoArq(arquivo);
            reg.gravaNoArq(arquivo2);
            j++;
        }
    }

    public void merge(Arquivo arquivo1, Arquivo arquivo2, int seq){
        int i=0, j = 0, k = 0, tam_seq = seq;
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();
        while(k < filesize()){
            while(i < seq && j < seq){
                arquivo1.seekArq(i);
                reg1.leDoArq(arquivo1.arquivo);
                arquivo2.seekArq(j);
                reg2.leDoArq(arquivo2.arquivo);
                if(reg1.getNumero() < reg2.getNumero()) {
                    seekArq(k++);
                    reg1.gravaNoArq(arquivo);
                    i++;
                }
                else{
                    seekArq(k++);
                    reg2.gravaNoArq(arquivo);
                    j++;
                }
            }
            while(i < seq){
                arquivo1.seekArq(i++);
                reg1.leDoArq(arquivo1.arquivo);
                seekArq(k++);
                reg1.gravaNoArq(arquivo);
            }
            while(j < seq){
                arquivo2.seekArq(j++);
                reg2.leDoArq(arquivo2.arquivo);
                seekArq(k++);
                reg2.gravaNoArq(arquivo);
            }
            seq = seq + tam_seq;
        }
    }

    public void merge_sort(){
        Arquivo arquivo1 = new Arquivo("arquivo1.dat");
        Arquivo arquivo2 = new Arquivo("arquivo2.dat");
        int seq = 1, tl = filesize();
        while(seq < tl){
            arquivo1.truncate(0);
            arquivo2.truncate(0);
            particao(arquivo1.arquivo, arquivo2.arquivo);
            merge(arquivo1, arquivo2, seq);
            seq = seq * 2;
        }
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
