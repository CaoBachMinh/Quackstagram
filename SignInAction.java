import javax.swing.*;

public class SignInAction {
    private SignInUI signInUI;
    private CredentialsQuery credentialsQuery;

    public SignInAction(SignInUI signInUI) {
        this.signInUI = signInUI;
        this.credentialsQuery = new CredentialsQuery();
    }


    // Combined method to configure both sign-in and register buttons
    public void configureButtons(JButton btnSignIn, JPanel loginPanel, 
                                 JButton btnRegisterNow, JPanel buttonPanel) {
        // Configure Sign-In Button
        btnSignIn.addActionListener(e -> {
            // Sign-in logic
            String username = signInUI.txtUsername.getText();
            String password = signInUI.txtPassword.getText();

            if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(signInUI, 
                    "Username and password cannot be empty", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (credentialsQuery.verifyCredentials(username, password)) {
                String bio = new CredentialsManager().getUserDetails(username).getBio();
                LoggedinUser.setLoggedinUser(username,bio);
                signInUI.dispose();
                new InstagramProfileUI(LoggedinUser.getInstance()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(signInUI, 
                    "Invalid username or password", 
                    "Sign In Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // Configure Register Now Button
        btnRegisterNow.addActionListener(e -> {
            
            // Navigate to Sign Up
            OpenSignUI.OpenSignUp();
        });
    }
}
