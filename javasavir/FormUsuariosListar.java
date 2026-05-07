package javasavir;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FormUsuariosListar extends JFrame
{
    JTable tabela;
    DefaultTableModel modelo;

    public FormUsuariosListar()
    {
        super("Usuários");

        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== BARRA DE BOTÕES =====
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);

        Dimension tamanhoPadrao = new Dimension(100, 40);

        JButton btnIncluir = new JButton("INCLUIR");
        JButton btnExcluir = new JButton("EXCLUIR");
        JButton btnSair = new JButton("SAIR");

        JButton[] botoes = {btnIncluir, btnExcluir, btnSair};
        
        for (JButton b:botoes)
        {
            b.setPreferredSize(tamanhoPadrao);
            b.setMinimumSize(tamanhoPadrao);
            b.setMaximumSize(tamanhoPadrao);
            barra.add(b);
        }

    btnIncluir.addActionListener(e->
        JOptionPane.showMessageDialog(null, "Incluir usuário")
    );

    btnExcluir.addActionListener(e->
        JOptionPane.showMessageDialog(null, "Excluir usuário")
    );

    btnSair.addActionListener(e->
        dispose());
    
    add(barra, BorderLayout.NORTH);

    // ===== GRID =====
    String[] colunas = {"ID", "Nome", "Login", "Ativo"};

    modelo = new DefaultTableModel(colunas, 0)
    {
        @Override
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }

    };
    

tabela = new JTable(modelo);
tabela.setRowHeight(25);

// Alinhar ID alinhar à  direita
DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
direita.setHorizontalAlignment(SwingConstants.RIGHT);
tabela.getColumnModel().getColumn(0).setCellRenderer(direita);

JScrollPane scroll = new JScrollPane(tabela);
add(scroll, BorderLayout.CENTER);

carregarUsuarios();

setVisible(true); 
}

private void carregarUsuarios()
{
    modelo.setRowCount(0);

    String sql = "SELECT usuario_id, nome, login, ativo FROM tb_usuarios ORDER BY nome";

    try
    {
        Connection conn = Conexao.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next())
        {
           int usuario_id = rs.getInt("usuario_id");
           String nome = rs.getString("nome");
           String login = rs.getString("login");
           boolean ativo = rs.getBoolean("ativo");

          modelo.addRow(new Object[]{
            usuario_id, 
            nome, 
            login, 
            ativo
          });
        }
        rs.close();
        stmt.close();
        conn.close();
    }

    catch (Exception e)
    {
        JOptionPane.showMessageDialog(this,
            "Erro ao validar login: \n" + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

}
        