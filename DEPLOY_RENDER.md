# Deploy no Render - Backend Spring Boot

## 📋 Pré-requisitos

- Conta no GitHub (para conectar o repositório)
- Conta no Render (https://render.com)
- URL do banco de dados SQL Server (já temos: AbsenceManager.mssql.somee.com)
- Credenciais do SQL Server

## 🚀 Passo 1: Preparar o repositório

```bash
cd AbsenceManager
git add Dockerfile render.yaml .dockerignore src/main/resources/application.properties
git commit -m "Add Render deployment configuration"
git push origin main
```

## 🚀 Passo 2: Criar serviço no Render

1. Acesse https://render.com e faça login
2. Clique em "New +"
3. Selecione "Web Service"
4. Conecte seu repositório GitHub (autorize se necessário)
5. Selecione o repositório `Ligação`

## 🚀 Passo 3: Configurar o serviço

Preencha os campos:
- **Name**: `absence-manager-api`
- **Root Directory**: `AbsenceManager` (caminho do projeto relativo ao repositório)
- **Environment**: Docker
- **Region**: Ohio (mais barato)
- **Plan**: Free
- **Dockerfile Path**: `./Dockerfile`

## 🚀 Passo 4: Adicionar variáveis de ambiente

Na seção "Environment Variables", clique em "Add Environment Variable":

| Key | Value |
|-----|-------|
| `SPRING_DATASOURCE_URL` | `jdbc:sqlserver://AbsenceManager.mssql.somee.com:1433;databaseName=AbsenceManager;integratedSecurity=false;encrypt=true;trustServerCertificate=true` |
| `SPRING_DATASOURCE_USERNAME` | `Castrinho` |
| `SPRING_DATASOURCE_PASSWORD` | `12345678` (sua senha real) |
| `SPRING_DATASOURCE_DRIVER_CLASS_NAME` | `com.microsoft.sqlserver.jdbc.SQLServerDriver` |
| `SPRING_JPA_DATABASE_PLATFORM` | `org.hibernate.dialect.SQLServerDialect` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` |
| `PORT` | `8080` |

## 🚀 Passo 5: Deploy

Clique em "Create Web Service" e aguarde o build completar.

Você receberá uma URL como: `https://absence-manager-api.onrender.com`

## 🔗 Conectar Frontend (Vercel)

### No Frontend (Absence-Manager):

1. Atualize o arquivo `.env.production`:
```env
VITE_API_BASE_URL=https://absence-manager-api.onrender.com
```

2. No Vercel, adicione a variável de ambiente:
   - Vá para Project Settings
   - Environment Variables
   - Adicione: `VITE_API_BASE_URL=https://absence-manager-api.onrender.com`

3. Redeploy no Vercel (git push)

## ⚠️ Importante

- **Free tier do Render**: A aplicação "dorme" após 15 min sem uso. Leva ~30s para acordar.
- **CORS**: O backend permite requisições do Vercel automaticamente
- **Banco de dados**: Continua no SomeEE, nenhuma mudança necessária

## 🧪 Testar a conexão

Após o deploy, teste com:
```bash
curl https://absence-manager-api.onrender.com/usuarios
```

Você deve receber uma resposta (pode ser erro de autenticação, mas significa que o backend está rodando).

## 📝 URLs importantes

- **Backend (Render)**: https://absence-manager-api.onrender.com
- **Frontend (Vercel)**: https://absence-manager.vercel.app
- **Banco de dados**: jdbc:sqlserver://AbsenceManager.mssql.somee.com:1433
