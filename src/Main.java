void main() {
    Arquivo arquivo = new Arquivo("teste.dat");
    arquivo.geraArquivoRandomico();
    arquivo.exibir();
    //arquivo.heap_sort();
    //arquivo.selecao_direta();
    //arquivo.insercao_binaria();
    //arquivo.insercao_direta();
    //arquivo.shell_sort();
    //arquivo.bubble_sort();
    //arquivo.shake_sort();
    arquivo.quickSemPivo();

    System.out.println();
    arquivo.exibir();

    //arrumar insercao binaria, insercao direta, shell
}