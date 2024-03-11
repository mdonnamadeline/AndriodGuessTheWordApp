package com.example.guesstheword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView questionTextView;
    private TextView blanksTextView;
    private EditText letterEditText;
    private Button okButton;
    private EditText wordEditText;
    private Button submitButton;
    private Button nextButton;

    private String currentQuestion;
    private String currentWord;
    private int attemptsRemaining = 5;
    private int hintAttempts = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.txthint);
        blanksTextView = findViewById(R.id.txtblank);
        letterEditText = findViewById(R.id.txtEnterLetter);
        okButton = findViewById(R.id.btnOk);
        wordEditText = findViewById(R.id.txtFinalWord);
        submitButton = findViewById(R.id.btnSubmit);
        nextButton = findViewById(R.id.btnNext);

        // Initialize the app with the first question
        loadQuestion(getString(R.string.Q1));

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInput();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkWord();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to the next question or end the game if needed
                if (currentQuestion.equals(getString(R.string.Q1))) {
                    loadQuestion(getString(R.string.Q2));
                } else if (currentQuestion.equals(getString(R.string.Q2))) {
                    loadQuestion(getString(R.string.Q3));
                } else {
                    // You can implement game-end logic here
                    // For example, show a message or go back to the main menu
                    showMessage("Game completed! Your final score is: " + attemptsRemaining);
                }
            }
        });
    }

    private void loadQuestion(String question) {
        currentQuestion = question;
        questionTextView.setText(question);

        // Assign a value to currentWord based on the current question
        if (question.equals(getString(R.string.Q1))) {
            currentWord = "KEYBOARD";
        } else if (question.equals(getString(R.string.Q2))) {
            currentWord = "FOOTSTEPS";
        } else if (question.equals(getString(R.string.Q3))) {
            currentWord = "CLOUD";
        }

        // Update blanksTextView based on the length of the word
        StringBuilder displayWord = new StringBuilder();
        for (char c : currentWord.toCharArray()) {
            if (Character.isLetter(c)) {
                displayWord.append("_ ");
            } else if (Character.isWhitespace(c)) {
                displayWord.append("   "); // Add more spaces for word separation
            }
        }
        blanksTextView.setText(displayWord.toString().trim());

        // Reset attempts for the new question
        attemptsRemaining = 5;
        hintAttempts = 5;

        // Reset the input fields
        letterEditText.getText().clear();
        wordEditText.getText().clear();

        // Enable letter input, word submission, and hint
        okButton.setEnabled(true);
        submitButton.setEnabled(true);
    }

    private void checkInput() {
        String input = letterEditText.getText().toString().trim().toUpperCase();

        // Check if the input is a letter or hint
        if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
            checkLetter();
        } else {
            // Treat non-letter input as a hint
            provideHint();
        }
    }

    private void checkLetter() {
        String guessedLetter = letterEditText.getText().toString().trim().toUpperCase();

        // Update attempts remaining
        attemptsRemaining--;

        // Check if the guessed letter is in the current word
        if (currentWord.contains(guessedLetter)) {
            // Update blanksTextView with the guessed letter
            StringBuilder updatedBlanks = new StringBuilder(blanksTextView.getText().toString());
            for (int i = 0; i < currentWord.length(); i++) {
                if (Character.isLetter(currentWord.charAt(i)) && currentWord.charAt(i) == guessedLetter.charAt(0)) {
                    updatedBlanks.setCharAt(i * 2, guessedLetter.charAt(0));
                }
            }
            blanksTextView.setText(updatedBlanks.toString());

            // Display a message for the correct attempt
            showMessage("Correct attempt! " + attemptsRemaining + " attempts left.");
        } else {
            // Display a message for the wrong attempt
            showMessage("Wrong attempt! " + attemptsRemaining + " attempts left.");

            // Disable further attempts if the limit is reached
            if (attemptsRemaining == 0) {
                okButton.setEnabled(false);
                showMessage(getString(R.string.Wrong) + " Attempts reached 0. Your final score is: " + attemptsRemaining);
            }
        }

        // Decrease hint attempts for each letter guessed
        hintAttempts--;

        // Disable OK button if hint attempts reach 0
        if (hintAttempts == 0) {
            okButton.setEnabled(false);
            showMessage("Hint attempts reached 0.");
        }
    }

    private void checkWord() {
        String guessedWord = wordEditText.getText().toString().trim().toUpperCase();

        // Disable further attempts and word submission after checking
        okButton.setEnabled(false);
        submitButton.setEnabled(false);

        if (guessedWord.equals(currentWord.replace(" ", ""))) {
            // Display the correct word in blanksTextView
            blanksTextView.setText(currentWord);

            showMessage(getString(R.string.correct));
        } else {
            showMessage(getString(R.string.Wrong) + "correct word is " + currentWord);
        }
    }

    //comment

    private void provideHint() {
        if (hintAttempts > 0) {
            // Decrease hint attempts for each hint
            hintAttempts--;

            // Display a message with remaining hint attempts
            showMessage("Hint: " + hintAttempts + " attempts left.");

            // Disable OK button if hint attempts reach 0
            if (hintAttempts == 0) {
                okButton.setEnabled(false);
                showMessage("Hint attempts reached 0.");
            }
        } else {
            showMessage("No more hint attempts remaining.");
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
