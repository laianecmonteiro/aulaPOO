package javasavir; //Define o pacote onde a classe está localizada

import javax.swing.*; // Importa componentes gráficos do Swing (JFrame, JButton, etc.)
import java.awt.*; // Importa classes de layout, dimensões, etc.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDialog extends JDialog // Declara a classe herdando de JDialog (janela modal)
{
    private JTextField txtUsuario; // Campo de texto para o usuário
    private JPasswordField txtSenha; // Campo de senha (texto oculto)
    private boolean autenticado = false; // Indica se o login foi realizado com sucesso
    private String usuario; // Armazena o nome do usuário autenticado

    public LoginDialog(Frame parent) // Construtor que recebe a janela principal como referência
    {
        super(parent, "Login", true); // Chama o construtor da superclasse (JDialog)
        // "Login" = título da janela
        // true = modal (bloqueia a janela principal)
        setLayout(new GridBagLayout()); // Define o layout como GridBagLayout (mais flexível)
        GridBagConstraints gbc = new GridBagConstraints(); // Objeto para configurar posições/layout
        gbc.insets = new Insets(5, 5, 5, 5); // Define espaçamento interno (margem) dos componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Faz os componentes ocuparem horizontalmente о espaço

        // ===== Usuário =====
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 0; // Linha 0
        add(new JLabel("Usuário:"), gbc); // Adiciona o rótulo "Usuário"

        gbc.gridx = 1; // Coluna 1
        txtUsuario = new JTextField(15); // Cria campo de texto com 15 colunas
        add(txtUsuario, gbc); // Adiciona o campo ao layout

        // ===== Senha =====
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 1; // Linha 1
        add(new JLabel("Senha:"), gbc); // Adiciona o rótulo "Senha"

        gbc.gridx = 1; // Coluna 1
        txtSenha = new JPasswordField(15); // Cria campo de senha com 15 colunas
        add(txtSenha, gbc); // Adiciona o campo ao layout

        // ===== Botões =====   
        JButton btnLogin = criarBotao("Entrar"); // Cria botão "Entrar" com tamanho padrão
        JButton btnCancelar = criarBotao("Cancelar"); // Cria botão "Cancelar"
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Cria painel com layout FlowLayout centralizado e espaçamento
        painelBotoes.add(btnLogin); // Adiciona botão Entrar ao painel
        painelBotoes.add(btnCancelar); // Adiciona botão Cancelar ao painel
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 2; // Linha 2
        gbc.gridwidth = 2; // Faz o componente ocupar 2 colunas
        add(painelBotoes, gbc); // Adiciona o painel de botões ao layout principal

        // ===== AÇÕES =====

        btnLogin.addActionListener(e -> fazerLogin());
        // Ao clicar em "Entrar", chama o método fazerLogin()
        btnCancelar.addActionListener(e -> {
            autenticado = false; // Define como não autenticado
            dispose(); // Fecha a janela
        });
        getRootPane().setDefaultButton(btnLogin); // Define o botão padrão (pressionar Enter executa o login)

        // ===== CONFIGURAÇÃO FINAL =====
        pack(); // Ajusta o tamanho da janela automaticamente conforme os componentes
        setLocationRelativeTo(parent); // Centraliza a janela em relação à janela principal
        setResizable(false); // Impede redimensionamento da janela
    }

    //===== MÉTODO PADRÃO PARA BOTÕES =====
    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto); // Cria botão com o texto informado
        btn.setPreferredSize(new Dimension(110, 35)); // Define tamanho padrão do botão
        return btn; // Retorna o botão criado
    }

    // =====LÓGICA DE LOGIN =====

    private void fazerLogin() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtSenha.getPassword()).trim();
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe usuário e senha.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Testa conexão
        if (!Conexao.testarConexao()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível conectar ao banco de dados.\nVerifique o console da anlicacão para ver o erro real",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }
        String sql = "SELECT nome FROM tb_usuarios WHERE login = ? AND senha = ? LIMIT 1";
        try (
                Connection conn = Conexao.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, user);
            pst.setString(2, pass);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                autenticado = true;
                usuario = rs.getString("nome");

                JOptionPane.showMessageDialog(
                        this,
                        "Login realizado com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Usuário ou senha inválidos!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao validar login:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);

        }
    }

    public boolean isAutenticado() {
        return autenticado; // Retorna se o usuário foi autenticado
    }

    public String getUsuario() {
        return usuario; // Retorna o nome do usuário logado
    }
}
