import java.io.*;

public class Test {
    /**
     * args[0] 에 dt_result.txt
     * args[1] 에 dt_answer.txt 넣고 실행
     */
    public static void main(String[] args) throws IOException {
        File resultFile = new File(args[0]);
        File answerFile = new File(args[1]);

        FileReader result_fr = new FileReader(resultFile);
        FileReader answer_fr = new FileReader(answerFile);

        BufferedReader result_br = new BufferedReader(result_fr);
        BufferedReader answer_br = new BufferedReader(answer_fr);

        int correction = 0;
        int total = 0;
        String result_buffer = "";
        String answer_buffer = "";
        while ((answer_buffer = answer_br.readLine()) != null) {
            result_buffer = result_br.readLine();
            total++;
            if (result_buffer.equals(answer_buffer)) {
                correction++;
            }
        }

        System.out.println(correction + " / " + total);

    }
}
