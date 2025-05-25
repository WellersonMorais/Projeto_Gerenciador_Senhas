
# 🔐 Gerenciador de Senhas Seguro em Java

Este projeto é um **gerenciador de senhas seguro** desenvolvido em Java, como parte de uma atividade acadêmica com foco em **segurança da informação**.

## ✅ Funcionalidades

- [x] Cadastro de usuário com sugestão de senha forte
- [x] Login seguro com autenticação de dois fatores (2FA) via e-mail real
- [x] Armazenamento de senhas com criptografia AES
- [x] Senhas principais protegidas com hash BCrypt
- [x] Interface para gerenciamento de senhas com geração de senhas fortes
- [x] Verificação de senhas comprometidas usando a API do Have I Been Pwned

## 🧰 Tecnologias Utilizadas

- Java 8+
- Java Swing (Interface gráfica)
- JavaMail (envio de e-mails via SMTP)
- Bouncy Castle (BCrypt)
- AES (criptografia simétrica)
- API Have I Been Pwned

## 🚀 Como executar o projeto

1. Clone o repositório ou extraia o `.zip`
2. Certifique-se de que possui o JDK instalado
3. Compile e execute a classe `Main.java`
4. Utilize a interface gráfica para:
   - Criar uma conta
   - Realizar login com código enviado por e-mail
   - Adicionar senhas de serviços com segurança

## ⚙️ Configuração de E-mail

Este projeto utiliza o envio real de e-mails para autenticação 2FA. Os dados de e-mail estão configurados no arquivo:
```
src/security/EmailService.java
```
> É recomendável utilizar uma senha de aplicativo para garantir a segurança da sua conta.

## 📁 Estrutura de Diretórios

```
PasswordManagerProject/
├── src/
│   ├── main/
│   │   └── Main.java
│   ├── ui/
│   │   ├── RegisterPage.java
│   │   ├── LoginPage.java
│   │   └── PasswordManagerPage.java
│   └── security/
│       ├── EmailService.java
│       ├── PasswordUtils.java
│       ├── EncryptionUtils.java
│       └── LeakChecker.java
├── credentials.txt
```

## ⚠️ Segurança

- Todas as senhas armazenadas são criptografadas com AES.
- As senhas mestres são protegidas com hashing BCrypt.
- A verificação de senhas comprometidas é feita usando SHA-1 parcial (k-Anonymity).

## 👨‍🏫 Requisitos atendidos (atividade)

- ✅ Linguagem Java
- ✅ Criptografia (AES, BCrypt)
- ✅ Autenticação 2FA real (envio de e-mail)
- ✅ Sugestão de senhas seguras
- ✅ Verificação de vazamento via API
- ✅ Documentação e versionamento Git

## 📅 Entrega

**Data limite: 27/05/2025**

Todos os requisitos foram implementados com foco em segurança e boas práticas. O código está documentado e pronto para ser entregue.

---

Desenvolvido para fins acadêmicos 📘
