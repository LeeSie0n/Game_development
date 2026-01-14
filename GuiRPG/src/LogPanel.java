import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class LogPanel extends JPanel {

    private JTextPane pane = new JTextPane();
    private StyledDocument doc = pane.getStyledDocument();

    public LogPanel() {
        setLayout(new BorderLayout());
        pane.setEditable(false);
        add(new JScrollPane(pane), BorderLayout.CENTER);
    }

    private void addLog(String msg, int align, Color color) {
        try {
            int start = doc.getLength();
            doc.insertString(start, msg + "\n", null);

            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setAlignment(attr, align);
            StyleConstants.setForeground(attr, color);
            doc.setParagraphAttributes(start, msg.length() + 1, attr, false);
        } catch (Exception e) {}
    }

    public void monster(String msg) { addLog(msg, StyleConstants.ALIGN_LEFT, Color.RED); }
    public void player(String msg) { addLog(msg, StyleConstants.ALIGN_RIGHT, Color.RED); }
    public void potion(String msg) { addLog(msg, StyleConstants.ALIGN_CENTER, new Color(0, 150, 0)); }
    public void system(String msg) { addLog(msg, StyleConstants.ALIGN_CENTER, Color.ORANGE); }

    public void clear() {
        pane.setText("");
    }
}
