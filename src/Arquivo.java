import java.io.IOException;
import java.io.RandomAccessFile;

public class Arquivo {
    private String nomearquivo;
    private RandomAccessFile arquivo;

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

    public void selecao_direta(){
        Registro regi = new Registro();
        Registro regj = new Registro();
        Registro regmenor = new Registro();
        int menor, tl = filesize(), posmenor;
        for (int i = 0; i < tl-1; i++){
            seekArq(i);
            regi.leDoArq(arquivo);
            menor = regi.getNumero();
            posmenor = 1;
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


}
