import java.io.*;

public class Principal {
    // Calcula comparações

    private double calculaCompInsDirOrd(int TL){
        return TL-1;
    }

    private double calculaCompInsDirRev(int TL){
        return ((Math.pow(TL, 2)) + (TL-2)) / 4;
    }

    private double calculaCompInsDirRand(int TL){
        return ((Math.pow(TL, 2)) + (TL-4)) / 4;
    }

    private double calculaCompInsBin(int TL){
        return (TL * (Math.log(TL) / Math.log(2))) - TL + 1;
    }

    private double calculaCompSelDir(int TL){
        return (Math.pow(TL, 2) - TL) / 2;
    }

    // Calcula movimentações

    private int calculaMovInsDireBinOrd(int TL){
        return 3 * (TL-1);
    }

    private double calculaMovInsDireBinRev(int TL){
        return ((Math.pow(TL, 2)) + (9 * TL) - 10) / 4;
    }

    private double calculaMovInsDireBinRand(int TL){
        return ((Math.pow(TL, 2)) + (3 * TL) - 4) / 2;
    }

    private double calculaMovSelRev(int TL){
        return (Math.pow(TL, 2) / 4.0) + 3 * (TL - 1);
    }

    private double calculaMovSelRand(int TL){
        return TL * (Math.log(TL) + 0.577216);
    }

    private double calculaMovBolhaOrd(int TL){
        return 0;
    }

    private double calculaMovBolhaRand(int TL){
        return 3 * ((Math.pow(TL, 2) - TL) / 2.0);
    }

    private double calculaMovBolhaRev(int TL){
        return 3 * ((Math.pow(TL, 2) - TL) / 4.0);
    }


