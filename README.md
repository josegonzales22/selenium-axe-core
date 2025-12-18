# 🧪 Selenium + Axe-Core Automation Framework

Este proyecto ejecuta **pruebas automatizadas de accesibilidad** utilizando **Selenium WebDriver**, **JUnit 5**, y **axe-core**.  
Incluye soporte para generación de reportes HTML y compatibilidad con flujos **CI/CD multiplataforma** (Windows, Linux, macOS).

---

## ⚙️ Requisitos previos

Antes de ejecutar las pruebas, asegúrate de tener instaladas las siguientes herramientas:

- **Java 17 o superior**
- **Maven 3.8 o superior**
- **Node.js 18 o superior**
- **Google Chrome / ChromeDriver**

---

## 🚀 Modo 1: Ejecución manual (paso a paso)

Este modo es ideal para desarrollo local o entornos donde quieras verificar manualmente el flujo de pruebas.

### **1️⃣ Instalar dependencias de Node**
Dentro de la carpeta `nodejs` (donde está `axe-core` y `generate-html.js`):

```bash
cd nodejs
npm install
cd ..
```

### **2️⃣ Ejecutar las pruebas con Maven**
```bash
mvn clean test
```
Al finalizar, los reportes se generarán en:
```bash
reports/axe/<nombre-navegador>/
```

Cada archivo HTML corresponde a una página escaneada con axe-core.
La carpeta artifacts/ se usa temporalmente y se limpia automáticamente después de cada ejecución.

## ⚡ Modo 2: Ejecución semiautomatizada (recomendada para CI/CD)

Este modo utiliza un script universal compatible con GitHub Actions, GitLab CI, Jenkins o ejecución local.

Ejecutar el archivo: run-tests.js

```bash
node run-tests.js
```
El script:

- Elimina carpetas previas (reports/axe y artifacts)

- Instala dependencias Node necesarias (axe-core, gson, etc.)

- Ejecuta mvn clean test

- Limpia la carpeta temporal artifacts

- Retorna código de error 1 si algo falla (para CI/CD)