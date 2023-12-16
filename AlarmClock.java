import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class AlarmClock extends JFrame {
    private JTextField timeField;
    private JButton setAlarmButton;
    private Timer timer;
    private boolean isAlarmSet = false;
    private Clip alarmClip;

    public AlarmClock() {
        // GUI Setup
        setTitle("Alarm Clock");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        timeField = new JTextField(8);
        add(timeField);

        setAlarmButton = new JButton("Set Alarm (24 Hour Format)");
        add(setAlarmButton);

        setAlarmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isAlarmSet) {
                    setAlarm();
                    setAlarmButton.setEnabled(false); // Disable the button after setting the alarm
                    isAlarmSet = true; // Set the flag to indicate the alarm is set
                } else {
                    JOptionPane.showMessageDialog(AlarmClock.this, "The button has been clicked already.");
                }
            }
        });
    }

    private void setAlarm() {
        String alarmTime = timeField.getText();
        timer = new Timer();
        TimerTask alarmTask = new TimerTask() {
            @Override
            public void run() {
                if (getCurrentTime().equals(alarmTime)) {
                    playSound("alarm.wav"); // Start playing the sound
                    JOptionPane.showMessageDialog(null, "Alarm! Wake up!"); // Show the popup
                    stopSound(); // Stop the sound when the dialog is closed
                    timer.cancel();
                    isAlarmSet = false; // Reset the flag
                    SwingUtilities.invokeLater(() -> setAlarmButton.setEnabled(true)); // Re-enable the button on the
                                                                                       // Swing event thread
                }
            }
        };

        timer.scheduleAtFixedRate(alarmTask, 0, 1000); // Check every second
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return String.format("%02d:%02d", hour, minute);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AlarmClock().setVisible(true);
            }
        });
    }

    private void playSound(String soundFileName) {
        try {
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(new File(soundFileName).getAbsoluteFile());
            alarmClip = AudioSystem.getClip(); // Use the member variable
            alarmClip.open(audioInputStream);
            alarmClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopSound() {
        if (alarmClip != null) {
            alarmClip.stop(); // Stop the clip
            alarmClip.close(); // Close the clip
            alarmClip = null; 
        }
    }
}
