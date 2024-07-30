# Aplicação de Chat

Este projeto é uma aplicação de chat desenvolvida em Java, permitindo a comunicação entre clientes através de mensagens de texto. A aplicação possui uma interface de linha de comando e uma interface gráfica utilizando Swing.

## Estrutura do Projeto

O projeto está organizado nos seguintes pacotes:

### `chat`

Contém as classes principais para gerenciamento de mensagens e comunicação.

#### Classes

- **ChatException**: Classe para tratamento de exceções específicas do chat.
- **ChatFactory**: Classe fábrica para criar instâncias de `Sender` e `Receiver`.
- **MessageContainer**: Interface para contêineres de mensagens.
- **Receiver**: Interface para componentes que recebem mensagens.
- **Sender**: Interface para componentes que enviam mensagens.
- **UDPReceiver**: Implementação de `Receiver` utilizando UDP.
- **UDPSender**: Implementação de `Sender` utilizando UDP.

### `client`

Contém as classes relacionadas ao cliente, incluindo a interface gráfica e de linha de comando.

#### Classes

- **Chat**: Cliente de linha de comando para o chat.
- **ChatGUI**: Cliente gráfico para o chat utilizando Swing.
- **SysOutContainer**: Implementação de `MessageContainer` que imprime as mensagens no console.

![udp](https://github.com/user-attachments/assets/b217dec3-2996-47fb-b1cd-4415514ea3c3)
