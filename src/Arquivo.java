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
