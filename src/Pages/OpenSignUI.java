package src.Pages;

import javax.swing.SwingUtilities;
import java.awt.Window;
import java.awt.KeyboardFocusManager;

public class OpenSignUI {
    public static void OpenSignIn() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            SignInUI frame = new SignInUI();
            frame.setVisible(true);
        });
    }

    public static void OpenSignUp() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            SignUpUI signUpFrame = new SignUpUI();
            signUpFrame.setVisible(true);
        });
    }

    public static void dispose() {
        Window activeWindow = KeyboardFocusManager
                .getCurrentKeyboardFocusManager()
                .getActiveWindow();
        
        if (activeWindow != null) {
            activeWindow.dispose();
        }
    }
}
