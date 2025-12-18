// run-tests.js
import { execSync } from "child_process";
import { rmSync, existsSync } from "fs";

function cleanDir(path) {
  try {
    if (existsSync(path)) {
      console.log(`[INFO] Eliminando carpeta: ${path}`);
      rmSync(path, { recursive: true, force: true });
    }
  } catch (err) {
    console.warn(`[ERROR] No se pudo eliminar ${path}:`, err.message);
  }
}

try {
  console.log("[INFO] Inicio de ejecución automatizada de pruebas");

  cleanDir("reports");
  cleanDir("images");
  cleanDir("artifacts");

  console.log("[INFO] Instalando dependencias Node...");
  execSync("npm install", { cwd: "nodejs", stdio: "inherit" });

  console.log("[INFO] Ejecutando pruebas con Maven...");
  execSync("mvn clean test", { stdio: "inherit" });

  cleanDir("artifacts");
  process.exit(0);
} catch (error) {
  console.error("[ERROR] Error durante la ejecución:", error.message);
  process.exit(1);
}
