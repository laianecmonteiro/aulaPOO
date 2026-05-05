package javasavir;

import javax.swing.*;
import java.awt.*;

public class FormPrincipal extends JFrame
{

    private  String usuarioLogado = null;

    // Menus principais
    private final JMenuBar menuBar;
    private final JMenu menuSistema;

    // Segurança deve existir só quando logado:
    private JMenu menuSeguranca; // começa null
    private JMenuItem itemUsuarios; // começa null

    // Itens de menu
    private final JMenuItem itemFechar;
    private final JMenuItem itemLogin;

    public FormPrincipal()
    {
        super("Savir Software");

        // ===== Fundo =====
        JPanel painelFundo = new JPanel()
        {
            Image img = new ImageIcon(getClass().getResource("/javasavir/JavaLogo.png")).getImage();
            @Override protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };

        setLayout(new BorderLayout());
        add(painelFundo, BorderLayout.CENTER);

        // ===== MENU =====
        menuBar = new JMenuBar();

        menuSistema = new JMenu("Sistema");
        itemLogin = new JMenuItem("Logar");
        itemFechar = new JMenuItem("Fechar");
        menuSistema.add(itemLogin);
        menuSistema.addSeparator();
        menuSistema.addSeparator();
        menuSistema.add(itemFechar);

        menuBar.add(menuSistema);
        setJMenuBar(menuBar);

        // ===== AÇÕES =====

        // Login / Logout
        itemLogin.addActionListener(
            e -> {
                if (usuarioLogado == null) 
                {
                    realizarLogin(); // abre o diálogo e autentica   
                }
                else
                {
                    int op = JOptionPane.showConfirmDialog(this,
                        "Deseja sair da sessão de \"" + usuarioLogado + "\"?",
                        "Logout", JOptionPane.YES_NO_OPTION);

                    if (op == JOptionPane.YES_OPTION)
                    {
                        usuarioLogado = null;
                        aplicarEstadoAutenticacao(); // remove menu Segurança
                        JOptionPane.showMessageDialog(this, "Sessão encerrada.");
                    }
                }
            }
        );

        // Fechar
        itemFechar.addActionListener
        (e -> {
                int resposta = JOptionPane.showConfirmDialog(this, 
                    "Deseja realmente sair?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION) System.exit(0);
                    
                }
        );

        // ===== JANELA =====
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inicial: sem login -> sem menu Segurança
        aplicarEstadoAutenticacao();

        setVisible(true);
    }

    /** Mostra/esconde o menu Segurança conforme esteja logado */
    private void aplicarEstadoAutenticacao()
    {
        boolean logado = (usuarioLogado != null);
        itemLogin.setText(logado ? "Logout" : "Logar");

        if (logado) 
        {
            // cria uma vez
            if (menuSeguranca == null) 
            {
             menuSeguranca = new JMenu("Segurança");
             itemUsuarios = new JMenuItem("Usuários");
             itemUsuarios.addActionListener( e-> 
                JOptionPane.showMessageDialog(this, 
                    "Abrir tela de Gestão de Usuários...",
                    "Segurança > Usuários",
                    JOptionPane.INFORMATION_MESSAGE));
                menuSeguranca.add(itemUsuarios);
            }
            
            // adiciona se ainda não estiver na barra
            if (!isMenuPresente(menuSeguranca))
            {
                menuBar.add(menuSeguranca);
            }
        }
        else
        {
            // remove se existir
            if (isMenuPresente(menuSeguranca))
            {
                menuBar.remove(menuSeguranca);
            }
        }

        menuBar.revalidate();
        menuBar.repaint();
    }

    /** Checa se um menu já está na barra */
    private boolean  isMenuPresente(JMenu menu)
    {
        if (menu == null) return false;
        for (int i = 0; i< menuBar.getMenuCount(); i++)
        {
            if (menuBar.getMenu(i) ==menu) return true;
        }
        return false;
    }

    /**Abre o diálogo de login e, se OK, aplica estado logado */
    private void realizarLogin()
    {
        LoginDialog dlg = new LoginDialog(this);
        dlg.setVisible(true);
        if (dlg.isAutenticado())
        {
            this.usuarioLogado = dlg.getUsuario();
            aplicarEstadoAutenticacao(); //adiciona menu Segurança
            JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuarioLogado + "!");
        
        }
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(FormPrincipal::new);
    }
}

