
import java.awt.event.ActionListener; //skrbi za moj input. ko pritisnimo enter, miško itd.
import java.awt.event.ActionEvent;  //izvrši
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.*;


public class wordle extends JFrame implements ActionListener {

    private static JPanel panel;
    private static JFrame frame;
    private static JLabel Title, stats;
    private static JTextField userText1;
    private static JLabel[] labels;

    //Barve
    public static Scanner s = new Scanner(System.in);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";

    static String[] mozneBesede;
    static int poskusi;
    static char[] input;
    static long startTime;
    static char[] answer;
    static boolean done;
    static String answerChoosen;

	public static void main(String[] args) throws FileNotFoundException {
        panel = new JPanel();
        frame = new JFrame();
        frame.setSize(220, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("GUI");
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        panel.setLayout(null);
        Title = new JLabel("Wordle: ");
        Title.setBounds(10, 20, 80, 25);
        panel.add(Title);
        panel.setLayout(null);
        stats = new JLabel("Vpiši besedo, dolgo pet črk");
        stats.setBounds(10, 50, 180, 25);
        panel.add(stats);
        userText1 = new JTextField();
        userText1.addActionListener(new GUI());
        userText1.setBounds(40, 80, 80, 25);
        panel.add(userText1);
        JButton button = new JButton("Potrdi  ");
        button.setBounds(100, 20, 80, 25);
        button.addActionListener(new GUI());
        panel.add(button);
        labels = new JLabel[6];
        for (int i = 0; i < 6; i++) {
            labels[i] = new JLabel("<html><font size='5' color=blue> ----- </font> <font");
            labels[i].setBounds(44, 80 + (i * 25), 80, 25);
            panel.add(labels[i]);
        }
        frame.setVisible(true);
        StartWordle();
    }

    public static void StartWordle() throws FileNotFoundException {
        mozneBesede = new String[12947];
        File myObj = new File("/home/jakob/IdeaProjects/APS2_5/src/wordleWords.txt");
        Scanner myReader = new Scanner(myObj);
        int indexCounter = 0;
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            mozneBesede[indexCounter] = data;
            indexCounter++;
        }
        startTime = System.currentTimeMillis();
        poskusi = 0;
        System.out.println("Wordle: Type A Five Letter Word");
        answerChoosen = ReturnRandomWord();
        answer = new char[5];
        for (int i = 0; i < 5; i++ ) answer[i] = answerChoosen.charAt(i);
        input = new char[5];
        myReader.close();
    }
    
    public static void EndWordle() {
        System.out.println("Wordle: Odgovor je bil: " + new String(answerChoosen));
        System.out.println("Wordle: Odgovor si našel v" + ((System.currentTimeMillis() - startTime) / 1000) + " sekund in " + poskusi + " poskusov.");
        userText1.setEnabled(false);
        userText1.setVisible(false);

        if (!done) stats.setText("<html><font size='1' color=red> " + "The Answer Was: " + new String(answerChoosen) + ". You wasted \n " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds (:" + "</font> <font");
        else  stats.setText("<html><font size='1' color=green> " + "You Found The Answer in \n " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds and " + poskusi + " poskusi." + "</font> <font");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        EnterWord();
    }

    public static void EnterWord(){ 
        if ( IsAValidWord(userText1.getText(), mozneBesede) ) ButtonPressed();
        else System.out.println("Wordle: That is not a valid word");
    }

    public static void ButtonPressed(){
        userText1.setBounds(40, 80 + ((poskusi + 1) * 25), 80, 25);
        String userInput = userText1.getText();
        int[] colorOfLetters = PlayWordle(userInput);
        done = true;
        for (int i : colorOfLetters) {
            if (i != 2) {
                done = false;
                break;
            }
        }
        if (done || poskusi > 5) EndWordle();
        String[] numsToColors = new String[5];
        for (int i = 0; i < 5; i++) {
            if (colorOfLetters[i] == 0) numsToColors[i] = "black";
            else if (colorOfLetters[i] == 1) numsToColors[i] = "orange";
            else if (colorOfLetters[i] == 2) numsToColors[i] = "green";
        }
        System.out.println("Set colors to " + numsToColors[0] + " " + numsToColors[1] + " " + numsToColors[2] + " " + numsToColors[3] + " " + numsToColors[4] + " User Input was" + userInput + " answer was " + answerChoosen + " work on word is " + new String(answer));
        String finalString = (
        "<html><font size='5' color=" + numsToColors[0] + "> " + userInput.charAt(0) + "</font> <font            " + 
        "<html><font size='5' color=" + numsToColors[1] + "> " + userInput.charAt(1) + "</font> <font            " + 
        "<html><font size='5' color=" + numsToColors[2] + "> " + userInput.charAt(2) + "</font> <font            " + 
        "<html><font size='5' color=" + numsToColors[3] + "> " + userInput.charAt(3) + "</font> <font            " + 
        "<html><font size='5' color=" + numsToColors[4] + "> " + userInput.charAt(4) + "</font> <font            ");
        setNextLabel(finalString);
        userText1.setText("");
    }

    public static int[] PlayWordle(String InputWordleWord) {
        done = false;
        ++poskusi;
        String R1 = InputWordleWord.toLowerCase();
        if (!IsAValidWord(R1, mozneBesede)) System.out.println("Beseda ni veljavna");
        else for (int i = 0; i < 5; i++ ) input[i] = R1.charAt(i);
        for (int i = 0; i < 5; i++ ) answer[i] = answerChoosen.charAt(i);
        return ReturnColorOfLeters(input, answer);
    }

    public static void setNextLabel(String string){ labels[poskusi - 1].setText(string);}
    public static int[] ReturnColorOfLeters(char[] inputWord, char[] correctWord) {
        int[] colorForLetter = new int[5];
        for (int i = 0; i < 5; i++) {
            if (inputWord[i] == correctWord[i]) {
                correctWord[i] = '-';
                colorForLetter[i] = 2;
            }
        }
        for (int j = 0; j < 5; j++) {
            for (int k = 0; k < 5; k++){
                if (inputWord[j] == correctWord[k] && colorForLetter[j] != 2) {
                    colorForLetter[j] = 1;
                    correctWord[k] = '-';
                }
            }
        }
        for (int m = 0; m < 5; m++) {
            if (colorForLetter[m] == 0) System.out.print(inputWord[m]);
            if (colorForLetter[m] == 1) System.out.print(ANSI_YELLOW + inputWord[m] + ANSI_RESET);
            if (colorForLetter[m] == 2) System.out.print(ANSI_GREEN + inputWord[m] + ANSI_RESET);
        }
        System.out.println("");
        return colorForLetter;
    }

    public static boolean IsAValidWord(String input, String[] mozneBesede) {
        if (input.length() < 5) System.out.println("Beseda naj bo dolga natančno 5 črk");
        for (String string : mozneBesede) if (string.equals(input)) return true;
        return false;
    }

    public static String ReturnRandomWord() throws FileNotFoundException {
        String[] seznamBesed = new String[2315];
        File myObj = new File("/home/jakob/IdeaProjects/APS2_5/src/wordleAnswers.txt");
        Scanner myReader = new Scanner(myObj);
        int indexCounter = 0;
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            seznamBesed[indexCounter] = data;
            indexCounter++;
        }
        myReader.close();
        return seznamBesed[(int)(Math.random() * (seznamBesed.length - 1))];
    }
}