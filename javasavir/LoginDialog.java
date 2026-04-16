package javasavir;

import java.awt.*;
import javax.swing.*;

public class LoginDialog extends JDialog {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private boolean autenticado = false;
    private String usuarioLogado = null;

    public LoginDialog(Frame parent) {
        super(parent, "Login do Sistema", true); // true para ser modal (trava a tela de trás)
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel(" Usuário:"));
        txtUsuario = new JTextField();
        add(txtUsuario);

        add(new JLabel(" Senha:"));
        txtSenha = new JPasswordField();
        add(txtSenha);

        JButton btnLogin = new JButton("Entrar");
        JButton btnCancelar = new JButton("Cancelar");

        add(btnLogin);
        add(btnCancelar);

        // Ação do botão Login
        btnLogin.addActionListener(e -> {
            // Aqui você pode colocar sua lógica de banco de dados. 
            // Por enquanto, vamos aceitar qualquer usuário com a senha "123"
            String senha = new String(txtSenha.getPassword());
            
            if (!txtUsuario.getText().isEmpty() && senha.equals("123")) {
                autenticado = true;
                usuarioLogado = txtUsuario.getText();
                dispose(); // fecha o diálogo
            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação do botão Cancelar
        btnCancelar.addActionListener(e -> dispose());

        pack(); // ajusta o tamanho ao conteúdo
        setLocationRelativeTo(parent); // centraliza na tela principal
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    public String getUsuario() {
        return usuarioLogado;
    }
}