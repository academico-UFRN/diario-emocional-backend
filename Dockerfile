# Estágio 1: Build da aplicação
FROM eclipse-temurin:26-jdk AS builder
WORKDIR /app

# Instala curl para o Maven Wrapper baixar o binário do Maven
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

# Copia a estrutura do Maven Wrapper e o POM primeiro para cache de dependências
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Converte quebras de linha CRLF (Windows) para LF e concede permissão de execução
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Pré-carrega as dependências para acelerar builds subsequentes
RUN ./mvnw dependency:go-offline -B || true

# Copia o código-fonte e compila o pacote .jar
COPY src src
RUN ./mvnw clean package -DskipTests

# Estágio 2: Imagem final enxuta para execução (Runtime)
FROM eclipse-temurin:26-jre
WORKDIR /app

# Copia o artefato .jar gerado no estágio anterior
COPY --from=builder /app/target/*.jar app.jar

# Configurações de ambiente e memória para o Render
ENV PORT=8080
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080

# Inicia a aplicação garantindo que escute na porta informada pelo Render ($PORT)
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=${PORT:-8080}"]
