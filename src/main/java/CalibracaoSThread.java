import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class Calibracao {
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

    public class CalibracaoSThread {
        public static void main(String[] args) throws IOException {

            long tempoInicial = System.currentTimeMillis();

            Path path = Paths.get(System.getProperty("user.dir") + "\\src\\main\\java\\new_calibration_text.txt");
            List<String> calibrations = Files.readAllLines(path);

            int soma = 0;
            int count = 0;

            for (String line: calibrations) {
                soma += CalibracaoThread.valorCalibracao(line);
                count++;
            }

            System.out.println("A soma dos valores é: " + soma);
            System.out.println("Total de linhas: " + count);

            long tempoFinal = System.currentTimeMillis();
            System.out.print("TEMPO TOTAL SEM THREAD: ");
            System.out.printf("%.3f ms%n", (tempoFinal - tempoInicial) / 1000d);


        }
    }
