Behive Transfer - Gestão de Vistorias
Técnicas
Ferramenta interna da Behive Tecnologia para upload simplificado de vistorias em formato
compactado.
🚀 Como Rodar
1. Suba o banco PostgreSQL via Docker: docker-compose up -d.
2. Inicie a aplicação Java no IntelliJ.
3. Abra o arquivo index.html no navegador para acessar a interface de upload.
️ Funcionalidades Atuais
● Interface Drag & Drop: Arraste e solte arquivos .zip/.rar diretamente no navegador.
● Armazenamento Seguro: Renomeação automática via UUID.
● Rastreabilidade: Cada upload é vinculado a um Site ID e ao nome do técnico no banco
de dados.
📂 Estrutura do Projeto
● /src: Código fonte Java (Spring Boot).
● /docs: Documentação técnica do sistema.
● /uploads: Pasta local onde os arquivos são armazenados (Ignorada no Git).
● index.html: Interface Web de teste.
Propriedade da Behive Tecnologia.
