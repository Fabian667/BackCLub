# Club Social API

API Spring Boot para el Club Social, preparada para ejecución en Docker y subida a GitHub sin secretos.

## Requisitos
- Docker 24+
- Docker Compose v2
- (Opcional) Java 21 y Maven Wrapper para ejecución local

## Archivos clave
- `Dockerfile`: build multi-stage (Temurin JDK/JRE 21)
- `docker-compose.yml`: orquestación de la app, lee variables desde `.env`
- `.dockerignore`: evita subir artefactos y secretos
- `.env.example`: plantilla de variables (copiar a `.env`)
- `src/main/resources/application.properties`: parametrizado para leer credenciales desde variables de entorno
 - `samples/login.example.json`: ejemplo de payload de login con placeholders (sin secretos)

## Configuración de entorno
1. Copia el archivo `.env.example` a `.env` y completa tus valores:
```
SPRING_DATASOURCE_URL=jdbc:mysql://metro.proxy.rlwy.net:17801/railway?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=REPLACE_ME
SPRING_PROFILES_ACTIVE=default
JAVA_OPTS=
JWT_SECRET=CHANGE_ME_CHANGE_ME_CHANGE_ME_32BYTES
```
- Si tu proveedor requiere SSL: añade `useSSL=true&requireSSL=true&verifyServerCertificate=true` a la URL y configura certificados.

## Construcción y ejecución
### Docker directo
```
docker build -t clubsocial-app:latest .
docker run --rm -p 8080:8080 --env-file ./.env clubsocial-app:latest
```

### Docker Compose
```
docker compose up --build -d
```
- Compose leerá el archivo `.env` automáticamente y mapeará `SPRING_*` y `JAVA_OPTS` al contenedor.

## Endpoints de prueba
- `GET http://localhost:8080/api/informacion`
- `GET http://localhost:8080/api/sliders/activos`

## Buenas prácticas
- No subas tu `.env` (ya está ignorado en `.dockerignore`).
- Usa secretos/vars en GitHub Actions o tu plataforma de despliegue.
- Define `JWT_SECRET` en producción y gestiona tokens de forma segura.
 - No incluyas credenciales reales en `samples/` ni archivos temporales (`tmp_*.json`).

## Ejecución local (opcional)
```
./mvnw spring-boot:run
```
- Recuerda exportar variables de entorno si no usas Docker:
  - `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`

## Licencia
- Este proyecto no declara una licencia específica. Añade una si lo necesitas.

## CI/CD con GitHub Actions y Render
- Este repo incluye un workflow en `/.github/workflows/deploy.yml` que:
  - Compila el proyecto con Maven.
  - Construye y publica la imagen Docker en `ghcr.io/<owner>/<repo>:latest`.
  - Dispara un redeploy en Render mediante un Deploy Hook.

### Configuración requerida
- En tu repositorio de GitHub, añade el secreto `RENDER_DEPLOY_HOOK_URL` con la URL de Deploy Hook de tu servicio en Render.
- No necesitas credenciales extra para `ghcr.io`; el workflow usa `GITHUB_TOKEN` con permisos de `packages: write`.

### Cómo se ejecuta
- En cada `push` a `main` o desde `Run workflow` (workflow_dispatch), se ejecutará:
  1) `./mvnw -DskipTests package`
  2) Build & push de la imagen Docker a GHCR
  3) `curl -X POST $RENDER_DEPLOY_HOOK_URL` para redeploy en Render

### Verificación
- Ve a GitHub → `Actions` para revisar los logs del pipeline.
- En Render, revisa el historial de deploys y los logs de build.

### Troubleshooting
- Asegúrate de que tu `Dockerfile` buildé correctamente localmente.
- Si tu servicio en Render buildé desde GitHub directamente, el Deploy Hook es suficiente; publicar en GHCR es opcional.
- Si usas Docker como fuente en Render, apunta al `ghcr.io/<owner>/<repo>:latest` y habilita el acceso.