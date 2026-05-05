package javasavir;
// Define o pacote ao qual esta classe pertence

import java.sql.Connection;
// Importa a interface Connection, usada para representar uma conexão com o banco de dados
import java.sql.DriverManager;
// Importa a classe DriverManager, responsável por gerenciar os drivers JDBC e criar conexões

public class Conexao
// Declaração da classe pública chamada Conexao
{
    private static final String URL = "jdbc:mysql://bd_savir.mysql.dbaas.com.br:3306/bd_savir?useSSL=false&serverTimezone=America/Fortaleza";
    // String constante (final) e estática contendo a URL de conexão JDBC com o
    // banco MySQL
    // Inclui host, porta (3306), nome do banco (bd_savir) e parâmetros adicionais

    private static final String USER = "bd_savir";
    // String constante com o nome do usuário do banco de dados

    private static final String PASS = "B@nc0D@d0s";
    // String constante com a senha do banco de dados

    public static Connection getConnection() throws Exception
    // Método público e estático que retorna uma conexão com o banco
    // Lança Exception para tratar possíveis erros de conexão ou drive
    // Lança Exception para tratar possíveis erros de conexão ou driver
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        // Carrega dinamicamente o driver JDBC do MySQL na memória
        // Necessário em algumas versões para registrar o driver
        return DriverManager.getConnection(URL, USER, PASS);
        // Cria e retorna uma conexão com o banco usando a URL, usuário e senha

    }

    // Método opcional para testar conexão
    public static boolean testarConexao()
    // Método público e estático que retorna true ou false indicando sucesso na
    // conexão
    {
        try (Connection conn = getConnection()) {
            // Bloco try-with-resources: abre a conexão e garante que será fechada
            // automaticamente
            return conn != null && !conn.isClosed();
            // Retorna true se a conexão não for nula e estiver aberta
        } catch (Exception e)
        // Captura qualquer exceção ocorrida durante a tentativa de conexão
        {
            System.err.println("ERRO DE CONEXÃO: " + e.getMessage()); // Isso vai printar o erro real
            e.printStackTrace();
            return false;
            // Retorna false caso ocorra erro na conexão
        }

    }

}
