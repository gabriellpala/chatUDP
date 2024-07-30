package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import chat.ChatException;
import chat.ChatFactory;
import chat.MessageContainer;
import chat.Sender;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ChatGUI extends JFrame {
    private static final String KEY_TO_EXIT = "q";
    private static final int RECEIVER_BUFFER_SIZE = 1000;

    private JTextField localPortField;
    private JTextField serverPortField;
    private JTextField nameField;
    private JTextArea chatArea;
    private JTextField messageField;
    private Sender sender;
    private String from;

    public ChatGUI() {
        setTitle("Chat Client");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel de Entrada
        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GroupLayout layout = new GroupLayout(inputPanel);
        inputPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel localPortLabel = new JLabel("Porta local:");
        localPortLabel.setForeground(Color.BLUE);
        localPortField = new JTextField();
        localPortField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel serverPortLabel = new JLabel("Porta remota:");
        serverPortLabel.setForeground(Color.RED);
        serverPortField = new JTextField();
        serverPortField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel nameLabel = new JLabel("Nome:");
        nameLabel.setForeground(Color.GREEN);
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(localPortLabel)
                    .addComponent(serverPortLabel)
                    .addComponent(nameLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(localPortField)
                    .addComponent(serverPortField)
                    .addComponent(nameField))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(localPortLabel)
                    .addComponent(localPortField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(serverPortLabel)
                    .addComponent(serverPortField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameField))
        );

        // Área de Chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        chatArea.setBackground(Color.LIGHT_GRAY);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chat"));

        // Campo de Mensagem
        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Botões
        JButton connectButton = new JButton("Conectar");
        connectButton.setBackground(Color.YELLOW);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });

        JButton sendButton = new JButton("Enviar");
        sendButton.setBackground(Color.CYAN);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Painel Inferior
        JPanel southPanel = new JPanel(new BorderLayout(5, 5));
        southPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        southPanel.add(messageField, BorderLayout.CENTER);
        southPanel.add(connectButton, BorderLayout.WEST);
        southPanel.add(sendButton, BorderLayout.EAST);

        // Painel Principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void connect() {
        int localPort;
        int serverPort;
        try {
            localPort = Integer.parseInt(localPortField.getText());
            serverPort = Integer.parseInt(serverPortField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira números válidos para as portas.");
            return;
        }

        from = nameField.getText();
        if (from.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um nome.");
            return;
        }

        try {
            sender = ChatFactory.build("localhost", serverPort, localPort, new GUIContainer(chatArea));
            JOptionPane.showMessageDialog(this, "Conectado com sucesso.");
        } catch (ChatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageField.getText();

        if (message.equals(KEY_TO_EXIT)) {
            System.exit(0);
        }

        if (!message.isEmpty() && sender != null) {
            String formattedMessage = String.format("%s%s%s", from, MessageContainer.FROM, message);
            try {
                sender.send(formattedMessage);
                messageField.setText("");
                chatArea.append(formattedMessage + "\n"); // Adiciona a mensagem enviada ao chat do remetente
            } catch (ChatException e) {
                JOptionPane.showMessageDialog(this, "Erro ao enviar mensagem: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatGUI().setVisible(true);
            }
        });
    }

    private static class GUIContainer implements MessageContainer {
        private JTextArea chatArea;

        public GUIContainer(JTextArea chatArea) {
            this.chatArea = chatArea;
        }

        @Override
        public void newMessage(String message) {
            if (message == null || message.equals("")) {
                return;
            }
            chatArea.append(message + "\n"); // Adiciona a mensagem recebida ao chat
        }
    }
}
