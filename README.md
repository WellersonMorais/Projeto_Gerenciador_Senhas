
#  Gerenciador de Senhas Seguro em Java

Este projeto é um **gerenciador de senhas seguro** desenvolvido em Java, como parte de uma atividade acadêmica da matéria de **Desenvolvimento Seguro**.

---

##  Funcionalidades

- [x] Cadastro de usuário com sugestão automática de senha forte (preenchida no clique)
- [x] Botão para alternar entre cadastro e login
- [x] Envio real de e-mail com código de verificação (2FA)
- [x] Autenticação segura com hash BCrypt
- [x] Armazenamento local com criptografia AES
- [x] Geração e gerenciamento de senhas fortes
- [x] Verificação de senhas vazadas usando a API [Have I Been Pwned](https://haveibeenpwned.com/)
- [x] Indicação visual de senhas comprometidas
- [x] Exclusão de blocos cadastrados

---

##  Tecnologias Utilizadas

- Java Development Kit (JDK) v.21
- Java Swing (interface gráfica)
- JavaMail (envio de e-mails via SMTP)
- jBCrypt (hash seguro de senhas)
- AES com chave derivada por PBKDF2 (criptografia simétrica)
- API Have I Been Pwned (verificação de vazamento)
- H2 Database Engine (Banco de dados local)

---

##  Estrutura de Diretórios

```
PasswordManagerProject/
├── src/
│ ├── config/
| | ├── Descriptografador.java
│ │ ├── private_key.enc
│ ├── db/
│ │ ├── ConfigPass.java
│ │ ├── DatabaseConnection.java
│ │ ├── SetupDatabase.java
│ │ └── UserSave.java
│ ├── ui/
│ │ ├── RegisterPage.java
│ │ ├── LoginPage.java
│ │ └── PasswordManagerPage.java
│ ├── security/
│ │ ├── PasswordUtils.java
│ │ ├── EncryptionUtils.java
│ │ ├── LeakChecker.java
│ │ └── EmailSender.java
│ │ └── CryptoUtils.java
│ ├── model/
│ │ └── User.java
├── lib/ # Bibliotecas .jar (jBCrypt, mail, etc.)
├── out/ # Diretório de saída da compilação
├── compile-run.bat # Script para compilar e executar
└── README.md
```

##  Segurança

- senhas armazenadas são criptografadas com AES.
- senhas mestres são protegidas com hashing BCrypt.
- A verificação de senhas comprometidas é feita usando SHA-1 parcial (k-Anonymity).

---

##  Observações

Para facilitar a execução e entrega do projeto, as credenciais do banco no arquivo ```'DatabaseConnection.java'``` estão **inseridas** no código, para que a execução possa ser feita sem qualquer configuração adicional. 

Em projetos reais, **não é se deve deixar credenciais hardcoded** no código. O ideal é:

- Configurar credenciais por variáveis de ambiente
- Usar arquivos externos de configuração fora do código-fonte
- Nunca versionar senhas ou chaves no repositório

---

##  Como Executar

1. **Clone o repositório** ou extraia o `.zip`
2. **Compile e rode o projeto** com o arquivo .bat `compile.bat`:

---

Este projeto foi desenvolvido com fins educacionais e demonstrativos. 