    public void gerarTabela() throws IOException {
        FileWriter arq = iniciaTabela();

        Arquivo arqOrd = new Arquivo("arqOrdenado.dat");
        Arquivo arqRev = new Arquivo("arqReverso.dat");
        Arquivo arqRand = new Arquivo("arqRandomico.dat");
        Arquivo auxRand = new Arquivo("arqRandomicoAux.dat");
        Arquivo auxRev = new Arquivo("arqReversoAux.dat");


        arqOrd.geraArquivoOrdenado();
        arqRev.geraArquivoReverso();
        arqRand.geraArquivoRandomico();

        insercaoDireta(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        insercaoBinaria(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        selecaoDireta(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        bubbleSort(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        shakeSort(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        shellSort(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        heapSort(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        quickSemPivo(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        quickComPivo(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        mergeSort(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);      // Merge impl 1
        mergeSort2(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);     // Merge impl 2

        // Pesquisados na literatura
        countingSort(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        bucketSort(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        radixSort(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        combSort(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        gnomeSort(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);
        timSort(arqOrd, arqRev, arqRand, auxRand, auxRev, arq);


        arq.close();
        exibirTabela();
    }

    public void exibirTabela() throws FileNotFoundException, IOException{

        FileInputStream stream = new FileInputStream("Tabela1.txt");
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(reader);
        String linha = br.readLine();
        while(linha != null) {
            System.out.println(linha);
            linha = br.readLine();
        }
    }

    public FileWriter iniciaTabela() throws IOException {
        FileWriter arq = new FileWriter("Tabela1.txt");
        PrintWriter gravarArq = new PrintWriter(arq, true);

        String sep = "+------------------+----------+----------+----------+----------+-------+----------+----------+----------+----------+-------+----------+----------+----------+----------+-------+";
        String header1 = String.format("| %-16s | %-41s | %-41s | %-41s |%n",
                "Métodos Ordenação",
                "         Arquivo Ordenado",
                "       Arquivo Ordem Reversa",
                "        Arquivo Randômico");
        String header2 = String.format("| %-16s | %-8s | %-8s | %-8s | %-8s | %-5s | %-8s | %-8s | %-8s | %-8s | %-5s | %-8s | %-8s | %-8s | %-8s | %-5s |%n",
                "",
                "Comp Prog", "Comp Equa", "Mov Prog", "Mov Equa", "Tempo",
                "Comp Prog", "Comp Equa", "Mov Prog", "Mov Equa", "Tempo",
                "Comp Prog", "Comp Equa", "Mov Prog", "Mov Equa", "Tempo");

        gravarArq.println(sep);
        gravarArq.print(header1);
        gravarArq.println(sep);
        gravarArq.print(header2);
        gravarArq.println(sep);

        return arq;
    }

    private void gravaLinhaTabela(FileWriter arq, String metodo,
                                  int compOrd, int compRev, int compRand,
                                  int movOrd,  int movRev,  int movRand,
                                  double compEquaOrd, double compEquaRev, double compEquaRand,
                                  double movEquaOrd,  double movEquaRev,  double movEquaRand,
                                  int ttotalOrd, int ttotalRev, int ttotalRand) throws IOException {

        PrintWriter gravarArq = new PrintWriter(arq, true);

        gravarArq.printf("| %-16s | %-8d | %-8.1f | %-8d | %-8.1f | %-5d | %-8d | %-8.1f | %-8d | %-8.1f | %-5d | %-8d | %-8.1f | %-8d | %-8.1f | %-5d |%n",
                metodo,
                compOrd, compEquaOrd, movOrd, movEquaOrd, ttotalOrd,
                compRev, compEquaRev, movRev, movEquaRev, ttotalRev,
                compRand, compEquaRand, movRand, movEquaRand, ttotalRand);

        String sep = "+------------------+----------+----------+----------+----------+-------+----------+----------+----------+----------+-------+----------+----------+----------+----------+-------+";
        gravarArq.println(sep);
    }

    public void insercaoDireta(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Insertion");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0, compRev = 0, movRev = 0, ttotalRev = 0, compRand = 0, movRand = 0, ttotalRand = 0;
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.insercao_direta();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        //Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.insercao_direta();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        //Randomico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.insercao_direta();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(arq, "Inserção Direta", compOrd, compRev, compRand, movOrd, movRev, movRand, calculaCompInsDirOrd(arqOrd.filesize()), calculaCompInsDirRev(arqRev.filesize()), calculaCompInsDirRand(arqRand.filesize()),
                calculaMovInsDireBinOrd(arqOrd.filesize()), calculaMovInsDireBinRev(arqRev.filesize()), calculaMovInsDireBinRand(arqRand.filesize()),
                ttotalOrd, ttotalRev, ttotalRand);

        System.out.println("Fim Insertion");
    }

    public void insercaoBinaria(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Insercao Binaria");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.insercao_binaria();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int) (tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp();
        arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.insercao_binaria();
        tfim = System.currentTimeMillis();
        ttotalRev = (int) (tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp();
        arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.insercao_binaria();
        tfim = System.currentTimeMillis();
        ttotalRand = (int) (tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(
                arq,
                "Inserção Binária",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                calculaCompInsBin(arqOrd.filesize()),
                calculaCompInsBin(arqRev.filesize()),
                calculaCompInsBin(arqRand.filesize()),
                calculaMovInsDireBinOrd(arqOrd.filesize()),
                calculaMovInsDireBinRev(arqRev.filesize()),
                calculaMovInsDireBinRand(arqRand.filesize()),
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Insercao Binaria");
    }
    public void selecaoDireta(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Selecao Direta");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.selecao_direta();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int) (tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp();
        arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.selecao_direta();
        tfim = System.currentTimeMillis();
        ttotalRev = (int) (tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp();
        arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.selecao_direta();
        tfim = System.currentTimeMillis();
        ttotalRand = (int) (tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        double compEqua = calculaCompSelDir(arqOrd.filesize());
        gravaLinhaTabela(
                arq,
                "Selecao Direta",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                compEqua, compEqua, compEqua,
                calculaMovInsDireBinOrd(arqOrd.filesize()),
                calculaMovSelRev(arqRev.filesize()),
                calculaMovSelRand(arqRand.filesize()),
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Selecao Direta");
    }

    public void bubbleSort(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Bubble");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0, compRev = 0, movRev = 0, ttotalRev = 0, compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.bubble_sort();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.bubble_sort();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.bubble_sort();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        double compEqua = calculaCompSelDir(arqOrd.filesize());
        gravaLinhaTabela(arq, "Bolha", compOrd, compRev, compRand, movOrd, movRev, movRand,
                compEqua, compEqua, compEqua,
                calculaMovBolhaOrd(arqOrd.filesize()), calculaMovBolhaRev(arqRev.filesize()), calculaMovBolhaRand(arqRand.filesize()),
                ttotalOrd, ttotalRev, ttotalRand);

        System.out.println("Fim Bubble");
    }

    public void shakeSort(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Shake");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0, compRev = 0, movRev = 0, ttotalRev = 0, compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.shake_sort();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.shake_sort();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.shake_sort();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        double compEqua = calculaCompSelDir(arqOrd.filesize());
        gravaLinhaTabela(arq, "Bolha", compOrd, compRev, compRand, movOrd, movRev, movRand,
                compEqua, compEqua, compEqua,
                calculaMovBolhaOrd(arqOrd.filesize()), calculaMovBolhaRev(arqRev.filesize()), calculaMovBolhaRand(arqRand.filesize()),
                ttotalOrd, ttotalRev, ttotalRand);

        System.out.println("Fim Shake");
    }

    public void shellSort(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Shell");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.shell_sort();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.shell_sort();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.shell_sort();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(
                arq,
                "Shell",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                0, 0, 0,   // Comp. Equa.
                0, 0, 0,   // Mov. Equa.
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Shell");
    }

    public void heapSort(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Heap");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.heap_sort();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.heap_sort();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.heap_sort();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(
                arq,
                "Heap",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                0, 0, 0,   // Comp. Equa.
                0, 0, 0,   // Mov. Equa.
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Heap");
    }

    public void quickSemPivo(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Quick Sem Pivo");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.quickSemPivo();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.quickSemPivo();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.quickSemPivo();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(
                arq,
                "Quick s/ pivo",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                0, 0, 0,   // Comp. Equa.
                0, 0, 0,   // Mov. Equa.
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Quick Sem Pivo");
    }

    public void quickComPivo(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Quick Com Pivo");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.quickComPivo();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.quickComPivo();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.quickComPivo();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(
                arq,
                "Quick c/ pivo",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                0, 0, 0,   // Comp. Equa.
                0, 0, 0,   // Mov. Equa.
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Quick Com Pivo");
    }

    public void mergeSort(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Merge");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.merge_sort();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.merge_sort();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.merge_sort();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(
                arq,
                "Merge",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                0, 0, 0,   // Comp. Equa.
                0, 0, 0,   // Mov. Equa.
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Merge");
    }

    public void mergeSort2(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Merge 2");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.mergeSort_segunda();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.mergeSort_segunda();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.mergeSort_segunda();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(
                arq,
                "Merge 2",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                0, 0, 0,   // Comp. Equa.
                0, 0, 0,   // Mov. Equa.
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Merge");
    }

    public void countingSort(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Counting");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.countingSort();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.countingSort();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.countingSort();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(
                arq,
                "Counting",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                0, 0, 0,   // Comp. Equa.
                0, 0, 0,   // Mov. Equa.
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Counting");
    }

    public void combSort(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Comb");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.combSort();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.combSort();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.combSort();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(
                arq,
                "Comb",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                0, 0, 0,   // Comp. Equa.
                0, 0, 0,   // Mov. Equa.
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Comb");
    }

    public void gnomeSort(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Gnome");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.gnomeSort();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.gnomeSort();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.gnomeSort();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(
                arq,
                "Gnome",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                0, 0, 0,   // Comp. Equa.
                0, 0, 0,   // Mov. Equa.
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Gnome");
    }

    public void radixSort(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Radix");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.radixSort();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.radixSort();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.radixSort();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(
                arq,
                "Radix",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                0, 0, 0,   // Comp. Equa.
                0, 0, 0,   // Mov. Equa.
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Radix");
    }

    public void bucketSort(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Bucket");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.bucket_sort();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.bucket_sort();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.bucket_sort();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(
                arq,
                "Bucket",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                0, 0, 0,   // Comp. Equa.
                0, 0, 0,   // Mov. Equa.
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Bucket");
    }
    public void timSort(Arquivo arqOrd, Arquivo arqRev, Arquivo arqRand, Arquivo auxRand, Arquivo auxRev, FileWriter arq) throws IOException {
        System.out.println("Inicio Tim");
        long tini, tfim;
        int compOrd = 0, movOrd = 0, ttotalOrd = 0;
        int compRev = 0, movRev = 0, ttotalRev = 0;
        int compRand = 0, movRand = 0, ttotalRand = 0;

        // Ordenado
        arqOrd.initComp(); arqOrd.initMov();
        System.out.println("Inicio Ordenado");
        tini = System.currentTimeMillis();
        arqOrd.timSort();
        tfim = System.currentTimeMillis();
        ttotalOrd = (int)(tfim - tini) / 1000;
        compOrd = arqOrd.getComp();
        movOrd = arqOrd.getMov();

        // Reverso
        System.out.println("Inicio Reverso");
        arqRev.copiaArquivo(auxRev);
        arqRev.initComp(); arqRev.initMov();
        tini = System.currentTimeMillis();
        arqRev.timSort();
        tfim = System.currentTimeMillis();
        ttotalRev = (int)(tfim - tini) / 1000;
        compRev = arqRev.getComp();
        movRev = arqRev.getMov();

        // Randômico
        System.out.println("Inicio Randomico");
        arqRand.copiaArquivo(auxRand);
        arqRand.initComp(); arqRand.initMov();
        tini = System.currentTimeMillis();
        arqRand.timSort();
        tfim = System.currentTimeMillis();
        ttotalRand = (int)(tfim - tini) / 1000;
        compRand = arqRand.getComp();
        movRand = arqRand.getMov();

        gravaLinhaTabela(
                arq,
                "Tim",
                compOrd, compRev, compRand,
                movOrd, movRev, movRand,
                0, 0, 0,   // Comp. Equa.
                0, 0, 0,   // Mov. Equa.
                ttotalOrd, ttotalRev, ttotalRand
        );

        System.out.println("Fim Tim");
    }
}
