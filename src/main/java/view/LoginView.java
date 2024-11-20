package view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

/**
 * The View for the Login Use Case.
 */
public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "log in";

    private final LoginViewModel loginViewModel;
    private LoginController loginController;

    private final JButton login;

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        loginViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel(LoginViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel buttons = new JPanel();
        login = new JButton(LoginViewModel.LOGIN_BUTTON_LABEL);
        buttons.add(login);

        login.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        loginController.execute();
                    }
                }
        );
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(buttons);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Implement this method
        System.out.println(e.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoginState loginState = (LoginState) evt.getNewValue();
        if (loginState.getError() != null) {
            JOptionPane.showMessageDialog(null, loginState.getError());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}