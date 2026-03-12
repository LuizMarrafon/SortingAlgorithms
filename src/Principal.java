public class Principal {
    Arquivo arqOrd, arqRev, arqRand, auxRev, auxRand;

    public void gerarTabela(){
        arqOrd.geraArquivoOrdenado();
        arqRev.geraArquivoReverso();
        arqRand.geraArquivoRandomico();
    }
}
