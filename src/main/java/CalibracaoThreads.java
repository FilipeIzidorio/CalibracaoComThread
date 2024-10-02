
        import java.io.IOException;
        import java.nio.file.Files;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import java.util.List;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;
        import java.util.concurrent.TimeUnit;

class CalibracaoThread {
    public static int valorCalibracao(String linha) {
        Character primeiroCaractere = null;
        Character ultimoCaractere = null;

        for (char c : linha.toCharArray()) {
            if (Character.isDigit(c)) {
                if (primeiroCaractere == null) {
                    primeiroCaractere = c;
                }
                ultimoCaractere = c;
            }
        }

        if (primeiroCaractere != null) {
            return Integer.parseInt(primeiroCaractere.toString() + ultimoCaractere.toString());
        } else {
            return 0;
        }
    }
}
        class ResultContainer {
            private int soma = 0;
            private int count = 0;

            public void add(int valor) {
                this.soma += valor;
                this.count++;
            }

            public int getSoma() {
                return soma;
            }

            public int getCount() {
                return count;
            }
        }

class WorkerThread implements Runnable {
    private final String linha;
    private final ResultContainer resultContainer;

    public WorkerThread(String linha, ResultContainer resultContainer) {
        this.linha = linha;
        this.resultContainer = resultContainer;
    }

    @Override
    public void run() {
        int valor = CalibracaoThread.valorCalibracao(linha);
        synchronized (resultContainer) {
            resultContainer.add(valor);
        }
        //System.out.println(Thread.currentThread().getName() + " processou valor: " + valor);
    }
}



public class CalibracaoThreads {
    public static void main(String[] args) throws IOException {
        // Inicia o cronômetro
        long tempoInicial = System.currentTimeMillis();

        // Lê as linhas do arquivo
        Path path = Paths.get(System.getProperty("user.dir") + "\\src\\main\\java\\new_calibration_text.txt");
        List<String> calibrations = Files.readAllLines(path);

        // Container para os resultados
        ResultContainer resultContainer = new ResultContainer();

        // Cria um pool com 5 threads TEVE A MELHOR PERFORMACE COM 210MS
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Envia as tarefas para serem executadas no pool de threads
        for (String line : calibrations) {
            Runnable worker = new WorkerThread(line, resultContainer);
            executor.execute(worker); // Executa a tarefa usando uma thread do pool
        }

        // Solicita ao executor para não aceitar novas tarefas e para encerrar após a execução das tarefas pendentes
        executor.shutdown();

        try {
            // Aguarda até que todas as tarefas sejam concluídas
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Força o encerramento das tarefas pendentes
            }
        } catch (InterruptedException e) {
            executor.shutdownNow(); // Força o encerramento em caso de interrupção
        }

        // Exibe os resultados
        System.out.println("A soma dos valores é: " + resultContainer.getSoma());
        System.out.println("Total de linhas: " + resultContainer.getCount());

        // Exibe o tempo total
        long tempoFinal = System.currentTimeMillis();
        System.out.print("TEMPO TOTAL COM THREADS: ");
        System.out.printf("%.3f ms%n", (tempoFinal - tempoInicial) / 1000d);
    }
}

